CREATE TABLE categories
(
    id            serial primary key,
    short_id      varchar not null,
    category_name varchar not null
);

CREATE TABLE products
(
    id                  serial primary key,
    uid                 varchar   not null,
    active            boolean   not null DEFAULT FALSE,
    product_name        varchar   not null,
    main_desc           TEXT      not null,
    desc_html           TEXT      not null,
    price               decimal(12,2)     not null,
    image_urls          varchar[] not null,
    parameters          TEXT,
    create_at           DATE,
    category_id integer REFERENCES "categories" (id)
)
