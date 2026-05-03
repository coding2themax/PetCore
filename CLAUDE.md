---
name: petcore-design
description: Use this skill to generate well-branded interfaces and assets for PetCore and PetStore Web, either for production or throwaway prototypes/mocks/etc. Contains essential design guidelines, colors, type, fonts, assets, and UI kit components for prototyping.
user-invocable: true
---

Read the README.md file within this skill, and explore the other available files.
If creating visual artifacts (slides, mocks, throwaway prototypes, etc), copy assets out and create static HTML files for the user to view. If working on production code, you can copy assets and read the rules here to become an expert in designing with this brand.
If the user invokes this skill without any other guidance, ask them what they want to build or design, ask some questions, and act as an expert designer who outputs HTML artifacts _or_ production code, depending on the need.

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PetCore is a domain-driven **modular monolith** for managing pet intake, profiles, matching, and search. AI features (AWS Bedrock) are strictly optional, feature-flagged, and never in the critical execution path.

## Repository Layout

```
PetCore/
├── backend-service/     # Spring Boot 4 reactive backend (Java 25)
├── front-end/           # React Router 7 + TypeScript + Tailwind frontend
├── api-specs/           # OpenAPI spec (petcore-openapi.yaml)
├── acceptance-tests/    # Gherkin feature files
├── docs/adr/            # Architecture Decision Records
├── docker-compose.yml   # Local dev: PostgreSQL + backend
```

## Commands

### Backend (run from `backend-service/`)

```bash
./mvnw spring-boot:run                          # start backend (port 3030)
./mvnw test                                     # run all tests
./mvnw test -Dtest=PetIntakeHandlerTest         # run a single test class
./mvnw verify                                   # tests + JaCoCo coverage check (≥50% line coverage per package)
./mvnw package -DskipTests                      # build jar
```

### Frontend (run from `front-end/`)

```bash
pnpm dev          # start dev server
pnpm build        # production build
pnpm typecheck    # react-router typegen + tsc
```

### Local Infrastructure

```bash
docker compose up -d postgres    # start PostgreSQL only
docker compose up -d             # start PostgreSQL + backend
```

Copy `.env.example` to `.env` before running. The backend requires `DATABASE_HOST`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`.

## Backend Architecture

**Tech stack:** Spring Boot 4, WebFlux (reactive), R2DBC, PostgreSQL, Java 25.

The backend is organized as a modular monolith under `com.coding2themax.petcore.pet.service`:

| Package                               | Responsibility                                                                       |
| ------------------------------------- | ------------------------------------------------------------------------------------ |
| `profile`                             | Pet intake & profiles — the only module with write access to `petcore.pets`          |
| `matching`                            | Rule-based compatibility scoring — reads from `profile`, no writes                   |
| `search`                              | Filtering & explainable ranking — reads from `profile`, no writes                    |
| `augmentation`                        | Optional AI (Bedrock) augmentation — feature-flagged, depends on `matching`/`search` |
| `api`                                 | Routing entry points                                                                 |
| `security`, `config`, `observability` | Cross-cutting concerns                                                               |

**Strict dependency rule:** `profile` has no dependencies on `matching`, `search`, or `augmentation`. Only `matching` and `search` may read from `profile`.

### Within each domain module (e.g. `profile`):

```
profile/
├── api/
│   ├── domain/model/    # Entities and enums (Pet, Species, PetStatus, etc.)
│   ├── dto/             # Request/response records
│   ├── handler/         # WebFlux functional handlers + validators
│   ├── router/          # Route definitions (RouterFunction)
│   └── service/         # Service interface + PostgreSQL impl
├── application/         # Use-case orchestration (currently in service/)
└── infrastructure/      # Repository (ReactiveCrudRepository)
```

Routing uses **Spring WebFlux functional style** (`RouterFunction` + `HandlerFunction`), not `@RestController`. All handlers are in `handler/`, routes are wired in `router/`.

Idempotency is enforced via `X-Idempotency-Key` header on intake requests; the key is stored on the `pets` table with a unique constraint.

### Database

Single PostgreSQL schema `petcore`. Schema and seed SQL live in `backend-service/src/main/resources/` and are mounted into Docker on first run.

## Frontend Architecture

**Tech stack:** React 19, React Router 7 (framework mode), TypeScript, Tailwind CSS v4, Vite.

Routes are file-based, declared in `front-end/app/routes.ts`. Current routes:

- `/` → `routes/home.tsx`
- `/pets/new` → `routes/pets.new.tsx`

API calls to the backend go through `front-end/app/api/`.

## Testing Approach

- **Backend unit tests** use Mockito + `reactor.test.StepVerifier` for reactive streams. No real database in unit tests.
- **Backend integration tests** use Testcontainers (`r2dbc:tc:postgresql`) — `src/test/resources/application.yml` switches the datasource automatically.
- Test class locations mirror `src/main` package structure exactly.
- JaCoCo enforces ≥50% line coverage per package; `./mvnw verify` will fail if coverage drops below this.

## Key Conventions

- `PetIntakeRequest` and `PetResponse` are Java records (immutable DTOs).
- `Pet` domain entity uses setters (mutable, mapped by R2DBC).
- Enum values (`Species`, `PetStatus`, `IntakeType`, etc.) are stored as strings in PostgreSQL.
- The backend runs on port **3030** (not 8080) in native mode; Docker maps it to 8080.
- Actuator endpoints are fully exposed; Prometheus metrics are enabled.
