# SecurityConfig 移行ガイド

> **Spring Boot 2.x → 3.x / Spring Security 5.x → 6.x への移行解説**
> **更新日**: 2025-11-08
> **対象ファイル**: `src/main/java/com/example/its/config/SecurityConfig.java`

## 📋 目次

- [移行の背景](#移行の背景)
- [修正前後の比較](#修正前後の比較)
- [主な変更点の詳細](#主な変更点の詳細)
- [移行のポイント](#移行のポイント)
- [トラブルシューティング](#トラブルシューティング)

---

## 🎯 移行の背景

Spring Boot 2.x から 3.x へのアップグレードに伴い、Spring Security も 5.x から 6.x にバージョンアップしました。

### なぜ変更が必要なのか？

Spring Security 6では、従来の`WebSecurityConfigurerAdapter`が**完全に廃止**され、新しい設定方式への移行が必須となりました。

**主な理由**:
- **コンポーネントベース設計**: 継承よりも依存性注入を優先
- **Lambda DSLの推奨**: より明確で読みやすい設定
- **モジュール化の促進**: 各セキュリティ機能を独立したBeanとして管理

---

## 📊 修正前後の比較

### 修正前 (Spring Security 5.x)

```java
package com.example.its.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // H2コンソール用の設定
        http
            .authorizeRequests().antMatchers("/h2-console/**").permitAll()
            .and()
            .csrf().ignoringAntMatchers("/h2-console/**")
            .and()
            .headers().frameOptions().disable();

        // 通常の認証設定
        http
            .authorizeRequests()
            .mvcMatchers("/login/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
```

---

### 修正後 (Spring Security 6.x)

```java
package com.example.its.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // H2 Console configuration
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

---

## 🔍 主な変更点の詳細

### 1. クラス構造の変更

| 項目 | 修正前 (5.x) | 修正後 (6.x) |
|------|-------------|-------------|
| 継承 | `extends WebSecurityConfigurerAdapter` | 継承なし |
| アノテーション | `@EnableWebSecurity` | `@Configuration` + `@EnableWebSecurity` |
| 設定方法 | `configure()` メソッドのオーバーライド | `@Bean` メソッドで返却 |

**理由**: Spring Security 6では、コンポーネントベースの設定を推奨。継承よりも依存性注入を優先する設計に変更されました。

#### 変更詳細

**修正前**:
```java
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 設定
    }
}
```

**修正後**:
```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 設定
        return http.build();
    }
}
```

---

### 2. SecurityFilterChain の導入

**修正前**:
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()...
}
```

**修正後**:
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth...)
    return http.build();
}
```

**変更点**:
- `configure()` メソッド → `@Bean` で `SecurityFilterChain` を返すメソッド
- `authorizeRequests()` → `authorizeHttpRequests()` (メソッド名変更)
- `antMatchers()` / `mvcMatchers()` → `requestMatchers()` (統一)

**メリット**:
- 複数の `SecurityFilterChain` を定義可能（パス別に異なるセキュリティ設定）
- テストしやすい（Beanとして注入可能）
- より明確な責務分離

---

### 3. Lambda DSL スタイルの採用

**修正前 (メソッドチェーン方式)**:
```java
http
    .authorizeRequests()
    .mvcMatchers("/login/**").permitAll()
    .anyRequest().authenticated()
    .and()  // ← andで繋ぐ
    .formLogin()
    .loginPage("/login");
```

**修正後 (Lambda DSL)**:
```java
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login/**").permitAll()
        .anyRequest().authenticated()
    )  // ← Lambdaでスコープを明確化
    .formLogin(form -> form
        .loginPage("/login")
        .permitAll()
    );
```

**メリット**:
- `.and()` の連続が不要になり、可読性向上
- 設定のスコープが明確化（Lambda内で完結）
- IDEの補完が効きやすい
- ネストが浅くなり、視覚的に理解しやすい

**比較表**:

| 観点 | メソッドチェーン | Lambda DSL |
|------|----------------|-----------|
| 可読性 | `.and()` が多く煩雑 | スコープが明確で読みやすい |
| 保守性 | 変更時に影響範囲が広い | Lambdaごとに独立 |
| IDE補完 | 補完が効きにくい場合あり | 補完が効きやすい |
| ネスト深度 | 深くなりがち | 浅く保てる |

---

### 4. 認証マネージャーの設定変更

**修正前**:
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(NoOpPasswordEncoder.getInstance());
}
```

**修正後**:
```java
@Bean
public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authProvider);
}

@Bean
@SuppressWarnings("deprecation")
public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
}
```

**変更点**:
- `AuthenticationManagerBuilder` → `DaoAuthenticationProvider` + `ProviderManager`
- 明示的に `AuthenticationManager` を `@Bean` として定義
- `PasswordEncoder` も別 `@Bean` として切り出し

**理由**:
- Spring Security 6では、認証処理をより細かく制御できるように、各コンポーネントを独立した Bean として管理する設計に変更されました
- テスト時に個別のBeanをモック化しやすくなる
- 複数の認証プロバイダーを組み合わせやすくなる

---

### 5. H2コンソールとCSRF設定の統合

**修正前** (分離した設定):
```java
// H2コンソール用の設定
http
    .authorizeRequests().antMatchers("/h2-console/**").permitAll()
    .and()
    .csrf().ignoringAntMatchers("/h2-console/**")
    .and()
    .headers().frameOptions().disable();

// 通常の認証設定
http
    .authorizeRequests()
    .mvcMatchers("/login/**").permitAll()
    .anyRequest().authenticated()
    .and()
    .formLogin()
    .loginPage("/login");
```

**修正後** (統合された設定):
```java
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/h2-console/**").permitAll()
        .requestMatchers("/login/**").permitAll()
        .anyRequest().authenticated()
    )
    .csrf(csrf -> csrf
        .ignoringRequestMatchers("/h2-console/**")
    )
    .headers(headers -> headers
        .frameOptions(frameOptions -> frameOptions.disable())
    )
    .formLogin(form -> form
        .loginPage("/login")
        .permitAll()
    );
```

**改善点**:
- すべての設定が1つの `SecurityFilterChain` に集約
- Lambda DSLで各設定のスコープが明確
- 重複した `http` 記述が不要
- 設定の関連性が視覚的に理解しやすい

---

## 🎓 移行のポイント

### ✅ **推奨される変更**

1. **`WebSecurityConfigurerAdapter` を削除**
   - 継承をやめる
   - `@Configuration` アノテーションを追加

2. **`SecurityFilterChain` を返す**
   - `@Bean` メソッドで設定を返す
   - `http.build()` で SecurityFilterChain を構築

3. **Lambda DSL を使用**
   - `.and()` を使わず、Lambdaでスコープを明確化
   - より読みやすく保守しやすいコードに

4. **`requestMatchers()` に統一**
   - `antMatchers()` / `mvcMatchers()` を置き換え
   - パターンマッチングのAPI統一

5. **認証コンポーネントをBean化**
   - `AuthenticationManager` を明示的に定義
   - `PasswordEncoder` を独立したBeanとして管理

---

### ⚠️ **注意点**

#### 1. NoOpPasswordEncoder の非推奨

```java
@Bean
@SuppressWarnings("deprecation")  // ← 警告抑制
public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
}
```

**問題点**: `NoOpPasswordEncoder` はパスワードを平文で保存するため、セキュリティリスクが高い

**推奨される対応**:
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();  // ← 本番環境ではこちらを使用
}
```

**本プロジェクトでの方針**:
- **開発環境**: `NoOpPasswordEncoder` を継続使用（デバッグ容易性のため）
- **本番環境**: `BCryptPasswordEncoder` への移行を推奨

---

#### 2. H2コンソールのフレームオプション無効化

```java
.headers(headers -> headers
    .frameOptions(frameOptions -> frameOptions.disable())  // ← セキュリティリスク
)
```

**問題点**: クリックジャッキング攻撃に対して脆弱になる

**対応方針**:
- **開発環境**: H2コンソール使用のため無効化を許容
- **本番環境**: H2を使用しない（PostgreSQL/MySQL等）ため設定削除

---

#### 3. CSRF保護の部分的無効化

```java
.csrf(csrf -> csrf
    .ignoringRequestMatchers("/h2-console/**")  // ← CSRF無効化
)
```

**問題点**: H2コンソールへのCSRF攻撃に対して無防備

**対応方針**:
- H2コンソールは開発環境でのみ使用
- 本番環境ではH2を使用しないため問題なし
- 開発環境では localhost からのみアクセス可能にする

---

## 🛠️ 移行手順

### Step 1: 依存関係の更新

**build.gradle**:
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'  // ← 6に変更
}
```

### Step 2: インポート文の変更

**不要になるインポート**:
```java
// 削除
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
```

**追加するインポート**:
```java
// 追加
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
```

### Step 3: クラス定義の変更

**修正前**:
```java
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
```

**修正後**:
```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
```

### Step 4: configure メソッドの変更

**修正前**:
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()...
}
```

**修正後**:
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth...)
    return http.build();
}
```

### Step 5: メソッド名の置き換え

| 修正前 | 修正後 |
|-------|-------|
| `authorizeRequests()` | `authorizeHttpRequests()` |
| `antMatchers()` | `requestMatchers()` |
| `mvcMatchers()` | `requestMatchers()` |
| `.and()` | Lambda DSLに置き換え |

### Step 6: 認証設定の変更

**修正前**:
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(NoOpPasswordEncoder.getInstance());
}
```

**修正後**:
```java
@Bean
public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authProvider);
}

@Bean
@SuppressWarnings("deprecation")
public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
}
```

---

## 🐛 トラブルシューティング

### 問題1: コンパイルエラー「WebSecurityConfigurerAdapter が見つからない」

**エラーメッセージ**:
```
error: cannot find symbol
  symbol:   class WebSecurityConfigurerAdapter
  location: package org.springframework.security.config.annotation.web.configuration
```

**原因**: Spring Security 6 では `WebSecurityConfigurerAdapter` が完全に削除されました

**解決方法**:
1. `extends WebSecurityConfigurerAdapter` を削除
2. `@Configuration` アノテーションを追加
3. 上記の移行手順に従って書き換え

---

### 問題2: 「authorizeRequests() は非推奨」という警告

**警告メッセージ**:
```
warning: authorizeRequests() in HttpSecurity has been deprecated
```

**原因**: `authorizeRequests()` は Spring Security 6 で非推奨

**解決方法**:
```java
// 修正前
.authorizeRequests()

// 修正後
.authorizeHttpRequests()
```

---

### 問題3: 「antMatchers() が見つからない」

**エラーメッセージ**:
```
error: cannot find symbol
  symbol:   method antMatchers(String)
```

**原因**: `antMatchers()` / `mvcMatchers()` は `requestMatchers()` に統一されました

**解決方法**:
```java
// 修正前
.antMatchers("/h2-console/**").permitAll()
.mvcMatchers("/login/**").permitAll()

// 修正後
.requestMatchers("/h2-console/**").permitAll()
.requestMatchers("/login/**").permitAll()
```

---

### 問題4: H2コンソールにアクセスできない

**症状**: http://localhost:8080/h2-console にアクセスすると403エラー

**原因**: CSRF保護またはフレームオプション設定が不足

**解決方法**:
```java
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/h2-console/**").permitAll()  // ← H2へのアクセス許可
    )
    .csrf(csrf -> csrf
        .ignoringRequestMatchers("/h2-console/**")  // ← H2のCSRF無効化
    )
    .headers(headers -> headers
        .frameOptions(frameOptions -> frameOptions.disable())  // ← フレーム許可
    );
```

---

### 問題5: ログイン後に404エラー

**症状**: ログインは成功するが、その後404エラーになる

**原因**: ログイン成功後のリダイレクト先が存在しない

**解決方法**:
```java
.formLogin(form -> form
    .loginPage("/login")
    .defaultSuccessUrl("/", true)  // ← 成功後のリダイレクト先を指定
    .permitAll()
);
```

---

## 📚 参考資料

### 公式ドキュメント
- [Spring Security 6.0 Migration Guide](https://docs.spring.io/spring-security/reference/migration/index.html)
- [Spring Security without WebSecurityConfigurerAdapter](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)
- [Lambda DSL Configuration](https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#jc-httpsecurity)

### 関連ドキュメント
- [Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Jakarta EE Migration](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired.html)

---

## ✅ まとめ

| 観点 | 修正前 (5.x) | 修正後 (6.x) |
|------|-------------|-------------|
| **設計思想** | 継承ベース | コンポーネントベース |
| **設定スタイル** | メソッドチェーン + `.and()` | Lambda DSL |
| **メソッド名** | `authorizeRequests()`, `antMatchers()` | `authorizeHttpRequests()`, `requestMatchers()` |
| **認証設定** | `AuthenticationManagerBuilder` | `DaoAuthenticationProvider` + Bean |
| **可読性** | やや低い（`.and()` の連続） | 高い（Lambdaでスコープ明確） |
| **テスタビリティ** | やや低い | 高い（Bean注入可能） |
| **モジュール化** | 難しい | 容易（複数FilterChain可能） |

### 移行の効果

✅ **コードの可読性向上**: Lambda DSLにより設定のスコープが明確
✅ **保守性向上**: 各コンポーネントが独立したBeanとして管理
✅ **テスタビリティ向上**: Beanとして注入可能でモック化が容易
✅ **モダンな設計**: Spring Security 6の推奨パターンに準拠

この修正により、Spring Boot 3.x + Spring Security 6.x の**モダンな設定方式**に対応し、より保守性・拡張性の高いコードになりました。

---

**ドキュメント作成日**: 2025-11-08
**作成者**: Claude Code
**バージョン**: 1.0
