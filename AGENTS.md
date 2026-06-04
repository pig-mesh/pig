# Repository Guidelines

## Project Structure & Module Organization

`pig` aggregates the open-source Spring Cloud services through the root `pom.xml`. Runtime services sit under dedicated
folders: `pig-register` (Nacos), `pig-gateway` (edge routing), `pig-auth` (authorization), `pig-upms` (user and
permission), `pig-boot` (single-service launcher), and `pig-visual` (monitor, codegen, quartz). Shared libraries and
DTOs live in `pig-common`. Sample SQL and Docker build contexts reside in `db/`, while infra manifests live in
`docker-compose.yml`. Every module uses the standard `src/main/java` and `src/test/java` layout.

The open-source edition intentionally excludes workflow, app server, MP, payment, report, BI, multi-tenant, data-scope,
and dynamic gateway route management code. Gateway routes are maintained through normal configuration files.

## Build, Test, and Development Commands

- Run `mvn clean install -T 4 -Pcloud` from the project root to compile the full cloud edition with the managed BOM.

- `docker compose build && docker compose up` builds images and starts the local service stack.

## Testing Guidelines

Use `spring-boot-starter-test` (JUnit 5, AssertJ, Mockito) for unit and slice tests. Name classes `*Tests.java` and
colocate fixtures in `src/test/resources`. Focus coverage on authentication, gateway filters, user/permission logic,
scheduled jobs, and code generation. Run `mvn verify` before opening a PR to exercise the full plugin stack.

## Commit & Pull Request Guidelines

Follow the repo’s `type(scope): summary` history, e.g. `fix(upms): clear login failure cache` or
`feat(codegen): add template option`. Each PR must describe the impact area, list affected modules, and reference
issues when applicable. Attach curl/Postman snippets or screenshots whenever UI or OpenAPI responses change. Avoid
committing generated artifacts, and call out schema/config updates explicitly.

## Security & Configuration Tips

Never commit environment secrets; rely on `docker-compose.yml` plus `.env` overrides ignored by Git. Keep `db/` seed
data sanitized, and drive end-to-end checks against `pig-register` (ports 8848/9848) with `pig-gateway` (9999) so
discovery behavior matches production.

## Behavioral Guidelines

The following four rules apply to all tasks and take precedence over other default behaviors.

### 1. Think Before Coding

- When requirements are ambiguous, ask first — never make silent assumptions
- If a simpler solution exists, say so explicitly rather than quietly picking a direction
- When something is unclear, surface the confusion instead of pushing forward with guesswork

### 2. Prefer Simplicity

- Use the minimum code needed to solve the current problem; add nothing unrequested
- Do not introduce abstractions for one-off code or design for hypothetical future needs
- Ask yourself: would a senior engineer call this over-engineered?

### 3. Surgical Changes

- Only touch what the task requires; do not refactor adjacent code as a side effect
- Do not alter style or structure unrelated to the current task
- Every changed line must trace directly to a stated requirement

### 4. Goal-Driven Execution

- Convert vague instructions into verifiable goals — prefer tests as the success criterion
- "Fix a bug" → write a failing test that reproduces it, then make it pass
- "Add validation" → write tests covering invalid inputs, then make them pass
