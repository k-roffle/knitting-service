CREATE TYPE design_type AS ENUM ('Top', 'Blanket', 'Socks');

CREATE TYPE pattern_type AS ENUM ('Letter', 'Image', 'Video');

CREATE TABLE knitter (
    id BIGSERIAL NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    name VARCHAR,
    profile_image_url VARCHAR,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE design (
    id BIGSERIAL NOT NULL,
    knitterId BIGSERIAL NOT NULL REFERENCES knitter(id),
    name VARCHAR NOT NULL,
    design_type design_type NOT NULL,
    pattern_type pattern_type NOT NULL,
    stitches NUMERIC NOT NULL,
    rows NUMERIC NOT NULL,
    needle VARCHAR NOT NULL,
    yarn VARCHAR,
    extra VARCHAR,
    price NUMERIC DEFAULT '0',
    total_length NUMERIC NOT NULL,
    sleeve_length NUMERIC NOT NULL,
    shoulder_width NUMERIC NOT NULL,
    bottom_width NUMERIC NOT NULL,
    armhole_depth NUMERIC NOT NULL,
    pattern VARCHAR NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);
