-- Table: public.product

-- DROP TABLE IF EXISTS public.product;

CREATE TABLE IF NOT EXISTS public.product
(
    id serial CONSTRAINT product_pkey PRIMARY KEY,
    available_items integer NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    reserved_items integer NOT NULL
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.product
    OWNER to postgres;