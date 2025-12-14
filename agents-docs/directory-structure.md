<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# ディレクトリ構成

## ツリー概要

```
PlaygroundL/
├── app/                # Compose Desktop 本体 (UI/VM/履歴/関数)
│   └── src/jvmMain/
│       ├── kotlin/net/longbowxxx/playground/
│       │   ├── ui/          # 画面と共通ウィジェット
│       │   ├── viewmodel/   # Chat/Discuss/Image の状態管理
│       │   ├── history/     # Realm ベース履歴
│       │   ├── function/    # 関数呼び出しプラグイン
│       │   ├── preference/  # 設定保存
│       │   ├── recorder/    # 録音関連
│       │   └── logger/      # ログ出力
│       └── resources/       # アイコン・バンドルリソース
├── openai/             # OpenAI API クライアント
├── generativeai/       # Google PaLM Discuss クライアント
├── search/             # Google Custom Search クライアントと関数
├── utils/              # ログ・ユーティリティ
├── chatPrompt/         # システムプロンプトサンプル
├── chatMessage/        # ユーザーメッセージテンプレート
├── chatFunction/       # カスタム関数定義 (JSON/レスポンスサンプル)
├── agents-docs/        # AI エージェント向けドキュメント
├── knowledge/          # ナレッジベースとテンプレート
├── agents-artifacts/   # 出力成果物 (specs 等)
├── images/             # 操作用動画・アイコン
├── build.gradle.kts    # ルートビルドスクリプト
├── settings.gradle.kts # モジュール定義
└── gradle.properties   # バージョン設定
```

## 主要ディレクトリの役割

| ディレクトリ                                              | 目的                                   | 主要ファイル                                          |
| :-------------------------------------------------------- | :------------------------------------- | :---------------------------------------------------- |
| app/src/jvmMain/kotlin/net/longbowxxx/playground          | デスクトップ UI と状態管理             | Main.kt, ui/PlaygroundScreen.kt, viewmodel/\*         |
| app/src/jvmMain/kotlin/net/longbowxxx/playground/history  | 履歴永続化                             | ChatHistory.kt, DiscussHistory.kt, RealmBase.kt       |
| app/src/jvmMain/kotlin/net/longbowxxx/playground/function | 関数プラグイン群                       | ChatFunctionLoader.kt, CreateImageFunctionPlugin など |
| openai/src/main/kotlin/net/longbowxxx/openai              | OpenAI ラッパー                        | OpenAiClient.kt, OpenAiClientImpl.kt                  |
| generativeai/src/main/kotlin/net/longbowxxx/generativeai  | PaLM Discuss ラッパー                  | GenerativeAiClient.kt                                 |
| search/src/main/kotlin/net/longbowxxx/search              | Google 検索連携                        | GoogleCustomSearchClient.kt, function/WebSearchPlugin |
| utils/src/main/kotlin/net/longbowxxx/utils                | 共通ユーティリティ                     | DebugLogger.kt, UrlExtensions.kt                      |
| knowledge                                                 | エージェント向けナレッジ・テンプレート | pr-creation-guidelines.md など                        |
| agents-docs                                               | 生成ドキュメント                       | architecture.md ほか 8 文書                           |

## 依存関係の眺め

- ルート `build.gradle.kts` で Dokka/ktlint と配布用タスクを設定し、モジュール共通のバージョンを `gradle.properties` で管理。
- `app` は `openai`, `generativeai`, `search`, `utils` に依存し、UI 以外の外部 API 呼び出しは各モジュールに集約。
- 履歴・設定ファイルは `%APPDATA%/PlaygroundL/` 配下に生成され、配布アーティファクトは `app:copyArtifacts` で build ディレクトリへ集約。
