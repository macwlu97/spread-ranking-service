# ADR-004 — Rounding Strategy

## Status

Accepted

## Context

The task examples present spread values rounded to two decimal places.

## Decision

* calculations are performed using higher internal precision,
* values returned to API consumers are rounded to 2 decimal places using `HALF_UP`.

## Consequences

### Positive

* compliance with business examples
* stable presentation formatting

### Negative

* difference between internal precision and response DTO representation

---