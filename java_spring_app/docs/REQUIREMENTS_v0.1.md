# 結びの霊園 要件定義（叩き台）v0.1

> **最終更新**: 2026-02-22

## 1. 目的・概要

### 1.1 システムの目的

「結びの霊園」は、故人や家族の思い出（文章・画像など）をオンラインで保存し、関係者が閲覧・交流できる"オンライン霊園"を作成・運用するWebアプリケーションである。

### 1.2 解決したい課題

- 遠方等の理由で、現地のお墓参りが難しい
- お墓だけでは、思い出や伝えたいことを家族に残しづらい
- 管理費・維持費の負担
- 少子高齢化などで継承が難しい
- 「生きていた証」を残す

### 1.3 想定ユーザー

- **一般会員**：霊園ページ（故人・家系単位など）を作成し、思い出を投稿・公開管理する
- **閲覧者（会員のみ）**：ログイン済み会員として閲覧・フォロー等を行う
- **管理者**：問い合わせ対応、ユーザー・投稿の管理

**決定（2026-02-22）**: 全ページ閲覧にログインを必須とする。非会員閲覧は対応しない。

---

## 2. スコープ

### 2.1 MVP（最初のリリースで実装する範囲）

**実装済み ✅**
- 会員登録 / ログイン / ログアウト
- 霊園ページの作成・一覧・詳細閲覧
- 思い出投稿（テキスト＋画像）・詳細閲覧

**未実装（次フェーズ）**
- マイページ
- 思い出・霊園ページの編集・削除
- フォロー機能（片方向）
- 問い合わせフォーム
- バリデーション・例外処理の共通化

### 2.2 将来拡張（今回必須ではない）

- 購入プラン（無料/有料）による機能差分（投稿数上限、画像容量、公開範囲の種類など）
- コメント/いいね等の交流機能
- 招待制（特定メンバーのみ閲覧可能）・承認制フォロー
- 退会時のデータ取り扱い強化（エクスポート/引き継ぎ）

---

## 3. 用語定義（案）

| 用語 | 定義 |
|------|------|
| **霊園ページ** | 故人や家系などの単位で作られるページ（公開範囲を持つ場合あり） |
| **思い出** | 霊園ページに紐づく投稿（文章、画像など） |
| **公開範囲** | 閲覧可能な対象の制御（例：公開/フォロワーのみ/相互のみ/自分のみ） |
| **フォロー** | 他会員との関係（片方向）。相互は両者がフォローしている状態 |

---

## 4. 権限・公開範囲ルール（未確定を含む叩き台）

### 4.1 ロール

- 一般会員
- 管理者

### 4.2 公開範囲（案）

公開範囲は、最低限「思い出（投稿）」に設定できる。

#### 公開範囲候補

| 範囲 | 説明 |
|------|------|
| **PUBLIC** | 誰でも閲覧可（非会員含むかは未決定） |
| **FOLLOWER** | フォロワーのみ閲覧可 |
| **MUTUAL** | 相互フォローのみ閲覧可 |
| **PRIVATE** | 自分のみ |

**決定（2026-02-22）**: 全ページログイン必須のため、PUBLIC = ログイン済み全会員が閲覧可能。FOLLOWER / MUTUAL / PRIVATE はフォロー機能実装後に対応。

---

## 5. 機能要件

### 5.1 認証・会員

- 会員登録（メール＋パスワード等）
- ログイン/ログアウト
- パスワード再発行（MVPでやるか未決定）
- プロフィール編集（表示名、自己紹介、アイコン等：MVPでは最小で）

### 5.2 マイページ

- 自分が作成した霊園ページ一覧
- フォロー/フォロワー/相互フォロー一覧
- 自分の投稿一覧（下書き対応は将来）

### 5.3 霊園ページ

**作成**：名称、説明、代表画像（任意）

**閲覧**：公開範囲に応じて閲覧可能

**編集/削除**：作成者のみ（削除は論理削除が望ましい）

**決定（2026-02-22）**: オーナーは1人。共同管理者なし。

### 5.4 思い出投稿

**作成**：本文、画像（任意）、公開範囲、投稿日時

**閲覧**：公開範囲に応じて表示

**編集/削除**：投稿者のみ

**決定（2026-02-22）**: 画像アップロードをMVPに含める。DB BLOB方式で実装済み（JPG・PNG・GIF、最大10MB）。

### 5.5 会員検索

- 検索条件（例：表示名、キーワード）
- 検索結果は「公開プロフィールのみ」表示
- 詳細ページでフォロー可能

#### 未決定ポイント

- 検索の対象範囲（メール検索は避けるのが無難。表示名検索が基本）

### 5.6 フォロー

- フォローする/外す
- フォロー/フォロワー/相互の判定・一覧表示
- 公開範囲判定に利用（FOLLOWER/MUTUAL）

**決定（2026-02-22）**: 承認制なし。片方向フォローのみ。ブロックは将来対応。

### 5.7 問い合わせ

- 問い合わせフォーム（件名、本文、返信先メール）
- 管理者にメール送信
- DBに問い合わせ内容を保存（対応履歴として）

---

## 6. 画面一覧（叩き台）

※URLは例。実装方式（Thymeleaf or SPA）は未決定でも、まず画面と目的を固定する。

### 6.1 公開側（閲覧）

| 画面 | URL | 説明 |
|------|-----|------|
| TOP | `/` | サービス説明、ログイン導線、検索導線 |
| 霊園ページ閲覧 | `/cemeteries/{id}` | 霊園概要、思い出一覧 |
| 思い出詳細 | `/memories/{id}` | 本文・画像表示 |

### 6.2 会員向け

| 画面 | URL |
|------|-----|
| ログイン | `/login` |
| 会員登録 | `/signup` |
| マイページ | `/mypage` |
| 霊園作成 | `/cemeteries/new` |
| 霊園編集 | `/cemeteries/{id}/edit` |
| 思い出投稿 | `/cemeteries/{id}/memories/new` |
| 思い出編集 | `/memories/{id}/edit` |
| 会員検索 | `/users/search` |
| 会員プロフィール | `/users/{id}` |
| フォロー一覧 | `/mypage/follows` |
| フォロワー一覧 | `/mypage/followers` |
| 相互フォロー一覧 | `/mypage/mutual` |

### 6.3 管理者向け

| 画面 | URL |
|------|-----|
| 管理ダッシュボード | `/admin` |
| 問い合わせ一覧 | `/admin/inquiries` |
| ユーザー管理 | `/admin/users` |

※ユーザー管理はMVPで要るか未決定

---

## 7. 非機能要件（MVP向け最小）

### 7.1 セキュリティ

- パスワードはハッシュ化（Spring Security標準）
- 認可：作成者のみ編集可能
- 公開範囲：検索結果や直URLアクセスでも漏れないこと

### 7.2 運用

- 問い合わせメールの送信失敗時、ユーザーにエラー表示し、ログに記録

### 7.3 品質

- 入力バリデーション（必須、桁数、禁止文字など）
- 例外処理の共通化（画面エラーを統一）

---

## 8. 残未決定事項

- 霊園ページ自体にも公開範囲を持たせるか（フォロー実装後に再検討）
- コメント/いいね等の交流機能（将来拡張）

---

## 9. 技術前提（確定）

| 項目 | 決定内容 |
|------|----------|
| フレームワーク | Spring Boot 3.2.1 |
| 言語 | Java 21 |
| テンプレートエンジン | Thymeleaf（SSR） |
| DB | SQLite（`./data/cemetery.db`） |
| 認証 | Spring Security セッション方式 |
| 画像保存 | DB BLOB |
| デプロイ | ローカルのみ（`./gradlew bootRun`） |
| チーム規模 | 約6名 |

---

## 10. 現在の実装状況（2026-02-22 時点）

### 実装済み画面・機能

| 画面 | URL | 状態 |
|------|-----|------|
| ログイン | `/login` | ✅ |
| 会員登録 | `/signup` | ✅ |
| トップ | `/` | ✅ |
| 霊園一覧 | `/cemeteries` | ✅ |
| 霊園詳細 | `/cemeteries/{id}` | ✅ |
| 霊園作成 | `/cemeteries/new` | ✅ |
| 思い出投稿 | `/cemeteries/{id}/memories/new` | ✅ |
| 思い出詳細 | `/cemeteries/{id}/memories/{id}` | ✅ |
| 思い出画像配信 | `/cemeteries/{id}/memories/{id}/image` | ✅ |

### 未実装

- マイページ
- 霊園・思い出の編集/削除
- フォロー機能
- 問い合わせフォーム
- 共通エラーハンドリング
- テスト

---

## 11. ソースコードファイル一覧（機能別）

> パッケージルート: `src/main/java/com/example/its/`
> テンプレートルート: `src/main/resources/templates/`
> リソースルート: `src/main/resources/`

---

### 11.1 認証・会員登録

| 種別 | ファイルパス | 役割 |
|-----|------------|------|
| Controller | `web/IndexController.java` | トップページ表示、ログインフォーム表示 |
| Controller | `web/auth/SignupController.java` | 会員登録フォーム表示・登録処理 |
| Service | `domain/auth/UserService.java` | 会員登録ビジネスロジック（パスワードハッシュ化） |
| Repository | `domain/auth/UserRepository.java` | users テーブルへのアクセス（MyBatis） |
| Entity | `domain/auth/User.java` | ユーザーエンティティ（users テーブル対応） |
| Form | `domain/auth/SignupForm.java` | 会員登録フォームのバリデーション定義 |
| Security | `config/SecurityConfig.java` | Spring Security 設定（全ページ認証必須、ログインページ指定） |
| Security | `domain/auth/CustomUserDetailsService.java` | Spring Security 用ユーザー取得ロジック |
| Security | `domain/auth/CustomUserDetails.java` | SecurityContext に userId・displayName を拡張保持 |
| Template | `templates/index.html` | トップページ |
| Template | `templates/login.html` | ログインフォーム |
| Template | `templates/auth/signup.html` | 会員登録フォーム |

---

### 11.2 霊園ページ

| 種別 | ファイルパス | 役割 |
|-----|------------|------|
| Controller | `web/cemetery/CemeteryController.java` | 霊園の一覧・詳細・作成フォーム表示・作成処理 |
| Service | `domain/cemetery/CemeteryService.java` | 霊園のビジネスロジック（取得・作成） |
| Repository | `domain/cemetery/CemeteryRepository.java` | cemeteries テーブルへのアクセス（MyBatis） |
| Entity | `domain/cemetery/CemeteryEntity.java` | 霊園エンティティ（cemeteries テーブル対応） |
| Form | `domain/cemetery/CemeteryForm.java` | 霊園作成フォームのバリデーション定義（name 100字以内、description 1000字以内） |
| Template | `templates/cemeteries/list.html` | 霊園一覧画面 |
| Template | `templates/cemeteries/new.html` | 霊園作成フォーム |
| Template | `templates/cemeteries/detail.html` | 霊園詳細・思い出一覧画面 |

---

### 11.3 思い出投稿

| 種別 | ファイルパス | 役割 |
|-----|------------|------|
| Controller | `web/memory/MemoryController.java` | 思い出の詳細・投稿フォーム表示・投稿処理・画像配信 |
| Service | `domain/memory/MemoryService.java` | 思い出のビジネスロジック（取得・作成・画像取得） |
| Repository | `domain/memory/MemoryRepository.java` | memories テーブルへのアクセス（MyBatis、BLOB マッピング含む） |
| Entity | `domain/memory/MemoryEntity.java` | 思い出エンティティ（imageData: byte[]、imageContentType を含む） |
| Form | `domain/memory/MemoryForm.java` | 思い出投稿フォームのバリデーション定義（title 256字以内、body 必須、image 任意） |
| Template | `templates/memories/new.html` | 思い出投稿フォーム |
| Template | `templates/memories/detail.html` | 思い出詳細画面（画像表示含む） |

---

### 11.4 共通・インフラ

| 種別 | ファイルパス | 役割 |
|-----|------------|------|
| Template | `templates/fragments/layout.html` | 全画面共通レイアウト（ヘッダー・ナビ等） |
| DB スキーマ | `resources/schema.sql` | テーブル定義（users / cemeteries / memories / follows） |
| DB 初期データ | `resources/data.sql` | テスト用初期データ（ユーザー3名・霊園2件・思い出3件） |
| アプリ設定 | `resources/application.properties` | SQLite 接続設定・ファイルアップロード上限（10MB）・SQL 初期化設定 |
| メインクラス | `ItsApplication.java` | Spring Boot アプリケーション起動クラス |
| サンプルデータ | `infrastructure/SampleDataLoader.java` | 起動時にサンプル画像を memories テーブルへ投入（image_data が NULL の場合のみ） |
| サンプル画像 | `resources/sample-images/memory1.jpg` 〜 `memory3.jpg` | 思い出ID 1〜3 に対応するサンプル画像ファイル |
