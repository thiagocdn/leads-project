CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE leads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    referral_source VARCHAR(50) NOT NULL,
    referral_others TEXT,
    quantity_requested INT NOT NULL DEFAULT 1,
    contacted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);
