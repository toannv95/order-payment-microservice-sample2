-- Table: public.orders

-- DROP TABLE IF EXISTS public.orders;

CREATE TABLE IF NOT EXISTS public.orders
(
    id serial CONSTRAINT orders_pkey PRIMARY KEY,
    customer_id bigint,
    price integer NOT NULL,
    product_count integer NOT NULL,
    product_id bigint,
    source character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default"
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.orders
    OWNER to postgres;