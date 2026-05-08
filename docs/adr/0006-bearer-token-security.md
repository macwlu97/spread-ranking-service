# ADR-006 — Bearer Token Security

## Status

Accepted

## Context

Endpoints must be secured using the `Bearer ABC123` token.

## Decision

A simple authorization mechanism based on a Spring Security filter was implemented.

## Consequences

### Positive

* fulfills task requirements
* minimal complexity

### Negative

* no full OAuth2/JWT implementation
* static token

---