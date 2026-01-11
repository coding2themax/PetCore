# ADR-0006: Enforce Domain-Driven Module Boundaries

## Status

Accepted

## Context

As a modular monolith (see ADR-0001), we need clear boundaries between domain modules to prevent coupling and enable future extraction to microservices. Without enforcement, developers may create cross-module dependencies that violate these boundaries.

## Decision

We will organize code into domain-based packages with explicit module boundaries:

- `pet.profile` - Pet and shelter management
- `pet.matching` - Compatibility scoring and match logic
- `pet.search` - Search, filtering, and ranking
- `pet.ai` - AI augmentation features

Each module will:

- Have its own package structure
- Expose only public service interfaces
- Communicate through defined contracts
- Own its domain entities
- Be independently testable

## Alternatives Considered

- **Technical layer separation** (controller/service/repository): Doesn't prevent cross-domain coupling
- **Separate repositories from day one**: Too much overhead for initial development
- **No formal boundaries**: Risk of tight coupling and big ball of mud

## Consequences

### Positive

- Clear ownership and responsibility for each domain
- Easier to reason about system behavior
- Future microservice extraction is straightforward
- Reduces merge conflicts across teams
- Enables independent testing and deployment later
- Forces explicit API design between domains

### Negative

- Requires discipline and code review enforcement
- Some duplication across modules (DTOs, utilities)
- Learning curve for developers new to DDD
- May feel like over-engineering early on

## Implementation Notes

- Use ArchUnit tests to enforce package dependencies
- Create module-specific README files documenting APIs
- Use Spring's `@Service` and `@Component` with package visibility
- Consider multi-module Maven project structure if modules grow
