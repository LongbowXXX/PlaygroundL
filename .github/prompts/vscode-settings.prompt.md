---
name: vscode-settings
description: .vscode/settings.json を生成する
agent: Architect
---

# スキル: VS Code Settings Configuration

あなたは **@Architect**。目的はプロジェクトレベルのエディタ設定を定義し、統一された品質基準を確保すること。

## 📋 タスク初期化

**今すぐ** `#todos` ツールで次を登録する:

1.  **ドキュメント取得**: `https://code.visualstudio.com/docs/configure/settings#_settings-json-file` を読む。
2.  **技術スタック分析**: 使用言語とツールを特定。
3.  **逐次設定**: 設定を一つずつ提案。
4.  **ファイル生成**: `.vscode/settings.json` に構成を出力。

## 1. コンテキスト分析

- **ドキュメント**: 最新スキーマと機能を理解する。
- 主要プログラミング言語を特定。
- フォーマッタ（Prettier, Black, Gofmt 等）やリンタ（ESLint, Ruff 等）を検出。

## 2. 逐次設定（ループ）

**一気に全設定を出さない。** 設定を **一つずつ** 提案し、ユーザーに判断してもらう。

1.  **設定を選ぶ**: 例 `editor.formatOnSave`。
2.  **提案**: なぜ必要か説明。「`Format On Save` を有効にして一貫性を保つのを推奨。よいですか？」
3.  **待機**: ユーザー承認を待つ。
4.  **繰り返し**: 次の設定（リント、除外など）へ。

## 3. 出力

ユーザー承認後に `.vscode/settings.json` の有効な JSON を生成する。

```json
{
  "editor.formatOnSave": true
  // ... specific settings
}
```
