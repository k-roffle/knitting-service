ALTER TABLE design ADD COLUMN price NUMERIC;

UPDATE design SET price = 0;

ALTER TABLE design ALTER COLUMN price SET NOT NULL;
