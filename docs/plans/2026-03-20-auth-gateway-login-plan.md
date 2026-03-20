# Auth Gateway Login Redirect - Implementation Plan

**Goal:** Ensure all browser-visible authorization-code flow entry points stay on `pigx-gateway:9999/auth/**` while keeping `pigx-auth:3000` internal-only.

**Architecture:** Add proxy-aware URL reconstruction in `pigx-auth`, route all auth-side browser redirects through a shared external URL resolver, and keep `pigx-gateway` responsible only for forwarding `X-Forwarded-*` headers.

**Tech Stack:** Spring Authorization Server, Spring Security, Spring Cloud Gateway, Servlet forwarded headers, JUnit 5

**Design doc:** `docs/plans/2026-03-20-auth-gateway-login-design.md`

---

### Task 1: Verify gateway forwarded-header behavior

**Files:**
- Inspect: `pigx-gateway/src/main/java/com/pig4cloud/pigx/gateway/filter/PigxRequestGlobalFilter.java`
- Inspect or modify: gateway route configuration in Nacos if forwarded prefix is missing

**Step 1: Confirm required headers**

Verify requests forwarded to `pigx-auth` carry:

- `X-Forwarded-Proto`
- `X-Forwarded-Host`
- `X-Forwarded-Port`
- `X-Forwarded-Prefix=/auth`

**Step 2: Add gateway-side header propagation only if needed**

If `X-Forwarded-Prefix` is not reliably present, patch gateway routing/filter configuration so auth receives the external `/auth` prefix.

**Exit criteria**

A proxied request to `/auth/oauth2/authorize` reaches `pigx-auth` with a complete forwarded-header set.

---

### Task 2: Enable forwarded-header handling in auth

**Files:**
- Modify: `pigx-auth/src/main/resources/application.yml` or dev configuration entry point
- Optionally add: auth-side forwarded-header config bean if properties are insufficient

**Step 1: Turn on framework support**

Configure `pigx-auth` so servlet request URL reconstruction honors forwarded headers.

**Step 2: Verify request URL shape**

Add a focused test or temporary assertion to confirm auth-side request metadata resolves to:

- scheme `http`
- host `pigx-gateway`
- port `9999`
- prefix `/auth`

**Exit criteria**

Framework-managed URL generation in auth uses the external request origin when forwarded headers are present.

---

### Task 3: Add a shared external URL resolver

**Files:**
- Add: `pigx-auth/src/main/java/.../support/...` helper for external auth URL resolution
- Add tests under `pigx-auth/src/test/java/...`

**Step 1: Implement resolver responsibilities**

The resolver should:

- build an external absolute URL for auth-local paths such as `/token/login`
- rewrite an auth-internal absolute URL to the gateway external URL
- preserve query parameters
- fall back to direct auth origin when forwarded headers are missing

**Step 2: Add unit tests**

Cover at least:

- direct auth request with no forwarded headers
- proxied gateway request with `/auth` prefix
- internal absolute auth URL rewritten to external gateway URL
- non-auth third-party `redirect_uri` left untouched

**Exit criteria**

All auth-side redirect handlers can depend on one resolver instead of manually assembling URLs.

---

### Task 4: Fix unauthenticated authorization redirect

**Files:**
- Modify: `pigx-auth/src/main/java/com/pig4cloud/pigx/auth/config/AuthorizationServerConfiguration.java`
- Modify: `pigx-auth/src/main/java/com/pig4cloud/pigx/auth/support/core/FormIdentityLoginConfigurer.java`
- Add if needed: proxy-aware authentication entry point

**Step 1: Replace the default login entry behavior**

When an unauthenticated browser hits `/oauth2/authorize`, the redirect target must be the external login page:

`http://pigx-gateway:9999/auth/token/login`

**Step 2: Keep existing form-login processing path**

Do not change the actual login form submission endpoint unless required by the redirect fix.

**Exit criteria**

Unauthenticated authorization requests no longer leak the `pigx-auth` origin in the `Location` header.

---

### Task 5: Fix login failure redirect

**Files:**
- Modify: `pigx-auth/src/main/java/com/pig4cloud/pigx/auth/support/handler/FormAuthenticationFailureHandler.java`

**Step 1: Remove `contextPath`-based URL assembly**

Replace manual `String.format("%s/token/login...")` behavior with the shared external URL resolver.

**Step 2: Preserve parameters**

Ensure the redirect still carries:

- `error`
- `TENANT-ID`
- `client_id`

**Exit criteria**

All form-login failures redirect to `pigx-gateway:9999/auth/token/login?...` when proxied through gateway.

---

### Task 6: Fix login success redirect to saved authorization request

**Files:**
- Modify: `pigx-auth/src/main/java/com/pig4cloud/pigx/auth/support/handler/TenantSavedRequestAwareAuthenticationSuccessHandler.java`

**Step 1: Keep saved-request semantics**

Continue using the saved authorization request as the redirect source of truth.

**Step 2: Externalize before redirect**

Normalize the final saved-request URL through the shared resolver before calling the redirect strategy.

**Step 3: Preserve tenant propagation**

Keep the existing `TENANT-ID` append behavior intact.

**Exit criteria**

Post-login redirects return the browser to `pigx-gateway:9999/auth/oauth2/authorize?...` instead of `pigx-auth:3000/...`.

---

### Task 7: Verify confirm and logout paths

**Files:**
- Inspect or modify: `pigx-auth/src/main/java/com/pig4cloud/pigx/auth/endpoint/PigxTokenEndpoint.java`
- Inspect or modify: `pigx-common/pigx-common-security/src/main/java/com/pig4cloud/pigx/common/security/handler/SsoLogoutSuccessHandler.java`

**Step 1: Confirm consent-page entry**

Ensure the browser-visible entry for `/oauth2/confirm_access` remains on the gateway public origin when the request started through `/auth/**`.

**Step 2: Confirm logout path behavior**

Verify `/auth/oauth2/logout` remains browser-visible under the gateway origin and still honors `redirect_url`.

**Exit criteria**

Consent and logout do not regress to internal auth-host redirects.

---

### Task 8: Add regression tests

**Files:**
- Add/modify tests under `pigx-auth/src/test/java/...`

**Step 1: Handler-level coverage**

Add tests for:

- login failure redirect externalization
- login success redirect externalization

**Step 2: Authorization-entry coverage**

Add a test for unauthenticated `/oauth2/authorize` with forwarded headers and assert `Location` points to `/auth/token/login`.

**Step 3: Fallback coverage**

Add a test verifying direct auth access still behaves correctly when forwarded headers are absent.

**Exit criteria**

The redirect behavior is protected by focused regression tests instead of manual verification only.

---

### Task 9: Verify locally

**Step 1: Auth module tests**

Run:

```bash
cd /Users/lengleng/work/dev1227/pigx && mvn -pl pigx-auth test
```

**Step 2: Targeted auth/gateway compile**

Run:

```bash
cd /Users/lengleng/work/dev1227/pigx && mvn -pl pigx-auth,pigx-gateway -am -DskipTests compile
```

**Step 3: Manual redirect smoke test**

Start gateway + auth, request:

```bash
curl -I 'http://pigx-gateway:9999/auth/oauth2/authorize?response_type=code&client_id=test&scope=server&redirect_uri=https://example.com'
```

Verify the first `Location` header points to `http://pigx-gateway:9999/auth/token/login...`.

---

### Task 10: Commit implementation in safe slices

Recommended commit sequence:

1. `feat(auth): add forwarded-aware auth url resolver`
2. `fix(auth): externalize authorization login redirects`
3. `test(auth): cover gateway auth redirect flow`

Keep docs commits separate from code commits.
