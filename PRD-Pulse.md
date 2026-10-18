# Product Requirements Document (PRD)
# **PULSE** — Aplikasi Trading Multi-Asset Mobile

| Field | Detail |
|---|---|
| **Product Name** | **PULSE** |
| **Tagline** | *"The market's heartbeat, in your pocket"* |
| **Document Version** | 1.0 (Initial Draft — Derived from MIFX Analysis) |
| **Date** | 27 Agustus 2026 |
| **Author** | Mavis (Root Session) |
| **Status** | Draft for Review |
| **Target Release (MVP)** | Q1 2027 |
| **Platform** | Mobile (iOS & Android) via Expo |
| **Backend Stack** | Java Spring Boot 3.x + PostgreSQL 16 |
| **Market** | Indonesia (BAPPEBTI-regulated), extensible to SEA |
| **Reference Analysis** | Based on MIFX (PT Monex Investindo Futures) feature gap analysis |

---

## 1. Executive Summary

**PULSE** adalah aplikasi mobile trading multi-aset generasi baru yang dirancang untuk trader Indonesia — dari pemula hingga profesional. Berbeda dari kompetitor legacy (MIFX, Gainscope, dll) yang mengandalkan platform MT4/MT5, PULSE membangun UX mobile-first dengan teknologi modern, fee transparan, dan AI-assisted trading tools.

PULSE mengambil pelajaran dari MIFX (PT Monex Investindo Futures — broker forex lokal terlama, 25+ tahun) namun menutup gap-gap yang ada di pasar:
- UX modern & onboarding < 5 menit
- Social copy-trading (tidak ada di MIFX)
- Fee lebih kompetitif (MIFX $5-$20/lot, PULSE target $2/lot flat)
- AI insights & smart alerts
- Multi-aset roadmap (forex → commodities → indices → saham → crypto)

**Tech Stack** (sama dengan PRD sebelumnya, terbukti untuk fintech):
- **Frontend**: Expo SDK 53+ (React Native) — cross-platform iOS + Android
- **Backend**: Java Spring Boot 3.3 — modular monolith (Spring Modulith)
- **Database**: PostgreSQL 16 + TimescaleDB untuk tick data

**Goal 12 bulan**: 100.000 user, AUM USD 50 juta, 5.000 daily active trader.

---

## 2. Analisis Kompetitor: MIFX Deep-Dive

### 2.1 Profil MIFX

| Field | Detail |
|---|---|
| **Brand** | MIFX (d/h Monex Investindo Futures) |
| **Owner** | PT Monex Investindo Futures |
| **Pengalaman** | 25+ tahun (sejak 2000-an) |
| **Regulasi** | BAPPEBTI, OJK, BI, ISO 27001 |
| **App Rating** | Google Play 4.0/5 (17.6K review), App Store 4.1/5 (13.6K review) |
| **Downloads** | 1 juta+ di Play Store |
| **Trustpilot** | 2.5/5 (7 review — buruk tapi sampel kecil) |
| **Penghargaan** | ICDX Broker Award 2024 & 2025, JFX Award 2015 |
| **Kantor** | Sahid Sudirman Center Lt. 17, Jl. Sudirman Kav. 86, Jakarta |

### 2.2 MIFX — Strengths (yang Harus Dipertahankan di PULSE)

| # | Strength | Implikasi untuk PULSE |
|---|---|---|
| 1 | **Regulasi kuat** (BAPPEBTI + OJK + BI + ISO 27001) | PULSE juga harus terdaftar BAPPEBTI & ISO 27001 |
| 2 | **Segregated account** (rekening terpisah) | Wajib ada di PULSE — dana klien tidak tercampur |
| 3 | **Demo $10,000 virtual** | Pertahankan, ideal untuk onboarding |
| 4 | **MIFX Academy** (edukasi gratis) | Versi PULSE: "Pulse Academy" dengan konten interaktif |
| 5 | **MIFX Rewards** (loyalty point) | Adaptasi: "Pulse Points" + tier (Bronze/Silver/Gold/Platinum) |
| 6 | **Multi-platform** (App, MT4, MT5, Web, Desktop) | MVP fokus App + Web, MT4/MT5 optional post-MVP |
| 7 | **Multiple account types** (5 tier) | Adaptasi: 4 tier (Standard, Pro, Pro+, Elite) |
| 8 | **Multiple deposit methods** (bank, VA, e-wallet, QRIS) | Pertahankan semua, tambah QRIS langsung |
| 9 | **Trading tools** (Signal, Top Mover, Top Volume) | Adaptasi + tambah AI-powered version |
| 10 | **24/5 customer support** (chat, WhatsApp, telpon) | Pertahankan + tambah in-app help center |
| 11 | **Low minimum deposit** (Rp 500K) | Pertahankan (Rp 500K atau bahkan Rp 100K di tier lite) |
| 12 | **No requote, instant execution** | Wajib, pakai matching engine low-latency |

### 2.3 MIFX — Weaknesses (Gap yang Akan Di-close PULSE)

| # | Weakness | Solusi PULSE |
|---|---|---|
| 1 | **App UX terasa jadul** (review: "susah dipahami pemula", "tampilan kuno") | Modern UI, design system fresh, onboarding interaktif |
| 2 | **Leverage max 1:100** untuk retail (regulasi BAPPEBTI) | Sama, tapi transparan jelaskan di awal |
| 3 | **Fee tidak transparan** (tersembunyi di spread & komisi) | Fee calculator real-time sebelum order, semua harga all-in |
| 4 | **Premium tools dikunci** deposit besar (Autochartist butuh Pro $100K) | Semua tools bisa diakses, premium features = lebih banyak insight |
| 5 | **Tidak ada social/copy trading** | PULSE: "Pulse Copy" — copy trader top performer |
| 6 | **Customer support tidak 24/7** (weekend tutup) | PULSE target 24/7 dengan AI chatbot + weekend escalation |
| 7 | **Tidak ada saham/kripto** | Roadmap: V2 tambah saham ID, V2.5 kripto |
| 8 | **Manual deposit confirmation** (perlu upload bukti transfer) | Auto-confirm via virtual account & open banking API |
| 9 | **E-wallet deposit ada admin fee** | Nego rate lebih baik atau subsidi sebagian |
| 10 | **Tidak ada analytics portfolio advanced** | PULSE: "Pulse Analytics" — heat map, korelasi, drawdown |
| 11 | **Limited instant withdrawal bank** | PULSE: instant withdrawal ke 5+ bank utama |
| 12 | **Trust issue** (Trustpilot 2.5/5) | Public audit, transparansi, verifiable reserves |

### 2.4 Positioning PULSE vs MIFX

| Aspek | MIFX | PULSE |
|---|---|---|
| Target | Trader tradisional, B2C | Gen-Z & millennials, mobile-first |
| Onboarding | 1-3 hari (verifikasi telpon) | < 5 menit (e-KYC + e-Sign) |
| UX | Functional, jadul | Modern, smooth, animated |
| Tools | Trading Signal (manual) | AI signals + social insights |
| Community | Tidak ada | "Pulse Feed" — leaderboard, top traders |
| Fee | $1-$20/lot, opaque | $2/lot flat, transparan all-in |
| Assets | Forex, komoditi, indeks | + saham ID (V2), kripto (V2.5) |
| Education | MIFX Academy (video) | Pulse Academy (interaktif, gamified) |
| Rewards | Points → hadiah | Points + cashback + NFT badge |

---

## 3. Target Users & Personas

### 3.1 Primary Persona — "Raka, Trader Mobile-First" (40% user)

| Atribut | Detail |
|---|---|
| **Nama** | Raka, 28 tahun |
| **Pekerjaan** | Software engineer, gaji Rp 18 jt/bulan |
| **Pengalaman trading** | 2 tahun, mulai dari MT4, sering kesal dengan UI |
| **Pain points** | MT4 jadul, app MIFX lambat, tidak ada social proof, susah belajar dari trader lain |
| **Goals** | Side income Rp 5-10 jt/bulan dari trading, sambil kerja utama |
| **Kebutuhan** | UX modern, eksekusi cepat, social feed, copy trader berpengalaman |
| **Channel** | Twitter/X, Telegram trading, YouTube, Discord |
| **Device** | iPhone 15 Pro, Android (Samsung S24) |

### 3.2 Secondary Persona — "Sinta, Pemula yang Berani" (30% user)

| Atribut | Detail |
|---|---|
| **Nama** | Sinta, 24 tahun |
| **Pekerjaan** | Fresh graduate, kerja part-time |
| **Pengalaman trading** | 0, baru denger dari TikTok |
| **Pain points** | Takut rugi, tidak ngerti istilah, modal kecil, gaptek |
| **Goals** | Belajar trading dari Rp 100rb, target Rp 500rb/bulan |
| **Kebutuhan** | Onboarding yang sabar, demo wajib, edukasi ringan, rekomendasi siap copy |
| **Channel** | TikTok finance creator, Instagram, grup WhatsApp keluarga |
| **Device** | iPhone 12, Xiaomi Redmi Note 12 |

### 3.3 Tertiary Persona — "Pak Denny, Trader Korporat" (20% user)

| Atribut | Detail |
|---|---|
| **Nama** | Denny, 42 tahun |
| **Pekerjaan** | Manager finance di perusahaan multinasional |
| **Pengalaman trading** | 8 tahun, sudah pakai 3 broker sebelumnya |
| **Pain points** | Butuh platform serius, eksekusi cepat, leverage kompetitif, laporan pajak |
| **Goals** | Hedging portofolio, target return 15%/tahun |
| **Kebutuhan** | Pro tools, kalender ekonomi, news premium, tax statement |
| **Channel** | Bloomberg, Reuters, email newsletter |
| **Device** | iPhone 15 Pro Max + iPad Pro |

### 3.4 Quaternary Persona — "Mbak Ika, Ibu Rumah Tangga Trader" (10% user)

| Atribut | Detail |
|---|---|
| **Nama** | Ika, 38 tahun |
| **Pekerjaan** | IRT, suami bisnis |
| **Pengalaman trading** | 1 tahun, coba-coba |
| **Pain points** | Waktu terbatas, trading harus efisien, butuh automasi |
| **Goals** | Passive income, trading 30 menit/hari |
| **Kebutuhan** | Auto-trade/copy, alert, ringkasan harian, no FOMO |
| **Channel** | Instagram, YouTube, marketplace komunitas |
| **Device** | iPhone 14, Android Oppo |

---

## 4. Goals, Success Metrics, dan KPIs

### 4.1 Business Goals (12 bulan)

| Goal | Target |
|---|---|
| Downloads (Play Store + App Store) | 500.000 |
| Registered users (KYC complete) | 100.000 |
| Monthly Active Users (MAU) | 40.000 |
| Daily Active Traders (DAT) | 8.000 |
| Funded accounts (deposit ≥ 1x) | 30.000 |
| Total AUM | USD 50 juta (~ Rp 780 miliar) |
| Retention D7 | 45% |
| Retention D30 | 30% |
| Net Promoter Score (NPS) | ≥ 55 |

### 4.2 Product KPIs

| KPI | Target | Cara Ukur |
|---|---|---|
| Onboarding completion rate | ≥ 80% | Backend funnel analytics |
| Onboarding time (p50) | ≤ 5 menit | Time tracking in-app |
| Demo → Live conversion (D30) | ≥ 25% | Cohort analysis |
| Order placement success rate | ≥ 99.5% | Backend + mobile crashlytics |
| Real-time quote latency (p95) | ≤ 300 ms | WebSocket RTT metrics |
| Order execution (tap → ACK) | ≤ 1.2 detik | APM |
| App crash-free sessions | ≥ 99.7% | Sentry/Crashlytics |
| API p99 latency | ≤ 600 ms | Datadog/APM |
| Copy-trade followers growth | +5.000/bulan | Internal metric |
| Pulse Points claim rate | ≥ 60% | Engagement metric |

### 4.3 Anti-Goals (MVP TIDAK Termasuk)

- ❌ Trading derivatif kompleks (options, futures spread)
- ❌ Crypto (V2.5 — butuh Bappebti crypto license terpisah)
- ❌ Saham Indonesia/US (V2 — perlu OJK/sekuritas partnership)
- ❌ Robo-advisor otomatis
- ❌ Margin lending (V3)
- ❌ Komunitas open-forum (moderation berat)
- ❌ OTC / large-block trading

---

## 5. Functional Requirements

### 5.1 P0 — MVP Core

#### FR-1: Onboarding & KYC
- **FR-1.1** Registrasi: email + nomor HP (OTP WhatsApp + SMS)
- **FR-1.2** e-KYC: foto KTP + selfie liveness detection
- **FR-1.3** Verifikasi NPWP (opsional di MVP, wajib di V1.1)
- **FR-1.4** Risk profile questionnaire (5 pertanyaan): konservatif/moderat/agresif
- **FR-1.5** Pembukaan akun live via e-Sign (POJK 6/2019 + BAPPEBTI compliance)
- **FR-1.6** Demo account otomatis saat registrasi ($10,000 virtual, sama seperti MIFX)
- **FR-1.7** Pilihan 4 tipe akun: Standard, Pro, Pro+, Elite
- **FR-1.8** Status verifikasi real-time (push notification)
- **FR-1.9** Leverage selection (1:1 sampai 1:100 sesuai risk profile)

#### FR-2: Market Data & Discovery
- **FR-2.1** Real-time price streaming untuk 50+ forex pairs (major, minor, exotic)
- **FR-2.2** Komoditas: emas (XAUUSD), perak (XAGUSD), minyak (WTI/Brent), gas alam
- **FR-2.3** Indeks saham global: US30 (Dow), NAS100 (Nasdaq), SPX500, GER40, JPN225, HK50
- **FR-2.4** Crypto (V2.5): BTC, ETH, SOL, dll
- **FR-2.5** Market overview: top movers, top volume, daily gainers/losers
- **FR-2.6** Symbol detail: bid/ask, spread, swap, margin requirement
- **FR-2.7** Economic calendar: news impact tinggi (high impact = merah)
- **FR-2.8** Market hours indicator (forex 24/5, indeks sesuai bursa)
- **FR-2.9** Symbol search dengan auto-suggest
- **FR-2.10** Symbol info popup: contract spec, trading hours, swap rate

#### FR-3: Charting & Analysis
- **FR-3.1** Interactive chart: time frame (1m, 5m, 15m, 30m, 1H, 4H, 1D, 1W, 1M)
- **FR-3.2** Chart types: candle, bar, line, Heikin-Ashi
- **FR-3.3** 30+ technical indicators: MA, EMA, RSI, MACD, Bollinger, Stochastic, ATR, ADX, Ichimoku, VWAP, Pivot Point
- **FR-3.4** Drawing tools: trendline, horizontal line, Fibonacci retracement, rectangle, text
- **FR-3.5** Multi-chart (split screen 2/4 chart) untuk pro
- **FR-3.6** Save chart template (gabungan indicator + drawing)
- **FR-3.7** Compare symbol (overlay 2 chart dalam 1)
- **FR-3.8** Replay mode (backtest visual)

#### FR-4: Watchlist
- **FR-4.1** Multiple watchlist (max 20)
- **FR-4.2** Quick add via search
- **FR-4.3** Live price update
- **FR-4.4** Color coding: hijau (naik), merah (turun)
- **FR-4.5** Sort by: alphabetical, change %, volume
- **FR-4.6** Watchlist widget untuk home screen (Android)
- **FR-4.7** Quick trade dari watchlist (3-tap)

#### FR-5: Trading Execution
- **FR-5.1** Order types: Market, Limit, Stop, Stop-Limit, Trailing Stop
- **FR-5.2** New order: pilih symbol, lot size, SL, TP
- **FR-5.3** One-Click Trading (configurable, dengan konfirmasi popup)
- **FR-5.4** Real-time P&L pada posisi terbuka
- **FR-5.5** Modify posisi: tambah SL/TP, partial close
- **FR-5.6** Close position: full atau partial (25%, 50%, 75%)
- **FR-5.7** Pending orders management
- **FR-5.8** Order history dengan filter (hari/minggu/bulan/symbol)
- **FR-5.9** Trading calculator: margin, P&L, pip value, swap
- **FR-5.10** Validasi: margin cukup, market hours, lot min/max

#### FR-6: Pulse Copy (Social Trading)
- **FR-6.1** Top traders leaderboard (sorted by ROI, win rate, followers)
- **FR-6.2** Trader profile: stats historis, risk score, trading style
- **FR-6.3** Follow trader → auto-copy semua trade-nya
- **FR-6.4** Copy settings: proportional copy, fixed amount, max loss limit
- **FR-6.5** Copy history (siapa ngikutin siapa, return masing-masing)
- **FR-6.6** Performance fee (jika return positif, 20% untuk top trader)
- **FR-6.7** Stop following kapan saja
- **FR-6.8** Verified trader badge (KYC + track record > 6 bulan)
- **FR-6.9** Pulse Score (composite: win rate, risk-adjusted return, consistency)

#### FR-7: Portfolio & Analytics
- **FR-7.1** Real-time equity (balance + floating P&L)
- **FR-7.2** Margin level & free margin
- **FR-7.3** Open positions: list, P&L per posisi, total
- **FR-7.4** Closed positions history
- **FR-7.5** Account statement (semua transaksi: trade, deposit, withdrawal, fee, swap, dividend)
- **FR-7.6** Performance analytics:
  - Win rate %, profit factor, average win/loss
  - Max drawdown, Sharpe ratio
  - Equity curve chart
  - Trade distribution by symbol, time, day of week
- **FR-7.7** Heat map portfolio (per symbol exposure)
- **FR-7.8** Tax statement (format CSV + PDF) untuk pelaporan SPT
- **FR-7.9** Export semua data (GDPR-like data portability)

#### FR-8: Deposit & Withdrawal
- **FR-8.1** Deposit methods: virtual account (BCA, Mandiri, BRI, BNI, CIMB), QRIS, e-wallet (OVO, DANA, GoPay, ShopeePay)
- **FR-8.2** Auto-confirm deposit via VA (real-time)
- **FR-8.3** Deposit dalam IDR atau USD
- **FR-8.4** Minimum deposit: Rp 100.000 (Standard) — lebih rendah dari MIFX Rp 500K
- **FR-8.5** Withdrawal ke rekening bank (cut-off 14:00 WIB, same day)
- **FR-8.6** Instant withdrawal (max Rp 5 juta, fee 0.1%)
- **FR-8.7** Internal transfer antar akun (PULSE Standard → PULSE Pro)
- **FR-8.8** Transaction history dengan filter & export
- **FR-8.9** Multi-currency wallet: IDR, USD, USDT (V2.5)

#### FR-9: Account Management
- **FR-9.1** Multi-account (max 5 akun trading per user)
- **FR-9.2** Switch account cepat
- **FR-9.3** Profile management (foto, bio, preferensi)
- **FR-9.4** Security: ganti password, PIN, 2FA
- **FR-9.5** Biometric login (Face ID / Touch ID / Fingerprint)
- **FR-9.6** Device management (list & revoke)
- **FR-9.7** Notification preferences (push, email, in-app)
- **FR-9.8** Language: ID, EN
- **FR-9.9** Currency display: IDR, USD
- **FR-9.10** Referral code & link

#### FR-10: Notifications
- **FR-10.1** Push: order opened/closed, margin call, price alert, news high impact
- **FR-10.2** In-app notification center
- **FR-10.3** Price alert: target price, % change
- **FR-10.4** Margin call warning (level 100% & 50%)
- **FR-10.5** Daily summary (jika diaktifkan)
- **FR-10.6** Economic calendar reminder (15 menit sebelum)

#### FR-11: Pulse Academy (Education)
- **FR-11.1** Kursus terstruktur: Forex 101, Analisis Teknikal, Analisis Fundamental, Risk Management
- **FR-11.2** Video pendek (3-5 menit) per lesson
- **FR-11.3** Quiz interaktif di setiap module
- **FR-11.4** Sertifikat digital setelah lulus course
- **FR-11.5** Progress tracker
- **FR-11.6** Glossary istilah trading
- **FR-11.7** Live webinar mingguan (recorded + replay)
- **FR-11.8** Curated articles/blog post

#### FR-12: Pulse Points (Loyalty)
- **FR-12.1** Earn points dari: trading volume, referral, belajar, daily login
- **FR-12.2** Tier: Bronze (< 1.000), Silver (1.000-5.000), Gold (5.000-20.000), Platinum (> 20.000)
- **FR-12.3** Benefits per tier: cashback %, fee discount, exclusive content
- **FR-12.4** Redeem: cash (IDR), e-wallet, voucher, merchandise
- **FR-12.5** Leaderboard bulanan (top points earner)
- **FR-12.6** Streak bonus (login 7 hari berturut = bonus points)

#### FR-13: Customer Support
- **FR-13.1** In-app help center (searchable FAQ)
- **FR-13.2** Live chat (AI bot + human escalation)
- **FR-13.3** WhatsApp integration (deep link)
- **FR-13.4** Email support
- **FR-13.5** Call center (24/7 — lebih baik dari MIFX yang 24/5)
- **FR-13.6** Ticket tracking
- **FR-13.7** Screen sharing untuk technical issue (planned V1.1)

#### FR-14: Security
- **FR-14.1** Login: email/password + biometric
- **FR-14.2** 2FA wajib untuk withdrawal & ganti password
- **FR-14.3** Auto-logout: 5 menit inaktif, 2 menit background
- **FR-14.4** Biometric gate untuk: trade, withdrawal, settings, view balance
- **FR-14.5** PIN 6-digit untuk trade
- **FR-14.6** Whitelist withdrawal bank (hanya rekening yang sudah terverifikasi)
- **FR-14.7** Anti-phishing code (custom code di email resmi)

---

### 5.2 P1 — V1.1 (3-6 bulan)

- Algo trading marketplace (MQL5-style strategy store)
- Advanced analytics (correlation matrix, Monte Carlo simulation)
- Multi-broker aggregation (best execution routing)
- Pulse Pro+: dedicated account manager untuk high-AUM user
- Tax optimization tool (auto-categorize gains/losses)
- Web trading platform (companion)

### 5.3 P2 — V2.0 (6-12 bulan)

- Saham Indonesia (IDX) — integrasi sekuritas partner
- Saham US (NYSE, NASDAQ) — integrasi IBKR
- Crypto spot trading (lisensi Bappebti crypto exchange)
- AI assistant: "Pulse AI" — natural language trade, insights, recommendation
- Pulse Signal Marketplace (signal provider subscription)

### 5.4 Out of Scope

- Futures spread betting
- Binary options
- MLM / referral pyramid
- Game-like gambling mechanics (regulated sebagai game jika ada)
- Open community forum (moderation berat, abuse risk tinggi)

---

## 6. Non-Functional Requirements

### 6.1 Performance

| Metric | Target |
|---|---|
| App cold start (p50) | ≤ 2 detik |
| App warm start (p50) | ≤ 0.8 detik |
| API p95 (read) | ≤ 250 ms |
| API p99 (write/trade) | ≤ 600 ms |
| Real-time quote push p95 | ≤ 300 ms |
| Order execution (tap → API ACK) | ≤ 1.2 detik |
| Chart render (1Y daily) | ≤ 1.2 detik |
| App download size | ≤ 45 MB |
| Memory idle | ≤ 120 MB |
| Battery (1 jam active use) | ≤ 8% |

### 6.2 Availability

- **Uptime**: 99.95% (4.4 jam downtime/tahun)
- **Trading hours**: 24/5 (Senin-Jumat), dengan scheduled maintenance weekend
- **RTO**: ≤ 15 menit
- **RPO**: ≤ 1 menit (synchronous replication)

### 6.3 Scalability

- **Initial**: 50.000 user, 5.000 concurrent, 500 order/sec
- **Year 1**: 200.000 user, 20.000 concurrent, 2.000 order/sec
- **Year 3**: 1 juta user, 100.000 concurrent, 10.000 order/sec
- **Horizontal scaling**: stateless services, K8s HPA

### 6.4 Security (OWASP + Regulatory)

- **OWASP Mobile M1-M10**: semua di-mitigasi
- **OWASP API Top 10**: 2023 version compliance
- **PCI DSS**: tidak applicable (tidak store kartu)
- **ISO 27001**: target Year 1 (MIFX sudah punya)
- **Pen test**: 2x/tahun
- **Bug bounty**: HackerOne, post-launch
- **Compliance**: POJK 22/2014, BAPPEBTI regulasi broker, UU PDP 27/2022

### 6.5 Observability

- **Mobile**: Sentry (crash + ANR + performance)
- **Backend**: Micrometer + Prometheus + Grafana
- **APM**: Datadog (atau New Relic) — distributed tracing
- **Logs**: ELK stack, structured JSON, correlation ID
- **Real-time alerting**: PagerDuty untuk P0/P1
- **Uptime monitoring**: Datadog Synthetics (cek endpoint tiap 30 detik)

---

## 7. Technical Architecture

### 7.1 High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│              FRONTEND (Expo / React Native)                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │  Auth &  │  │  Market  │  │ Trading  │  │  Pulse   │    │
│  │  KYC     │  │  Data    │  │  Engine  │  │  Copy    │    │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └────┬─────┘    │
│       │             │             │             │          │
│  ┌────▼─────────────▼─────────────▼─────────────▼────┐     │
│  │     Shared Layer: API client, State, Theme         │     │
│  └────────────────────────┬──────────────────────────┘     │
└───────────────────────────┼─────────────────────────────────┘
                            │ HTTPS / WSS
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    API GATEWAY (Kong)                       │
│  • JWT validation          • Rate limiting (per user/IP)   │
│  • WAF (OWASP rules)       • Request routing               │
│  • Request signing         • Logging / metrics             │
└───────────────────────────┬─────────────────────────────────┘
                            │
        ┌───────────────────┼─────────────────────┐
        │                   │                     │
        ▼                   ▼                     ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────────┐
│ Auth Service │   │ Market Data  │   │  Trading Service │
│ (Spring Boot)│   │ (Spring Boot)│   │  (Spring Boot)   │
│              │   │              │   │  + Matching      │
│              │   │              │   │    Engine        │
└──────┬───────┘   └──────┬───────┘   └────────┬─────────┘
       │                  │                     │
       │                  │   WebSocket         │
       │                  │   Push Server        │
       │                  │   (Netty)            │
       │                  │                     │
       ▼                  ▼                     ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────────┐
│ PostgreSQL   │   │ TimescaleDB  │   │   PostgreSQL     │
│ (User/Account│   │  (OHLCV,     │   │   (Trading DB)   │
│   DB)        │   │  Ticks)      │   │                  │
└──────────────┘   └──────────────┘   └──────────────────┘
                            │
                            ▼
                  ┌──────────────────┐
                  │  Apache Kafka    │
                  │  (Event Stream)  │
                  └──────────────────┘
                            │
        ┌───────────────────┼──────────────────┐
        ▼                   ▼                  ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────────┐
│  Liquidity   │   │   BAPPEBTI   │   │  KSEI / Bank     │
│  Provider    │   │   Reporting  │   │  Integrations    │
│  (Tier-1)    │   │              │   │                  │
└──────────────┘   └──────────────┘   └──────────────────┘
```

### 7.2 Frontend (Expo) — Detailed

#### Stack

| Layer | Pilihan | Notes |
|---|---|---|
| Framework | Expo SDK 53+ (New Architecture) | Default Hermes, JSI |
| Language | TypeScript strict | no-any policy |
| Navigation | Expo Router (file-based) | Deep linking |
| Server State | TanStack Query v5 | Cache, retry, optimistic |
| Client State | Zustand | Lightweight |
| Form | React Hook Form + Zod | Validation |
| List | FlashList | Watchlist, history, news |
| Chart | react-native-skia + custom (primary) + TradingView WebView (pro) | Bundle: primary in-app, TradingView optional |
| WebSocket | Native + reconnection | Real-time quote |
| HTTP | Axios + interceptors | JWT refresh |
| Secure Storage | expo-secure-store | Token, PIN |
| Cache Storage | MMKV | App preferences |
| Animations | react-native-reanimated v3 | 60fps |
| Push | expo-notifications | FCM + APNs |
| Analytics | Segment + Amplitude | Funnel, retention |
| Crash | Sentry | Crash + ANR |
| Biometric | expo-local-authentication | |
| Build | EAS Build + EAS Submit | |
| Update | EAS Update (OTA, JS only) | |

#### Folder Structure

```
pulse-app/
├── app/                              # Expo Router
│   ├── (auth)/
│   │   ├── _layout.tsx
│   │   ├── welcome.tsx
│   │   ├── register.tsx
│   │   ├── verify-otp.tsx
│   │   ├── kyc.tsx
│   │   ├── risk-profile.tsx
│   │   └── esign.tsx
│   ├── (main)/                        # Authenticated routes
│   │   ├── _layout.tsx                # Tab navigator
│   │   ├── (home)/
│   │   │   ├── index.tsx              # Home / Watchlist
│   │   │   ├── market.tsx             # Market overview
│   │   │   └── movers.tsx
│   │   ├── (trade)/
│   │   │   ├── _layout.tsx
│   │   │   ├── index.tsx              # Quick trade
│   │   │   ├── symbol/[symbol].tsx    # Chart + order
│   │   │   └── confirm.tsx
│   │   ├── (portfolio)/
│   │   │   ├── index.tsx
│   │   │   ├── positions.tsx
│   │   │   ├── history.tsx
│   │   │   └── analytics.tsx
│   │   ├── (copy)/                    # Pulse Copy
│   │   │   ├── index.tsx              # Leaderboard
│   │   │   ├── trader/[id].tsx
│   │   │   └── my-copies.tsx
│   │   └── (profile)/
│   │       ├── index.tsx
│   │       ├── accounts.tsx
│   │       ├── wallet.tsx
│   │       ├── academy.tsx
│   │       ├── rewards.tsx
│   │       └── settings.tsx
│   └── _layout.tsx                    # Root
├── src/
│   ├── features/
│   │   ├── auth/
│   │   ├── kyc/
│   │   ├── market-data/
│   │   ├── trading/
│   │   ├── portfolio/
│   │   ├── watchlist/
│   │   ├── copy-trading/
│   │   ├── account/
│   │   ├── academy/
│   │   ├── rewards/
│   │   └── notifications/
│   ├── components/                    # UI primitives
│   ├── lib/
│   │   ├── api-client.ts
│   │   ├── ws-client.ts
│   │   ├── secure-storage.ts
│   │   ├── query-client.ts
│   │   ├── analytics.ts
│   │   └── sentry.ts
│   ├── theme/
│   ├── types/
│   └── utils/
├── assets/
├── app.config.ts
├── eas.json
├── babel.config.js
├── tsconfig.json
└── package.json
```

### 7.3 Backend (Spring Boot) — Detailed

#### Stack

| Layer | Pilihan |
|---|---|
| Java | Java 21 LTS (virtual threads untuk IO-bound) |
| Framework | Spring Boot 3.3.x |
| Architecture | Spring Modulith (modular monolith, evolvable to microservices) |
| API | REST (JSON) + WebSocket (STOMP + raw) |
| DB Migration | Flyway |
| ORM | Spring Data JPA (Hibernate) + jOOQ (reporting) |
| Validation | Jakarta Bean Validation |
| Security | Spring Security + OAuth2 Resource Server (JWT RS256) |
| Resilience | Resilience4j (Circuit Breaker, Retry, Bulkhead, Rate Limiter) |
| Cache | Redis + Caffeine (L1) |
| Message Broker | Apache Kafka |
| Service Discovery | Kubernetes native (DNS) |
| Config | Spring Cloud Config + K8s ConfigMap/Secret + Vault |
| Matching Engine | In-house (low-latency) + LMAX Disruptor pattern |
| API Doc | SpringDoc OpenAPI 2.x |
| Testing | JUnit 5, Mockito, Testcontainers, Pact |
| Observability | Micrometer + Prometheus + OpenTelemetry |
| Logging | Logback (JSON) + ELK |
| Container | Docker distroless |
| Orchestration | Kubernetes (EKS) |

#### Modules (Spring Modulith)

```
pulse-backend/
├── auth/                  # Login, JWT, refresh, biometric device
├── user/                  # Profile, device, preferences
├── kyc/                   # KYC flow, e-Sign, risk profile
├── market-data/           # Quotes, OHLCV, economic calendar
├── trading/               # Order, position, matching, history
├── portfolio/             # Equity, analytics, statement
├── copy-trading/          # Top trader, follower, performance fee
├── account/               # Multi-account, currency wallet
├── payment/               # Deposit, withdrawal, transfer
├── academy/               # Course, quiz, certificate
├── rewards/               # Points, tier, redemption
├── notification/          # Push, email, in-app
├── audit/                 # Immutable audit log
├── reporting/             # BAPPEBTI reporting, analytics
└── common/                # Shared utilities
```

#### REST API Endpoints

**Base URL**: `https://api.pulse.id/v1`

##### Auth (`/auth`)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register email + phone |
| POST | `/auth/send-otp` | Send OTP via WhatsApp/SMS |
| POST | `/auth/verify-otp` | Verify OTP |
| POST | `/auth/login` | Login (email + password) |
| POST | `/auth/refresh` | Refresh access token |
| POST | `/auth/logout` | Logout |
| POST | `/auth/forgot-password` | Forgot password |
| POST | `/auth/reset-password` | Reset password |
| POST | `/auth/2fa/enable` | Enable 2FA |
| POST | `/auth/2fa/verify` | Verify 2FA token |
| GET | `/auth/me` | Current user |

##### KYC (`/kyc`)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/kyc/upload-ktp` | Upload KTP image |
| POST | `/kyc/liveness-check` | Face liveness detection |
| POST | `/kyc/submit` | Submit KYC application |
| GET | `/kyc/status` | Check KYC status |
| POST | `/kyc/risk-profile` | Submit risk profile |
| POST | `/kyc/esign` | e-Sign agreement |
| POST | `/kyc/open-account` | Open trading account |
| GET | `/kyc/accounts` | List trading accounts |

##### Market Data (`/market`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/market/symbols` | All tradeable symbols |
| GET | `/market/symbols/{symbol}` | Symbol detail |
| GET | `/market/quote/{symbol}` | Latest quote |
| POST | `/market/quotes` | Multiple quotes |
| GET | `/market/ohlcv/{symbol}?tf=1m&limit=1000` | OHLCV |
| GET | `/market/movers?type=gainer` | Top movers |
| GET | `/market/most-active` | Most active |
| GET | `/market/categories` | Symbol categories |
| GET | `/market/search?q=...` | Search symbols |
| GET | `/market/calendar` | Economic calendar |
| GET | `/market/news?symbol=...` | News per symbol |
| GET | `/market/symbols/{symbol}/spec` | Contract spec |

##### Watchlist (`/watchlists`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/watchlists` | All watchlists |
| POST | `/watchlists` | Create |
| PATCH | `/watchlists/{id}` | Update name |
| DELETE | `/watchlists/{id}` | Delete |
| POST | `/watchlists/{id}/items` | Add symbol |
| DELETE | `/watchlists/{id}/items/{symbol}` | Remove |
| PATCH | `/watchlists/{id}/reorder` | Reorder items |

##### Trading (`/orders`, `/positions`)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/orders/preview` | Preview order (fee/margin calc) |
| POST | `/orders` | Place order |
| GET | `/orders` | Order history |
| GET | `/orders/{id}` | Order detail |
| PATCH | `/orders/{id}` | Modify pending order |
| DELETE | `/orders/{id}` | Cancel pending order |
| GET | `/positions` | Open positions |
| GET | `/positions/{id}` | Position detail |
| PATCH | `/positions/{id}/sl-tp` | Modify SL/TP |
| POST | `/positions/{id}/close` | Close position (full/partial) |
| GET | `/positions/closed` | Closed positions history |
| POST | `/calculator/margin` | Margin calculator |
| POST | `/calculator/pnl` | P&L calculator |
| POST | `/calculator/pip` | Pip value calculator |

##### Pulse Copy (`/copy`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/copy/leaderboard?sort=roi` | Top traders |
| GET | `/copy/traders/{id}` | Trader profile + stats |
| GET | `/copy/traders/{id}/trades` | Recent trades |
| POST | `/copy/follow` | Follow trader |
| DELETE | `/copy/follow/{id}` | Stop following |
| GET | `/copy/my-copies` | My copy subscriptions |
| GET | `/copy/my-copies/{id}/performance` | Copy performance |
| PATCH | `/copy/my-copies/{id}/settings` | Update copy settings |

##### Portfolio (`/portfolio`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/portfolio/summary` | Overall summary |
| GET | `/portfolio/equity-curve?period=1m` | Equity curve |
| GET | `/portfolio/stats?period=1m` | Win rate, profit factor, dll |
| GET | `/portfolio/trade-distribution` | By symbol, time, day |
| GET | `/portfolio/statement?from=...&to=...` | Account statement |
| GET | `/portfolio/tax-statement?year=2026` | Tax statement (PDF/CSV) |
| GET | `/portfolio/export?format=csv` | Full export |

##### Account (`/accounts`, `/wallet`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/accounts` | Trading accounts |
| POST | `/accounts` | Open new account |
| PATCH | `/accounts/{id}` | Update account settings |
| POST | `/accounts/{id}/leverage` | Change leverage |
| GET | `/wallet/balance` | Multi-currency balance |
| GET | `/wallet/currencies` | Supported currencies |

##### Payment (`/deposit`, `/withdraw`)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/deposit/virtual-account` | Create VA |
| GET | `/deposit/methods` | Available methods |
| GET | `/deposit/history` | Deposit history |
| POST | `/withdraw` | Request withdrawal |
| GET | `/withdraw/history` | Withdrawal history |
| POST | `/transfer/internal` | Transfer antar akun |
| GET | `/banks` | Linked banks |
| POST | `/banks` | Add bank |
| DELETE | `/banks/{id}` | Remove bank |

##### Academy (`/academy`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/academy/courses` | All courses |
| GET | `/academy/courses/{id}` | Course detail |
| GET | `/academy/courses/{id}/lessons` | Lessons |
| POST | `/academy/lessons/{id}/complete` | Mark complete |
| GET | `/academy/quiz/{id}` | Quiz |
| POST | `/academy/quiz/{id}/submit` | Submit quiz |
| GET | `/academy/certificates` | My certificates |
| GET | `/academy/webinars` | Upcoming webinars |
| GET | `/academy/articles` | Articles |

##### Rewards (`/rewards`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/rewards/balance` | Points balance + tier |
| GET | `/rewards/history` | Earn/redeem history |
| GET | `/rewards/catalog` | Redeem options |
| POST | `/rewards/redeem` | Redeem points |
| GET | `/rewards/leaderboard` | Top earner leaderboard |

##### Notification (`/notifications`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/notifications` | Notification list |
| PATCH | `/notifications/{id}/read` | Mark read |
| POST | `/notifications/read-all` | Mark all read |
| POST | `/alerts` | Create price alert |
| GET | `/alerts` | List alerts |
| DELETE | `/alerts/{id}` | Delete alert |
| POST | `/devices/register` | Register device push |

#### WebSocket Protocol

**Endpoint**: `wss://ws.pulse.id/v1`

##### Subscribe Quote
```json
{
  "action": "subscribe",
  "channel": "quote",
  "symbols": ["EURUSD", "XAUUSD", "BTCUSD"]
}
```

##### Quote Push
```json
{
  "channel": "quote",
  "data": {
    "symbol": "EURUSD",
    "bid": 1.08542,
    "ask": 1.08545,
    "spread": 0.3,
    "last": 1.08544,
    "change": 0.00012,
    "change_pct": 0.011,
    "high": 1.08750,
    "low": 1.08400,
    "volume": 254789321,
    "timestamp": 1724760000000
  }
}
```

##### Trade Update
```json
{
  "channel": "trade",
  "data": {
    "account_id": "uuid",
    "order_id": "uuid",
    "status": "FILLED",
    "filled_price": 1.08545,
    "filled_qty": 0.1,
    "pnl": 12.50,
    "timestamp": 1724760001000
  }
}
```

#### Kafka Topics

| Topic | Producer | Consumer | Event |
|---|---|---|---|
| `user.registered` | auth | user, notification, kyc | New user |
| `kyc.approved` | kyc | trading, account, notification | KYC passed |
| `kyc.rejected` | kyc | notification | KYC failed |
| `account.opened` | kyc | trading, account | New trading account |
| `order.placed` | trading | matching-engine, audit, notification | New order |
| `order.filled` | matching-engine | trading, portfolio, notification | Order executed |
| `order.cancelled` | trading | notification, audit | Order cancelled |
| `order.rejected` | trading | notification, audit | Order rejected |
| `position.opened` | trading | portfolio, copy-trading | Position opened |
| `position.closed` | trading | portfolio, rewards, copy-trading | Position closed |
| `deposit.received` | payment | trading, account, notification | Deposit confirmed |
| `withdraw.requested` | payment | payment-worker, audit | Withdrawal |
| `withdraw.completed` | payment-worker | notification | Withdrawal sent |
| `alert.triggered` | market-data | notification | Price alert hit |
| `copy.followed` | copy-trading | copy-trading, notification | New follower |
| `copy.performance-fee` | copy-trading | payment, notification | Fee charged |
| `margin.call` | trading | notification, risk | Margin level critical |

### 7.4 Database Schema (PostgreSQL + TimescaleDB)

#### ERD Overview

```
users ─┬─< kyc_documents
       ├─< user_devices
       ├─< bank_accounts
       ├─< trading_accounts ─< account_balance
       ├─< watchlists ─< watchlist_items
       ├─< orders ─< order_fills
       ├─< positions ─< position_history
       ├─< copy_subscriptions
       ├─< price_alerts
       ├─< notifications
       ├─< transactions
       ├─< points_ledger
       └─< audit_logs

symbols ─< ohlcv_data (TimescaleDB)
       └─< symbol_specs

academy_courses ─< lessons ─< lesson_completions
                └─< quizzes ─< quiz_attempts
```

#### Core Tables (DDL excerpt)

```sql
-- USERS
CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) UNIQUE NOT NULL,
    phone           VARCHAR(20) UNIQUE NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255) NOT NULL,
    pin_hash        VARCHAR(255),
    anti_phishing_code VARCHAR(20),
    is_2fa_enabled  BOOLEAN DEFAULT FALSE,
    totp_secret     VARCHAR(255) ENCRYPTED,  -- pgcrypto
    kyc_status      VARCHAR(20) DEFAULT 'pending',
    risk_profile    VARCHAR(20),
    referred_by     UUID REFERENCES users(id),
    referral_code   VARCHAR(20) UNIQUE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_kyc_status ON users(kyc_status);

-- TRADING ACCOUNTS
CREATE TABLE trading_accounts (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    account_number  VARCHAR(20) UNIQUE NOT NULL,  -- e.g., PULSE-2026-000001
    account_type    VARCHAR(20) NOT NULL,  -- STANDARD, PRO, PRO_PLUS, ELITE
    currency        VARCHAR(3) DEFAULT 'USD',
    leverage        INTEGER DEFAULT 100,  -- 1:100
    balance         DECIMAL(20,2) DEFAULT 0,
    equity          DECIMAL(20,2) DEFAULT 0,
    margin_used     DECIMAL(20,2) DEFAULT 0,
    free_margin     DECIMAL(20,2) DEFAULT 0,
    margin_level    DECIMAL(10,2),  -- percentage
    status          VARCHAR(20) DEFAULT 'active',
    opened_at       TIMESTAMPTZ DEFAULT NOW(),
    closed_at       TIMESTAMPTZ
);

CREATE INDEX idx_trading_accounts_user_id ON trading_accounts(user_id);

-- SYMBOLS
CREATE TABLE symbols (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code            VARCHAR(20) UNIQUE NOT NULL,  -- EURUSD, XAUUSD, etc.
    name            VARCHAR(100) NOT NULL,
    category        VARCHAR(50) NOT NULL,  -- FOREX, COMMODITY, INDEX, CRYPTO
    sub_category    VARCHAR(50),
    base_currency   VARCHAR(10),
    quote_currency  VARCHAR(10),
    contract_size   DECIMAL(15,4),
    min_lot         DECIMAL(10,4) DEFAULT 0.01,
    max_lot         DECIMAL(15,4) DEFAULT 100,
    lot_step        DECIMAL(10,4) DEFAULT 0.01,
    digits          INTEGER DEFAULT 5,
    spread_avg      DECIMAL(10,2),  -- in pip
    swap_long       DECIMAL(15,4),
    swap_short      DECIMAL(15,4),
    margin_percent  DECIMAL(5,2) DEFAULT 1,  -- margin requirement %
    trading_hours   JSONB,  -- {mon: "00:00-23:59", tue: ..., ...}
    is_active       BOOLEAN DEFAULT TRUE,
    icon_url        VARCHAR(500),
    description     TEXT
);

-- ORDERS
CREATE TABLE orders (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id      UUID NOT NULL REFERENCES trading_accounts(id),
    user_id         UUID NOT NULL REFERENCES users(id),
    order_number    VARCHAR(30) UNIQUE NOT NULL,
    symbol          VARCHAR(20) NOT NULL,
    side            VARCHAR(4) NOT NULL,  -- BUY, SELL
    order_type      VARCHAR(20) NOT NULL,  -- MARKET, LIMIT, STOP, STOP_LIMIT, TRAILING_STOP
    quantity        DECIMAL(15,4) NOT NULL,
    price           DECIMAL(15,5),
    stop_price      DECIMAL(15,5),
    trailing_offset DECIMAL(15,5),
    sl_price        DECIMAL(15,5),
    tp_price        DECIMAL(15,5),
    filled_qty      DECIMAL(15,4) DEFAULT 0,
    avg_fill_price  DECIMAL(15,5),
    status          VARCHAR(20) DEFAULT 'PENDING',  -- PENDING, FILLED, PARTIAL, CANCELLED, REJECTED, EXPIRED
    reject_reason   TEXT,
    is_one_click    BOOLEAN DEFAULT FALSE,
    placed_at       TIMESTAMPTZ DEFAULT NOW(),
    filled_at       TIMESTAMPTZ,
    expires_at      TIMESTAMPTZ,
    magic          INTEGER DEFAULT 0  -- for copy trade tagging
);

CREATE INDEX idx_orders_account_id ON orders(account_id);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_symbol ON orders(symbol);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_placed_at ON orders(placed_at DESC);

-- POSITIONS
CREATE TABLE positions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id      UUID NOT NULL REFERENCES trading_accounts(id),
    user_id         UUID NOT NULL REFERENCES users(id),
    symbol          VARCHAR(20) NOT NULL,
    side            VARCHAR(4) NOT NULL,
    quantity        DECIMAL(15,4) NOT NULL,
    open_price      DECIMAL(15,5) NOT NULL,
    current_price   DECIMAL(15,5),
    sl_price        DECIMAL(15,5),
    tp_price        DECIMAL(15,5),
    swap            DECIMAL(15,2) DEFAULT 0,
    commission      DECIMAL(15,2) DEFAULT 0,
    unrealized_pnl  DECIMAL(15,2) DEFAULT 0,
    realized_pnl    DECIMAL(15,2) DEFAULT 0,
    margin_required DECIMAL(15,2),
    magic           INTEGER DEFAULT 0,  -- copy trade ID
    copied_from     UUID REFERENCES users(id),  -- top trader
    opened_at       TIMESTAMPTZ DEFAULT NOW(),
    closed_at       TIMESTAMPTZ,
    status          VARCHAR(20) DEFAULT 'OPEN'  -- OPEN, CLOSED
);

CREATE INDEX idx_positions_account_id ON positions(account_id);
CREATE INDEX idx_positions_user_id ON positions(user_id);
CREATE INDEX idx_positions_status ON positions(status);

-- COPY TRADING
CREATE TABLE top_traders (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    display_name    VARCHAR(100) NOT NULL,
    bio             TEXT,
    avatar_url      VARCHAR(500),
    pulse_score     DECIMAL(5,2),  -- 0-100 composite
    is_verified     BOOLEAN DEFAULT FALSE,
    total_followers INTEGER DEFAULT 0,
    total_aum       DECIMAL(20,2) DEFAULT 0,
    performance_fee_percent DECIMAL(4,2) DEFAULT 20,
    is_accepting    BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE copy_subscriptions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    follower_id     UUID NOT NULL REFERENCES users(id),
    trader_id       UUID NOT NULL REFERENCES top_traders(id),
    account_id      UUID NOT NULL REFERENCES trading_accounts(id),
    copy_mode       VARCHAR(20) NOT NULL,  -- PROPORTIONAL, FIXED
    fixed_amount    DECIMAL(15,2),
    max_loss_limit  DECIMAL(15,2),
    max_position_size DECIMAL(15,4),
    is_active       BOOLEAN DEFAULT TRUE,
    started_at      TIMESTAMPTZ DEFAULT NOW(),
    stopped_at      TIMESTAMPTZ,
    total_pnl       DECIMAL(15,2) DEFAULT 0,
    performance_fee_paid DECIMAL(15,2) DEFAULT 0
);

-- WALLET / TRANSACTIONS
CREATE TABLE transactions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    account_id      UUID REFERENCES trading_accounts(id),
    tx_type         VARCHAR(30) NOT NULL,  -- DEPOSIT, WITHDRAW, TRADE, SWAP, COMMISSION, FEE, COPY_FEE
    amount          DECIMAL(20,4) NOT NULL,
    currency        VARCHAR(3) NOT NULL,
    balance_before  DECIMAL(20,4),
    balance_after   DECIMAL(20,4),
    reference_id    UUID,
    description     TEXT,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_created_at ON transactions(created_at DESC);

-- POINTS / REWARDS
CREATE TABLE points_ledger (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    points_delta    INTEGER NOT NULL,  -- positive earn, negative redeem
    action_type     VARCHAR(50) NOT NULL,  -- TRADE_VOLUME, REFERRAL, DAILY_LOGIN, COURSE_COMPLETE, REDEEM
    reference_id    UUID,
    balance_after   INTEGER,
    description     TEXT,
    expires_at      TIMESTAMPTZ,  -- points expire in 12 months
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- AUDIT LOG (immutable)
CREATE TABLE audit_logs (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID,
    action          VARCHAR(100) NOT NULL,
    entity_type     VARCHAR(50),
    entity_id       UUID,
    metadata        JSONB,
    ip_address      INET,
    user_agent      TEXT,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_audit_created_at ON audit_logs(created_at DESC);
```

#### TimescaleDB Hypertables

```sql
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- TICK DATA (real-time quotes, 7 days retention)
CREATE TABLE ticks (
    symbol      VARCHAR(20) NOT NULL,
    time        TIMESTAMPTZ NOT NULL,
    bid         DECIMAL(15,5),
    ask         DECIMAL(15,5),
    last        DECIMAL(15,5),
    volume      BIGINT
);
SELECT create_hypertable('ticks', 'time');
CREATE INDEX idx_ticks_symbol ON ticks(symbol, time DESC);

-- OHLCV (multiple timeframes)
CREATE TABLE ohlcv (
    symbol      VARCHAR(20) NOT NULL,
    timeframe   VARCHAR(5) NOT NULL,  -- 1m, 5m, 15m, 1H, 1D
    time        TIMESTAMPTZ NOT NULL,
    open        DECIMAL(15,5) NOT NULL,
    high        DECIMAL(15,5) NOT NULL,
    low         DECIMAL(15,5) NOT NULL,
    close       DECIMAL(15,5) NOT NULL,
    volume      BIGINT NOT NULL
);
SELECT create_hypertable('ohlcv', 'time');
CREATE INDEX idx_ohlcv_symbol ON ohlcv(symbol, timeframe, time DESC);

-- Continuous aggregate (for fast 1H/1D queries)
CREATE MATERIALIZED VIEW ohlcv_1h
WITH (timescaledb.continuous) AS
SELECT
    symbol,
    time_bucket('1 hour', time) AS bucket,
    first(open, time) AS open,
    max(high) AS high,
    min(low) AS low,
    last(close, time) AS close,
    sum(volume) AS volume
FROM ohlcv
WHERE timeframe = '1m'
GROUP BY symbol, bucket;
```

#### Data Retention

| Data | Retention | Strategy |
|---|---|---|
| Ticks | 7 days | Drop chunks |
| OHLCV 1m | 30 days | Compress + drop |
| OHLCV 1H | 1 year | Compress |
| OHLCV 1D | 10 years | Compress |
| Orders/Positions | Permanent | Normal table |
| Audit logs | 7 years | Archive to cold |
| Notifications | 90 days | Delete |

---

## 8. User Flows

### 8.1 Onboarding Flow (target ≤ 5 menit)

```
[Splash / Welcome]
    ↓
[Welcome Carousel: 3 slide]
    "Trade global markets 24/5"
    "Copy top traders, earn together"
    "Transparent fees, no hidden cost"
    ↓
[Register: Email + Phone]
    ↓
[OTP Verification: WhatsApp preferred]
    ↓
[Set Password + 6-digit PIN]
    ↓
[Auto-create Demo Account ($10,000 virtual)]
    ↓
[Choose Path: "Try Demo" atau "Open Live"]
    ↓
[Live Path: KYC]
    - Foto KTP
    - Selfie liveness
    - NPWP (opsional)
    - e-Sign agreement
    ↓
[Risk Profile Quiz: 5 pertanyaan]
    ↓
[Akun Live Created → Demo can also be live tested]
    ↓
[Deposit Reminder (skip-able, demo works without deposit)]
    ↓
[Home: Show "Start Demo" CTA + watchlist kosong]
```

### 8.2 Place Order (3-tap dari watchlist)

```
[Watchlist] → Tap symbol "EURUSD"
    ↓
[Symbol Detail Page: Chart + Quote + Order Panel]
    ↓
[Tap "Buy" (large green button)] → [Order form sheet muncul]
    ↓
[Order Form: default Market, 0.01 lot, no SL/TP]
    ↓
[Tap "Confirm Order"] → [Biometric Auth]
    ↓
[Order Sent → API ACK in < 1.2s]
    ↓
[Confirmation: "Order Filled at 1.08545"]
    ↓
[Position now visible in Portfolio tab]
```

### 8.3 Copy Trader Flow

```
[Pulse Copy Tab] → [Leaderboard: Top traders]
    ↓
[Trader Profile: RakaFX, 142% ROI 6mo, 2.3K followers, Pulse Score 87]
    ↓
[Tap "Follow"]
    ↓
[Copy Settings: 
    - Proportional (1:1) atau Fixed ($100)
    - Max loss limit: $200
    - Max position size: 0.5 lot]
    ↓
[Biometric Auth]
    ↓
[Confirmation: "You are now following RakaFX"]
    ↓
[Next trade RakaFX → auto-copied to your account]
    ↓
[Track in "My Copies" → see live performance]
```

---

## 9. UX/UI Design (Brand PULSE)

### 9.1 Brand Identity

| Element | Spec |
|---|---|
| **Brand Name** | **PULSE** |
| **Tagline** | "The market's heartbeat, in your pocket" |
| **Personality** | Energetic, modern, transparent, empowering |
| **Logo** | Pulse wave / heartbeat line integrated dengan letter "P" |
| **Primary Color** | `#0EA5E9` (Electric Sky — energetic, modern, trust) |
| **Secondary Color** | `#7C3AED` (Pulse Purple — premium, creative) |
| **Success / Long** | `#10B981` (Emerald — up market) |
| **Error / Short** | `#EF4444` (Crimson — down market) |
| **Warning** | `#F59E0B` (Amber) |
| **Background Light** | `#FAFAFA` |
| **Background Dark** | `#0A0F1C` (Deep Navy — trader favorite) |
| **Surface Light** | `#FFFFFF` |
| **Surface Dark** | `#1A1F2E` |
| **Text Primary (light)** | `#0F172A` |
| **Text Primary (dark)** | `#F8FAFC` |
| **Font Primary** | Inter Variable |
| **Font Numeric** | JetBrains Mono (harga, P&L, quantity) |
| **Border Radius** | 12px card, 16px button, 20px modal |
| **Spacing** | 4px base (Tailwind-like: 4/8/12/16/24/32/48) |

### 9.2 Design Principles

1. **Speed-first**: max 2 detik dari tap ke result. Loading harus instant optimistic.
2. **Numbers readable**: font mono untuk semua angka, warna konsisten (hijau/merah).
3. **Dark mode default**: trader lebih nyaman, hemat baterai OLED.
4. **No clutter**: minimal chrome, max info density yang dibutuhkan.
5. **Gesture-driven**: swipe untuk quick action, long-press untuk context menu.
6. **Error tolerance**: konfirmasi reversible action (close position) dengan delay 1-3 detik.

### 9.3 Key Screens (Wireframe Outline)

1. **Home / Watchlist**
   - Header: greeting + total equity + daily P&L
   - Segment: Watchlist | Market | Movers | Calendar
   - Watchlist list: live price, sparkline mini, % change
   - Bottom nav: Home, Trade, Pulse Copy, Academy, Profile

2. **Symbol Detail**
   - Top: bid/ask besar, last, change, spread
   - Chart (default candle, 1H)
   - Timeframe picker, indicator drawer, drawing tool
   - Info tabs: Spec | News | Calendar
   - Sticky bottom: Buy / Sell buttons besar

3. **Order Form (Modal)**
   - Symbol, current price
   - BUY/SELL toggle
   - Order type, quantity, SL/TP
   - Live calculator: margin, P&L scenarios, swap
   - Confirm button (biometric protected)

4. **Portfolio**
   - Equity curve chart (interaktif)
   - Stats: win rate, profit factor, drawdown
   - Open positions list (live P&L)
   - Tabs: Open | History | Analytics

5. **Pulse Copy**
   - Leaderboard: top traders dengan mini-sparkline
   - Filter: ROI, win rate, followers, style
   - Trader profile: full stats, recent trades, followers growth
   - "Follow" button prominent

---

## 10. Compliance & Regulatory

### 10.1 Wajib (Indonesia)

| Regulasi | Implementasi |
|---|---|
| **BAPPEBTI** (Badan Pengawas Perdagangan Berjangka & Komoditi) | Lisensi Pialang Berjangka, pelaporan bulanan |
| **POJK 22/2014** (KYC Pasar Modal) | KYC/CDD/EDD lengkap |
| **POJK 6/SEOJK.04/2019** | e-Sign untuk pembukaan rekening |
| **ISO 27001** (InfoSec) | Sertifikasi di Year 1 (MIFX sudah punya) |
| **PCI DSS** | Tidak applicable (no card storage) |
| **UU PDP 27/2022** (Pelindungan Data Pribadi) | Enkripsi, consent, right to be forgotten |
| **UU TPPU 25/2003** (Anti Pencucian Uang) | Transaction monitoring, STR ke PPATK |
| **PSD2/SEBI** (jika ekspansi ke India) | Future |

### 10.2 Compliance Features di Produk

- **KYC**: KTP + selfie liveness + NPWP
- **Risk warning**: mandatory acknowledgment setiap 90 hari
- **Suitability test**: risk profile vs instrument
- **Leverage restriction**: 1:100 max (sesuai BAPPEBTI)
- **Margin call**: notif di 100% & 50% margin level
- **Negative balance protection**: auto-stop out
- **Regulated disclosure**: fee, risk warning, broker info
- **Reporting bulanan ke BAPPEBTI**: trading volume, user growth, complaints

### 10.3 Data Protection

- **Encryption at rest**: AES-256 untuk PII (KTP, NPWP, bank)
- **Encryption in transit**: TLS 1.3 + SSL pinning
- **Data residency**: Indonesia (AWS Jakarta atau local DC)
- **Data retention**: 5-7 tahun sesuai regulasi
- **Right to be forgotten**: 30 hari proses
- **Consent management**: granular per data usage
- **Privacy policy**: in-app, jelas & bahasa sederhana

---

## 11. Security Architecture (OWASP Mobile + API)

| # | Risk | Mitigasi |
|---|---|---|
| **M1** | Credential Storage | `expo-secure-store` (Keychain/Keystore). Tidak pakai AsyncStorage. Token rotation. |
| **M2** | Supply Chain | `npm audit` di CI, exact version untuk security-critical. Dependency review manual. |
| **M3** | Auth/Authz | Biometric untuk trade, withdrawal, settings. 2FA wajib untuk high-value. 5 attempt limit. |
| **M4** | Input/Output | Zod validation di frontend, Jakarta Bean Validation di backend, parameterized SQL. |
| **M5** | Communication | TLS 1.3 only, SSL public key pinning (2 keys, rotation plan), cert monitoring. |
| **M6** | Privacy | PII masking di logs, no PII ke analytics, encrypted local storage. |
| **M7** | Binary Protections | Hermes, source map stripped, debug off, jailbreak/root detection. |
| **M8** | Misconfiguration | Least privilege, no default creds, env-specific config via Vault. |
| **M9** | Data Storage | Tiered: SecureStore (token) + MMKV encrypted (app data) + encrypted SQLite. |
| **M10** | Cryptography | `expo-crypto` CSPRNG, no MD5/SHA1, PBKDF2/HKDF. |
| **API1** | Broken Object Level Authz | User_id check di setiap endpoint, tidak percaya client-side. |
| **API3** | Broken Object Property Level Authz | DTO pattern, whitelist fields. |
| **API4** | Unrestricted Resource Consumption | Rate limit (100/min/user, 1000/min/IP), payload size limit, connection pooling. |
| **API5** | Broken Function Level Authz | RBAC enforcement di service layer. |
| **API8** | Security Misconfiguration | Spring Security defaults, no verbose error, CORS allowlist. |
| **API9** | Improper Inventory Mgmt | API versioning, OpenAPI doc, deprecated endpoint sunset. |

---

## 12. Testing Strategy

### 12.1 Mobile

| Level | Tool | Coverage |
|---|---|---|
| Unit | Jest + RNTL | ≥ 80% pure logic |
| Component | Storybook + snapshot | All reusable |
| E2E | Maestro | All P0 user flows |
| Visual | Percy | Regressions |
| Performance | Flashlight + Flipper | Frame rate, memory |
| Crash | Sentry | Monitoring |

### 12.2 Backend

| Level | Tool | Coverage |
|---|---|---|
| Unit | JUnit 5 + Mockito | ≥ 80% |
| Integration | Testcontainers (Postgres, Kafka, Redis) | Critical paths |
| Contract | Pact | Inter-service |
| Load | k6 | 2.000 order/sec |
| Chaos | Chaos Monkey | Resilience |

### 12.3 Critical Test Scenarios (must pass)

1. **Order placement under high load** (2.000 req/sec, latency p99 < 1s)
2. **Margin call cascading** (100 positions, 1 symbol drop 50%, all margin called correctly)
3. **Copy trade distribution** (top trader places 1 order, 500 followers get filled in < 5s)
4. **Database failover** (primary down, replica takes over in < 30s, zero data loss)
5. **WebSocket reconnect** (10K connections drop, all reconnect within 5s)
6. **KYC happy path** (onboarding complete in < 5 min, all 3 docs uploaded)
7. **Withdrawal fraud attempt** (invalid bank, blocked, alert triggered)

---

## 13. Deployment & DevOps

### 13.1 Environments

| Env | Purpose | Infra |
|---|---|---|
| Local | Dev workstation | Docker Compose |
| Dev | Per-branch integration | K8s single node |
| Staging | Pre-prod, mirror | K8s scaled-down |
| Production | Live | K8s multi-AZ (AWS Jakarta) |

### 13.2 CI/CD

```
Git Push / PR
    ↓
Lint + Type Check (TS / SpotBugs + Checkstyle)
    ↓
Unit + Integration Tests
    ↓
Code Coverage Check (≥ 80%)
    ↓
Build Artifacts (EAS / Docker)
    ↓
Security Scan (Snyk, OWASP dependency check)
    ↓
Deploy to Dev/Staging
    ↓
Smoke Test + E2E (Maestro / Pact)
    ↓
Performance Test (k6, sampled)
    ↓
[Manual Approval] for Production
    ↓
Blue/Green or Canary Deploy
    ↓
Smoke Test (auto)
    ↓
Monitor (Datadog) — auto rollback if error > 1%
```

### 13.3 Release Strategy

- **Mobile**: phased rollout (EAS) — 1% → 10% → 50% → 100% dalam 5 hari
- **Backend**: blue/green, zero downtime
- **Feature flags**: LaunchDarkly
- **OTA**: EAS Update (JS-only, instant)
- **Hotfix**: full release (App Store 1-3 hari review)

### 13.4 Monitoring Stack

- **APM**: Datadog (RUM + APM + Logs)
- **Metrics**: Prometheus + Grafana
- **Logs**: ELK (Elasticsearch + Logstash + Kibana)
- **Errors**: Sentry (mobile + backend)
- **Uptime**: Datadog Synthetics, Pingdom
- **Alerting**: PagerDuty (P0/P1) + Slack (P2/P3)

---

## 14. Release Plan & Milestones

### 14.1 Timeline (7-8 bulan dari kickoff ke public launch)

| Phase | Timeline | Deliverable |
|---|---|---|
| **Phase 0**: Discovery & Design | W1-W4 | Final PRD, design system, wireframes, tech design doc |
| **Phase 1**: Backend Foundation | W5-W14 | Auth, KYC, User, Account, basic Market Data services |
| **Phase 2**: Mobile Foundation | W7-W14 (parallel) | Expo scaffold, auth flow, navigation, theme |
| **Phase 3**: Trading Core | W13-W22 | Order, Position, Portfolio (frontend + backend) |
| **Phase 4**: Market Data + Charting | W15-W22 | Real-time quote, WebSocket, custom chart |
| **Phase 5**: Pulse Copy + Academy | W20-W26 | Copy trading, Academy CMS, basic content |
| **Phase 6**: Hardening | W24-W30 | Security audit, perf test, bug bash, compliance |
| **Phase 7**: Closed Beta | W28-W32 | 2.000 user beta, feedback, iteration |
| **Phase 8**: Public Launch** | W33 | App Store + Play Store release |

### 14.2 MVP Release Criteria (Definition of Done)

- [ ] Onboarding end-to-end (e-KYC + e-Sign, target < 5 min)
- [ ] Demo account ($10K virtual) live sejak registrasi
- [ ] Real-time quote 50+ forex pairs + 10+ commodities + 10+ indices
- [ ] Custom charting (15+ indicators, 5+ drawing tools)
- [ ] Watchlist (multi-list, live update)
- [ ] Order placement (market, limit, stop, SL/TP) < 1.2s ACK
- [ ] Copy trading (follow top trader, proportional, risk limit)
- [ ] Portfolio analytics (win rate, drawdown, equity curve)
- [ ] Deposit (VA auto-confirm, e-wallet) + Withdrawal (same day)
- [ ] Pulse Academy (5 course, 30 lessons, quiz)
- [ ] Pulse Points + Rewards catalog
- [ ] Push notification (order, margin call, alert)
- [ ] Security: OWASP M1-M10 + API 2023 Top 10
- [ ] Performance: API p99 ≤ 600ms, app cold start ≤ 2s
- [ ] Crash-free ≥ 99.7%
- [ ] BAPPEBTI license verified
- [ ] Load test: 2.000 order/sec sustained 30 min
- [ ] Pen test: zero critical, max 3 high
- [ ] Beta: 2.000 user, NPS ≥ 45
- [ ] App Store / Play Store listing ready
- [ ] Customer support 24/7 staffed

---

## 15. Team & Roles

| Role | Count | Responsibility |
|---|---|---|
| Product Manager | 1 | Vision, roadmap, prioritization |
| Product Designer | 2 | UX, UI, design system |
| Tech Lead (Frontend) | 1 | Expo architecture, mobile code review |
| Tech Lead (Backend) | 1 | Spring Boot architecture, matching engine |
| Mobile Engineer (Expo) | 3 | Frontend implementation, testing |
| Backend Engineer (Java) | 4 | Service, DB, integration (1 khusus matching engine) |
| DevOps / SRE | 1 | K8s, CI/CD, on-call |
| QA Engineer | 2 | Test plan, automation, manual |
| Security Engineer | 1 | OWASP, pen test coordination |
| Compliance Officer | 1 | BAPPEBTI reporting, audit |
| Data Engineer | 1 | Market data pipeline, analytics |
| Customer Support Lead | 1 | 24/7 team setup (5 agents) |
| Total FTE | ~20 | |

---

## 16. Risks & Mitigations

| # | Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|---|
| 1 | BAPPEBTI license delay | High | Critical | Apply early (month 1), konsultasi konsultan hukum, fallback: white-label dengan MIFX |
| 2 | Liquidity provider integration delay | High | High | Multi-LP, kontrak dengan 2 (LMAX + ECN), 6 bulan lead time |
| 3 | Matching engine bug (slippage, requote) | Medium | Critical | Heavy load test, chaos test, gradual rollout dengan limit |
| 4 | Copy trading abuse (man-in-the-middle) | Medium | High | Verified trader badge, max followers, drawdown limit, kill switch |
| 5 | Trader top scam followers | Medium | High | Performance fee only on profit, clawback 30 hari, verified trader requirement |
| 6 | Margin call cascading | Medium | Critical | Real-time margin monitoring, circuit breaker per symbol |
| 7 | User kehilangan uang (trading loss) | High | High | Risk warning, max loss per hari, cool-down period, negative balance protection |
| 8 | Trust issue (broker scam di Indonesia marak) | High | High | Verifiable reserve, public audit, transparent fee, OJK/BAPPEBTI logo prominent |
| 9 | Compliance berubah (regulasi baru) | Medium | High | Compliance officer full-time, monitoring BAPPEBTI news weekly |
| 10 | Talent shortage (Java + RN di Indonesia) | High | Medium | Remote-first hiring, retain via ESOP, contractor pool |
| 11 | WebSocket unstable di mobile network | High | Medium | Auto-reconnect exponential backoff, fallback REST polling, offline mode untuk order queue |
| 12 | FX rate volatility (USD/IDR) | Medium | Medium | Multi-currency wallet, FX rate locked saat order |
| 13 | Kompetitor launch fitur serupa | Medium | Low | Speed of execution, kualitas lebih penting dari feature parity |
| 14 | Bug bounty reveals critical vuln | Low | Critical | Reserve bug bounty budget, incident response plan ready |

---

## 17. Open Questions

1. **BAPPEBTI license**: apply sendiri atau partnership white-label dengan broker existing (misal MIFX)? Timeline impact 6-12 bulan.
2. **Liquidity provider**: LMAX, PrimeXM, atau ECN aggregation? Pricing & SLA trade-off.
3. **Initial capital**: butuh modal minimal USD 5 juta untuk segregated account & operasional.
4. **Fee structure**: $2/lot flat atau tiered (Standard $3, Pro $2, Elite $1)?
5. **Copy trading fee**: 20% dari profit (standar industri) atau model lain (subscription)?
6. **Crypto integration**: V2.5 atau skip? Butuh lisensi BAPPEBTI khusus crypto exchange.
7. **Saham ID/US**: prioritaskan di V2 atau V3? Butuh partnership dengan sekuritas atau PPI.
8. **Initial market**: Indonesia only atau langsung SEA (Singapore, Malaysia, Filipina)?
9. **iPad support**: universal app atau iPhone-only di MVP?
10. **Customer support 24/7**: outsource atau in-house team?
11. **Insurance**: cyber insurance + E&O untuk broker?
12. **Brand registry**: daftarkan trademark PULSE + domain pulse.id segera.

---

## 18. Success Measurement & Review

### 18.1 KPI Dashboard (real-time)

- DAU/WAU/MAU
- Conversion: download → register → KYC → demo → live → funded
- Trading volume (lots per hari)
- Revenue (fee, spread, swap)
- AUM growth
- Retention cohort (D1, D7, D30)
- Churn rate
- NPS per minggu
- App store rating
- Support ticket SLA

### 18.2 Review Cadence

- **Daily standup** (eng)
- **Weekly sync** (PM + leads)
- **Bi-weekly sprint review** (full team)
- **Monthly business review** (stakeholders)
- **Quarterly OKR review** (leadership + board)

### 18.3 Pivot Triggers (6 bulan pertama)

- D7 retention < 25% → UX issue
- Demo → Live conversion < 15% → friction atau trust issue
- NPS < 30 → product-market fit belum
- AUM < USD 5 juta → distribution issue
- Monthly burn > runway → re-evaluate

---

## 19. Appendix

### 19.1 Glossary

- **BAPPEBTI**: Badan Pengawas Perdagangan Berjangka & Komoditi
- **PIP**: Percentage in Point, unit pergerakan harga terkecil
- **Lot**: satuan trading, 1 lot standar = 100.000 unit (forex)
- **Leverage**: daya ungkat modal (1:100 = modal USD 1 kontrol USD 100)
- **Margin**: jaminan yang dibutuhkan untuk buka posisi
- **Spread**: selisih bid-ask
- **Swap**: biaya inap (overnight fee)
- **Slippage**: selisih harga expected vs actual saat eksekusi
- **PIP Value**: nilai moneter dari 1 pip movement
- **Stop Out**: auto-close posisi saat margin level < 20%
- **Segregated Account**: rekening terpisah dana klien dari operasional broker
- **KYC**: Know Your Customer
- **CDD/EDD**: Customer Due Diligence / Enhanced Due Diligence
- **e-Sign**: Tanda tangan elektronik (UU ITE)
- **SID**: Single Investor Identification (KSEI, untuk saham)
- **OTC**: Over The Counter
- **LMAX**: London Multi-Asset Exchange (liquidity provider)
- **OTA**: Over-The-Air (mobile update)

### 19.2 References

- BAPPEBTI Regulation No. 2/BAPPEBTI/SWADANA/V/2019 (Pialang Berjangka)
- POJK No. 22/POJK.04/2014 (KYC Pasar Modal)
- POJK No. 15/2023 (LAPMN via KSEI)
- SEOJK No. 6/SEOJK.04/2019 (Rekening Elektronik)
- UU PDP No. 27/2022 (Pelindungan Data Pribadi)
- OWASP Mobile Top 10 (2024)
- OWASP API Security Top 10 (2023)
- ISO 27001:2022
- MIFX Mobile App analysis (Google Play, App Store review 2024-2026)
- MIFX.com website & fitur (https://mifx.com, https://mifx.com/mobile-app)
- Financer.id MIFX review 2026
- Investing.com MIFX review 2026
- Fastbull MIFX review 2026
- SeputarForex MIFX feature breakdown

### 19.3 Change Log

| Version | Date | Author | Change |
|---|---|---|---|
| 1.0 | 2026-08-27 | Mavis | Initial draft based on MIFX competitive analysis |

---

## 20. Final Note

PULSE dibangun dengan filosofi **"Don't fight the market, feel its pulse"** — aplikasi yang memberikan trader Indonesia akses cepat, transparan, dan modern ke pasar global, dengan bantuan AI dan komunitas sebagai pembeda utama dari kompetitor legacy seperti MIFX.

Keunggulan kompetitif inti:
1. **Mobile-first UX** (MIFX masih MT4-centric)
2. **Social copy trading** (MIFX tidak punya)
3. **Transparent fee** (flat $2/lot, no hidden spread)
4. **AI-assisted tools** (smart signal, risk scoring)
5. **Modern tech stack** (Expo + Spring Boot, ready untuk ekspansi fitur cepat)

Stack teknis yang dipilih (Expo + Spring Boot + PostgreSQL) sudah battle-tested di industri fintech, mudah di-hire talent-nya, dan scalable dari MVP hingga unicorn-scale.

---

**END OF DOCUMENT**

*Dokumen ini adalah living document. Update secara berkala sesuai perkembangan riset, feedback stakeholder, dan pivot berdasarkan data.*

**Next step recommendations:**
1. Validasi dengan sekuritas partner / potential investor
2. Konsultasi awal dengan BAPPEBTI untuk lisensi
3. Setup sandbox dengan LMAX atau LP alternatif
4. Hire core team (tech lead + compliance officer)
5. Mulai Phase 0: design system + tech design doc detail
