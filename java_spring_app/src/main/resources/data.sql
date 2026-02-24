-- テストユーザー（パスワード: password / BCryptハッシュ）
-- INSERT OR IGNORE: DB ファイルが既に存在する場合でも安全に再実行できる
INSERT OR IGNORE INTO users (username, display_name, email, password, bio, role) VALUES
    ('tom',   'Tom田中',  'tom@example.com',   '$2a$10$uFUw8aa1/yI7gk.2YCgTLOl34Vrw32USI3VASc5msA4s5Z/IQYVmy', '霊園を管理しています', 'USER'),
    ('bob',   'Bob鈴木',  'bob@example.com',   '$2a$10$uFUw8aa1/yI7gk.2YCgTLOl34Vrw32USI3VASc5msA4s5Z/IQYVmy', 'よろしくお願いします', 'USER'),
    ('admin', '管理者',   'admin@example.com', '$2a$10$uFUw8aa1/yI7gk.2YCgTLOl34Vrw32USI3VASc5msA4s5Z/IQYVmy', 'サービス管理者です',   'ADMIN');

-- テスト霊園
INSERT OR IGNORE INTO cemeteries (id, owner_id, name, description) VALUES
    (1, 1, '田中家の思い出',   '田中家の大切な思い出を集めた霊園です'),
    (2, 2, '鈴木家の霊園',     'みんなで思い出を残しましょう');

-- テスト思い出
INSERT OR IGNORE INTO memories (id, cemetery_id, author_id, title, body, visibility) VALUES
    (1, 1, 1, '父との思い出',     '子供の頃、毎週末に一緒に釣りに行きました。懐かしい日々です。', 'PUBLIC'),
    (2, 1, 1, '家族旅行の記憶',   '家族みんなで行った北海道旅行は一生の思い出です。',             'PUBLIC'),
    (3, 2, 2, '祖母の手料理',     '祖母が作ってくれたおはぎの味が忘れられません。',               'PUBLIC');

-- テストフォロー（tom が bob をフォロー）
INSERT OR IGNORE INTO follows (follower_id, followee_id) VALUES (1, 2);
