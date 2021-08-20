CREATE TYPE product_item_type AS ENUM ('DESIGN', 'GOODS');
CREATE TYPE input_status AS ENUM ('DRAFT', 'REGISTERED');

CREATE TABLE product (
    id BIGSERIAL NOT NULL,
    knitter_id bigint NOT NULL REFERENCES knitter(id),
    name VARCHAR NOT NULL,
    full_price NUMERIC NOT NULL,
    discount_price NUMERIC NOT NULL,
    representative_image_url VARCHAR NOT NULL,
    specified_sales_start_date DATE NOT NULL,
    specified_sales_end_date DATE NOT NULL,
    content VARCHAR,
    input_status input_status NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE product_item (
    id BIGSERIAL NOT NULL,
    product_id bigint NOT NULL REFERENCES product(id),
    item_id NUMERIC NOT NULL,
    type product_item_type NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE product_tag (
    id BIGSERIAL NOT NULL,
    product_id bigint NOT NULL REFERENCES product(id),
    tag VARCHAR NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);

