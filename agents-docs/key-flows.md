<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# 主要フロー

## チャット (OpenAI) フロー

1. ユーザーがプロンプトとメッセージを入力し送信。
2. `ChatViewModel.requestChat()` が OpenAI Chat API をストリームで呼び出し、関数呼び出しが許可されていればプラグイン一覧から選択して実行。
3. ストリーム delta を `correctStreamResponse()` で結合し UI に逐次反映。
4. 応答完了後に次のユーザーメッセージ枠を追加し、`ChatLogger` と `ChatHistory` に保存。

## Discuss (PaLM) フロー

1. ユーザーがメッセージを入力し送信。
2. `DiscussViewModel.requestChat()` が `GenerativeAiClient` に `DiscussRequest` を送信。
3. 受信した候補をメッセージに追加し、履歴タイトルを更新後に次メッセージ枠を準備。
4. 履歴を Realm に保存し、ログを `DiscussLogger` に記録。

## 画像生成・編集フロー

1. プロンプト (必要なら和文 → 英文翻訳) を入力。
2. `ImageViewModel.requestCreateImage()` が DALL·E 3 で画像生成し、レスポンス画像をローカル保存・表示。
3. 既存画像を選択して `requestImageVariation()` または `requestEditImage()` を実行。編集時はマスク画像を生成して送信。
4. 生成結果とマスクは `ImageLogger` が保存。エラーは `errorMessage` に反映。

## 設定・起動

- 初回起動時に `%APPDATA%/PlaygroundL/` を作成し、API キーやウィンドウ位置を保存。
- `PlaygroundWindow` が起動時にスプラッシュ表示後、タブ (Chat / Discuss / Image / Settings) を表示。
- 設定変更は `appProperties` / `chatProperties` / `discussProperties` / `imageProperties` に保持され、終了時に保存される。
