CREATE TABLE draft_product (
    id BIGSERIAL NOT NULL,
    knitter_id bigint NOT NULL REFERENCES knitter(id),
    product_id bigint REFERENCES product(id),
    value VARCHAR NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_At TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE product DROP COLUMN IF EXISTS input_status;
