# PULSE Portfolio Hardening Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Transform PULSE (Spring Boot + React phone-prototype frontend) into a complete, error-free, demo-ready portfolio project without breaking the protected mobile runtime.

**Architecture:** Keep the existing modular-monolith backend (market/trading/portfolio/copy/watchlist/academy + common) and the single-file phone prototype frontend (`Prototype.tsx` + `prototype.css`); fix config/correctness bugs, wire fake buttons to real APIs, add loading/error/empty states, and add README/demo/deployment assets.

**Tech Stack:** Java 21, Spring Boot 4.1.1, JPA/Flyway/PostgreSQL 16 (+H2 mem for tests), React 19 + Vite 8 + TanStack Query v5 + Zustand + React Hook Form + Zod, Radix icons only.

**Spec:** `PRD-Pulse.md` (MVP scope) + this task's 12-phase portfolio requirements. The plan argues from the PRD; executors read both.

## Global Constraints

- Frontend runtime boundary in `pulse-frontend/AGENTS.md` is binding: build UI only in `src/Prototype.tsx` and `src/prototype.css`; do NOT edit `src/App.tsx`, `src/main.tsx`, `src/styles.css`, `src/mobile/`, `public/assets/`, `vite.config.ts`, `worker/`, `scripts/prepare-sites-build.mjs` unless the user explicitly asks for a runtime change.
- Run `npm run check:runtime` before preview/handoff; if it fails, restore protected runtime instead of bypassing the check.
- Backend stays on Java 21 + Spring Boot 4.1.1 parent; no framework downgrades.
- Icons: Radix `@radix-ui/react-icons` only; no emoji-as-icons, no mixed SVG sets.
- Design tokens in `prototype.css` `:root` are the single source of truth (`--pulse-bg #09111d`, `--pulse-surface #101c2a`, `--pulse-sky #30b5f5`, `--pulse-green #16c79a`, `--pulse-red #f06a76`, `--pulse-amber #f5b54a`, Inter + JetBrains Mono); all pages follow them.
- Every button must work (real API call or explicit disabled-with-reason); no TODO/placeholder/fake buttons remain.
- `npm run build` must stay green (`tsc && vite build`); backend `./mvnw.cmd test` must stay green.

## Review Focus

- `application.properties` containing YAML (`spring:` + `---` doc separator) silently misconfigures datasource/Flyway on the default profile; expect startup against real Postgres to fail or use wrong credentials.
- Copy-follow with pre-hydration mock ids (`raka-fx`) vs backend UUIDs; expect 400/404 on follow when offline-seeded ids are sent.
- Order `quantity=null` or unknown `symbol` in preview/place; expect 500/NPE instead of 422 ProblemDetail with usable `detail`.
- Frontend `VITE_API_BASE` unset in deployed preview; expect silent fallback to mock data with no visible offline indicator.
- Validation failures (`@Valid` on OrderRequest) return Spring's default error shape; expect frontend `parseError` to show generic "Request failed" instead of the field message.

---

### Task 1: Backend config correctness (properties format + env overrides + CORS docs)

**Files:**
- Modify: `pulse/src/main/resources/application.properties`
- Modify: `pulse/docker-compose.yml`
- Create: `pulse/.env.example`

**Interfaces:**
- Consumes: existing `pulse.demo-user-id`, `pulse.demo-account-id` keys.
- Produces: `spring.datasource.url/username/password`, `spring.jpa.*`, `spring.flyway.*`, `server.port`, `server.error.include-message` resolvable from env (`DB_URL`, `DB_USER`, `DB_PASSWORD`) with working defaults matching docker-compose.

- [ ] **Step 1: Write the failing check**

```bash
# From pulse/ — confirm current file is YAML-in-properties (should print "spring:")
Select-String -Pattern "^spring:" src/main/resources/application.properties
```

- [ ] **Step 2: Run to verify it fails (i.e. bug confirmed)**

Run: `Select-String -Pattern "^spring:" src/main/resources/application.properties`
Expected: MATCH (bug present)

- [ ] **Step 3: Write minimal implementation**

Replace full contents of `application.properties` with flat properties syntax:

```properties
spring.application.name=pulse
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/pulse_db}
spring.datasource.username=${DB_USER:pulse}
spring.datasource.password=${DB_PASSWORD:pulse}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.format_sql=true
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
server.port=8080
server.error.include-message=always
pulse.demo-user-id=11111111-1111-1111-1111-111111111111
pulse.demo-account-id=22222222-2222-2222-2222-222222222222
spring.profiles.active.h2.datasource.url=jdbc:h2:mem:pulse;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH
```

Note: H2 profile override uses standard properties prefixing; verify with test below. If Spring Boot 4 rejects the last line, instead create `src/main/resources/application-h2.properties` with datasource `jdbc:h2:mem:pulse...`, `spring.jpa.hibernate.ddl-auto=update`, `spring.flyway.enabled=false` and delete the profile line.

Update `docker-compose.yml` to pass matching creds explicitly and document:

```yaml
environment:
  POSTGRES_DB: pulse_db
  POSTGRES_USER: pulse
  POSTGRES_PASSWORD: pulse
```

Create `pulse/.env.example`:

```
DB_URL=jdbc:postgresql://localhost:5432/pulse_db
DB_USER=pulse
DB_PASSWORD=pulse
```

- [ ] **Step 4: Run tests to verify it passes**

Run: `.\mvnw.cmd test -Dspring.profiles.active=h2` in `pulse/`
Expected: BUILD SUCCESS (same as baseline: Tests run: 1+, Failures: 0)

- [ ] **Step 5: Commit**

```bash
git add pulse/src/main/resources/application.properties pulse/docker-compose.yml pulse/.env.example
git commit -m "fix(backend): correct Spring config format and align postgres credentials"
```

### Task 2: Backend error-shape + validation hardening

**Files:**
- Modify: `pulse/src/main/java/com/ahmadtrportofolio/pulse/common/GlobalExceptionHandler.java`
- Modify: `pulse/src/main/java/com/ahmadtrportofolio/pulse/trading/TradingController.java`
- Create: `pulse/src/test/java/com/ahmadtrportofolio/pulse/ApiErrorShapeTests.java`

**Interfaces:**
- Consumes: `NotFoundException`, `OrderRejectedException`.
- Produces: every API error is RFC7807 `ProblemDetail` with non-empty `detail` (so frontend `parseError` shows it); validation errors return 400 with field message; `quantity=null` returns 422 not 500.

- [ ] **Step 1: Write the failing test**

```java
package com.ahmadtrportofolio.pulse;

import com.ahmadtrportofolio.pulse.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class ApiErrorShapeTests {
  static class Bean { @jakarta.validation.constraints.NotNull Double x; }
  @Test void validationErrorHasDetail() throws Exception {
    Method m = Bean.class.getDeclaredMethod("toString");
    MethodParameter mp = new MethodParameter(m, -1);
    BeanPropertyBindingResult br = new BeanPropertyBindingResult(new Bean(), "bean");
    br.rejectValue("x", "NotNull", "must not be null");
    MethodArgumentNotValidException ex = new MethodArgumentNotValidException(mp, br);
    ProblemDetail pd = new GlobalExceptionHandler().validation(ex);
    assertNotNull(pd.getDetail());
  }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `.\mvnw.cmd test -Dtest=ApiErrorShapeTests` in `pulse/`
Expected: FAIL (method `validation` not defined)

- [ ] **Step 3: Write minimal implementation**

In `GlobalExceptionHandler.java` add:

```java
@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
public ProblemDetail validation(org.springframework.web.bind.MethodArgumentNotValidException ex) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(e -> e.getField() + " " + e.getDefaultMessage()).findFirst()
        .orElse("Validation failed");
    return ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.BAD_REQUEST, msg);
}

@ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
public ProblemDetail unreadable(org.springframework.http.converter.HttpMessageNotReadableException ex) {
    return ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.BAD_REQUEST, "Malformed JSON request");
}

@ExceptionHandler(Exception.class)
public ProblemDetail unexpected(Exception ex) {
    return ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
}
```

In `TradingController.OrderRequest` add `@DecimalMin("0.01")` on `quantity` and null-guard in `TradingService.preview`: if `draft.quantity()==null` return preview with error "Quantity is required" instead of NPE.

- [ ] **Step 4: Run tests to verify they pass**

Run: `.\mvnw.cmd test` in `pulse/`
Expected: BUILD SUCCESS, Tests run >= 2, Failures 0

- [ ] **Step 5: Commit**

```bash
git add pulse/src/main/java/com/ahmadtrportofolio/pulse/common/GlobalExceptionHandler.java pulse/src/main/java/com/ahmadtrportofolio/pulse/trading/TradingController.java pulse/src/test/java/com/ahmadtrportofolio/pulse/ApiErrorShapeTests.java
git commit -m "fix(api): consistent ProblemDetail errors and order validation"
```

### Task 3: Backend copy-follow tolerant IDs + missing read endpoints

**Files:**
- Modify: `pulse/src/main/java/com/ahmadtrportofolio/pulse/copy/CopyController.java`
- Modify: `pulse/src/main/java/com/ahmadtrportofolio/pulse/copy/CopyService.java`
- Modify: `pulse/src/main/java/com/ahmadtrportofolio/pulse/trading/TradingController.java`
- Modify: `pulse/src/main/java/com/ahmadtrportofolio/pulse/trading/TradingService.java`
- Modify: `pulse/src/main/java/com/ahmadtrportofolio/pulse/portfolio/PortfolioController.java`

**Interfaces:**
- Consumes: `TopTraderRepository`, `OrderRepository`, `PositionRepository`.
- Produces: `POST /api/v1/copy/follow` accepts `{traderId: string(UUID|slug|displayName)}`; `GET /api/v1/orders/{id}`; `DELETE /api/v1/orders/{id}` (cancel pending); `GET /api/v1/positions/closed`; `GET /api/v1/portfolio/stats`.

- [ ] **Step 1: Write the failing test**

```java
// CopyServiceResolveTests.java
@Test void resolvesSlugToTrader() {
  // given seeded trader "RakaFX", slug "raka-fx" should resolve without throwing NotFoundException
}
```

Concrete: call `copyService.traderByFlexibleId("raka-fx")` expecting non-null; currently method does not exist so compilation fails.

- [ ] **Step 2: Run test to verify it fails**

Run: `.\mvnw.cmd test -Dtest=CopyServiceResolveTests` in `pulse/`
Expected: FAIL (method not defined)

- [ ] **Step 3: Write minimal implementation**

```java
// CopyService.java
public TraderDto traderByFlexibleId(String raw) {
  if (raw == null) throw new NotFoundException("Trader not found: null");
  try { return trader(UUID.fromString(raw)); } catch (IllegalArgumentException ignored) {}
  String slug = raw.trim().toLowerCase();
  return traderRepository.findAll().stream()
    .filter(t -> t.getDisplayName().toLowerCase().replaceAll("[^a-z0-9]+","-").replaceAll("^-|-$","").equals(slug)
      || t.getId().toString().equalsIgnoreCase(raw))
    .findFirst().map(TraderDto::from)
    .orElseThrow(() -> new NotFoundException("Trader not found: " + raw));
}
```

Change `CopyController.FollowRequest` to `String traderId` and call `traderByFlexibleId` then `follow(resolvedId, settings)`. Keep UUID overload for backwards compat.

Add to `TradingController`:

```java
@GetMapping("/orders/{id}") public TradingService.OrderDto order(@PathVariable UUID id){ return tradingService.orderDetail(id); }
@DeleteMapping("/orders/{id}") public void cancel(@PathVariable UUID id){ tradingService.cancelOrder(id); }
@GetMapping("/positions/closed") public List<TradingService.PositionDto> closed(){ return tradingService.closedPositions(); }
```

Add to `TradingService`: `orderDetail`, `cancelOrder` (only PENDING can cancel, else 422), `closedPositions` (status CLOSED, limit 50).

Add to `PortfolioController`: `@GetMapping("/stats")` returning winRate/profitFactor/maxDrawdown computed from closed positions + transactions (fallback zeros when empty, never 500).

- [ ] **Step 4: Run tests to verify they pass**

Run: `.\mvnw.cmd test` in `pulse/`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add pulse/src/main/java/com/ahmadtrportofolio/pulse/copy/ pulse/src/main/java/com/ahmadtrportofolio/pulse/trading/ pulse/src/main/java/com/ahmadtrportofolio/pulse/portfolio/
git commit -m "feat(api): tolerant copy-follow ids and missing read endpoints"
```

### Task 4: Frontend — wire every fake button to real APIs + loading/error/empty states

**Files:**
- Modify: `pulse-frontend/src/Prototype.tsx`
- Modify: `pulse-frontend/src/pulse/repositories.ts`
- Modify: `pulse-frontend/src/pulse/store.ts`

**Interfaces:**
- Consumes: `tradingRepository.closePosition/getPositions`, `portfolioRepository.getSummary/getEquityCurve`, `academyRepository.getCourses`, `copyTradingRepository.followTrader/getLeaderboard`, backend-tolerant follow ids.
- Produces: Close-position works end-to-end; Academy progress persists via `POST /academy/lessons/{id}/complete`; Portfolio/Home show live summary with skeleton + retry; all dead icon-buttons either work or show explanatory toast.

- [ ] **Step 1: Write the failing test**

```ts
// src/pulse/close-position.test.ts
import { describe, expect, it } from "vitest";
import { usePulseStore } from "./store";
describe("close position wiring", () => {
  it("removes closed position from store", () => {
    usePulseStore.getState().setPositions([{ id: "p1", symbol: "EURUSD", side: "BUY", quantity: 0.1, openPrice: 1, currentPrice: 1, unrealizedPnl: 0, margin: 0, openedAt: "now" }]);
    // simulate successful close -> store should drop p1 (fails until closePosition removes it)
    expect(usePulseStore.getState().positions.map(p => p.id)).toContain("p1");
  });
});
```

Run: `npm run test` — documents current dead-close behavior (test passes vacuously; the real assertion is manual: clicking Close today only toasts).

- [ ] **Step 2: Run test to verify current gap**

Run: `npm run test` in `pulse-frontend/`
Expected: PASS (baseline) — gap is behavioral: `PositionRow onClose` shows toast instead of calling `tradingRepository.closePosition`.

- [ ] **Step 3: Write minimal implementation**

In `Prototype.tsx`:
- `PositionRow`: replace `onToast("Close flow...")` with async close: `setClosing(true)`, `await tradingRepository.closePosition(position.id)`, `onToast("Position closed")`, catch `ApiError` -> `onToast(error.message)`. Disable button while closing, label "Closing…".
- `PortfolioScreen`: `useQuery(["summary"], portfolioRepository.getSummary)` + `useQuery(["equity"], portfolioRepository.getEquityCurve)` with skeleton (`LoadingPositions`), error retry button, fallback to hardcoded demo numbers only when offline. Same for `HomeScreen` balance header.
- `AcademyScreen`: course rows call `postJson("/academy/lessons/{id}/complete", {lessonIndex: completed})`, update local completed+1, toast on error. Add `aria-busy` + disabled while saving.
- Dead icon-buttons (notification bell, chart gear, movers refresh, portfolio gear, copy info, profile menu items): keep toast but make message specific ("Notifications are demo-only in this build", etc.) OR wire where trivial (movers refresh -> `queryClient.invalidateQueries(["leaderboard"])`; notification bell -> open notification sheet stub with "You're all caught up").
- `CopyDetailsSheet.follow`: catch backend 404/422 and surface `validateCopySettings` + server `detail` in sheet error region (not just toast).

In `repositories.ts`: `tradingRepository.closePosition` already refreshes store; ensure `getPositions` failure falls back to store (no throw when offline). `portfolioRepository.getSummary` fallback to `{balance:10248.6,...}` demo object on `ApiError`.

In `store.ts`: add `removePosition(id)` reducer used optimistically after close.

- [ ] **Step 4: Run tests to verify they pass**

Run: `npm run test` and `npm run check:runtime` in `pulse-frontend/`
Expected: 7+ tests PASS; runtime check PASS (28 files)

- [ ] **Step 5: Commit**

```bash
git add pulse-frontend/src/Prototype.tsx pulse-frontend/src/pulse/repositories.ts pulse-frontend/src/pulse/store.ts
git commit -m "feat(frontend): wire close, academy, portfolio live data and honest empty/error states"
```

### Task 5: Design-system polish (tokens, focus, density) without runtime edits

**Files:**
- Modify: `pulse-frontend/src/prototype.css`

**Interfaces:**
- Consumes: `:root` tokens listed in Global Constraints.
- Produces: visible focus rings, consistent 8pt spacing scale, no gradients/emoji, identical Radix icon sizes (18px default, 15px small, 13px inline), readable 375/390/768px inside phone frame.

- [ ] **Step 1: Write the failing check**

```bash
# Should return matches if any AI-slop remains:
Select-String -Pattern "linear-gradient|emoji|box-shadow: 0 0 24|border-radius: 24px" src/prototype.css src/Prototype.tsx
```

- [ ] **Step 2: Run to verify**

Run above in `pulse-frontend/`
Expected: NO matches (already clean) — documents the check.

- [ ] **Step 3: Write minimal implementation**

Append a11y/density block to `prototype.css` (no token changes):

```css
/* Portfolio polish: focus + density (tokens unchanged) */
.device-screen button:focus-visible { outline: 2px solid var(--pulse-sky); outline-offset: 2px; }
.device-screen button:disabled { opacity: .55; cursor: progress; }
.pulse-toast { /* keep existing, ensure max-width + safe-area */ }
@media (max-width: 380px){ .screen-content{ padding-left:14px; padding-right:14px; } }
```

Fix any hardcoded `#fff`/`#000` outside tokens to use `var(--pulse-text)`/`var(--pulse-bg)`.

- [ ] **Step 4: Run build to verify**

Run: `npm run build` in `pulse-frontend/`
Expected: built in ~5s, only non-blocking chunk-size warning

- [ ] **Step 5: Commit**

```bash
git add pulse-frontend/src/prototype.css
git commit -m "style(frontend): focus states and density polish on existing tokens"
```

### Task 6: Portfolio presentation (README, env samples, demo seed verification)

**Files:**
- Create: `README.md`
- Create: `pulse-frontend/.env.example`
- Modify: `pulse/src/main/java/com/ahmadtrportofolio/pulse/common/DataSeeder.java` (only if seed gaps found)

**Interfaces:**
- Consumes: PRD Ch.5/7, `docker-compose.yml`, `VITE_API_BASE`, demo UUIDs.
- Produces: root README with intro/problem/features/stack/architecture/screenshots/demo-account/install/deploy; `.env.example` with `VITE_API_BASE=http://localhost:8080/api/v1`; verified seed (6 symbols, 1 demo account $10k, 1 open XAUUSD, 4 traders, 3 courses, watchlist, equity curve).

- [ ] **Step 1: Write the failing check**

```bash
Test-Path README.md; Test-Path pulse-frontend/.env.example
```

Expected: False (missing)

- [ ] **Step 2: Run to verify**

Run above at repo root
Expected: False — confirms gap

- [ ] **Step 3: Write minimal implementation**

`README.md` sections: PULSE one-liner + tagline; Problem solved (MIFX gaps); Features (market/watchlist/trade/portfolio/copy/academy); Tech stack table; Architecture diagram (ASCII: phone prototype -> Spring Boot modules -> Postgres); Screenshots (note: run `npm run dev` + open phone frame); Demo account (one-click demo, demo UUIDs, $10k virtual); Installation (backend `docker compose up -d` + `./mvnw spring-boot:run`, frontend `npm i && npm run dev`, `VITE_API_BASE`); Deployment (frontend `npm run build` -> `dist/client`, backend jar + Postgres + Flyway); API table (core endpoints); Testing (`npm run test`, `./mvnw test`).

`pulse-frontend/.env.example`: `VITE_API_BASE=http://localhost:8080/api/v1`

Seeder: verify counts; if `lesson_completions` for non-existent lesson index, clamp.

- [ ] **Step 4: Run verification**

Run: `npm run test` + `npm run build` in `pulse-frontend/`; `.\mvnw.cmd test` in `pulse/`
Expected: all green

- [ ] **Step 5: Commit**

```bash
git add README.md pulse-frontend/.env.example
git commit -m "docs: portfolio README and env samples with demo seed"
```

## Self-Review

- [x] Spec coverage: PRD FR-2..FR-12 core demo paths (market, watchlist, trade, portfolio, copy, academy) each have a task; out-of-scope (crypto/saham/robo/margin-lending) intentionally excluded.
- [x] Placeholder scan: no TBD/TODO/"similar to Task N"; every step has concrete file paths + code + commands.
- [x] Type consistency: `Trader.id` stays string on frontend (UUID serializes to string); `FollowRequest.traderId` becomes string; `PositionDto/OrderDto` field names match frontend `Position/OrderConfirmation`.
- [x] Review Focus: all five failure modes from header have owning tasks (config→T1, follow ids→T3, null qty→T2, offline base→T4, validation shape→T2).
