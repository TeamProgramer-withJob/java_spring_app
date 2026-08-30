# java_spring_app

## 開発環境

### java jdk21
### Eclipse IDE: Version: 2025-09 (4.37.0), Build id: 20250905-1456
### SpringBoot 4.0.2
### PostgreSQL 16.0, pgAdmin

## PostgreSQL database

### database接続ユーザーuiditsの作成

pgAdminを開き、PostgreSQLと接続する。  
Login/Group Roles -> Create -> Login/Group Roleを選択し、以下のようにユーザーuiditsを作成する。  
General: Name: uidits  
Privileges: Can login? ON, Create database? ON, Inherit rights from the parent roles? ON  
SAVEボタンをクリックしてユーザー登録する  

### database itsの作成

Databasesをマウス右クリックし、Create -> Databaseを選択し、以下のようにdatabaseを作成する。  
General: Database: its, Owner: uidits, Comment: 任意   
Definition: Encoding: UTF8  
Tablespace: pg_default  
Locale Provider: libc  
Collation: Japanese_Japan.932  
Character type: Japanese_Japan.932  
SAVEボタンをクリックしてdatabase itsを作成する  

### schema itsの作成

pgAdminを開き、uiditsでitsデータベースと接続して、itsをマウス右クリックでQuery Toolを開く。Query Toolで以下のコマンドを実行する  

CREATE SCHEMA IF NOT EXISTS its AUTHORIZATION uidits;  

### tablesの作成

src/main/resources/schema4postgreSQL.sqlを開き、以下の行以降をQuery Toolにコピーして実行する  

CREATE TABLE IF NOT EXISTS its.users  
...  

### 初期管理ユーザーの作成

下記の内容をQuery Toolにコピーして実行する  
set search_path ='its';  
insert into users(name,password,email,role,created_at,last_updated) 
values('user 01','$2a$10$BsF1RMe/Vf0dXzTmRbO11O6UUqOD/lkL5ygspZ/bykle1pQcMl6YG','test01@test.example.org','ADMIN',current_timestamp,current_timestamp);  

ログイン画面で電子メール: test01@test.example.orgとパスワード: passwordでログインできる。  

## Eclipseにprojectの取り込む

後ほど追記  

## webアプリ

### ログイン

Tomcat起動後に、http://localhost:8080にアクセスする。  
登録済み管理者または一般ユーザーの電子メールとパスワードでログインできる。  

### ユーザーサインアップ

ナビゲーションバーから「サインアップ」をクリックし、ユーザー名、パスワードとパスワード確認、電子メールを入力してサインアップボタンをクリックする。  

※ 電子メールのフォーマットチェックは未実装  

### 思い出一覧

ログイン後のデフォルト画面は思い出一覧画面となる。  
「作成」リンクをクリックし、画像付き思い出の投稿ができる。  

「詳細」から内容のリンク（今後、リンクをタイトルに移植する予定）をクリックし、思い出の詳細画面へ遷移できる。  

### 思い出の投稿

### 思い出の削除

### 思い出の詳細と「いいね」

※ 作りながら、DBスキーマ、画面と機能を作っていく予定。完成した機能の説明はここで追記していく。  


鍾 志華  
2026/02/24  
