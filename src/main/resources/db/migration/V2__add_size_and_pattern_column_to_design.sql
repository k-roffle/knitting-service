ALTER TYPE design_type RENAME TO tmp_design_type;
ALTER TYPE pattern_type RENAME TO tmp_pattern_type;

CREATE TYPE design_type AS ENUM ('Sweater');
CREATE TYPE pattern_type AS ENUM ('Text', 'Image', 'Video');


ALTER TABLE design ALTER COLUMN design_type TYPE design_type USING 'Sweater';
ALTER TABLE design ALTER COLUMN pattern_type TYPE pattern_type USING pattern_type::text::pattern_type;

DROP TYPE tmp_design_type;
DROP TYPE tmp_pattern_type;

CREATE TABLE size (
      id UUID NOT NULL,
      totalLength NUMERIC NOT NULL,
      sleeveLength NUMERIC NOT NULL,
      shoulderWidth NUMERIC NOT NULL,
      bottomWidth NUMERIC NOT NULL,
      armholeDepth NUMERIC NOT NULL,
      PRIMARY KEY (id)
);

ALTER TABLE design ADD COLUMN size_id UUID not null;
ALTER TABLE design ADD COLUMN pattern VARCHAR not null;
