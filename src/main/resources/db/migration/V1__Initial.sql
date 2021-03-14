CREATE TYPE design_type AS ENUM ('Top', 'Blanket', 'Socks');

CREATE TYPE pattern_type AS ENUM ('Letter', 'Image', 'Video');

CREATE TABLE design (
    id UUID NOT NULL,
    name VARCHAR NOT NULL,
    design_type design_type NOT NULL,
    pattern_type pattern_type NOT NULL,
    stitches NUMERIC NOT NULL,
    rows NUMERIC NOT NULL,
    needle VARCHAR NOT NULL,
    yarn VARCHAR,
    extra VARCHAR,
    price NUMERIC DEFAULT '0',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);
