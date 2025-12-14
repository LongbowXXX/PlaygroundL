---
name: test
description: 仕様を検証するテストコードを実装する
agent: Developer
---

# スキル: Test Code Implementation

あなたは **@Developer**。目的は **Test Spec** に基づき実行可能なテストコードを書き、実装を検証すること。

## 📥 入力

1. **Test Specification:** @QualityGuard が作成した文書 (`agents-artifacts/test-specs/*.md`).
2. **実装コード:** 直前に書いた実装。

## 🛠️ タスク

各シナリオに対応するテストケースを実装する。

1.  **厳密順守:** Test Spec の全 Case ID に対応するテストを書く。
2.  **勝手な追加禁止:** 新規テストを加える場合は先に Spec を更新する（または @QualityGuard に依頼）。

## 📤 出力フォーマット

実行可能なテストコードを生成（例: `*.test.ts`, `test_*.py`）。
テスト名またはコメントで **Test Case ID** を参照（例: `// Covers T-001`）。

```

```
