CREATE TABLE matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    game_type VARCHAR(16),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    result JSONB
);
