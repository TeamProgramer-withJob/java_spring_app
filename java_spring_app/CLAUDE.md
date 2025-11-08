# CLAUDE.md - AI開発支援ドキュメント

> **目的**: このプロジェクト専用のClaude Code AI開発支援設定ファイル
> **更新日**: 2025-11-08
> **プロジェクト**: ITS (Issue Tracking System) - 課題管理システム

## 🛠️ **技術スタック**

### **フレームワーク・ライブラリ**
- **Spring Boot**: 3.2.1 (メインフレームワーク)
- **MyBatis**: 3.0.3 (データベースマッピング)
- **Thymeleaf**: テンプレートエンジン
- **Spring Security**: 認証・認可
- **Lombok**: コード生成（getter/setter等）

### **言語・環境**
- **Java**: 21 (プロジェクト標準)
- **Gradle**: 8.5 (依存関係管理・ビルドツール)
- **H2 Database**: インメモリデータベース (開発環境)

### **開発ツール**
- **Spring Boot DevTools**: ホットリロード
- **Spring Boot Test**: 自動テストフレームワーク
- **Spring Security Test**: セキュリティテスト

### **重要な移行事項 (Spring Boot 3.x)**
- **Jakarta EE**: `javax.*` パッケージから `jakarta.*` への移行
- **Spring Security 6**: SecurityFilterChain ベースの設定推奨
- **Java 17以上必須**: Java 21を使用
- **Thymeleaf Extras**: springsecurity5 → springsecurity6

## 📋 **コーディング規約**

### **Javaコーディングスタイル**
```java
// ✅ 推奨: 明確なクラス構造
@Service
public class IssueService {
    private final IssueRepository issueRepository;

    // コンストラクタインジェクション推奨
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    /**
     * Javadocコメントを必須とする
     */
    public List<IssueEntity> findAll() {
        // 実装
    }
}
```

### **命名規約**
- **クラス**: PascalCase (`IssueService`, `IssueController`)
- **メソッド**: camelCase (`findAll`, `createIssue`)
- **定数**: UPPER_SNAKE_CASE (`DEFAULT_PAGE_SIZE`)
- **パッケージ**: `com.example.its.{layer}`

### **パッケージ構造**
```
src/main/java/com/example/its/
├── config/         # 設定クラス (SecurityConfig等)
├── web/            # コントローラー層
│   └── issue/      # Issue関連コントローラー
├── domain/         # ドメイン層
│   ├── auth/       # 認証・ユーザー管理
│   └── issue/      # 課題管理
└── ItsApplication.java  # アプリケーションエントリーポイント
```

## 🔐 **セキュリティルール**

### **必須セキュリティ要件**
1. **機密情報の取り扱い**
   - パスワード・APIキーは環境変数使用必須
   - ハードコード禁止
   ```properties
   # ✅ 正しい方法
   spring.datasource.password=${DB_PASSWORD}
   
   # ❌ 禁止
   spring.datasource.password=hardcoded_password_example
   ```

2. **入力値検証**
   - 全公開エンドポイントで入力値検証実装
   - SQLインジェクション対策（MyBatis使用）

3. **ログ出力**
   - 機密情報をログに出力禁止
   - デバッグレベルでのPII情報制限

### **データベースセキュリティ**
- H2データベースの本番環境使用禁止（開発環境のみ）
- 本番環境ではPostgreSQL/MySQL等の使用を推奨
- DB認証情報は環境変数または外部設定で管理

## 🧪 **テスト方針**

### **テスト戦略**
- **単体テスト**: 各Service/Controllerクラス
- **統合テスト**: データベース接続を含むテスト
- **カバレッジ目標**: 80%以上

### **テストファイル命名**
```
src/test/java/com/example/its/
├── domain/
│   ├── auth/UserRepositoryTest.java
│   └── issue/IssueServiceTest.java
└── web/
    └── issue/IssueControllerTest.java
```

### **モックとスタブ**
- 外部依存はMockito使用
- データベーステストは@SpringBootTest

## 🏗️ **アーキテクチャガイドライン**

### **レイヤー分離**
- **Controller**: HTTPリクエスト処理のみ
- **Service**: ビジネスロジック集約
- **Repository**: データアクセス専用

### **クラス設計**
- **単一責任原則**: 1クラス1責務
- **最大行数制限**: 200行以下を推奨
- **大きなクラスは分割**: 機能ごとにサービスクラスを分離

### **例外処理**
- カスタム例外クラス作成推奨
- 汎用Exception使用を避ける

## 📊 **パフォーマンス要件**

### **データベース最適化**
- MyBatisマッパーの効率的な使用
- N+1問題の回避（適切なJOIN使用）
- インデックスの適切な設計

### **レスポンス時間目標**
- Webページレンダリング: 200ms以下
- API応答時間: 95%tile で 100ms以下
- 課題一覧表示: 100件で500ms以内

## 🚀 **開発ワークフロー**

### **ブランチ戦略**
- 現在: `develop` (開発ブランチ)
- メイン: `main` (本番リリース)
- 機能開発: `feature/feature-name`
- バグ修正: `fix/issue-description`

### **コミットメッセージ**
```
feat: 新機能追加
fix: バグ修正
refactor: リファクタリング
docs: ドキュメント更新
security: セキュリティ修正
```

### **必須チェック項目**
- [ ] セキュリティ要件遵守
- [ ] テストケース作成
- [ ] Javadoc記述
- [ ] 命名規約準拠
- [ ] 例外処理実装

## 🔄 **現在の重要課題**

### **完了済み** ✅
- ログイン機能実装
- DB共通機能実装
- Gradle化対応

### **今後の開発予定**
- 課題管理機能の拡張
- ユーザー権限管理の強化
- レスポンシブデザイン対応

## 🔧 **頻繁に使用するGradleコマンド**

> **注意**: Windows環境での実行

### **開発・テスト**
- `./gradlew bootRun`: アプリケーション起動
- `./gradlew test`: 単体テスト実行
- `./gradlew build`: ビルドとテスト実行

### **ビルド・デプロイ**
- `./gradlew clean build`: クリーンビルド
- `./gradlew bootJar`: 実行可能JARファイル作成
- `./gradlew bootBuildImage`: Spring Boot Dockerイメージ作成

### **Windows実行の注意点**
- Windows PowerShellでは `.\gradlew` または `./gradlew` を使用
- Git Bashでは `./gradlew` を使用
- 初回実行時に自動的にGradleラッパーがダウンロードされる

## 💡 **開発時の重要な実装パターン**

### **1. Constructor Injection**
```java
// ✅ 推奨: コンストラクタインジェクション
@Service
public class IssueService {
    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }
}

// ❌ 非推奨: @Autowired フィールドインジェクション
@Autowired
private IssueRepository issueRepository;
```

### **2. Formオブジェクトパターン**
```java
// 入力値をFormクラスで受け取る
@PostMapping("/issues")
public String createIssue(@Validated IssueForm form, BindingResult result) {
    if (result.hasErrors()) {
        return "issues/new";
    }
    // フォームからエンティティへ変換
}
```

### **3. MyBatis Repository抽象化**
```java
// MyBatisマッパーインターフェース
@Mapper
public interface IssueRepository {
    List<IssueEntity> findAll();
    Optional<IssueEntity> findById(long id);
    void insert(IssueEntity issue);
    void update(IssueEntity issue);
    void deleteById(long id);
}
```

### **4. Transaction境界**
```java
// @Transactionalの適切な配置
@Service
@Transactional(readOnly = true)
public class IssueService {

    @Transactional // 書き込み操作のみ
    public void create(String summary, String description) {
        IssueEntity issue = new IssueEntity();
        issue.setSummary(summary);
        issue.setDescription(description);
        issueRepository.insert(issue);
    }
}
```

### **5. バリデーション**
```java
// Bean Validationの活用
public class IssueForm {
    @NotBlank(message = "件名は必須です")
    @Size(max = 256, message = "件名は256文字以内で入力してください")
    private String summary;

    @NotBlank(message = "内容は必須です")
    @Size(max = 1000, message = "内容は1000文字以内で入力してください")
    private String description;
}
```

### **6. Java 11モダンパターン** ⭐

#### **6.1 不変コレクションの活用**

```java
// ✅ 推奨: List.of() による型安全な不変リスト作成 (Java 9+)
List<String> statuses = List.of("open", "in_progress", "closed");

// ✅ 可変リストが必要な場合: new ArrayList<>() でラップ
List<IssueEntity> issues = new ArrayList<>(List.of(
    issue1, issue2, issue3
));

// ❌ 非推奨: Arrays.asList() (固定サイズリスト)
List<String> oldStyle = Arrays.asList("a", "b", "c");
```

**理由**:
- `List.of()` は完全に不変で型安全 (Java 9+で利用可能)
- `Arrays.asList()` は固定サイズで add/remove が使えない
- 可変性が必要な場合は必ず `new ArrayList<>()` でラップ

#### **6.2 テストコードでのMockito型安全性**

```java
// ✅ 推奨: チェーン方式による型安全なモック設定
when(mockRepository.findById(userId))
    .thenReturn(Optional.of(user))
    .thenReturn(Optional.empty());

// ❌ 非推奨: varargs方式（型推論警告）
when(mockRepository.findById(userId))
    .thenReturn(Optional.of(user), Optional.empty());
```

**理由**:
- varargs形式 `thenReturn(T... values)` はジェネリック型推論で警告発生
- チェーン方式 `.thenReturn(T).thenReturn(T)` は型安全で警告なし
- 可読性も向上（各戻り値が明示的）

#### **6.3 Raw Type の適切な扱い**

```java
// ✅ Mockitoの型推論制約がある場合: スコープ限定的に@SuppressWarnings使用
@Test
@SuppressWarnings({"rawtypes", "unchecked"})
void testWithMockedConstruction() {
    try (MockedConstruction<ExecutorCompletionService> mocked =
         mockConstruction(ExecutorCompletionService.class, ...)) {
        // テスト実装
    }
}

// ❌ クラスレベルでの抑制は避ける
@SuppressWarnings("rawtypes") // クラス全体に影響
public class MyTest {
    // ...
}
```

**理由**:
- `MockedConstruction<T>` でジェネリック型パラメータを指定できない（Mockito内部制約）
- 警告抑制は最小スコープ（メソッドレベル）に限定
- クラスレベル抑制は他の警告も隠してしまう

#### **6.4 Null安全プログラミング**

```java
// ✅ 推奨: null参照前に必ずガードチェック
@Override
public QuizQuestion randomizeChoices(QuizQuestion question, String sessionId) {
    if (question == null) {
        logger.warn("問題がnullです - sessionId={}", sessionId);
        return null;
    }

    logger.debug("問題ID={}, sessionId={}", question.getId(), sessionId);
    // 安全にquestion.getId()を使用
}

// ❌ 禁止: null参照後のチェック（NullPointerException発生）
logger.debug("問題ID={}", question.getId()); // ここでNPE発生の可能性
if (question == null) {
    return null;
}
```

**理由**:
- nullチェックは必ずオブジェクト参照の**前**に実施
- ログ出力でも `question.getId()` のような参照は危険
- null安全なコードは保守性とデバッグ性を向上

#### **6.5 テストドキュメント化（日本語Javadoc）**

```java
/**
 * SafeDataProcessingService 統合テスト
 *
 * <p>安全なデータ処理サービスの並列処理・例外処理動作を検証します。
 *
 * <p>テスト対象:
 * <ul>
 *   <li>チャンクサイズ境界値での正常動作</li>
 *   <li>複数チャンクの並列処理と集約</li>
 *   <li>InterruptedException発生時の割り込みフラグ設定</li>
 *   <li>ExecutorService終了タイムアウト時の強制終了</li>
 * </ul>
 */
@SpringBootTest
class SafeDataProcessingServiceTest {

    /**
     * 正常系: チャンクサイズ境界値でのデータ処理
     *
     * <p>検証内容:
     * <ul>
     *   <li>入力データ100件をチャンクサイズ100で処理</li>
     *   <li>1チャンクで全データが処理されること</li>
     *   <li>結果リストに全データが含まれること</li>
     * </ul>
     */
    @Test
    void processData_withExactChunkSize() {
        // テスト実装
    }
}
```

**理由**:
- テストの目的と検証内容を明示化
- 将来の保守性向上（何をテストしているかが一目瞭然）
- 箇条書き形式で複数の検証ポイントを整理

#### **6.6 Java 11モダンパターン適用推奨**

**推奨適用箇所**:
- テストコード: Mockito + JUnit 5の組み合わせ
- Null安全性: Optional の活用
- ストリームAPI: コレクション処理の関数型記述

**期待効果**:
- ✅ 型安全性向上
- ✅ テスト可読性向上
- ✅ コード保守性向上
- ✅ バグの早期発見

## 🤖 **AI行動原則**

### **Claude Code vs Codex CLI 使い分けルール**

プロジェクトでは2つのAIツールを効果的に使い分けることで、開発効率を最大化します：

#### **Codex CLI使用ケース**
- **バグ修正が3回以上失敗したら**: 根本原因分析
- **アーキテクチャ設計の相談**: 大規模設計判断
- **コードベース全体の改善点分析**: 品質・セキュリティ・パフォーマンス包括分析
- **複雑な技術的判断**: トレードオフ分析が必要な設計判断

#### **Claude Code使用ケース（通常）**
- **日常的な実装作業**: 機能追加・修正・リファクタリング
- **テストコード作成**: 単体テスト・統合テスト
- **コード品質改善**: 軽微なリファクタリング
- **ドキュメント作成・更新**: README・コメント・設計ドキュメント

この使い分けにより、体系的で効率的な開発プロセスを実現します。

### **AI支援の基本方針**
あなたはコーディング支援AIです。優秀な「新人エンジニア」として実装を進めつつ、仕様の不明点や設計判断の必要がある箇所を、適切なタイミングと方法で人間に確認することが役割です。

### **1. 曖昧性検知モード（デフォルト）**
- 仕様に不明点を見つけた場合、**推測は絶対にせず**、一度に一つだけクローズドな質問を行います
- 明確でない要件については実装前に必ず確認します

### **2. 仮定明文化モード**
- 実装上、何らかの仮定を置く必要がある場合、「仮定(Assumption): {内容}」の形式で明記し、人間に承認を求めます
- 仮定した内容は後で変更可能な形で実装します

### **3. トレードオフ分析モード**
- 実装方法に複数の選択肢があり、それぞれに明確なトレードオフが存在する場合：
  - 各案の長所・短所を簡潔にまとめます
  - AI推奨案と共に人間に最終判断を委ねます

### **4. 質問管理**
- **優先度High**: ブロッカーや設計全体に影響する質問は即時チャットで確認
- **優先度Low**: 実装の細部に関する確認は`questions.md`ファイルに追記

**`questions.md`のフォーマット**:
```markdown
## YYYY-MM-DD
- [優先度:Low] {質問内容} [ステータス:未解決]
```

### **5. 作業進行**
- 人間から指示された作業について、まず具体的な「作業計画」を提示し、承認を得てからコード生成を開始
- 作業中に新たな不明点が出た場合は、上記ルールに従って対応

### **AI支援の目標**
1. **仕様の明確化を最優先**: コード品質よりもまず、思い込みや推測をゼロにする
2. **人間の意思決定コストを最小化**: 質問は重要度順に、判断しやすい形で提示
3. **プロセスを記録**: 対話や仕様決定の履歴が「生きたドキュメント」となるように行動

## 🤖 **Claude Codeへの指示事項**

### **コード生成ガイドライン**
- ✅ コード生成時は必ずこの規約に従ってください
- ✅ 新機能実装時は既存パターンとの一貫性を保ってください
- ✅ セキュリティ関連のコードでは OWASP ベストプラクティスを適用してください
- ✅ テストコードも併せて生成してください
- ✅ 日本語コメントを適切に含めてください

### **重要な実装原則**
1. **セキュリティファースト**: 環境変数・入力値検証は必須
2. **テスト駆動**: 実装と同時にテストケース作成
3. **保守性重視**: 単一責任原則とクリーンコード
4. **パフォーマンス**: バッチ処理とメモリ効率を考慮

## 🔧 **開発中のトラブルシューティング**

### **テスト実行時の問題と対処**

#### **H2データベース接続問題**
```bash
# H2コンソール有効化（application.propertiesに追加）
spring.h2.console.enabled=true

# ブラウザからアクセス
# http://localhost:8080/h2-console
```

#### **Spring Security認証問題**
```bash
# テスト時の認証無効化
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser  // モックユーザーでテスト実行
class IssueControllerTest {
    // テストコード
}
```

#### **Thymeleafテンプレート問題**
```bash
# テンプレートキャッシュ無効化（開発時）
spring.thymeleaf.cache=false

# テンプレートパス確認
spring.thymeleaf.prefix=file:src/main/resources/templates/
```

### **Javaプロセス停止（ポート競合解決）**

開発中にSpring Bootアプリケーションが正常に停止しない場合やポート競合が発生した場合の対処法：

#### **基本手順**
```bash
# 1. 実行中のJavaプロセス一覧を確認
jps

# 2. ItsApplicationプロセスのIDを確認して強制終了
powershell "Stop-Process -Id <プロセスID> -Force"

# 3. ポート解放確認（8080ポートの例）
netstat -an | findstr :8080

# 4. 正常にアプリケーション再起動
./gradlew bootRun
```

#### **よくある状況と対処**
- **ポート8080競合**: `Web server failed to start. Port 8080 was already in use.`
- **IDE停止ボタンでプロセス残存**: DevToolsの再起動でプロセスが残る場合
- **H2データベースロック**: アプリケーション異常終了時のDBファイルロック

#### **予防策**
- Spring Boot DevToolsによる自動再起動を活用
- 設定ファイル変更時は構文チェックを実施
- IDEのアプリケーション停止機能を適切に使用

### **Gradle実行時の注意点**

#### **基本的な開発フロー**
```bash
# 1. 依存関係の更新・確認
./gradlew dependencies

# 2. ビルド（コンパイル+テスト）
./gradlew clean build

# 3. アプリケーション起動
./gradlew bootRun

# 4. 特定のテストのみ実行
./gradlew test --tests IssueServiceTest
```

#### **ログ出力確認**
- **開発環境**: コンソール出力
- **本番環境**: ログファイルに出力（設定による）

### **H2データベース管理**

#### **H2コンソールアクセス**
```bash
# application.propertiesで有効化
spring.h2.console.enabled=true

# ブラウザからアクセス
# URL: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:its
# Username: sa
# Password: (空欄)
```

#### **データベースリセット**
インメモリデータベースのため、アプリケーション再起動で自動的にリセットされます。

**⚠️ 重要**: H2データベースはインメモリ方式のため、アプリケーション停止時にデータが消失します。

## 🏗️ **設計パターン適用ベストプラクティス**

### **MVCパターンの徹底**

#### **レイヤー分離の重要性**

**Controller層**:
- HTTPリクエスト/レスポンス処理のみ
- ビジネスロジックは書かない
- バリデーション結果の確認

**Service層**:
- ビジネスロジックの実装
- トランザクション境界の定義
- 複数のRepositoryを組み合わせた処理

**Repository層**:
- データベースアクセスのみ
- ビジネスロジックを含めない
- MyBatisマッパーの実装

#### **フロントエンド・バックエンド連携**

**Thymeleafテンプレート**:
```html
<!-- Modelから値を取得 -->
<div th:text="${issue.summary}"></div>

<!-- 条件分岐 -->
<div th:if="${#lists.isEmpty(issues)}">
    課題がありません
</div>
```

**Controllerからの値渡し**:
```java
@GetMapping("/issues")
public String list(Model model) {
    List<IssueEntity> issues = issueService.findAll();
    model.addAttribute("issues", issues);
    return "issues/list";
}
```

#### **セキュリティ統合**

**Spring Securityとの連携**:
```java
// 現在のユーザー情報取得
@AuthenticationPrincipal CustomUserDetails userDetails
```

**権限チェック**:
```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteIssue(long id) {
    // 管理者のみ実行可能
}
```

---

**AI開発支援**: このドキュメントはClaude Codeによる開発支援最適化のために作成されました。