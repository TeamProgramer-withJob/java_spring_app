CREATE DATABASE its
    WITH
    OWNER = uidits
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

COMMENT ON DATABASE its
    IS 'database for treasure box project developed with SpringBoot4.0';

CREATE SCHEMA IF NOT EXISTS its AUTHORIZATION uidits;

CREATE TABLE IF NOT EXISTS its.users
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,
    password character varying(64) COLLATE pg_catalog."default" NOT NULL,
    email character varying(256) COLLATE pg_catalog."default" NOT NULL,
    role character varying(8) COLLATE pg_catalog."default" NOT NULL DEFAULT 'USER',
    subscription_id character varying(36) COLLATE pg_catalog."default",
    plan_id smallint,
    icon bytea,
    created_at timestamp(3) with time zone DEFAULT CURRENT_TIMESTAMP,
    last_updated timestamp(3) with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT idx_users_email_unique UNIQUE (email)
) TABLESPACE pg_default;
ALTER TABLE IF EXISTS its.users OWNER to uidits;

insert into users(name,password,email,role,created_at,last_updated) 
values('user 01','$2a$10$BsF1RMe/Vf0dXzTmRbO11O6UUqOD/lkL5ygspZ/bykle1pQcMl6YG','test01@test.example.org','ADMIN',current_timestamp,current_timestamp);

CREATE TABLE IF NOT EXISTS its.memories
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id integer NOT NULL,
    title character varying(128) COLLATE pg_catalog."default" NOT NULL,
    details character varying(512) COLLATE pg_catalog."default",
    created_at timestamp(3) with time zone DEFAULT CURRENT_TIMESTAMP,
    last_updated timestamp(3) with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT memories_pkey PRIMARY KEY (id),
	CONSTRAINT fkey_memories_user_id FOREIGN KEY (user_id) REFERENCES its.users (id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE
) TABLESPACE pg_default;
ALTER TABLE IF EXISTS its.memories OWNER to uidits;

CREATE TABLE IF NOT EXISTS its.mem_images
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    mem_id integer NOT NULL,
    filename character varying(128) COLLATE pg_catalog."default" NOT NULL,
    content_type character varying(16) COLLATE pg_catalog."default" NOT NULL,
    image bytea NOT NULL,
    created_at timestamp(3) with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT mem_images_pkey PRIMARY KEY (id),
	CONSTRAINT fkey_mem_images_mem_id FOREIGN KEY (mem_id) REFERENCES its.memories (id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE
) TABLESPACE pg_default;
ALTER TABLE IF EXISTS its.mem_images OWNER to uidits;

CREATE TABLE IF NOT EXISTS its.sentiments
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id integer NOT NULL,
    mem_id integer NOT NULL,
    sentiment smallint NOT NULL DEFAULT 1,
    created_at timestamp(3) with time zone DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fkey_sentiments_mem_id FOREIGN KEY (mem_id) REFERENCES its.memories (id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fkey_sentiments_user_id FOREIGN KEY (user_id) REFERENCES its.users (id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE
) TABLESPACE pg_default;
ALTER TABLE IF EXISTS its.sentiments OWNER to uidits;
