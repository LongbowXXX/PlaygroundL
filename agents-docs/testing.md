<!-- This document is generated and updated by .github/prompts/doc-sync.prompt.md -->

# テスト方針

## テストレイヤー

- ユニットテスト: `kotlin-test-junit5` + JUnit 5 を各モジュールで使用。
- コルーチン: `kotlinx-coroutines-test` を用いて非同期をテスト (openai/search/utils など)。
- UI: Compose Desktop UI の自動テストは未整備。主要ロジックは ViewModel/クライアント層で検証する。
- 履歴/永続化: Realm の読み書きは小さなリポジトリテストを追加することを推奨。

## 実行方法

```bash
./gradlew test        # 全モジュールのテスト
./gradlew app:jvmTest # アプリモジュールのみ (JUnit Platform)
```

## 追加の推奨

- ストリーミング処理 (Chat/Image 翻訳) にはモックレスポンスを用いたフロー結合テストを検討する。
- 関数プラグイン呼び出しは例外系 (見つからないプラグイン、失敗時) もカバーする。
- 外部 API キー依存のテストはテストプロファイルで無効化し、契約テストをモックで代替する。
