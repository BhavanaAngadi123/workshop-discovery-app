# Workshop Discovery

> **🖱️ Clickable prototype:** [bhavanaangadi123.github.io/workshop-discovery-app](https://bhavanaangadi123.github.io/workshop-discovery-app/) — the full app concept as a static mockup (Discover, booking flow, host studio, community feed, Sunny AI concierge, seasonal calendar). Open `index.html` at the repo root. The production Java backend lives in `src/`.

A full-stack Java platform for discovering and booking local hands-on workshops —
pottery, pasta-making, gardening, knitting, chocolate-making, dance, seasonal
activities (pumpkin picking, spring garden tours) and more.

**What makes it different** (vs. booking-only rivals like ClassBento):
- 📸 **Social experience feed** — attendees post photos/reviews; "what people did last weekend" drives discovery
- ☀️ **AI concierge (Sunny)** — Spring AI + local Ollama LLM; handles vague intent ("plan a pottery date")
- ✨ **AI Listing Studio** — hosts describe their idea in plain words; AI writes the listing copy, designs a publicity poster, and sets up booking
- 🍂 **Seasonal calendar** — first-class seasons (autumn harvest, winter magic…) as a re-engagement engine

**Founding principle:** every listing MUST show price, timings/duration, location,
and what's-included — always visible, never "contact for price". The `publish`
endpoint enforces this: no listing goes live without them.

## Stack (verified at build time)

| Component | Version |
|---|---|
| Java | 25.0.4 LTS (Temurin) |
| Spring Boot | 4.1.0 |
| Vaadin (pure-Java UI) | 25.3.1 |
| Spring AI | 1.1.0 (Ollama, local LLM — zero paid APIs) |
| PostgreSQL + Flyway | 42.7.x / Flyway 12 |
| Spring Security + JWT | CUSTOMER / HOST / ADMIN roles |
| Stripe | test mode (29.x Java SDK) |
| OpenAPI | springdoc 3.0.0 (`/swagger-ui.html`) |
| Build | Maven 3.9.11 |

## Quick start

### Prerequisites
- Java 25, Maven 3.9+
- PostgreSQL 15+ (production) — or H2 for a quick smoke test (see below)
- **Ollama** (optional but recommended — AI features degrade gracefully without it)

### 1. Database
```sql
CREATE DATABASE workshops;
CREATE USER workshops WITH PASSWORD 'workshops';
GRANT ALL PRIVILEGES ON DATABASE workshops TO workshops;
```

### 2. Configure
```bash
export DB_URL=jdbc:postgresql://localhost:5432/workshops
export DB_USER=workshops
export DB_PASSWORD=workshops
export APP_JWT_SECRET='<at-least-32-chars-secret>'
# optional:
export OLLAMA_BASE_URL=http://localhost:11434
export OLLAMA_MODEL=llama3.1:8b
export STRIPE_ENABLED=false   # true + STRIPE_SECRET_KEY for live test mode
```

### 3. Run
```bash
mvn verify          # build + tests (H2, no external services needed)
mvn spring-boot:run # dev mode — Vaadin builds the frontend on first start
```
Open http://localhost:8080 — Discover, Experience Feed, AI Concierge, Host Studio.

### Quick smoke test without PostgreSQL
```bash
SPRING_DATASOURCE_URL='jdbc:h2:mem:demo;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;NON_KEYWORDS=USER' \
SPRING_DATASOURCE_USERNAME=sa \
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver \
APP_JWT_SECRET='<32+chars>' \
java -jar target/workshop-discovery-app-0.1.0.jar
```

## AI setup (Ollama — free, local, no API keys)

```bash
# Install: https://ollama.com
ollama pull llama3.1:8b
ollama serve   # listens on localhost:11434
```

- **AI Concierge** (`POST /api/concierge/chat` or the "Ask Sunny" view): grounded by real
  workshop data via Spring AI `@Tool`s (search, seasonal picks, details). Without Ollama
  it falls back to the Java recommendation engine's quick picks.
- **AI Listing Studio** (`POST /api/host/listings/draft`): generates title, description,
  what's-included, price suggestion, picks a poster template, and renders a 1080×1350
  PNG poster with pure Java2D (4 templates: Sunrise, Botanical, Ink, Dusk — no image-gen APIs).

## API overview

| Area | Endpoints |
|---|---|
| Auth | `POST /api/auth/register`, `POST /api/auth/login` → JWT |
| Discovery | `GET /api/workshops?category=&city=&q=&maxPriceCents=&seasonSlug=`, `GET /api/workshops/{id}` |
| Host | `POST /api/host/workshops` (draft), `PUT /api/host/workshops/{id}`, `POST /api/host/workshops/{id}/sessions`, `POST /api/host/workshops/{id}/publish` |
| AI Listing Studio | `GET /api/host/listings/templates`, `POST /api/host/listings/draft`, `POST /api/host/listings/publish` |
| Booking | `POST /api/bookings` (reserve), `GET /api/bookings/mine`, `DELETE /api/bookings/{id}` |
| Payments | `POST /api/payments/intent` (Stripe test mode), `POST /api/payments/webhook`, `POST /api/payments/simulate/{id}` (dev) |
| Feed & reviews | `GET/POST /api/feed`, `POST /api/feed/{id}/like`, `GET/POST /api/workshops/{id}/reviews` |
| Concierge | `POST /api/concierge/chat` `{sessionId, message}` |
| Seasons | `GET /api/seasons` |
| Admin | `POST /api/admin/hosts/{userId}/verify`, `GET /api/admin/audit` |

Full interactive docs: `/swagger-ui.html`.

### Publish rules (enforced)
A workshop can only go live with: **title, price (0 = free, never hidden),
location, what's-included, and ≥1 scheduled session** with real start/end times.
Sessions support any duration — 1-hour, 2-hour, full-day, multi-day.

## What's stubbed / needs input

- **Stripe** runs in test mode by default (`STRIPE_ENABLED=false` → payments are simulated;
  no real charges). Set `STRIPE_ENABLED=true` + `STRIPE_SECRET_KEY` + `STRIPE_WEBHOOK_SECRET`
  for real test-mode charges.
- **Ollama** is optional — the app boots and works without it; AI copy falls back to
  smart templates and the concierge falls back to engine picks.
- **Posters** render with Java2D from 4 built-in templates — no external image APIs.
- **Host verification** is manual (`POST /api/admin/hosts/{id}/verify`); no ID-document
  upload — we deliberately never store ID images/numbers.
- **Booking locks** are in-JVM; fine for a single instance (note for future horizontal scaling).
- Frontend bundle: Vaadin builds it on first `spring-boot:run` (needs Node — auto-downloaded).

## Security notes (OWASP Top 10 self-review)

- **A01 Broken Access Control:** RBAC via Spring Security (`/api/host/**` → HOST/ADMIN,
  `/api/admin/**` → ADMIN); hosts can only mutate their own workshops (ownership checks
  in `WorkshopService`); Vaadin views use `@RolesAllowed`.
- **A02 Cryptographic Failures:** BCrypt-12 passwords; JWT HS256 with ≥32-char secret from
  env (never committed); no sensitive data in logs.
- **A03 Injection:** JPA parameterized queries; Flyway migrations (no dynamic SQL);
  Bean Validation on all inputs; Stripe webhook signature-verified when live.
- **A05 Misconfiguration:** stateless API (`STATELESS` session), CSRF disabled only for
  `/api/**` (JWT bearer), actuator exposes only health/info.
- **A07 AuthN:** JWT expiry (24h default), login throttling via audit log, no default creds.
- **A08 Integrity:** offline-reproducible build; dependencies pinned via BOMs.
- **A09 Logging:** `AuditLog` records auth, bookings, payments, publishes, verifications.

**Production checklist:** set `APP_JWT_SECRET` (32+ chars), use PostgreSQL with TLS,
put Stripe live keys in a vault, run behind HTTPS, set `vaadin.productionMode=true`
with a prebuilt frontend, and back up `posters/` + the DB.
