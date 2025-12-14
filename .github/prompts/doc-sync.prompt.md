---
name: doc-sync
description: agents-docs/ 配下のプロジェクト文書を生成・更新する
agent: Librarian
---

# スキル: Documentation Sync

あなたは **@Librarian** を支援する。目的は、AI エージェントがコードベースを効率的に理解できるよう包括的なプロジェクト文書を生成・維持すること。

## 📋 タスク初期化

**今すぐ** 進捗管理のために `#todos` ツールで次を登録する:

1.  **コンテキスト分析**: README・依存・構成から情報を収集。
2.  **技術スタック検出**: 言語・フレームワーク・ツールを特定。
3.  **文書生成**: 必須 8 文書を生成/更新。
4.  **品質チェック**: 正確性・完全性・フォーマットを検証。
5.  **最終確認**: 「Final Check」セクションを確認。

## 🎯 目的

ワークスペースを分析し、`agents-docs/` に文書を生成/更新する。これらは他の AI エージェント（`@Architect`, `@Developer`, `@QualityGuard`）の「ナレッジマップ」となる。

## 📁 出力ファイル

以下の 8 文書を生成:

| File                         | Purpose                            | Key Sections                                        |
| ---------------------------- | ---------------------------------- | --------------------------------------------------- |
| `architecture.md`            | システム概要と設計根拠             | Components, Diagrams, Design Decisions              |
| `directory-structure.md`     | ファイル構成ガイド                 | Tree structure, Responsibilities, Dependencies      |
| `coding-conventions.md`      | コードスタイルと標準               | Naming, Formatting, Patterns                        |
| `key-flows.md`               | 主要ワークフローとエントリポイント | User Journeys, Data Flow, API Endpoints             |
| `tech-stack.md`              | 技術選定と依存                     | Languages, Frameworks, External Services            |
| `testing.md`                 | テスト戦略とパターン               | Test Types, Coverage Goals, Running Tests           |
| `constraints-and-gotchas.md` | 既知の制約・落とし穴               | Technical Debt, Platform Constraints, Common Issues |
| `glossary.md`                | 共通語彙・定義                     | Key Terms, Domain Concepts, Acronyms                |

## 🛠️ 生成プロセス

### 1. コンテキスト分析

以下から情報を収集:

- `README.md` - プロジェクト概要とクイックスタート
- `build.gradle.kts` など依存情報
- ソース構成 - アーキテクチャパターン
- 既存文書 - `agents-docs/`, `knowledge/`
- 設定ファイル - ビルド/リント/テスト設定

### 2. 技術スタック検出

`agents-docs/tech-stack.md` に次を特定:

- **Language(s)**: Kotlin など。
- **Framework(s)**: Compose Desktop など。
- **Database(s)**: 使用 DB。
- **Build Tools**: gradle 等。
- **Testing**: 使用テストフレームワーク。

### 3. 文書生成

各ファイルは次の構造で作成:

```markdown
<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# [Document Title]

## [Section 1]

[プロジェクトから得た具体情報]

## [Section 2]

[必要なら Mermaid 図]

...
```

## 📤 出力フォーマット

各ファイルのパスと完全な内容を出力する:

**File: `agents-docs/architecture.md`**

```markdown
<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# Architecture Overview

## System Overview

[分析したプロジェクトの説明]

## Main Components

[コンポーネント分解と責務]

## Architecture Diagrams

[構造を示す Mermaid 図]

## Design Rationale

[主要な設計判断と理由]
```

## 📋 品質チェックリスト

出力前に次を確認:

- [ ] **Accuracy**: 情報が実際のコードベースと一致。
- [ ] **Completeness**: 重要事項を網羅。
- [ ] **Clarity**: 深い前提なしでも理解できる。
- [ ] **Actionability**: 開発者がナビゲートに使える。
- [ ] **Diagrams**: 複雑な関係に Mermaid 図を使用。
- [ ] **Examples**: 必要に応じコード例。

## 🔄 更新モード

既存文書を更新する場合:

1. **構造維持**: 既存セクション構成を保持。
2. **変更検出**: 現在のコードベースと比較。
3. **最小差分**: 必要部分だけ更新。

## ⚠️ 注意

- **言語**: 特に指定がない限り文書出力は **English**。
- **具体性**: 実際のファイルパス・クラス名・関数名を使用。
- **Mermaid 構文**: 有効な Mermaid を使用。
- **DRY**: 情報重複を避ける。
- **リンク活用**: 詳細はファイルリンクで参照し、全文貼付を避ける。

## ✅ Final Check

終了前に必ず確認する:

- [ ] すべての todo が完了になっている。
- [ ] 8 文書すべてを生成/更新した。
- [ ] プレースホルダが残っていない。
- [ ] 図が有効な Mermaid 構文である。
