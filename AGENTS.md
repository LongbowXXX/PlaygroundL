# AGENTS.md - PlaygroundL

> OpenAI/PaLM/Google Search を統合した Compose Desktop 製 AI プレイグラウンド。

このファイルは AI エージェントがプロジェクトを理解し、正しい手順で作業するための索引です。

## 1. Executive Summary

**Purpose**: デスクトップ上で OpenAI チャット/関数呼び出し、PaLM Discuss、画像生成、Google 検索を一括操作し、履歴とログをローカル管理する。  
**Type**: Desktop アプリ (Compose) / マルチモジュールライブラリ  
**Status**: Beta 相当 (社内利用想定)

## 2. Architecture & Tech Stack

### Core Technologies

| Category | Technology                | Version              | Purpose                      |
| -------- | ------------------------- | -------------------- | ---------------------------- |
| Language | Kotlin                    | 1.9.10               | 全モジュール実装 (JVM 17)    |
| UI       | JetBrains Compose Desktop | 1.5.10               | デスクトップ UI              |
| Data     | Realm Kotlin              | 1.12.0               | チャット/ディスカス履歴保存  |
| API      | OpenAI API + OkHttp       | 4.12.0               | Chat/Function/Image 呼び出し |
| API      | Google PaLM Discuss SDK   | 0.0.0-SNAPSHOT       | Discuss 生成 AI              |
| API      | Google Custom Search      | v1-rev20230319-2.0.0 | Web 検索関数                 |

### Architectural Patterns

- **MVVM + Compose**: Compose UI に対し ViewModel が状態と非同期処理を管理し、ステートを `mutableStateOf` でバインド。
- **モジュール分割**: `app` が UI、`openai`/`generativeai`/`search` が外部 API、`utils` が共通基盤を担当するモジュラーモノリス。
- **プラグイン型関数呼び出し**: `ChatFunctionLoader` がバンドル/外部ディレクトリから関数プラグインを読み込み、OpenAI function calling に提供。

## 3. Directory Structure

```
PlaygroundL/
├─ app/                # Compose Desktop UI と ViewModel
├─ openai/             # OpenAI クライアント
├─ generativeai/       # PaLM Discuss クライアント
├─ search/             # Google Custom Search 連携
├─ utils/              # 共通ユーティリティ
├─ chatPrompt/         # システムプロンプト
├─ chatMessage/        # ユーザーメッセージテンプレート
├─ chatFunction/       # 関数プラグイン定義
├─ agents-docs/        # エージェント用ドキュメント
├─ knowledge/          # ナレッジベース
└─ build.gradle.kts    # ルートビルド
```

### Key Directories

| Directory                                                  | Purpose                            | Key Files                                                                                                                                                                                                                                                                                                             |
| ---------------------------------------------------------- | ---------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| app/src/jvmMain/kotlin/net/longbowxxx/playground           | エントリポイント・UI・設定         | [Main.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/Main.kt#L17), [ui/PlaygroundScreen.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/ui/PlaygroundScreen.kt#L21)                                                                                                                                        |
| app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel | チャット/画像/ディスカスの状態管理 | [ChatViewModel.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/ChatViewModel.kt#L28), [DiscussViewModel.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/DiscussViewModel.kt#L28), [ImageViewModel.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/ImageViewModel.kt#L33) |
| app/src/jvmMain/kotlin/net/longbowxxx/playground/history   | 履歴永続化 (Realm)                 | [ChatHistory.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/history/ChatHistory.kt#L21), [DiscussHistory.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/history/DiscussHistory.kt#L21)                                                                                                                    |
| app/src/jvmMain/kotlin/net/longbowxxx/playground/function  | OpenAI 関数プラグイン              | [ChatFunctionLoader.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/function/ChatFunctionLoader.kt#L16)                                                                                                                                                                                                          |
| openai/src/main/kotlin/net/longbowxxx/openai               | OpenAI API ラッパー                | [OpenAiClient.kt](openai/src/main/kotlin/net/longbowxxx/openai/client/OpenAiClient.kt), [OpenAiClientImpl.kt](openai/src/main/kotlin/net/longbowxxx/openai/client/OpenAiClientImpl.kt)                                                                                                                                |
| generativeai/src/main/kotlin/net/longbowxxx/generativeai   | PaLM Discuss ラッパー              | [GenerativeAiClient.kt](generativeai/src/main/kotlin/net/longbowxxx/generativeai/GenerativeAiClient.kt)                                                                                                                                                                                                               |
| search/src/main/kotlin/net/longbowxxx/search               | Google 検索連携                    | [GoogleCustomSearchClient.kt](search/src/main/kotlin/net/longbowxxx/search/GoogleCustomSearchClient.kt)                                                                                                                                                                                                               |

## 4. Key Concepts (Ubiquitous Language)

| Term            | Definition                                                                         | Example                                                    |
| --------------- | ---------------------------------------------------------------------------------- | ---------------------------------------------------------- |
| Chat            | OpenAI ベースのチャット機能。関数呼び出しと履歴保存付き。                          | `ChatViewModel.requestChat()` でストリーム応答生成         |
| Discuss         | PaLM Discuss による英語専用チャット。                                              | `GenerativeAiClient.requestDiscuss()`                      |
| Image           | DALL·E 3 で画像生成/編集/バリエーションを行う機能。                                | `ImageViewModel.requestCreateImage()`                      |
| Function Plugin | OpenAI function に渡すプラグイン。バンドルと `chatFunction` からの読み込みを統合。 | `ChatFunctionLoader` が `CreateImageFunctionPlugin` を提供 |
| History         | チャット/ディスカスセッションの Realm データ。                                     | `ChatHistory.saveSession()`                                |

## 5. Entry Points

| Entry Point | Location                                                                                                                                                             | Purpose             |
| ----------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------- |
| Main        | [app/src/jvmMain/kotlin/net/longbowxxx/playground/Main.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/Main.kt#L17)                                             | Compose アプリ起動  |
| Window/UI   | [app/src/jvmMain/kotlin/net/longbowxxx/playground/ui/PlaygroundScreen.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/ui/PlaygroundScreen.kt#L21)               | タブ付き UI 構築    |
| Chat VM     | [app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/ChatViewModel.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/ChatViewModel.kt#L28)       | OpenAI チャット処理 |
| Discuss VM  | [app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/DiscussViewModel.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/DiscussViewModel.kt#L28) | PaLM Discuss 処理   |
| Image VM    | [app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/ImageViewModel.kt](app/src/jvmMain/kotlin/net/longbowxxx/playground/viewmodel/ImageViewModel.kt#L33)     | 画像生成/編集処理   |

## 6. Development Rules (Constitution Summary)

### 🔍 Dynamic Context Protocol (Research Phase)

- このファイルは索引です。作業前に必ず `agents-docs/` と `knowledge/` をキーワード検索し、該当文書を読み込んでください。
- 具体的な手順・規約は各ドキュメントに詳細があります。リンクを辿って内容を確認すること。

### Must Follow

- API キーと CX を設定済みであることを前提にする。未設定時はユーザーに確認。
- 外部 API 呼び出しはクライアントモジュール経由で行い、UI から直接 HTTP を呼ばない。
- 履歴/設定は `%APPDATA%/PlaygroundL/` に保存するパスを尊重する。

### Must Avoid

- UI スレッドで長時間ブロックする処理。
- Compose 以外から直接 Realm を操作すること (必ず `RealmBase` 派生を利用)。
- `chatFunction` / `chatPrompt` / `chatMessage` の構成を壊すような配布タスク変更。

### Patterns to Use

- ViewModel + Coroutine で非同期を扱い、ストリームは Flow を合成して UI へ反映。
- OpenAI function calling はプラグインリストから有効化されたもののみ渡す。
- モジュール依存関係を保ち、共通処理は `utils` へ集約。

## 7. Quick Reference

### Common Commands

```bash
./gradlew build                   # 全ビルド
./gradlew test                    # 全テスト
./gradlew app:createReleaseDistributable # 配布用バイナリ生成
./gradlew downloadPalm2BetaSDK    # PaLM beta SDK を mavenLocal へ導入
```

### Important Files for AI Agents

| Purpose                | File                                       |
| ---------------------- | ------------------------------------------ |
| プロジェクト概要       | [README.md](README.md)                     |
| 使い方                 | [HowToUse.md](HowToUse.md)                 |
| ビルド設定             | [build.gradle.kts](build.gradle.kts)       |
| モジュール設定         | [settings.gradle.kts](settings.gradle.kts) |
| AGENT 向けドキュメント | [agents-docs/](agents-docs)                |
| ナレッジベース         | [knowledge/](knowledge)                    |

## 8. Documentation Index

- [agents-docs/architecture.md](agents-docs/architecture.md)
- [agents-docs/directory-structure.md](agents-docs/directory-structure.md)
- [agents-docs/coding-conventions.md](agents-docs/coding-conventions.md)
- [agents-docs/key-flows.md](agents-docs/key-flows.md)
- [agents-docs/tech-stack.md](agents-docs/tech-stack.md)
- [agents-docs/testing.md](agents-docs/testing.md)
- [agents-docs/constraints-and-gotchas.md](agents-docs/constraints-and-gotchas.md)
- [agents-docs/glossary.md](agents-docs/glossary.md)

## 9. Knowledge Base

→ **詳細**: [knowledge/](knowledge)

| Topic                    | Link                                                                                           |
| ------------------------ | ---------------------------------------------------------------------------------------------- |
| PR 作成ガイドライン      | [knowledge/pr-creation-guidelines.md](knowledge/pr-creation-guidelines.md)                     |
| プロンプト技法           | [knowledge/prompting-techniques.md](knowledge/prompting-techniques.md)                         |
| ソフトウェアレビュー視点 | [knowledge/software-review-perspectives.md](knowledge/software-review-perspectives.md)         |
| 仕様記述ガイド           | [knowledge/specification-guidelines.md](knowledge/specification-guidelines.md)                 |
| XML + Markdown ガイド    | [knowledge/xml-structured-prompting.md](knowledge/xml-structured-prompting.md)                 |
| AI リテラシー            | [knowledge/ai-literacy/README.md](knowledge/ai-literacy/README.md)                             |
| バグ修正計画テンプレ     | [knowledge/templates/bug_fix_plan.template.md](knowledge/templates/bug_fix_plan.template.md)   |
| 要件テンプレ             | [knowledge/templates/requirements.template.md](knowledge/templates/requirements.template.md)   |
| レビューレポートテンプレ | [knowledge/templates/review_report.template.md](knowledge/templates/review_report.template.md) |
| 仕様テンプレ             | [knowledge/templates/specification.template.md](knowledge/templates/specification.template.md) |
| テスト仕様テンプレ       | [knowledge/templates/test_spec.template.md](knowledge/templates/test_spec.template.md)         |
