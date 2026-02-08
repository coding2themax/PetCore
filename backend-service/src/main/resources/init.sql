-- Initial pet data for PetCore system

INSERT INTO petcore.pets (id, name, species, breed, sex, age_value, age_unit, size, intake_date, intake_type, status, external_reference_id) VALUES
(gen_random_uuid(), 'Max', 'Dog', 'Golden Retriever', 'Male', 3, 'Years', 'Large', '2025-11-15', 'Stray', 'Available', 'EXT-DOG-001'),
(gen_random_uuid(), 'Luna', 'Cat', 'Domestic Shorthair', 'Female', 2, 'Years', 'Small', '2025-12-01', 'Owner Surrender', 'Available', 'EXT-CAT-001'),
(gen_random_uuid(), 'Buddy', 'Dog', 'Labrador Mix', 'Male', 5, 'Years', 'Large', '2025-10-20', 'Stray', 'Available', 'EXT-DOG-002'),
(gen_random_uuid(), 'Whiskers', 'Cat', 'Siamese', 'Male', 8, 'Months', 'Small', '2026-01-10', 'Stray', 'Available', 'EXT-CAT-002'),
(gen_random_uuid(), 'Bella', 'Dog', 'Beagle', 'Female', 4, 'Years', 'Medium', '2025-11-28', 'Owner Surrender', 'Adopted', 'EXT-DOG-003'),
(gen_random_uuid(), 'Charlie', 'Dog', 'German Shepherd', 'Male', 6, 'Years', 'Large', '2025-12-15', 'Transfer', 'Available', 'EXT-DOG-004'),
(gen_random_uuid(), 'Mittens', 'Cat', 'Maine Coon', 'Female', 1, 'Years', 'Medium', '2026-01-05', 'Stray', 'Available', 'EXT-CAT-003'),
(gen_random_uuid(), 'Rocky', 'Dog', 'Boxer', 'Male', 2, 'Years', 'Large', '2025-12-22', 'Stray', 'In Care', 'EXT-DOG-005'),
(gen_random_uuid(), 'Daisy', 'Dog', 'Poodle Mix', 'Female', 7, 'Years', 'Small', '2025-11-10', 'Owner Surrender', 'Available', 'EXT-DOG-006'),
(gen_random_uuid(), 'Oliver', 'Cat', 'Persian', 'Male', 4, 'Years', 'Small', '2026-01-20', 'Transfer', 'Available', 'EXT-CAT-004');
