ALTER TABLE design ADD COLUMN total_length NUMERIC;
ALTER TABLE design ADD COLUMN sleeve_length NUMERIC;
ALTER TABLE design ADD COLUMN shoulder_width NUMERIC;
ALTER TABLE design ADD COLUMN bottom_width NUMERIC;
ALTER TABLE design ADD COLUMN armhole_depth NUMERIC;

WITH design_with_size as (
    SELECT
        d.id, totalLength, sleeveLength, shoulderWidth, bottomWidth, armholeDepth
    FROM design d
    JOIN size s ON s.id = d.size_id
)
UPDATE design
SET
    total_length = dws.totalLength,
    sleeve_length = dws.sleeveLength,
    shoulder_width = dws.shoulderWidth,
    bottom_width = dws.bottomWidth,
    armhole_depth = dws.armholeDepth
FROM design_with_size dws
WHERE design.id = dws.id;


ALTER TABLE design ALTER COLUMN total_length SET NOT NULL;
ALTER TABLE design ALTER COLUMN sleeve_length SET NOT NULL;
ALTER TABLE design ALTER COLUMN shoulder_width SET NOT NULL;
ALTER TABLE design ALTER COLUMN bottom_width SET NOT NULL;
ALTER TABLE design ALTER COLUMN armhole_depth SET NOT NULL;
ALTER TABLE design DROP COLUMN size_id;
DROP TABLE size;
