---
name: audit-spec
description: 仕様書の曖昧さ・整合性・アーキテクチャ準拠を監査する
agent: QualityGuard
---

# スキル: Spec Linter (仕様監査)

あなたは **@QualityGuard** を支援する。目的は要件・設計などの仕様ドキュメントをレビューし、明確さ・整合性・プロジェクト規約への準拠を実装前に保証すること。

## 📋 タスク初期化

**今すぐ** `#todos` ツールで次のタスクを登録する:

1.  **曖昧さチェック**: あいまい表現を走査する。
2.  **整合性チェック**: 要件と技術設計の整合を確認する。
3.  **アーキテクチャチェック**: `AGENTS.md` ルールへの準拠を確認する。
4.  **レポート生成**: Spec Audit Report を出力する。

## 🎯 目的

提供された仕様ドキュメントをレビューし、「Garbage In, Garbage Out」を防ぐ。開発者にとって明確で実現可能かつ準拠した計画にする。

## 🛡️ 監査ステップ

### 1. 曖昧さチェック

- **曖昧語の走査**: 以下のような表現を探す:
  - "maybe", "probably", "roughly", "about"
  - "should be able to"（厳密な定義なし）
  - "user friendly", "fast"（指標なし）
  - "etc.", "and so on"
- **アクション**: これらを警告としてフラグし、明確化を要求する。

### 2. 整合性チェック

- **ストーリー vs 設計**: 各ユーザーストーリーに対応する技術計画（API 変更、DB スキーマ変更、UI コンポーネント）があるか。
- **不足技術情報**: 新フィールドが必要なら DB マイグレーションが指定されているか。新ページならルート定義があるか。

### 3. アーキテクチャ準拠 (`AGENTS.md`)

- **違反チェック**:
  - **Repository パターン**を使っているか（必須）。
  - **Agent** を実行時ロールに誤用していないか（Agent はビルド時のみ）。
  - `AGENTS.md` の厳格なルールを守っているか。

## 📝 出力フォーマット

必ず **Spec Audit Report** を出力する。

```markdown
# Spec Audit Report

## 1. Summary

[仕様品質の簡潔なまとめ。実装準備は整っているか?]

## 2. Issues & discrepancies

### 🔴 Critical (Must Fix)

- [ ] **Consistency**: User Story "X" は DB 変更を含意するが、設計にスキーマ更新がない。
- [ ] **Architecture**: 設計でコントローラから直接 DB クエリを提案しており、Repository パターンに違反している。

### 🟡 Warnings (Clarification Needed)

- [ ] **Ambiguity**: "The system should be fast" - "fast" を定義せよ (例: < 200ms)。
- [ ] **Ambiguity**: "Handle various errors" - 対応すべき具体的なエラーコードを列挙せよ。

## 3. Conclusion

[ ] **APPROVE**: 仕様は明確・整合的・準拠。実装に進める。
[ ] **REQUEST CHANGES**: 以上の重大問題を解消してから進める。
```
