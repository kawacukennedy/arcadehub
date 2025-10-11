-- Players table
CREATE TABLE players (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(32) UNIQUE NOT NULL,
    elo INT NOT NULL DEFAULT 1000,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    last_login TIMESTAMP WITH TIME ZONE
);

-- Cheat logs table
CREATE TABLE cheat_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(32),
    violation TEXT NOT NULL,
    points INT NOT NULL,
    tick BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Lobbies table
CREATE TABLE lobbies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(64),
    host VARCHAR(32),
    type VARCHAR(16) CHECK (type IN ('SNAKE','PONG')),
    seed BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    last_active TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Replays table
CREATE TABLE replays (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lobby_id UUID REFERENCES lobbies(id),
    path TEXT NOT NULL,
    tick_start BIGINT,
    tick_end BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    size_bytes BIGINT
);

-- Revoked sessions table
CREATE TABLE revoked_sessions (
    session_hash BYTEA PRIMARY KEY,
    revoked_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes
CREATE INDEX idx_players_elo ON players (elo DESC);
CREATE INDEX idx_lobbies_last_active ON lobbies (last_active);
CREATE INDEX idx_cheat_logs_username ON cheat_logs (username);