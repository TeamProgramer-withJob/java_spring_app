# ROADMAP — 結びの霊園

> **作成日**: 2026-02-23
> **前提**: 会社プログラミング部の勉強会プロジェクト（6名程度）。業務品質レベルは不要。
> **前回完了**: 例外ハンドリング・パス整合性検証・Refererオープンリダイレクト修正・回帰テスト追加

---

## Now（直近 1〜2 週間で着手）

これらは既存コードへの影響が小さく、1〜2日で完了できる独立タスク。

- [x] **README 同期**：実装済み機能・開発フロー・起動手順を現在の状態に合わせる (2026-02-23)
- [x] **テスト追加 — unfollow の Referer フォールバック**：不正・欠落 Referer 時に `/cemeteries` へフォールバックすることを確認（`FollowControllerTest` に追記）(2026-02-23)
- [x] **テスト追加 — CemeteryController の 404/403 マッピング**：`GlobalExceptionHandler` が CemeteryController からの例外を正しく HTTP ステータスに変換することを確認 (2026-02-23)
- [x] **CHANGELOG.md 新規作成**：これまでの主要変更を 1 行ずつ時系列で記録する (2026-02-23)

---

## Next（1〜2 か月以内に対応）

Now が完了してから取り組む、やや規模が大きいタスク。

- [x] **Service 層の単体テスト整備**：`MemoryService` / `CemeteryService` / `FollowService` を Mockito ベースで検証する (2026-02-23, Follow/Cemetery 完了)
- [x] **バリデーションエラーの統一メッセージ**：入力エラー時のフラッシュメッセージやバインディングエラー表示を画面横断で統一する (2026-02-23, 主要フォームの表示統一完了)
- [x] **テストカバレッジの可視化**：JaCoCo を Gradle に追加してレポートを出力できるようにする (2026-02-23)
- [ ] **エラーページのデザイン改善**：`error/403.html` / `error/404.html` / `error/500.html` のレイアウト・メッセージを見直す

---

## Later（本番化・規模拡大時に対応）

現時点の勉強会スコープでは不要。将来に持ち越す。

- [ ] **プライバシー・モデレーション対応**：著作権/肖像権の同意フロー、通報・凍結機能の方針策定
- [ ] **遺族からの削除要請フロー**：コンテンツ削除対応の運用フロー設計
- [ ] **画像スキャン制限**：アップロード時のサイズ・拡張子・ウイルスチェック
- [ ] **パフォーマンス最適化**：N+1 問題の確認と JOIN クエリへの改善
- [ ] **本番 DB への移行**：SQLite → PostgreSQL / MySQL への切り替え設計

---

## 実装済み（完了履歴）

| 完了日 | 内容 |
|--------|------|
| 2026-02-22 | 公開範囲・画像機能・フォロー承認制などの仕様決定、REQUIREMENTS_v0.1.md 更新 |
| 2026-02-22 | 技術スタック確定（Spring Boot 3.2.1 / Java 21 / SQLite / MyBatis / Thymeleaf） |
| 2026-02-23 | フォロー機能・マイページ・思い出/霊園の編集・削除 実装 |
| 2026-02-23 | `GlobalExceptionHandler` 実装（IllegalArgumentException→404, IllegalStateException→403） |
| 2026-02-23 | `MemoryController` パス整合性検証（cemeteryId 不一致 → 404） |
| 2026-02-23 | `FollowController` オープンリダイレクト修正（Referer ホワイトリスト方式） |
| 2026-02-23 | 回帰テスト追加（`MemoryControllerTest` / `FollowControllerTest`） |
| 2026-02-23 | null 型安全性警告の修正（`SampleDataLoader` / `MemoryController` / テストコード） |
| 2026-02-23 | Service 層単体テスト追加（`FollowServiceTest` / `CemeteryServiceTest`） |
| 2026-02-23 | JaCoCo 導入（`build.gradle` 設定 + README 手順追記） |
| 2026-02-23 | 主要フォームのバリデーションエラー表示統一（cemeteries/memories） |
