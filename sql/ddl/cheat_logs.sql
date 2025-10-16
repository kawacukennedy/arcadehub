CREATE TABLE cheat_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(32),
    violation VARCHAR(128),
    details TEXT,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT now()
);
