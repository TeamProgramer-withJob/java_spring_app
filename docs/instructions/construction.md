
# 環境構築手順書

## GUI向け

整備中

## CUI向け

CUIでの環境構築手順を以下に示す。

### 0. 環境

* ubuntu
* bash

※Linuxベースの手順です。参考までに。

### 1. コード資産の取り込み

```
# apt経由でgitの取得。
apt update && apt install git

# リポジトリのクローン
$ git clone git@github.com:TeamProgramer-withJob/java_spring_app.git`
``` 

### 2. 実行環境を整備

javaを持ってなかったら、javaをインストールしましょう。
versionは21で（動けば何でも）

### 3. 実行確認

1. プロジェクトをビルド
    * `./gradlew build`
2. プロジェクトの起動
    * `./gradlew bootRun`
3. ブラウザで以下にアクセス
    * `http://localhost:8080`

下の画面が出てきたらOK（2026/01/24現在）

![top_page](/docs/assets/top_page_ss.png)

