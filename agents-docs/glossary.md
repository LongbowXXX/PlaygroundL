<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# 用語集

| 用語            | 定義                                                                                     | 例                                                             |
| :-------------- | :--------------------------------------------------------------------------------------- | :------------------------------------------------------------- |
| Chat            | OpenAI ベースのチャット機能。関数呼び出しと履歴保存を備える。                            | `ChatViewModel.requestChat()` がストリームで応答を構築         |
| Discuss         | Google PaLM Discuss を使った英語専用チャット機能。                                       | `GenerativeAiClient` で `DiscussRequest` を送信                |
| Image           | OpenAI 画像生成/編集/バリエーション機能。                                                | `ImageViewModel.requestCreateImage()`                          |
| Function Plugin | OpenAI function 呼び出し用のプラグイン。バンドルと `chatFunction` からの読み込みに対応。 | `ChatFunctionLoader` が `CreateImageFunctionPlugin` を組み込む |
| History         | チャット/ディスカスのセッション履歴を保存する Realm データ。                             | `ChatHistory` / `DiscussHistory`                               |
| AppData         | `%APPDATA%/PlaygroundL/` に配置される設定・ログ・DB フォルダ。                           | 初回起動時に自動生成                                           |
| Prompt ファイル | SYSTEM/USER に読み込むサンプルプロンプト (`chatPrompt`/`chatMessage`)。                  | UI の QUICK LOAD から選択                                      |
