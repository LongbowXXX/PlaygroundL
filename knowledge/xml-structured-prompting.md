<!-- この文書は AI により生成されました -->

# XML + Markdown 構造化プロンプトガイド

XML タグと Markdown を組み合わせ、機械にも人にも読みやすいプロンプトを書く方法を示します。

## 1. XML + Markdown の利点

XML は境界を明示し、Markdown は意味と可読性を与えます。両者を組み合わせることで、構造化と表現力を両立できます。

### なぜ効くのか

1. **XML は「入れ物」を定義**: どこに何が入るかを示し、文脈の混入を防ぐ (`<instruction>`, `<context>` など)。
2. **Markdown は「中身」の意味付け**: 強調、箇条書き、階層を示し、解釈を助ける。

> XML が封筒、Markdown が中の手紙と考える。

## 2. メリット

- **境界の明確化 (XML)**: 指示とデータの混同を防ぐ。
- **豊かな意味付け (Markdown)**: 見出しやリストで長文を整理できる。
- **二重の読みやすさ**: AI はまず XML 構造を解析し、人間は Markdown で読みやすい。

## 3. よく使う XML タグ

| 区分         | タグ                 | 目的                 |
| :----------- | :------------------- | :------------------- |
| **Meta**     | `<system>`           | システムの絶対ルール |
|              | `<role>`             | ペルソナ設定         |
| **Data**     | `<context>`          | タスクに必要な背景   |
|              | `<documents>`        | 参照資料や検索結果   |
|              | `<example>`          | Few-shot 例示        |
| **Control**  | `<instruction>`      | 中核タスクの指示     |
|              | `<constraints>`      | 禁則や制約           |
| **Thinking** | `<thinking>`         | 思考スペース (CoT)   |
| **Agentic**  | `<workflow>`         | 具体的な手順やループ |
|              | `<stopping_rules>`   | 安全停止条件         |
|              | `<plan_research>`    | 調査段階の指示       |
|              | `<plan_style_guide>` | 出力スタイルの指示   |
| **Output**   | `<output>`           | 最終回答を入れる箱   |

## 4. 実用例

```xml
<system>
    You are a helpful assistant specialized in summarizing technical documents.
</system>

<context>
    <documents>
        <document index="1">
            ### Spec v1.0
            (Content of the first document...)
        </document>
    </documents>
</context>

<instruction>
    ### Your Task
    Summarize the provided documents in **3 bullet points**.

    ### Process
    1. **Analyze**: Look for key themes in the `<thinking>` block.
    2. **Draft**: Write the summary in the `<output>` block.
</instruction>

<constraints>
    - **No Jargon**: Use simple language.
    - **Length**: Keep each point under 20 words.
</constraints>
```

**期待される出力**

```xml
<thinking>
    The document discusses X, Y, and Z. The key points are...
</thinking>

<output>
    - Point 1 summary.
    - Point 2 summary.
    - Point 3 summary.
</output>
```

## 5. 実例

エージェント向けタグの実利用例として **VS Code Copilot Plan Agent** を参照してください:

- https://github.com/microsoft/vscode-copilot-chat/blob/main/assets/agents/Plan.agent.md
