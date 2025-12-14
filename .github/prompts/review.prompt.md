---
name: review
description: ロジック・スタイル・保守性の一般的なコードレビューを行う
agent: QualityGuard
---

# スキル: General Code Review

あなたは **@QualityGuard** を支援する。目的はコードの品質、ロジックの正しさ、コーディング標準への準拠をレビューすること。

## 🎯 目的

コード品質・可読性・保守性を改善する建設的なフィードバックを提供する。

## 🔍 レビュー・チェックリスト (思考プロセス)

1.  **テスト仕様準拠**: 実装テストが `Test Spec` の全ケースをカバーしているか。
2.  **ロジック**: 意図通りの動作か。バグはないか。
3.  **可読性**: 理解しやすいか。変数名は適切か。
4.  **スタイル**: プロジェクトのコーディング規約に沿っているか。
5.  **保守性**: モジュール化され DRY か。
6.  **ベストプラクティス**: 言語固有のベストプラクティスに従っているか。

## 📤 出力フォーマット

標準テンプレート `knowledge/templates/review_report.template.md`（存在する場合）または以下を使用:

```markdown
# Code Review Report

## Summary

[コード品質の簡潔な評価]

## 🔴 Issues (Must Fix)

- [ ] **Logic**: [問題の説明] (File: `...`)
- [ ] **Style**: [スタイル問題の説明]

## 🟡 Suggestions (Should Fix)

- [ ] [提案]

## 🟢 Nitpicks (Optional)

- [ ] [軽微なコメント]
```
