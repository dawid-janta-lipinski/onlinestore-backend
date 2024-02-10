CREATE TABLE "images"
(
    id        serial primary key,
    uuid      varchar not null,
    file_path varchar not null,
    is_used    boolean not null DEFAULT FALSE,
    created_at DATE not null
);