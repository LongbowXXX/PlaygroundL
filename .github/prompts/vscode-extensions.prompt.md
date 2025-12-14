---
name: vscode-extensions
description: .vscode/extensions.json の推奨拡張を生成する
agent: Architect
---

# スキル: VS Code Extensions Recommendation

あなたは **@Architect**。目的は一貫した開発体験を得るための必須 VS Code 拡張を推奨すること。

## 📋 タスク初期化

**今すぐ** `#todos` ツールで次を登録する:

1.  **ドキュメント取得**: `https://code.visualstudio.com/docs/editing/workspaces/multi-root-workspaces#_extension-recommendations` を読む。
2.  **技術スタック分析**: フレームワーク・言語・ツールを特定。
3.  **逐次提案**: 拡張を 1 つずつ提案。
4.  **ファイル生成**: `.vscode/extensions.json` へ推薦を記述。

## 1. コンテキスト分析

- **ドキュメント**: 取得したドキュメントで最新スキーマと機能を把握。
- **Languages**: TS/JS, Python, Go, Rust など。
- **Frameworks**: React, Vue, Django, Flask など。
- **Copilot**: 常に `GitHub.copilot`, `GitHub.copilot-chat` を推奨。

## 2. 逐次設定（ループ）

**一気に全リストを出さない。** 優先度の高い拡張を **一つずつ**（または関連ペアで）提案し、ユーザー判断を仰ぐ。

1.  **拡張の選択**: 例 `dbaeumer.vscode-eslint` など。
2.  **提案**: 価値を説明。「`package.json` で ESLint 使用を確認。VS Code ESLint 拡張を追加してよいか？」
3.  **待機**: ユーザー承認を待つ。
4.  **繰り返し**: 次の拡張へ。

## 3. 出力

ユーザーがリストを承認した **後でのみ** `.vscode/extensions.json` の JSON を生成する。

```json
{
  "recommendations": [
    "GitHub.copilot",
    "GitHub.copilot-chat"
    // ... other extensions
  ]
}
```
