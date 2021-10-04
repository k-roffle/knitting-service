CREATE TABLE size (
    id BIGSERIAL NOT NULL,
    design_id bigint NOT NULL REFERENCES design(id),
    total_length NUMERIC NOT NULL,
    sleeve_length NUMERIC NOT NULL,
    shoulder_width NUMERIC NOT NULL,
    bottom_width NUMERIC NOT NULL,
    armhole_depth NUMERIC NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE design DROP COLUMN IF EXISTS total_length;
ALTER TABLE design DROP COLUMN IF EXISTS sleeve_length;
ALTER TABLE design DROP COLUMN IF EXISTS shoulder_width;
ALTER TABLE design DROP COLUMN IF EXISTS bottom_width;
ALTER TABLE design DROP COLUMN IF EXISTS armhole_depth;

DELETE FROM technique;
DELETE FROM design;
