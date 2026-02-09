create table IF NOT EXISTS issues (
id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
summary VARCHAR(256) NOT NULl,
description VARCHAR(256) NOT NULl,
fullpath VARCHAR(512),
media_type INTEGER
);

create table IF NOT EXISTS users (
username varchar(50) not null primary key,
password varchar(500) not null
);