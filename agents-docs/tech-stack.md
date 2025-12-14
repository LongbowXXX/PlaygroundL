<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# 技術スタック

| 区分         | 技術                                                  | バージョン                   | 用途                         |
| :----------- | :---------------------------------------------------- | :--------------------------- | :--------------------------- |
| 言語         | Kotlin                                                | 1.9.10                       | 全モジュール実装 (JVM 17)    |
| UI           | JetBrains Compose Desktop                             | 1.5.10                       | デスクトップ UI              |
| コルーチン   | kotlinx-coroutines-core                               | 1.7.3                        | 非同期処理                   |
| シリアライズ | kotlinx-serialization-json                            | 1.6.0                        | JSON シリアライズ            |
| OpenAI       | okhttp + 独自クライアント                             | 4.12.0                       | Chat/Function/Image 呼び出し |
| Google PaLM  | gapic-google-cloud-ai-generativelanguage-v1beta3-java | 0.0.0-SNAPSHOT               | Discuss API                  |
| 検索         | google-api-client / customsearch                      | 2.2.0 / v1-rev20230319-2.0.0 | Google Custom Search         |
| DB           | Realm Kotlin                                          | 1.12.0                       | チャット/ディスカス履歴保存  |
| UI キット    | Material3 Desktop                                     | 1.0.1                        | テーマ/コンポーネント        |
| ログ         | 自作 DebugLogger                                      | -                            | デバッグログ出力             |

## ビルド・配布

- ルート: Gradle Kotlin DSL。`./gradlew build` で全モジュールビルド。
- 静的解析: `ktlintCheck` / `ktlintFormat` タスクを全サブプロジェクトに登録。
- ドキュメント: `dokkaHtmlMultiModule` で README/HowToUse を含むドキュメント生成。
- 配布: `app:createReleaseDistributable` → `app:copyArtifacts` → `zipArtifacts` で配布 ZIP を生成。Win アイコン同梱。
- Palm2 SDK: `./gradlew downloadPalm2BetaSDK` で beta SDK を取得し `mavenLocal` にインストール。

## 実行環境

- OS: Windows 11 想定
- JDK: Amazon Corretto 17 (JAVA_HOME に設定)
- ネットワーク: OpenAI / Google API への外部通信が必要。
