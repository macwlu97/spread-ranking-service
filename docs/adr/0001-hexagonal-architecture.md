# ADR-001 — Hexagonal Architecture

## Status

Accepted

## Context

The system integrates with the external Kanga Exchange API and exposes a REST API.
Business logic should remain independent from frameworks and infrastructure concerns.

## Decision

Hexagonal Architecture (Ports & Adapters) was applied:

* `domain` — business logic
* `application` — use case orchestration
* `infrastructure` — external integrations and persistence
* `api` — REST controllers

## Consequences

### Positive

* easy adapter replacement
* high domain testability
* no Spring dependencies in the domain layer
* clear separation of responsibilities

### Negative

* increased number of classes
* more layers compared to a simple CRUD application

---