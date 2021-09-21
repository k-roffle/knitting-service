CREATE TABLE technique (
    id BIGSERIAL NOT NULL,
    design_id bigint NOT NULL REFERENCES design(id),
    name VARCHAR NOT NULL,
    PRIMARY KEY (id)
);
