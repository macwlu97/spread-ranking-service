# Spread Ranking Service

## Overview

A Spring Boot microservice that calculates and ranks cryptocurrency market spreads based on live data from Kanga Exchange.

The service:
- fetches available markets from Kanga API
- retrieves order book data for each market
- calculates spread using the Kanga formula
- groups markets into ranking categories
- exposes REST API secured with Bearer token

---

## Technology Stack

- Java 21
- Spring Boot 3.4.3
- Maven
- BigDecimal for financial precision
- Hexagonal Architecture (Ports & Adapters)

---

## Business Logic

### Spread formula

```

Spread = (ASK - BID) / ((ASK + BID) / 2) * 100%

```

---

### Ranking groups

- **group1** → spread ≤ 2%
- **group2** → spread > 2%
- **group3** → missing or invalid data (no BID/ASK)

---

## API

### Security

All endpoints require Bearer token:

```

Authorization: Bearer ABC123

```

---

## Endpoints

### Calculate ranking

```

POST /api/v1/spread/calculate

```

Triggers fetching data from Kanga API and recalculates ranking (stored in memory).

**Response:**
```

202 Accepted

```

---

### Get ranking

```

GET /api/v1/spread/ranking

````

Returns last calculated ranking.

**Response example:**

```json
{
  "timestamp": "2026-05-08T20:15:30Z",
  "ranking": {
    "group1": [
      { "market": "BTC_USDC", "spread": "1.25" }
    ],
    "group2": [
      { "market": "ETH_USDC", "spread": "3.40" }
    ],
    "group3": [
      { "market": "ABC_USDT", "spread": "N/A" }
    ]
  }
}
````

---

## External APIs

### Market pairs

```
https://public.kanga.exchange/api/market/pairs
```

### Order book

```
https://public.kanga.exchange/api/market/orderbook/{market}
```

---

## How to run

### 1. Build project

```bash
mvn clean install
```

### 2. Run application

```bash
cd spread-ranking
mvn clean spring-boot:run
```

Application runs by default on:

```
http://localhost:8083
```

---

## Example CURL

### Trigger calculation

```bash
curl -X POST http://localhost:8083/api/v1/spread/calculate \
  -H "Authorization: Bearer ABC123"
```

---

### Get ranking

```bash
curl http://localhost:8083/api/v1/spread/ranking \
  -H "Authorization: Bearer ABC123"
```

---

## Architecture

The project follows **Hexagonal Architecture (Ports & Adapters)**:

* `domain` → core business logic (no Spring dependencies)
* `application` → use cases orchestration
* `infrastructure` → external API integration + in-memory storage
* `api` → REST controllers

---

## Design Principles

* Clean Architecture / Hexagonal Architecture
* Domain-Driven Design (DDD)
* SOLID principles
* Immutable domain model
* BigDecimal for financial precision

---

## Notes

* Data is stored only in memory (no database)
* No external persistence layer required
* Designed as a stateless calculation service with cached result store


