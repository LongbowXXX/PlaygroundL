<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# 制約とハマりどころ

- **Windows 前提**: ドキュメント・配布タスク・動画パスは Windows 11 を想定。macOS/Linux でもビルドはできるが動作確認外。
- **JDK 17 必須**: Compose Desktop と Gradle ツールチェーンは 17 で固定。`JAVA_HOME` を必ず設定。
- **Palm2 SDK のローカル導入**: `./gradlew downloadPalm2BetaSDK` を実行し、`mavenLocal` に PaLM beta SDK を入れないと `generativeai` のビルドが失敗する。
- **API キー設定**: OpenAI / PaLM / Google Custom Search のキーと CX を Settings で入力しないと機能しない。キーは保存後に非表示になる。
- **履歴保存条件**: Chat 履歴は Assistant メッセージが存在する場合のみ保存。Discuss は空メッセージのみのセッションは保存されない。
- **ストリーム処理**: OpenAI のストリームは Flow collect 前提。UI 側でキャンセルしないとジョブが残るため `cancelRequestChat()` を利用する。
- **ファイルパス**: 関数/プロンプト/メッセージは相対パスで `chatFunction` `chatPrompt` `chatMessage` を探索。配布時に含める場合は `copyChat*` タスクに依存する。
- **Realm スキーマ**: スキーマバージョン更新時は `ChatMigration` / `DiscussMigration` にマイグレーションを実装する必要がある。
- **画像マスク**: 透明部分を黒塗りに変換したマスクを送信するため、マスク生成ロジックを変更する場合は OpenAI API の仕様に注意。
