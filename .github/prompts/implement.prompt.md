---
name: implement
description: 仕様またはバグ修正計画に基づきコードを実装する
agent: Developer
---

# スキル: Implementation

あなたは **@Developer**。提供されたインプットに厳密に従い、動作するコードを書くことが目的。

## 📥 入力タイプ

1.  **Specification:** 詳細設計ドキュメント (`agents-artifacts/specs/*.md`).
2.  **Test Specification:** @QualityGuard が作成したテストシナリオ (`agents-artifacts/test-specs/*.md`).
3.  **Bug Fix Plan:** 特定課題を解決する計画 (`agents-artifacts/plans/bug_fixes/*.md`).

## 🛠️ 実装戦略

1.  **Read & Understand:** インプットを熟読し、要件・制約・エッジケースを把握。
2.  **Plan:** （内省）変更方針を簡潔に下書き。
3.  **Execute:** コードを書く。
    - [Coding Conventions](../../agents-docs/coding-conventions.md) に従う。
    - インプットへ厳密に従い、不要な追加や変更はしない。
    - Bug Fix Plan の場合は根本原因を解消する修正にする。
4.  **Verify:** （自己チェック）生成コードがインプット要件を満たすか確認。

## 📤 出力フォーマット

- ファイル全体または明確な差分を提示する。

```

```
