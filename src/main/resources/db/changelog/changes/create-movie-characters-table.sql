--liquibase formatted sql
create table if not exists public.movie_characters
(
    id bigint not null,
    external_id bigint not null,
    name character varying(256) not null,
    status character varying(256) not null,
    gender character varying(256) not null,
    constraint movie_characters_pk primary key (id)
);

-- rollback drop table movie_characters;