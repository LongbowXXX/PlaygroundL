---
name: check-doc-consistency
description: プロジェクト文書の整合性を確認し修正を提案する
agent: Librarian
---

# スキル: Documentation Consistency Check

あなたは **@Librarian** を支援する。目的はプロジェクトの `README.md` から全ドキュメントの整合性を確認し、必要な修正を提案または実施すること。

## 📋 タスク初期化

**今すぐ** 進捗管理のために `#todos` ツールで次を登録する:

1.  **ドキュメント読解**: ルート `README.md` と agent docs を読む。
2.  **リンク検証**: ファイルパス・相対リンク・ナビゲーションを確認する。
3.  **内容整合性確認**: ディレクトリ構成・技術スタック・コード参照を確認する。
4.  **オンボーディング確認**: Setup -> Build -> Usage -> Architecture の流れを検証する。
5.  **レポート生成**: 評価結果をサマリ（Pass/Warning/Error）にまとめる。
6.  **修正提案/実施**: 単純な問題は自動修正し、複雑なものは提案する。
7.  **最終確認**: 「Final Check」セクションを確認する。

## 🎯 目的

1.  **構造が完備**: ルート `README.md` が適切な入口となり、リンクが機能する。
2.  **内容が一致**: ドキュメントがコードベースの実態と一致。
3.  **DRY**: 情報が重複しない。
4.  **プロジェクト規約遵守**: ドキュメントが `agents-docs/` 構成や他の規約に従う。
5.  **ナビゲーション完備**: ルート `README.md` からたどれる。
6.  **オンボーディング円滑**: Setup、Build、Usage、Architecture が明確。

## 🛠️ 手順

### Step 1: ドキュメント読解

1.  **入口の読解**:
    - プロジェクトルート `README.md` を読む。
    - 主要セクションとリンクを把握する。
2.  **エージェント文書**:
    - `agents-docs/` 内を一覧し、`agents-docs/directory-structure.md`、`agents-docs/tech-stack.md`、`agents-docs/key-flows.md` など主要ファイルを読む。

### Step 2: リンクとナビゲーションの検証

1.  **リンク抽出**:
    - 読んだ文書のファイルパス・相対リンクを抽出。
2.  **存在確認**:
    - リンク先のファイル/ディレクトリが存在するか確認。
    - 画像や外部リンク切れをチェック。
3.  **到達性確認**:
    - ルート `README.md` からリンクをたどって全ドキュメントに到達できるか。
    - 孤立ドキュメントがないか確認。

### Step 3: 内容整合性の検証

1.  **ディレクトリ構成**:
    - `agents-docs/directory-structure.md` と実際の構成を比較。
    - 足りない/不要なディレクトリを記録。
2.  **技術スタック・設定**:
    - `agents-docs/tech-stack.md` と `build.gradle.kts` など設定を比較。
    - バージョンや依存が一致するか確認。
3.  **コード参照**:
    - 文書に記載のクラス/関数が実在し名称が正しいか。
4.  **オンボーディングフロー**:
    - 新規ユーザーが **Environment Setup -> Build/Run -> Usage -> Architecture** をスムーズに辿れるか。
    - セクションが明確で論理的か。

### Step 4: レポートと修正

1.  **レポート生成**:
    - 結果を次のカテゴリでまとめる:
      - ✅ **Pass**: 整合している項目。
      - ⚠️ **Warning**: 軽微な不整合・潜在問題。
      - ❌ **Error**: リンク切れ・ファイル欠如・重大な矛盾。
2.  **修正提案/実施**:
    - **自動修正**: タイポや相対パス誤りなど簡単なものは即修正。
    - **提案**: 構造変更や欠落内容は提案として記載。

## 📤 出力フォーマット

次の形式で提示する:

```markdown
# Documentation Consistency Report

## Summary

[文書全体の状態を簡潔にまとめる]

## Findings

### ✅ Consistent Items

- README.md のリンクは有効。
- ...

### ❌ Issues Found

- **Broken Link**: `README.md` が存在しない `docs/setup.md` を参照。
- **Outdated Content**: `directory-structure.md` に新しい `services/payment` ディレクトリが欠落。

## Actions Taken

- `README.md` のタイポを修正。

## Recommendations

- `services/payment` を `directory-structure.md` に追記。
```

## ✅ Final Check

終了前に必ず確認する:

- [ ] すべての todo が完了になっている。
- [ ] すべてのリンク切れを報告または修正した。
- [ ] ルート `README.md` からのナビゲーションを確認した。
- [ ] オンボーディングフロー (Setup->Build->Usage->Architecture) を検証した。
- [ ] 指定フォーマットでレポートを作成した。
- [ ] 修正提案が実行可能である。
