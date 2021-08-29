ALTER TABLE product ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE;

UPDATE product SET updated_at = created_at;

ALTER TABLE product ALTER COLUMN updated_at SET NOT NULL;
