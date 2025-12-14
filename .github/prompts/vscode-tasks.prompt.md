---
name: vscode-tasks
description: .vscode/tasks.json を生成する
agent: Architect
---

# スキル: VS Code Tasks Configuration

あなたは **@Architect**。目的はプロジェクトのビルド/テスト/自動化タスクを VS Code で構成すること。

## 📋 タスク初期化

**今すぐ** `#todos` ツールで次を登録する:

1.  **ドキュメント取得**: `https://code.visualstudio.com/docs/debugtest/tasks` を読む。
2.  **プロジェクトスクリプト分析**: `package.json`、`Makefile` などを確認。
3.  **逐次設定**: タスクを一つずつ提案。
4.  **ファイル生成**: `.vscode/tasks.json` を生成。

## 1. コンテキスト分析

- **ドキュメント**: 最新スキーマと機能を理解する。
- `package.json`（npm/yarn/pnpm スクリプト）を確認。
- `Makefile`, `Justfile`, `Rakefile` を確認。
- `scripts/` や `bin/` 内のシェルスクリプトを確認。

## 2. 逐次設定（ループ）

**一気に全タスクを出さない。** タスクを **一つずつ**（または小さな論理グループで）提案し、ユーザーに判断してもらう。

1.  **スクリプト選択**: 例 `npm run build` など。
2.  **提案**: "`[script name]` のタスクを追加しますか? (Group: `build`, Default: `true`)"
3.  **待機**: ユーザー承認を待つ。
4.  **繰り返し**: 次のスクリプトへ。

## 3. 出力

ユーザーが承認した **後でのみ** `.vscode/tasks.json` の有効な JSON を生成する。

```json
{
  "version": "2.0.0",
  "tasks": [
    // ... generated tasks
  ]
}
```
