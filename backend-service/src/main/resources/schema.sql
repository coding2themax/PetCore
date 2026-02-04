-- Create custom schema for PetCore
CREATE SCHEMA IF NOT EXISTS petcore;

-- Create pets table in the petcore schema
CREATE TABLE IF NOT EXISTS petcore.pets (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    species VARCHAR(50) NOT NULL,
    breed VARCHAR(255),
    sex VARCHAR(20) NOT NULL,
    age_value INTEGER NOT NULL,
    age_unit VARCHAR(20) NOT NULL,
    size VARCHAR(50) NOT NULL,
    intake_date DATE NOT NULL,
    intake_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    external_reference_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(external_reference_id)
);
