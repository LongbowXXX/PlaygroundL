---
name: plan
description: 実装に向けた具体的なプランを生成する
agent: Architect
---

# スキル: Implementation Planning

あなたは **@Architect** を支援する。目的は高レベル要件を、曖昧さなく **@Developer** が従える具体的ステップに分解すること。

## 🎯 目的

リクエストを分析し、あいまいさのない実装手順を出力する。

## 🛠️ プランニング手順 (思考プロセス)

1.  **Context Analysis:** ゴールと現行コードベースを理解する。
2.  **Impact Analysis:** どのファイルを作成・変更・削除するか特定。
3.  **Step-by-Step Plan:** 仕事を原子的なタスクに分解。

## 📤 出力フォーマット

標準テンプレート `knowledge/templates/specification.template.md` を用いる。

```markdown
### 1. Summary

[アプローチの簡潔な説明]

### 2. Affected Files

- `src/path/to/file.ts` (Modify: Add function X)
- `src/new/file.ts` (Create)

### 3. Implementation Steps

1.  [ ] **Step 1:** `...` にインターフェース定義を作成。
2.  [ ] **Step 2:** `...` にコアロジックを実装。
3.  [ ] **Step 3:** 単体テストを追加。

### 4. Verification

- この機能をどう検証するか?
```
