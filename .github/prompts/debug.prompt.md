---
name: debug
description: ログ解析・再現・修正計画の策定
agent: Debugger
---

# スキル: Debugging & Root Cause Analysis

あなたは **@Debugger** を支援する。目的は問題を科学的に分析し、検証済みの修正計画を作成すること。

## 🎯 目的

「何か壊れている」から「原因と修正方法が明確」の状態に進める。

## 🕵️ デバッグ手順 (思考プロセス)

1.  **Observation**: 症状は何か?（エラーメッセージ、誤出力、クラッシュ）
2.  **Hypothesis**: 原因は何か?（ヌル参照、ロジックエラー、API 変更）
3.  **Experiment (再現)**:
    - スクリプトで再現できるか?
    - _アクション_: 可能なら再現テストを作成。
4.  **Analysis**:
    - 実行フローをトレース。
    - 変数状態を確認。
5.  **Conclusion**: 確定した根本原因。

## 📝 出力フォーマット

必ず `agents-artifacts/plans/bug_fixes/` に **Bug Fix Plan** ファイルを出力する。
標準テンプレート `knowledge/templates/bug_fix_plan.template.md` を使用。

**ファイル名**: `agents-artifacts/plans/bug_fixes/fix-[short-description].md`

````markdown
# Bug Fix Plan: [Title]

## 1. Problem Description

[問題の簡潔な説明]

## 2. Reproduction Steps

1. Run `...`
2. Observe error `...`

## 3. Root Cause Analysis

- **Location**: `src/path/to/file.ts:L123`
- **Cause**: [ロジックエラーの説明]

## 4. Proposed Fix

[変更内容の説明]

```diff
- old_code()
+ new_code()
```

## 5. Verification Plan

- [ ] Run reproduction script (should pass)
- [ ] Run existing tests (should pass)
````
