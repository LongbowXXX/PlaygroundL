<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# コーディング規約

## 共通スタイル

- Kotlin 公式コードスタイル (`kotlin.code.style=official`)。`./gradlew ktlintFormat` で整形。
- JDK 17 / Kotlin 1.9.10 を前提。Compose Desktop と Coroutine/Serialization を標準で使用。
- 非同期処理は `CoroutineScope` + `Dispatchers.Default` を基本とし、UI 側は Compose の状態 (`mutableStateOf`) で管理。

## 命名と構造

- 関数・変数は振る舞いが分かる英語名を使用 (`requestChat`, `requestCreateImage` など)。
- 真偽値は疑問形 (`is...`, `has...`) を用いる。型拡張は動詞 + 対象 (`toURL`, `toMaskImage`)。
- 単一責任を意識し、UI (Compose) / ViewModel / クライアント / 永続化 を分離。
- 公開 API や複雑処理には簡潔なコメントを付与する。自明な箇所では過剰コメントを避ける。

## Compose / UI

- 画面は `@Composable` 関数で構成し、ステートは ViewModel から受け取る。
- レイアウト余白やアニメーションは `Modifier` / `AnimatedVisibility` / `animateFloatAsState` など Compose 標準を利用。
- タブ切替は整数インデックスからタブ名への変換関数で一元管理 (`toTabName`)。

## 非同期・ストリーム

- OpenAI ストリームは Flow を collect し、差分を積み上げるユーティリティ (`correctStreamResponse`) で一貫処理。
- リクエスト中は `requesting` フラグで UI ロックし、キャンセル可能な `Job` を保持。

## 永続化とファイル

- 履歴は Realm を単一スレッドコンテキストで扱う `RealmBase` 経由で読書き。
- アプリデータは `%APPDATA%/PlaygroundL/` 配下に作成し、存在チェックを行う。
- プラグイン/プロンプト/メッセージはプロジェクトルートの `chatFunction` / `chatPrompt` / `chatMessage` から探索。

## ログとデバッグ

- ロギングは `net.longbowxxx.utils.log*` を使用し、デバッグレベルは `DebugLogLevel` で制御。
- 画像生成ではレスポンス画像・マスクをローカルに保存してトレース可能にする。
