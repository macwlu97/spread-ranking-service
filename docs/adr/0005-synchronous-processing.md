# ADR-005 — Synchronous Processing

## Status

Accepted

## Context

The recruitment task has a limited scope and no high-performance requirements.

## Decision

Synchronous processing was chosen without:

* Kafka,
* async pipelines,
* virtual threads,
* reactive streams.

## Consequences

### Positive

* simpler implementation
* easier debugging
* faster delivery

### Negative

* lower scalability for very large numbers of markets

---