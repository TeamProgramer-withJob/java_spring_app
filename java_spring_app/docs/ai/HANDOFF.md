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
- 判定: `OK` / `差し戻し`
- 指摘事項:
- 次アクション:

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
