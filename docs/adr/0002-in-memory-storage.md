# ADR-002 — In-Memory Storage

## Status

Accepted

## Context

The task explicitly requires storing calculation results only in application memory.

## Decision

An in-memory `RankingStore` based on JVM process memory was implemented.

## Consequences

### Positive

* simplicity
* no database configuration required
* faster implementation

### Negative

* data is lost after application restart
* no shared state between multiple instances

---