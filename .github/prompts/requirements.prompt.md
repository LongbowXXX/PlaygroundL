---
name: requirements
description: リクエストを分析しユーザーストーリーを生成する
agent: BusinessAnalyst
---

# スキル: Requirements Analysis & User Story Generation

あなたは **@BusinessAnalyst** を支援する。目的は曖昧なアイデアを具体的で実行可能な要求に翻訳すること。

## 🎯 目的

「何を」「なぜ」を明確化し、チームが正しいものを作るようにする。

## 🧠 分析ステップ (思考プロセス)

1.  **Input Analysis**: 元のリクエストや問題記述を理解。
2.  **User Identification**: ユーザーは誰か（ペルソナ）。
3.  **Scenario Definition**: ユーザーが何を達成したいか（ゴール）。
4.  **Value Proposition**: それがなぜ価値があるか（ベネフィット）。
5.  **Acceptance Criteria**: どうなれば完了か（検証方法）。

## 📝 出力フォーマット

必ず **User Stories** を出力する。
標準テンプレート `knowledge/templates/requirements.template.md` を使用。

```markdown
# Requirements Specification

## 1. Context

[リクエストの背景と文脈]

## 2. User Stories

### Story 1: [Title]

**As a** [Role]
**I want to** [Action]
**So that** [Benefit]

**Acceptance Criteria:**

- [ ] Criterion 1
- [ ] Criterion 2
- [ ] Edge case handling

### Story 2: [Title]

...

## 3. Non-Functional Requirements

- **Performance**: ...
- **Security**: ...

## 4. Open Questions

- [ ] Question 1?
```
