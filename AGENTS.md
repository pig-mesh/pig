# Repository Guidelines

## Project Structure & Module Organization

pigx aggregates Spring Cloud services through the root `pom.xml`. Runtime services sit under dedicated folders:
`pigx-register` (service discovery/Nacos), `pigx-gateway` (edge routing), `pigx-auth` (OAuth2), `pigx-upms` (user &
permission), `pigx-flow` (workflow), `pigx-app-server`, and `pigx-visual` (monitoring plus
schedulers). Shared libraries and DTOs live in `pigx-common`. Sample SQL and Docker build contexts reside in `db/`,
while infra manifests live in `docker-compose.yml`. Every module uses the standard `src/main/java` and `src/test/java`
layout.

## Build, Test, and Development Commands

- `mvn -T 1C clean install -DskipTests` compiles all modules with the managed BOM; add `-pl pigx-gateway -am` to limit
  the build to a single service.
- `mvn -pl pigx-gateway spring-boot:run` (swap the module name as needed) starts a service with dev profiles for live
  reloads.
- `docker-compose up -d pigx-mysql pigx-redis pigx-register` brings up local infrastructure; run
  `docker-compose up --build pigx-auth pigx-upms` when you need the auth stack.
- `mvn -pl pigx-knowledge test` executes module tests; append `-DskipITs` to bypass integration suites.

## Testing Guidelines

Use `spring-boot-starter-test` (JUnit 5, AssertJ, Mockito) for unit and slice tests. Name classes `*Tests.java` and
colocate fixtures in `src/test/resources`. Target >70% line coverage on the service layer that guards authentication,
gateway filters, and approval flows. Run `mvn verify` before opening a PR to exercise the full plugin stack.

## Commit & Pull Request Guidelines

Follow the repo’s `type(scope): summary` history, e.g. `fix(upms): clear login failure cache` or
`feat(ai-voice): add speech transcription`. Each PR must describe the impact area, list affected modules, and reference
internal tickets or issues. Attach curl/Postman snippets or screenshots whenever UI or OpenAPI responses change. Rebase
on `ai-dev`, avoid committing generated artifacts, and call out schema/config updates explicitly.

## Security & Configuration Tips

Never commit environment secrets; rely on `docker-compose.yml` plus `.env` overrides ignored by Git. Keep `db/` seed
data sanitized, and drive end-to-end checks against `pigx-register` (ports 8848/9848) with `pigx-gateway` (9999) so
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
