# 結びの霊園

オンライン霊園Webアプリケーション（Spring Boot / 勉強会プロジェクト）

## 実装済み機能

| 機能 | 概要 |
|------|------|
| ログイン・ログアウト / 会員登録 | Spring Security セッション方式 |
| 霊園ページ | 一覧・詳細・作成・編集・削除 |
| 思い出投稿 | 一覧・詳細・作成・編集・削除（画像アップロード対応） |
| フォロー機能 | 片方向フォロー（承認制なし） |
| マイページ | 自分の霊園・投稿一覧 |
| 共通エラーハンドリング | 403 / 404 / 500 ページ（`@ControllerAdvice`） |
| 回帰テスト | Controller 層・MockMvc 中心（主要エラーケースをカバー） |

## 必要な環境

### 必須
- **Java**: 21以上（推奨: Eclipse Temurin 21）
- **Gradle**: 8.5以上（Gradle Wrapperを使用するため不要）
- **Git**: 2.x以上

### 推奨IDE（どれか1つ）
- IntelliJ IDEA 2023.x以上
- Eclipse 2023-12以上
- VS Code + Java Extension Pack

## セットアップ手順

### 1. リポジトリのクローン
```bash
git clone git@github.com:TeamProgramer-withJob/java_spring_app.git
cd java_spring_app
```

### 2. Java 21のインストール確認
```bash
java -version
# Java version "21.x.x" が表示されることを確認
```

**Java 21がない場合**:
- Windows: [Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21) をダウンロード
- macOS: `brew install openjdk@21`
- Linux: `sudo apt install openjdk-21-jdk` (Ubuntu/Debian)

### 3. ビルドと起動
```bash
# Windows (PowerShell または Git Bash)
./gradlew clean build
./gradlew bootRun

# アプリケーションが http://localhost:8080 で起動します
```

### 4. 動作確認
ブラウザで以下のURLにアクセス:
- アプリケーション: http://localhost:8080

> **DB**: SQLite ファイルベース（`./data/cemetery.db`）。アプリ起動のたびに `schema.sql` / `data.sql` が流れます。

## 技術スタック

- **Spring Boot**: 3.2.1
- **Java**: 21
- **Gradle**: 8.5
- **MyBatis**: 3.0.3
- **Spring Security**: 6.x
- **Thymeleaf**: 3.x
- **SQLite**: 3.x (開発環境)
- **Lombok**: 1.18.x

## プロジェクト構成

```
src/
├── main/
│   ├── java/com/example/its/
│   │   ├── config/             # 設定クラス（SecurityConfig 等）
│   │   ├── domain/             # ドメイン層
│   │   │   ├── auth/           # 認証・ユーザー管理
│   │   │   ├── cemetery/       # 霊園ページ
│   │   │   ├── memory/         # 思い出投稿
│   │   │   └── follow/         # フォロー機能
│   │   ├── infrastructure/     # SampleDataLoader 等
│   │   ├── web/                # コントローラー層
│   │   │   ├── auth/           # 会員登録
│   │   │   ├── cemetery/       # 霊園 CRUD
│   │   │   ├── memory/         # 思い出 CRUD
│   │   │   ├── follow/         # フォロー
│   │   │   └── user/           # マイページ
│   │   └── ItsApplication.java
│   └── resources/
│       ├── templates/          # Thymeleafテンプレート
│       ├── schema.sql          # DBスキーマ
│       └── data.sql            # 初期データ
└── test/                       # テストコード
```

## よく使うコマンド

### 開発
```bash
# アプリケーション起動
./gradlew bootRun

# テスト実行
./gradlew test

# ビルド（テスト含む）
./gradlew clean build


# 依存関係確認
./gradlew dependencies
```

### テスト方針

- **現在**: 単体テスト / MockMvc による Controller テスト中心
- **今後**: 重要な画面導線には E2E テストの追加を予定
- テストファイルは `src/test/java/com/example/its/web/` 配下に配置

### カバレッジレポート（JaCoCo）

```bash
# テスト実行 + HTML レポート生成（test 完了後に自動実行）
./gradlew test jacocoTestReport
```

レポートは以下で確認できます:

```
build/reports/jacoco/test/html/index.html
```

### トラブルシューティング
```bash
# ポート8080が使用中の場合
# Windows
jps                                    # Javaプロセス一覧
powershell "Stop-Process -Id <PID> -Force"

# macOS/Linux
lsof -i :8080                          # ポート使用プロセス確認
kill -9 <PID>                          # プロセス強制終了

# Gradleキャッシュクリア
./gradlew clean --refresh-dependencies
```

## 環境別設定

### 個人環境固有の設定

チームメンバー間で設定が異なる場合は、以下のファイルを作成してください（Gitに含まれません）:

**`src/main/resources/application-local.properties`**
```properties
# 個人用ポート設定例
server.port=8081

# 個人用データベース設定例
spring.datasource.url=jdbc:sqlite:./data/mydb.db

# ログレベル調整
logging.level.com.example.its=DEBUG
```

**使用方法**:
```bash
# application-local.propertiesを使用して起動
./gradlew bootRun --args='--spring.profiles.active=local'
```

## ブランチ戦略

- `main`: 本番リリース用（保護ブランチ）
- `develop`: 開発ブランチ
- `feature/xxx`: 機能開発ブランチ
- `fix/xxx`: バグ修正ブランチ

### 開発フロー

**基本方針**: 小さなタスク単位で進め、`docs/ROADMAP.md` と `docs/ACTION_ITEMS_v0.1.md` を更新しながら作業する。

```bash
# 1. developブランチから新しいブランチを作成
git checkout develop
git pull origin develop
git checkout -b feature/your-feature-name

# 2. 開発・コミット
git add .
git commit -m "feat: 新機能の説明"

# 3. リモートにプッシュ
git push origin feature/your-feature-name

# 4. GitHub上でPull Requestを作成
```

## コミットメッセージ規約

```
<type>: <subject>

<body>

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>
```

**Type一覧**:
- `feat`: 新機能
- `fix`: バグ修正
- `docs`: ドキュメント変更
- `style`: コードフォーマット
- `refactor`: リファクタリング
- `test`: テスト追加・修正
- `chore`: ビルド・設定変更

## IDE別セットアップガイド

### IntelliJ IDEA
1. `File` → `Open` → プロジェクトフォルダを選択
2. Gradleプロジェクトとして自動認識
3. SDKを Java 21 に設定
4. Lombok Pluginをインストール（推奨）

### Eclipse
1. `File` → `Import` → `Gradle` → `Existing Gradle Project`
2. プロジェクトフォルダを選択
3. JRE設定で Java 21 を指定
4. Lombok jarを実行してEclipseにインストール

### VS Code
1. 拡張機能をインストール:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support
2. フォルダを開く
3. Java Homeを Java 21 に設定

## よくある質問

**Q: Gradleのバージョンが古いと言われる**
A: Gradle Wrapperを使用しているため、`./gradlew` で実行すれば自動的に正しいバージョンが使用されます。

**Q: ポート8080が既に使用されている**
A: 上記「トラブルシューティング」を参照して、既存プロセスを停止してください。

**Q: Lombokが動かない**
A: IDEにLombokプラグインをインストールし、Annotation Processingを有効化してください。

**Q: 起動のたびにデータがリセットされる**
A: `spring.sql.init.mode=always` の設定により、起動時に `schema.sql` / `data.sql` が毎回流れます。データを保持したい場合は `application-local.properties` で `spring.sql.init.mode=never` に変更してください。

## 開発ルール

### セキュリティ
- パスワードや機密情報はコードに直接書かない
- 環境変数または設定ファイルで管理
- `secrets.properties` などはGitignoreに追加済み

### コーディング規約
詳細は [CLAUDE.md](CLAUDE.md) を参照してください。

### Pull Request作成時のチェックリスト
- [ ] コードがビルド可能
- [ ] テストが全て通る
- [ ] コーディング規約に準拠
- [ ] コミットメッセージが規約に準拠
- [ ] 機密情報が含まれていない

## サポート

質問や問題がある場合:
1. まず [CLAUDE.md](CLAUDE.md) のトラブルシューティングを確認
2. GitHubのIssuesで質問を作成
3. チームのSlack/Discordで相談

## ライセンス

プロジェクト固有のライセンス情報をここに記載
