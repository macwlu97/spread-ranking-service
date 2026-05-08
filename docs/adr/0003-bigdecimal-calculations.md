# ADR-003 — BigDecimal For Financial Calculations

## Status

Accepted

## Context

Spread is a financial value and requires precise calculations.

## Decision

`BigDecimal` was used instead of `double` for all financial calculations.

## Consequences

### Positive

* no floating-point precision errors
* proper financial rounding
* deterministic results

### Negative

* more verbose API

---