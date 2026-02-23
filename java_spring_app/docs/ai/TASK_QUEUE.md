# AIタスクキュー

## ルール
- 1行 = 1タスク（小さな粒度）。
- `status` は `todo / doing / review / done` を使う。
- 担当は基本 `Claude`（実装）と `Codex`（レビュー）で分ける。
- タスク完了後は `HANDOFF.md` の該当 Task ID を必ず更新する。

## Queue
| Task ID | status | 担当 | 概要 | 依頼日 | レビュー日 |
|---|---|---|---|---|---|
| T-20260223-001 | done | Claude -> Codex | ROADMAP作成 + ACTION_ITEMS整合 | 2026-02-23 | 2026-02-23 |
| T-20260223-002 | done | Claude -> Codex | README同期（実装済み機能/テスト方針/開発フロー） | 2026-02-23 | 2026-02-23 |
| T-20260223-003 | done | Claude -> Codex | CHANGELOG.md 新規作成 | 2026-02-23 | 2026-02-23 |
| T-20260223-004 | done | Claude -> Codex | Follow/Cemetery の回帰テスト追加 + docs更新 | 2026-02-23 | 2026-02-23 |
| T-20260223-005 | done | Claude -> Codex | Service層単体テスト追加（Follow/Cemetery） | 2026-02-23 | 2026-02-23 |
| T-20260223-006 | done | Claude -> Codex | JaCoCo導入（カバレッジ可視化） | 2026-02-23 | 2026-02-23 |
| T-20260223-007 | done | Claude -> Codex | バリデーションエラー表示の統一（フォーム横断） | 2026-02-23 | 2026-02-23 |
| T-20260223-008 | done | Claude -> Codex | Playwright最小導入（E2Eスモーク1本） | 2026-02-23 | 2026-02-23 |
