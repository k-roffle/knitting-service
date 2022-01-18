ALTER TABLE design ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE;

UPDATE design SET updated_at = created_at;

ALTER TABLE design ALTER COLUMN updated_at SET NOT NULL;
