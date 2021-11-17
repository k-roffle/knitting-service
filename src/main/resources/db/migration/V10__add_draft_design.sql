CREATE TABLE draft_design (
    id BIGSERIAL NOT NULL,
    knitter_id bigint NOT NULL REFERENCES knitter(id),
    design_id bigint REFERENCES design(id),
    value VARCHAR NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_At TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);
