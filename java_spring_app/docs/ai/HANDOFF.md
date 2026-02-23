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
