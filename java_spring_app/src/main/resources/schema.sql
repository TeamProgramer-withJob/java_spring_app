CREATE TABLE IF NOT EXISTS users (
    id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    username     TEXT    NOT NULL UNIQUE,
    display_name TEXT    NOT NULL,
    email        TEXT    NOT NULL UNIQUE,
    password     TEXT    NOT NULL,
    bio          TEXT,
    role         TEXT    NOT NULL DEFAULT 'USER',
    created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cemeteries (
    id          INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
    owner_id    INTEGER  NOT NULL,
    name        TEXT     NOT NULL,
    description TEXT,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS memories (
    id          INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
    cemetery_id INTEGER  NOT NULL,
    author_id   INTEGER  NOT NULL,
    title       TEXT     NOT NULL,
    body        TEXT     NOT NULL,
    image_data         BLOB,
    image_content_type TEXT,
    visibility         TEXT     NOT NULL DEFAULT 'PUBLIC',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cemetery_id) REFERENCES cemeteries(id),
    FOREIGN KEY (author_id)   REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS follows (
    follower_id INTEGER  NOT NULL,
    followee_id INTEGER  NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followee_id),
    FOREIGN KEY (follower_id) REFERENCES users(id),
    FOREIGN KEY (followee_id) REFERENCES users(id)
);
