# AIハンドオフ

## 使い方
- Claude Code 実行前に、Codex が「依頼内容」ブロックを追記する。
- Claude Code 実行後に、Claude が「実行結果」ブロックを追記する。
- Codex がレビューし、「レビュー結果」ブロックを追記してクローズする。

---

## Task ID: T-YYYYMMDD-001
### 依頼内容（Codex -> Claude）
- 目的:
- 変更対象:
- 非対象:
- 完了条件:

### 実行結果（Claude -> Codex）
- 変更ファイル:
- 実施内容:
- テスト結果:
- 残課題/懸念:

### レビュー結果（Codex -> チーム）
- 判定: `OK` / `差し戻し`
- 指摘事項:
- 次アクション:

---

## Task ID: T-20260223-003
### 依頼内容（Codex -> Claude）
- 目的: 変更履歴を時系列で追えるように `docs/CHANGELOG.md` を新規作成し、今後継続更新できる最小フォーマットを定義する
- 変更対象: docs/CHANGELOG.md（新規）、docs/ai/HANDOFF.md（実行結果追記）、docs/ai/TASK_QUEUE.md（status更新）
- 非対象: Javaソース修正、README.md の再修正、ROADMAP/ACTION_ITEMS の内容変更
- 完了条件: CHANGELOG.md が今後追記しやすい形式で作成されていること

### 実行結果（Claude -> Codex）
- 変更ファイル: docs/CHANGELOG.md（新規）、docs/ai/HANDOFF.md（本エントリ追記）、docs/ai/TASK_QUEUE.md（status: todo → review）
- 実施内容: 運用ルール（1行1変更・フォーマット定義）を先頭に記載し、コミット履歴から全18エントリを時系列で記録。未コミット分（ROADMAP/README/CHANGELOG）は Task ID を refs に使用。
- テスト結果: ドキュメントのみの変更のため、ビルド・テスト実行は不要
- 残課題/懸念: 特になし

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: ROADMAP/ACTION_ITEMS の Next タスクへ進行

---

## Task ID: （ここに追記）
### 依頼内容（Codex -> Claude）
- 目的:
- 変更対象:
- 非対象:
- 完了条件:

### 実行結果（Claude -> Codex）
- 変更ファイル:
- 実施内容:
- テスト結果:
- 残課題/懸念:

### レビュー結果（Codex -> チーム）
- 判定: `OK` / `差し戻し`
- 指摘事項:
- 次アクション:

---

## Task ID: T-20260223-004
### 依頼内容（Codex -> Claude）
- 目的: Controller 層の回帰防止強化（unfollow の Referer フォールバック、CemeteryController の 404/403 マッピング確認）
- 変更対象: FollowControllerTest.java、CemeteryControllerTest.java（新規）、docs/ACTION_ITEMS_v0.1.md、docs/ROADMAP.md
- 非対象: 本番コード（src/main/java）の仕様変更、README更新、追加機能実装
- 完了条件: 指定4ファイルの更新、`./gradlew test` 成功、報告フォーマット提出

### 実行結果（Claude -> Codex）
- 変更ファイル:
  - src/test/java/com/example/its/web/follow/FollowControllerTest.java
  - src/test/java/com/example/its/web/cemetery/CemeteryControllerTest.java（新規）
  - docs/ACTION_ITEMS_v0.1.md
  - docs/ROADMAP.md
- 実施内容:
  - unfollow の Referer 不正/欠落時フォールバックテストを追加
  - CemeteryController 経由で IllegalArgumentException→404、IllegalStateException→403 を検証するテストを追加
  - docs の Now 項目を完了へ更新
- テスト結果: BUILD SUCCESSFUL（`./gradlew test`）
- 残課題/懸念: 特になし

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: コミット・プッシュ実施後、Next セクション（Service 層テスト整備 / バリデーション統一 / JaCoCo 導入）へ移行

---

## Task ID: T-20260223-005
### 依頼内容（Codex -> Claude）
- 目的: Next フェーズ着手として Service 層の単体テストを追加し、業務ロジックの回帰防止を強化する
- 変更対象: FollowServiceTest.java（新規）、CemeteryServiceTest.java（新規）
- 非対象: 本番コード変更、Controllerテスト変更、docs更新
- 完了条件: 指定2ファイルのみ変更、`./gradlew test` 成功、報告フォーマット提出

### 実行結果（Claude -> Codex）
- 変更ファイル:
  - src/test/java/com/example/its/domain/follow/FollowServiceTest.java（新規）
  - src/test/java/com/example/its/domain/cemetery/CemeteryServiceTest.java（新規）
- 実施内容:
  - FollowService の自己フォロー禁止、重複防止、unfollow 削除呼び出しを検証する4テストを追加
  - CemeteryService の未存在ID例外、非オーナー例外、オーナー削除時の連鎖削除を検証する3テストを追加
- テスト結果: BUILD SUCCESSFUL（`./gradlew test`）
- 残課題/懸念: なし

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: Next 残タスク（バリデーション統一 / JaCoCo 導入 / エラーページ改善）へ進行

---

## Task ID: T-20260223-006
### 依頼内容（Codex -> Claude）
- 目的: テストカバレッジ可視化のため JaCoCo を Gradle に導入し、今後の改善効果を定量評価できる基盤を作る
- 変更対象: build.gradle、README.md（JaCoCo 実行手順追記のみ）
- 非対象: 本番コード変更、テストケース追加、ROADMAP/ACTION_ITEMS/docs/ai 更新
- 完了条件: 指定2ファイルのみ変更、`./gradlew test jacocoTestReport` 成功、HTMLレポート生成確認

### 実行結果（Claude -> Codex）
- 変更ファイル:
  - build.gradle
  - README.md
- 実施内容:
  - `jacoco` プラグイン追加、`toolVersion = '0.8.11'` を設定
  - `test.finalizedBy jacocoTestReport` を設定
  - `jacocoTestReport` で HTML 出力有効、XML/CSV 無効を設定
  - README に JaCoCo 実行コマンドとレポート確認先を追記
- テスト結果: BUILD SUCCESSFUL（`./gradlew test jacocoTestReport`）
- 残課題/懸念: なし

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: Next 残タスク（バリデーション統一 / エラーページ改善）へ進行

---

## Task ID: T-20260223-007
### 依頼内容（Codex -> Claude）
- 目的: 主要フォームのバリデーションエラー表示を統一し、入力エラー時のUXを共通化する
- 変更対象: signup/cemeteries/memories のテンプレートおよび関連コントローラー
- 非対象: ドメインロジック変更、バリデーションルール変更、ROADMAP/ACTION_ITEMS/docs/ai 更新
- 完了条件: 指定対象で表示仕様統一、`./gradlew test` 成功

### 実行結果（Claude -> Codex）
- 変更ファイル:
  - src/main/resources/templates/cemeteries/new.html
  - src/main/resources/templates/cemeteries/edit.html
  - src/main/resources/templates/memories/new.html
  - src/main/resources/templates/memories/edit.html
- 実施内容:
  - 各フォームで `<h2>` 直下にグローバルエラー表示ブロック（`errorMessage`）を追加
  - フィールドエラー表示と BindingResult 処理は既存で統一済みのため変更なしを確認
- テスト結果: BUILD SUCCESSFUL（`./gradlew test`）
- 残課題/懸念: なし

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: Next 残タスク（エラーページ改善）へ進行

---

## Task ID: T-20260223-008
### 依頼内容（Codex -> Claude）
- 目的: E2E テスト基盤として Playwright を最小構成で導入し、スモークテスト1本を通す
- 変更対象: package.json、playwright.config.ts、tests/e2e/smoke-login.spec.ts、README.md
- 非対象: 本番コード変更、既存単体/統合テスト書き換え、ROADMAP/ACTION_ITEMS/docs/ai 更新
- 完了条件: 指定ファイル更新、`npx playwright test` 実行可能、README 手順追記

### 実行結果（Claude -> Codex）
- 変更ファイル:
  - package.json（新規）
  - package-lock.json（新規）
  - playwright.config.ts（新規）
  - tests/e2e/smoke-login.spec.ts（新規）
  - README.md
  - .gitignore（Node.js/Playwright 除外追加）
- 実施内容:
  - Playwright 導入（@playwright/test）
  - `baseURL` と `webServer` を含む設定追加
  - `/login` 主要要素表示を検証するスモークテスト追加
  - README に E2E 実行手順を追記
- テスト結果:
  - `npx playwright test` → 1 passed
- 残課題/懸念:
  - ログイン状態に関わらずナビバーに「ログアウト」ボタンが表示されるため、E2Eセレクターは role/name を優先すること

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: Next 残タスク（エラーページ改善）または E2E シナリオ拡張へ進行

---

## Task ID: T-20260223-009
### 依頼内容（Codex -> Claude）
- 目的: エラーページ（403/404/500）の見た目・文言・導線を統一し、次の行動を取りやすいUIに改善する
- 変更対象: error/403.html、error/404.html、error/500.html
- 非対象: Javaコード変更、CSSフレームワーク追加、ROADMAP/ACTION_ITEMS/docs/ai 更新
- 完了条件: 3ページの統一、ホーム/霊園一覧導線追加、`./gradlew test` 成功

### 実行結果（Claude -> Codex）
- 変更ファイル:
  - src/main/resources/templates/error/403.html
  - src/main/resources/templates/error/404.html
  - src/main/resources/templates/error/500.html
- 実施内容:
  - ステータスコード表示・見出しサイズ・ボタン配置を3ページで統一
  - `th:text="${errorMessage}"` を維持しつつ、各ステータスに応じたフォールバック文言へ改善
  - 「ホームへ戻る」「霊園一覧へ」の2導線を全ページに追加
- テスト結果: BUILD SUCCESSFUL（`./gradlew test`）
- 残課題/懸念: なし

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: E2E シナリオ拡張（Playwright）を次タスク候補として検討

---

## Task ID: T-20260223-010
### 依頼内容（Codex -> Claude）
- 目的: Playwright の E2E 基盤を拡張し、ログイン成功と霊園作成の回帰を自動検知できる状態にする
- 変更対象: tests/e2e/login-success.spec.ts（新規）、tests/e2e/cemetery-create.spec.ts（新規）
- 非対象: 本番コード変更、既存単体/統合テスト修正、ROADMAP/ACTION_ITEMS/docs/ai 更新
- 完了条件: 追加2シナリオ実装、`npx playwright test` 全通過

### 実行結果（Claude -> Codex）
- 変更ファイル:
  - tests/e2e/login-success.spec.ts（新規）
  - tests/e2e/cemetery-create.spec.ts（新規）
- 実施内容:
  - 未認証アクセスからログイン成功後 `/cemeteries` に遷移するシナリオを追加
  - 霊園作成フォーム遷移と作成完了（詳細画面表示）シナリオを追加
  - `/cemeteries?continue` を考慮した URL アサーションへ修正
- テスト結果:
  - `npx playwright test` → 4 passed
- 残課題/懸念:
  - 霊園作成E2Eは実DBにデータを追加するため、継続実行でテストデータが蓄積する
  - Windows 環境で `webServer.command` の運用に注意が必要

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: E2E実行安定化（テストデータ後片付け / webServer運用）を次タスク候補として検討

---

## Task ID: T-20260223-011
### 依頼内容（Codex -> Claude）
- 目的: Playwright E2E の実行安定性向上（DB後片付け・Windows向けwebServer運用改善）
- 変更対象: playwright.config.ts、tests/e2e/cemetery-create.spec.ts、README.md
- 非対象: 本番コード変更、既存単体/統合テスト修正、ROADMAP/ACTION_ITEMS/docs/ai 更新
- 完了条件: `npx playwright test` を2回連続成功、対象3ファイルのみ変更

### 実行結果（Claude -> Codex）
- 変更ファイル:
  - playwright.config.ts
  - tests/e2e/cemetery-create.spec.ts
  - README.md
- 実施内容:
  - webServer.command を OS 判定で分岐（Windows: gradlew.bat / macOS・Linux: ./gradlew）
  - 霊園作成E2Eの末尾に削除後片付けを追加（確認ダイアログ自動承認 + 一覧遷移確認）
  - README に Windows 実行時の推奨手順を追記
- テスト結果:
  - `npx playwright test` 2回連続成功（各回 4 passed）
- 残課題/懸念:
  - CIでの webServer 自動起動（process.env.CI=true 時）の追加検証余地あり

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: E2E シナリオ拡張（思い出投稿/フォロー導線）または CI 実行基盤整備へ進行

---

## Task ID: T-20260224-012
### 依頼内容（Codex）
- 目的: push/PR ごとにリモートで自動テストを実行する CI 基盤を追加し、手動テスト依存を減らす
- 変更対象: `.github/workflows/ci.yml`、README.md、docs/ROADMAP.md、docs/ACTION_ITEMS_v0.1.md、docs/ai/TASK_QUEUE.md、docs/CHANGELOG.md、docs/ai/HANDOFF.md
- 非対象: 本番コード（`src/main/java`）変更、E2Eシナリオの追加実装
- 完了条件: CIワークフロー追加、`./gradlew test` と `npx playwright test` の成功確認、docs整合更新

### 実行結果（Codex）
- 変更ファイル:
  - .github/workflows/ci.yml（新規）
  - README.md
  - docs/ROADMAP.md
  - docs/ACTION_ITEMS_v0.1.md
  - docs/ai/TASK_QUEUE.md
  - docs/CHANGELOG.md
  - docs/ai/HANDOFF.md
- 実施内容:
  - GitHub Actions で push/PR トリガーの CI を追加（Java 21 + Node 20）
  - CIで `./gradlew test` と `npx playwright test` を実行し、Playwright成果物をArtifact保存
  - README に CI 運用説明を追加
  - ROADMAP/ACTION_ITEMS/TASK_QUEUE を最新状態へ更新
- テスト結果:
  - `./gradlew test` 成功
  - `npx playwright test` 成功
- 残課題/懸念:
  - CI上での初回実行時間は Playwright ブラウザインストール分だけ長くなる

### レビュー結果（Codex -> チーム）
- 判定: `OK`
- 指摘事項: なし
- 次アクション: E2Eシナリオの次拡張（思い出投稿/フォロー導線）と CI 必須化（Branch protection）を検討
