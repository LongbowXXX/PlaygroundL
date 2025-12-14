<!-- この文書は AI により生成されました -->

# 仕様記述ガイド: XML + Gherkin + Mermaid

`specification.template.md` で用いる構造化仕様の考え方と使い方を説明します。

## 1. なぜこの組み合わせか

従来 Gherkin は「自動テストの中間言語」とみなされてきましたが、AI コーディングでは価値が大きく変わります。

### AI にとっての「究極のプロンプト」

LLM は構造化テキストを好みます。あいまいな自然文より、Given/When/Then で区切られた Gherkin の方が文脈を正確に理解し、高精度なコード生成ができます。Gherkin を書く行為自体が **Chain-of-Thought** として推論精度を高めます。

### 図を「コード」として扱う

複雑なフローや状態遷移を文章だけで伝えるのは困難です。**Mermaid** 記法を使えば図をテキストとして表現でき、Copilot が分岐や状態を読み取って実装・テストに反映できます。

### 乖離防止 (リビングドキュメント)

Mermaid の図と Gherkin の振る舞いを同じ仕様ファイルに置くことで、コードと一緒にバージョン管理され、陳腐化を防ぎます。

## 2. コンポーネントガイド

### XML コンテナ

セクションを囲むタグで、AI パーサーが情報種別を特定しやすくします。

- `<requirements>`: ビジネス要件やユーザー要求
- `<technical_design>`: 技術仕様
- `<gherkin>`: BDD シナリオブロック
- `<mermaid_diagram>`: 図表ブロック

### Gherkin (振る舞い仕様)

受け入れ条件を記述します。

- **Feature**: 高位の機能
- **Scenario**: 具体例
- **Given/When/Then**: 前提 / 行動 / 結果

### Mermaid (可視化仕様)

アーキテクチャやロジックフローを図示します。

- **Flowchart**: 分岐や手順
- **Sequence Diagram**: 非同期処理や API 連携
- **State Diagram**: 状態遷移 (例: 注文ステータス)

## 3. 複雑さへの対処

分岐が多いと単純な Gherkin 羅列では読みにくくなります。次の 3 パターンで整理します。

### パターン A: `Rule` で構造化 (Gherkin 6)

バリエーションが多いビジネスロジックではシナリオを平坦に並べず、`Rule` でグルーピングします。

**悪い例 (平坦):**

```gherkin
Scenario: 20 歳未満は購入できない
Scenario: 20 歳以上は購入できる
Scenario: 会員は割引
Scenario: 非会員は定価
```

**良い例 (Rule で整理):**

```gherkin
Feature: 商品購入

Rule: 年齢制限
  Scenario: 20 歳未満は購入できない
  Scenario: 20 歳以上は購入できる

Rule: 価格
  Scenario: 会員は割引
  Scenario: 非会員は定価
```

_効果: ロジックのまとまりを AI が理解し、条件分岐ミスが減る。_

### パターン B: ビジュアル BDD (Mermaid)

循環フローや非同期・状態機械ではテキストだけでは足りません。

- **戦略**: Gherkin はハッピーパスと代表的なエラーのみを書き、全網羅は Mermaid 図に委ねる。
- **AI への指示**: 「エッジケースは Mermaid 図を元に実装すること」と明記する。

### パターン C: 宣言的ステップ (Screenplay)

命令的で細かい UI 操作は避け、**何をしたいか** に集中します。

**悪い例 (命令的):**

```gherkin
When I click the ID field
And I type "user"
...（続く細かい操作）
```

**良い例 (宣言的 / Screenplay):**

```gherkin
When I login as a standard user
```

_効果: 仕様を UI 実装から切り離せる。`@Developer` などのエージェントが高レベル意図を具体的なコードに落とし込める。_

# Specification Guidelines: XML + Gherkin + Mermaid

This document explains the philosophy and usage of the structured specification format used in `specification.template.md`.

## 1. Philosophy: Why this combination now?

Traditionally, Gherkin was treated merely as an "intermediate language for automated tests". However, in the era of AI coding, its value has changed dramatically.

### The "Ultimate Prompt" for AI

LLMs (Large Language Models) prefer structured text. Compared to ambiguous natural language specifications, Gherkin (segmented by Given/When/Then) allows the AI to accurately understand context and generate high-precision code. The process of writing Gherkin itself functions as **Chain-of-Thought** prompting, enhancing the AI's reasoning accuracy.

### Understanding "Diagrams" as Code

Explaining complex flows or state transitions with text alone is difficult. By using **Mermaid** notation, diagrams can be written as "text (code)". GitHub Copilot understands Mermaid syntax, enabling it to read visualized logic (branching and state transitions) and reflect it in implementation code and test cases.

### Preventing Divergence (Living Documentation)

By co-locating Diagrams (Mermaid) and Behavior (Gherkin) within the specification (Feature file), the document is version-controlled alongside the code, preventing it from becoming stale.

## 2. Component Guide

### XML Containers

Use these tags to wrap content sections. This helps the AI parser locate specific information types.

- `<requirements>`: Business rules and user needs.
- `<technical_design>`: Engineering specifications.
- `<gherkin>`: Specific block for BDD scenarios.
- `<mermaid_diagram>`: Specific block for visual diagrams.

### Gherkin (Behavioral Specs)

Used for Acceptance Criteria.

- **Feature**: High-level functionality.
- **Scenario**: Concrete examples of behavior.
- **Given/When/Then**: Preconditions, Actions, Outcomes.

### Mermaid (Visual Specs)

Used for Architecture and Logic Flow.

- **Flowchart**: Decision trees, process flows.
- **Sequence Diagram**: Async processes, API interactions.
- **State Diagram**: Lifecycle management (e.g., Order statuses).

## 3. Guide for Handling Complexity

When specifications become complex (many branches, deep logic), simple Gherkin lists become unreadable. Use these three patterns to manage complexity.

### Pattern A: Structure with `Rule` (Gherkin 6.0)

For complex business logic with many variations, do not list Scenarios flatly. Use the `Rule` keyword to group them.

**Bad (Flat List):**

```gherkin
Scenario: User under 20 cannot buy
Scenario: User over 20 can buy
Scenario: Member gets discount
Scenario: Non-member pays full price
```

**Good (Structured with Rule):**

```gherkin
Feature: Product Purchase

Rule: Age Restriction
  Scenario: User under 20 cannot buy
    ...
  Scenario: User over 20 can buy
    ...

Rule: Pricing
  Scenario: Member gets discount
    ...
```

_Benefit: AI understands the logical grouping, reducing conditional logic errors._

### Pattern B: Visual BDD (Mermaid)

For cyclic flows, complex asynchronous processes, or state machines, text is insufficient.

- **Strategy**: Use Gherkin only for the "Happy Path" and "Representative Errors".
- **Full Logic**: Delegate the strict exhaustive logic to a Mermaid diagram.
- **Instruction to AI**: "Implement edge cases based on the Mermaid diagram."

### Pattern C: Declarative Steps (Screenplay Pattern)

Avoid imperative, low-level UI details in specifications. Focus on User Intent (**What**), not Implementation Details (**How**).

**Bad (Imperative):**

```gherkin
When I click the ID field
And I type "user"
And I click the password field
And I type "pass"
And I click the Login button
```

**Good (Declarative - Screenplay Style):**

```gherkin
When I login as a standard user
```

_Benefit: Decouples specification from UI implementation. Agents like `@Developer` are excellent at translating high-level intents ("login") into specific code (Playwright/Puppeteer actions)._
