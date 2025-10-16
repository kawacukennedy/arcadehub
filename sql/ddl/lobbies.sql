CREATE TABLE lobbies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(64),
    host_username VARCHAR(32),
    game_type VARCHAR(16),
    status VARCHAR(16) DEFAULT 'WAITING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
