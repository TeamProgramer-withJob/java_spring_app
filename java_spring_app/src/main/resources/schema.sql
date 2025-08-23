create table issues (
id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
summary VARCHAR(256) NOT NULl,
description VARCHAR(256) NOT NULl
);

create table users (
username varchar(50) not null primary key,
password varchar(500) not null
);

create table if not exists inquiry (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name varchar(255) not null,
  email varchar(255) not null,
  subject varchar(255) not null,
  message text not null,
  created_at timestamp default current_timestamp
);
