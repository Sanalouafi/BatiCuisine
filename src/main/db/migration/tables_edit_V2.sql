CREATE TYPE ComponentType AS ENUM ('Materiel', 'Labor');
ALTER TABLE Component
DROP COLUMN type;
ALTER TABLE Component
    ADD COLUMN type ComponentType NOT NULL;
