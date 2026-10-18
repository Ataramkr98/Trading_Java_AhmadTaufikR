-- PULSE MVP schema (part 1: market & trading)
CREATE TABLE symbols (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code            VARCHAR(20) UNIQUE NOT NULL,
    name            VARCHAR(100) NOT NULL,
    category        VARCHAR(20) NOT NULL,
    digits          INTEGER NOT NULL,
    min_lot         DECIMAL(10,4) NOT NULL,
    max_lot         DECIMAL(15,4) NOT NULL DEFAULT 100,
    contract_size   DECIMAL(15,4) NOT NULL,
    bid             DECIMAL(15,5) NOT NULL,
    ask             DECIMAL(15,5) NOT NULL,
    last            DECIMAL(15,5) NOT NULL,
    change_val      DECIMAL(15,5) NOT NULL DEFAULT 0,
    change_pct      DECIMAL(10,4) NOT NULL DEFAULT 0,
    spread_pips     DECIMAL(10,2) NOT NULL DEFAULT 0,
    volume_label    VARCHAR(20) NOT NULL DEFAULT '',
    market_status   VARCHAR(10) NOT NULL DEFAULT 'OPEN'
);

CREATE TABLE trading_accounts (
    id              UUID PRIMARY KEY,
    user_id         UUID NOT NULL,
    account_number  VARCHAR(20) UNIQUE NOT NULL,
    account_type    VARCHAR(20) NOT NULL DEFAULT 'STANDARD',
    currency        VARCHAR(3) NOT NULL DEFAULT 'USD',
    leverage        INTEGER NOT NULL DEFAULT 100,
    balance         DECIMAL(20,2) NOT NULL DEFAULT 0,
    status          VARCHAR(20) NOT NULL DEFAULT 'active',
    opened_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE ohlcv (
    id          BIGSERIAL PRIMARY KEY,
    symbol      VARCHAR(20) NOT NULL,
    timeframe   VARCHAR(5) NOT NULL,
    bar_index   INTEGER NOT NULL,
    open        DECIMAL(15,5) NOT NULL,
    high        DECIMAL(15,5) NOT NULL,
    low         DECIMAL(15,5) NOT NULL,
    close       DECIMAL(15,5) NOT NULL
);
CREATE INDEX idx_ohlcv_symbol_tf ON ohlcv(symbol, timeframe, bar_index);

CREATE TABLE orders (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id      UUID NOT NULL REFERENCES trading_accounts(id),
    order_number    VARCHAR(30) UNIQUE NOT NULL,
    symbol          VARCHAR(20) NOT NULL,
    side            VARCHAR(4) NOT NULL,
    order_type      VARCHAR(20) NOT NULL,
    quantity        DECIMAL(15,4) NOT NULL,
    sl_price        DECIMAL(15,5),
    tp_price        DECIMAL(15,5),
    filled_qty      DECIMAL(15,4) DEFAULT 0,
    avg_fill_price  DECIMAL(15,5),
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    reject_reason   TEXT,
    placed_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    filled_at       TIMESTAMPTZ
);
CREATE INDEX idx_orders_account ON orders(account_id, placed_at DESC);

CREATE TABLE positions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id      UUID NOT NULL REFERENCES trading_accounts(id),
    symbol          VARCHAR(20) NOT NULL,
    side            VARCHAR(4) NOT NULL,
    quantity        DECIMAL(15,4) NOT NULL,
    open_price      DECIMAL(15,5) NOT NULL,
    sl_price        DECIMAL(15,5),
    tp_price        DECIMAL(15,5),
    commission      DECIMAL(15,2) NOT NULL DEFAULT 0,
    realized_pnl    DECIMAL(15,2) NOT NULL DEFAULT 0,
    status          VARCHAR(10) NOT NULL DEFAULT 'OPEN',
    opened_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    closed_at       TIMESTAMPTZ
);
CREATE INDEX idx_positions_account ON positions(account_id, status);
