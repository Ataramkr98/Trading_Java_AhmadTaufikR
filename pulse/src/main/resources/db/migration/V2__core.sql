-- PULSE MVP schema (part 2: portfolio, copy, watchlist, academy)
CREATE TABLE transactions (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL,
    account_id  UUID,
    tx_type     VARCHAR(30) NOT NULL,
    amount      DECIMAL(20,4) NOT NULL,
    currency    VARCHAR(3) NOT NULL DEFAULT 'USD',
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_transactions_user ON transactions(user_id, created_at DESC);

CREATE TABLE equity_curve_points (
    id          BIGSERIAL PRIMARY KEY,
    user_id     UUID NOT NULL,
    point_index INTEGER NOT NULL,
    curve_value DECIMAL(20,2) NOT NULL
);

CREATE TABLE top_traders (
    id              UUID PRIMARY KEY,
    display_name    VARCHAR(100) NOT NULL,
    style           VARCHAR(100) NOT NULL,
    roi             DECIMAL(8,2) NOT NULL,
    win_rate        DECIMAL(5,2) NOT NULL,
    followers       INTEGER NOT NULL DEFAULT 0,
    pulse_score     DECIMAL(5,2) NOT NULL,
    drawdown        DECIMAL(5,2) NOT NULL,
    is_verified     BOOLEAN NOT NULL DEFAULT FALSE,
    months_tracked  INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE copy_subscriptions (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    follower_id         UUID NOT NULL,
    trader_id           UUID NOT NULL REFERENCES top_traders(id),
    copy_mode           VARCHAR(20) NOT NULL,
    fixed_amount        DECIMAL(15,2),
    max_loss_limit      DECIMAL(15,2) NOT NULL,
    max_position_size   DECIMAL(15,4) NOT NULL,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    started_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    stopped_at          TIMESTAMPTZ,
    total_pnl           DECIMAL(15,2) NOT NULL DEFAULT 0
);
CREATE INDEX idx_copy_subs_follower ON copy_subscriptions(follower_id, is_active);

CREATE TABLE watchlists (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL,
    name        VARCHAR(100) NOT NULL DEFAULT 'Default',
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE watchlist_items (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    watchlist_id    UUID NOT NULL REFERENCES watchlists(id) ON DELETE CASCADE,
    symbol          VARCHAR(20) NOT NULL,
    favorite        BOOLEAN NOT NULL DEFAULT FALSE,
    item_position   INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX idx_watchlist_items_list ON watchlist_items(watchlist_id, item_position);

CREATE TABLE academy_courses (
    id              UUID PRIMARY KEY,
    title           VARCHAR(150) NOT NULL,
    level           VARCHAR(50) NOT NULL,
    lessons_count   INTEGER NOT NULL,
    duration        VARCHAR(50) NOT NULL
);

CREATE TABLE lesson_completions (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL,
    course_id   UUID NOT NULL REFERENCES academy_courses(id),
    lesson_index INTEGER NOT NULL,
    completed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, course_id, lesson_index)
);
