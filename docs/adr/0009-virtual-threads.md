# ADR-009: Adoption of Virtual Threads for Market Processing

## Context

The system fetches data from external APIs (Market and OrderBook), which introduces I/O latency and sequential delays when processing a large number of markets.

---

## Decision

We adopt **Java 21 Virtual Threads** to enable parallel processing of markets using:

`Executors.newVirtualThreadPerTaskExecutor()`

---

## Rationale

* reduction of latency through parallel I/O-bound execution
* no need for thread pool tuning or sizing strategies
* better scalability compared to platform threads
* simpler model compared to `CompletableFuture` with custom executors

---

## Alternatives considered

* **Sequential processing** (rejected – too slow for current workload)
* **CompletableFuture + ForkJoinPool** (rejected – increased complexity and potential contention)
* **Reactive Streams** (rejected – overengineering for current system scale)

---

## Consequences

### Positive

* ✔ significantly lower response time
* ✔ simpler concurrency model

### Risks / Trade-offs

* ⚠ dependency on external API rate limits
* ⚠ requires per-task fallback handling
* ⚠ potential burst traffic increase toward external providers

---

## Notes

Requires monitoring of:

* API throttling (Kanga)
* timeout behavior
* per-task failure rate
