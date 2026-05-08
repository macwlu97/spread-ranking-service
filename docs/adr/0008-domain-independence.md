# ADR-008 — Domain Without Spring Dependencies

## Status

Accepted

## Context

DDD and Hexagonal Architecture require isolation of the domain layer from frameworks.

## Decision

The domain layer:

* does not use Spring annotations,
* contains no framework dependencies,
* contains only business logic.

## Consequences

### Positive

* clean domain model
* high testability
* framework independence

### Negative

* requires manual wiring in upper layers
