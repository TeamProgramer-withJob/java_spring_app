# CHANGELOG — 結びの霊園

## 運用ルール

- 1変更 = 1行で記録する
- 形式: `YYYY-MM-DD | category | summary | refs`
- category: `feat` / `fix` / `docs` / `test` / `security` / `chore` / `merge`
- refs: コミットハッシュ（先頭7文字）または Task ID
- 新しいエントリは**先頭**（この行のすぐ下）に追加する

---

## 履歴

| 日付 | カテゴリ | 概要 | refs |
|------|----------|------|------|
| 2026-02-24 | ci | GitHub Actions CI追加（push/PRで Gradleテスト + Playwright E2E 自動実行）と docs 整合更新 | T-20260224-012 |
| 2026-02-23 | docs | タスク009クローズ、エラーページ改善完了の docs 整合更新 | T-20260223-009 |
| 2026-02-23 | test | Playwright E2E基盤導入とタスク008レビュー反映 | 64a5d4f |
| 2026-02-23 | docs | CHANGELOG を最新コミット履歴に同期 | 4583c76 |
| 2026-02-23 | docs | タスク007クローズ、ROADMAP/ACTION_ITEMS/HANDOFF/TASK_QUEUE の整合更新 | bd6526b |
| 2026-02-23 | chore | JaCoCo 導入とタスク006レビュー反映（build.gradle/README 更新） | fbdfcd8 |
| 2026-02-23 | test | Service層単体テスト追加（FollowServiceTest / CemeteryServiceTest）とタスク005レビュー反映 | a51b7c0 |
| 2026-02-23 | test | Follow/Cemetery 回帰テスト追加とタスク004レビュー反映 | 14bfb3d |
| 2026-02-23 | docs | Codex主導のAI役割分担ルールを明確化（AGENTS/CLAUDE） | 4a34cd1 |
| 2026-02-23 | fix | favicon未配置時の例外ノイズ抑制とアプリアイコン追加 | c4c585a |
| 2026-02-23 | docs | ROADMAP/CHANGELOG/README/AI運用ファイルの整備 | b10d61c |
| 2026-02-23 | merge | claude/crazy-mayer の作業内容を feature/karasawy にマージ | 08e5058 |
| 2026-02-23 | docs | CHANGELOG.md 新規作成（本ファイル） | T-20260223-003 |
| 2026-02-23 | docs | README 同期（実装済み機能・テスト方針・開発フロー） | T-20260223-002 |
| 2026-02-23 | docs | ROADMAP.md 新規作成（Now/Next/Later） | T-20260223-001 |
| 2026-02-23 | test | 回帰テスト追加（MemoryControllerTest / FollowControllerTest） | 3f5ae2a |
| 2026-02-23 | security | Codexレビュー指摘3件修正（パス整合・Referer対策・null安全） | 5d88493 |
| 2026-02-23 | feat | 共通エラーハンドリング実装（GlobalExceptionHandler + 403/404/500ページ） | b9799e6 |
| 2026-02-23 | docs | ドキュメント最新化（ACTION_ITEMS / README / AGENTS / レビュー記録） | 1251378 |
| 2026-02-23 | feat | 思い出・霊園の編集機能を追加 | 431e44a |
| 2026-02-23 | feat | 霊園の削除機能を追加 | 61666d4 |
| 2026-02-23 | feat | 思い出の削除機能を追加 | 3a382b9 |
| 2026-02-22 | feat | フォロー機能・マイページ機能を追加 | e5fc25b |
| 2026-02-22 | feat | サンプル画像データ投入機能（SampleDataLoader）を追加 | 37d6b08 |
| 2026-02-22 | feat | 結びの霊園 基本機能（霊園・思い出・会員登録）を実装 | 00ba059 |
| 2026-02-22 | docs | ドキュメントを2026-02-22時点の実装状況に最新化 | bf6ce3d, 865f71e |
| 2026-02-22 | docs | REQUIREMENTS_v0.1 にソースコードファイル一覧を追加 | 5677a32 |
| 2026-02-14 | docs | ACTION_ITEMS_v0.1.md 追加・更新（Claude Code おすすめ方針含む） | ea32f88, eccd642 |
| 2026-02-14 | docs | 要件定義・アーキテクチャ課題整理・POC/MVP方針メモを追加 | fd06bd6, 12d0164 |
| 2025-11-08 | chore | .claude フォルダを Git 管理から除外 | f75c142 |
