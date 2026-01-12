CREATE TABLE IF NOT EXISTS pets (
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
    status VARCHAR(50) NOT NULL
);
