# ソースコードレビュー結果（2026-02-23）

## 対象
- `src/main/java` 全体（Controller / Service / Repository / Security）
- `src/main/resources/templates` 主要画面
- `src/test/java`

## 実施コマンド
- `./gradlew test`（成功）

## 指摘事項（重大度順）

### 1. [High] 権限エラー・未検出IDが 500 エラーになる
- 根拠:
  - `src/main/java/com/example/its/domain/cemetery/CemeteryService.java:47`
  - `src/main/java/com/example/its/domain/cemetery/CemeteryService.java:61`
  - `src/main/java/com/example/its/domain/memory/MemoryService.java:57`
  - `src/main/java/com/example/its/domain/memory/MemoryService.java:71`
  - `src/main/java/com/example/its/web/cemetery/CemeteryController.java:92`
  - `src/main/java/com/example/its/web/cemetery/CemeteryController.java:101`
  - `src/main/java/com/example/its/web/memory/MemoryController.java:107`
  - `src/main/java/com/example/its/web/memory/MemoryController.java:117`
- 内容:
  - Service が `IllegalStateException` / `IllegalArgumentException` を投げる実装ですが、Controller 側でハンドリングされていません。
  - `@ControllerAdvice` も存在しないため、権限不正やID不正で 500 応答になり、期待される 403 / 404 になりません。
- 影響:
  - 利用者には「サーバーエラー」に見える。
  - 不正操作時の挙動が不明瞭で、運用時の調査コストが上がる。
- 改善案:
  - `@ControllerAdvice` + `@ExceptionHandler` を追加し、`IllegalStateException` は 403、`IllegalArgumentException` は 404 へマッピング。

### 2. [High] `cemeteryId` と `memoryId` の関連整合性を検証していない
- 根拠:
  - `src/main/java/com/example/its/web/memory/MemoryController.java:67`
  - `src/main/java/com/example/its/web/memory/MemoryController.java:69`
  - `src/main/java/com/example/its/web/memory/MemoryController.java:78`
  - `src/main/java/com/example/its/web/memory/MemoryController.java:103`
  - `src/main/java/com/example/its/web/memory/MemoryController.java:122`
- 内容:
  - URL は `/cemeteries/{cemeteryId}/memories/{memoryId}` 形式ですが、`memoryId` が本当に `cemeteryId` 配下かを確認していません。
  - `memoryService.findById(memoryId)` と `cemeteryService.findById(cemeteryId)` を独立取得しており、パス不整合を許容します。
- 影響:
  - 想定外のデータ組み合わせ表示や、誤ったコンテキストでの操作誘導が起こり得ます。
- 改善案:
  - 取得後に `memory.getCemeteryId().equals(cemeteryId)` を必須チェックし、不一致は 404 とする。
  - もしくは Repository で `findByIdAndCemeteryId(memoryId, cemeteryId)` を用意して一括検証。

### 3. [Medium] `Referer` ヘッダーをそのままリダイレクト先に使用している
- 根拠:
  - `src/main/java/com/example/its/web/follow/FollowController.java:29`
  - `src/main/java/com/example/its/web/follow/FollowController.java:38`
  - `src/main/java/com/example/its/web/follow/FollowController.java:43`
- 内容:
  - `redirect:` に `Referer` の値を直接連結しています。
- 影響:
  - 外部URLを注入されると、オープンリダイレクトとして悪用される可能性があります。
- 改善案:
  - 相対パスのみ許可するホワイトリスト方式に変更（例: `/cemeteries`、`/mypage` など）。
  - バリデーション失敗時は固定遷移先へフォールバック。

### 4. [Medium] テストが実質未整備（回帰検知が困難）
- 根拠:
  - `src/test/java/com/example/its/ItsApplicationTests.java:7`
  - `src/test/java/com/example/its/ItsApplicationTests.java:10`
- 内容:
  - テストは `contextLoads()` のみで、Controller/Service の正常系・異常系・権限制御を検証していません。
- 影響:
  - 既存機能（フォロー、マイページ、編集・削除）の退行を自動検知できません。
- 改善案:
  - Controller: `@SpringBootTest + MockMvc` で認可・バリデーション・例外マッピングを追加。
  - Service: Mockito ベースで権限チェックと分岐ロジックを単体テスト化。

## 補足
- `./gradlew test` は成功しましたが、現状のテスト対象が最小限のため品質担保としては不十分です。
