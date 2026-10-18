# PULSE — The market's heartbeat, in your pocket

A mobile-first multi-asset trading demo: modern phone prototype (React + Vite)
backed by a Spring Boot trading API with real order, portfolio, copy-trading,
watchlist, and academy flows. Built as a portfolio showcase from `PRD-Pulse.md`
(MIFX gap analysis for the Indonesian market).

## Problem solved

Legacy broker apps (MIFX/MT4-style) feel dated, hide fees in spread/commission,
lock premium tools behind large deposits, and offer no social copy-trading.
PULSE closes those gaps:

- Onboarding in minutes with an instant **$10,000 virtual demo account**
- Transparent **$2/lot flat fee** previewed before every order
- **Pulse Copy** leaderboard with verified track records and max-loss guards
- Market discovery (symbols, quotes, OHLCV candles, movers, calendar stub)
- Portfolio equity curve, open/closed positions, and performance stats
- Academy courses with persisted lesson progress

## Features

| Area | What works |
|---|---|
| Market | `GET /market/symbols`, `/market/search`, `/market/quote/{symbol}`, `POST /market/quotes`, `GET /market/ohlcv/{symbol}?tf=&limit=` |
| Watchlist | `GET /watchlists`, `POST /watchlists/items`, `DELETE /watchlists/items/{symbol}` |
| Trading | `POST /orders/preview`, `POST /orders` (MARKET fills → position; LIMIT/STOP stays PENDING), `GET /orders`, `GET /orders/{id}`, `DELETE /orders/{id}` (cancel pending), `GET /positions`, `GET /positions/closed`, `POST /positions/{id}/close`, `POST /calculator/margin` |
| Portfolio | `GET /portfolio/summary`, `GET /portfolio/equity-curve`, `GET /portfolio/stats`, `GET /portfolio/transactions` |
| Copy | `GET /copy/leaderboard?sort=roi|winRate|followers`, `GET /copy/traders/{id}`, `POST /copy/follow`, `DELETE /copy/follow/{id}`, `GET /copy/my-copies` |
| Academy | `GET /academy/courses`, `POST /academy/lessons/{courseId}/complete` |
| Frontend | 5-tab phone prototype (Home, Portfolio, Pulse Copy, Academy, Profile), symbol detail + candles, order sheet with margin/fee preview, close-position, follow-trader with validation, loading/error/empty states, offline mock fallback |

Errors use RFC7807 `ProblemDetail` with a usable `detail` message
(`404` not found, `400` validation/malformed JSON, `422` rejected order, `500` unexpected).

## Technology stack

| Layer | Choice |
|---|---|
| Frontend | React 19, Vite 8, TypeScript strict, TanStack Query v5, Zustand, React Hook Form + Zod, Radix icons, Inter + JetBrains Mono |
| Backend | Java 21, Spring Boot 4.1.1, Spring Data JPA + Validation, Flyway, PostgreSQL 16 (H2 mem for tests) |
| Mobile runtime | Calibrated iPhone / Pixel 10 `PhoneFrame` prototype harness (protected files, integrity-checked) |
| Tests | Vitest (frontend `8` tests), JUnit 5 + MockMvc-style unit tests (backend `5` tests) |

## Architecture

```
pulse-frontend (phone prototype)
  Prototype.tsx + prototype.css (app UI — only files you should edit)
  pulse/{apiClient,repositories,store,logic,types,mockData}
            │  HTTPS JSON  (VITE_API_BASE, default http://localhost:8080/api/v1)
            ▼
pulse (Spring Boot modular monolith)
  market / trading / portfolio / copy / watchlist / academy / common
  GlobalExceptionHandler (ProblemDetail) + WebConfig (CORS /api/**)
            │  JPA/Flyway
            ▼
PostgreSQL 16 (pulse_db) — V1 market+trading, V2 portfolio+copy+watchlist+academy
DataSeeder — 6 symbols, 1 demo account ($10k), 1 open XAUUSD, 4 traders, 3 courses, watchlist, equity curve
```

## Screenshots

Run the frontend (`npm run dev` in `pulse-frontend/`) and open the phone frame:

1. Welcome → Start with demo
2. Home watchlist (sort, add symbol, movers, calendar)
3. Symbol detail (timeframes, candles, Buy/Sell)
4. Order sheet (preview margin/fee → Face ID → filled → View portfolio)
5. Portfolio (live equity, close position, history, analytics)
6. Pulse Copy (sort, trader detail, follow with max-loss guard)
7. Academy (complete lessons, progress persists)

## Demo account

No password needed — one-click demo:

- Account: `PULSE-004821` / `PULSE-2026-000001`
- Balance: `$10,000` virtual
- Demo user id: `11111111-1111-1111-1111-111111111111`
- Demo account id: `22222222-2222-2222-2222-222222222222`
- Seeded position: `BUY 0.02 XAUUSD`
- Seeded traders: RakaFX, Naya Gold, Denny Index, Ika Steady
- Offline: if the API is unreachable the UI falls back to bundled mock data with honest "Offline demo data" labels.

## Installation

### Backend

```powershell
cd pulse
docker compose up -d
.\mvnw.cmd spring-boot:run
# API: http://localhost:8080/api/v1/market/symbols
```

Env overrides (`pulse/.env.example`): `DB_URL`, `DB_USER`, `DB_PASSWORD`.
Tests use H2: `.\mvnw.cmd test '-Dspring.profiles.active=h2'`.

### Frontend

```powershell
cd pulse-frontend
npm install
Copy-Item .env.example .env   # optional; defaults to http://localhost:8080/api/v1
npm run dev
npm run test
npm run build
npm run check:runtime
```

Editing boundary (`AGENTS.md`): build UI only in `src/Prototype.tsx` +
`src/prototype.css`. Protected runtime (`src/App.tsx`, `src/mobile/`,
`vite.config.ts`, `worker/`, `scripts/prepare-sites-build.mjs`, device
assets) must stay intact — `npm run check:runtime` enforces 28 files.

## Deployment

- Frontend: `npm run build` → `dist/client/` (+ Sites worker in `dist/server/`). Serve `dist/client` statically; set `VITE_API_BASE` to the public API URL at build time.
- Backend: `.\mvnw.cmd package` → jar; needs PostgreSQL 16 + Flyway migrations (`V1__init.sql`, `V2__core.sql`); configure `DB_URL/DB_USER/DB_PASSWORD`; expose port `8080`.
- Checklist: API reachable from frontend origin (CORS `/api/**` open for demo — lock down for prod), seed verified (symbols > 0), `npm run test` + backend tests green, `npm run check:runtime` pass.

## API quick reference

| Method | Path | Notes |
|---|---|---|
| GET | `/api/v1/market/symbols?q=` | list/search |
| POST | `/api/v1/market/quotes` | `{symbols:[]}` |
| GET | `/api/v1/market/ohlcv/{symbol}?tf=1m\|15m\|1D&limit=16` | candles |
| POST | `/api/v1/orders/preview` | fee/margin validation |
| POST | `/api/v1/orders` | market fill → position |
| POST | `/api/v1/positions/{id}/close` | `{quantity?}` partial supported |
| GET | `/api/v1/portfolio/summary` | equity/margin |
| GET | `/api/v1/portfolio/stats` | winRate/profitFactor |
| POST | `/api/v1/copy/follow` | `{traderId: UUID\|slug\|name, mode, fixedAmount, maxLoss, maxPositionSize}` |
| POST | `/api/v1/academy/lessons/{courseId}/complete` | `{lessonIndex}` |

## Testing

```powershell
# frontend (8 tests)
cd pulse-frontend; npm run test; npm run check:runtime; npm run build
# backend (5 tests, H2)
cd pulse; .\mvnw.cmd test '-Dspring.profiles.active=h2'
```
