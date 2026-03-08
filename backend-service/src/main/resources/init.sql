-- Initial pet data for PetCore system

INSERT INTO petcore.pets (id, name, species, breed, sex, age_value, age_unit, size, intake_date, intake_type, status, external_reference_id) VALUES
(gen_random_uuid(), 'Max', 'DOG', 'Golden Retriever', 'MALE', 3, 'YEARS', 'LARGE', '2025-11-15', 'STRAY', 'AVAILABLE', 'EXT-DOG-001'),
(gen_random_uuid(), 'Luna', 'CAT', 'Domestic Shorthair', 'FEMALE', 2, 'YEARS', 'SMALL', '2025-12-01', 'OWNER_SURRENDER', 'AVAILABLE', 'EXT-CAT-001'),
(gen_random_uuid(), 'Buddy', 'DOG', 'Labrador Mix', 'MALE', 5, 'YEARS', 'LARGE', '2025-10-20', 'STRAY', 'AVAILABLE', 'EXT-DOG-002'),
(gen_random_uuid(), 'Whiskers', 'CAT', 'Siamese', 'MALE', 8, 'MONTHS', 'SMALL', '2026-01-10', 'STRAY', 'AVAILABLE', 'EXT-CAT-002'),
(gen_random_uuid(), 'Bella', 'DOG', 'Beagle', 'FEMALE', 4, 'YEARS', 'MEDIUM', '2025-11-28', 'OWNER_SURRENDER', 'ADOPTED', 'EXT-DOG-003'),
(gen_random_uuid(), 'Charlie', 'DOG', 'German Shepherd', 'MALE', 6, 'YEARS', 'LARGE', '2025-12-15', 'TRANSFER', 'AVAILABLE', 'EXT-DOG-004'),
(gen_random_uuid(), 'Mittens', 'CAT', 'Maine Coon', 'FEMALE', 1, 'YEARS', 'MEDIUM', '2026-01-05', 'STRAY', 'AVAILABLE', 'EXT-CAT-003'),
(gen_random_uuid(), 'Rocky', 'DOG', 'Boxer', 'MALE', 2, 'YEARS', 'LARGE', '2025-12-22', 'STRAY', 'HOLD', 'EXT-DOG-005'),
(gen_random_uuid(), 'Daisy', 'DOG', 'Poodle Mix', 'FEMALE', 7, 'YEARS', 'SMALL', '2025-11-10', 'OWNER_SURRENDER', 'AVAILABLE', 'EXT-DOG-006'),
(gen_random_uuid(), 'Oliver', 'CAT', 'Persian', 'MALE', 4, 'YEARS', 'SMALL', '2026-01-20', 'TRANSFER', 'AVAILABLE', 'EXT-CAT-004');
