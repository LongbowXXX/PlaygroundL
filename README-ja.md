PlaygroundL
===
OpenAI や Google Palm2 などの、AIを試すための Playground です。  
各API については[OpenAIのドキュメント](https://platform.openai.com/docs/introduction)や[Googleのドキュメント](https://developers.generativeai.google/api/python/google/generativeai)を確認してください。  

# 環境
Windows 11 環境で動作します。  
アプリの動作には JAVA 17 が必要になります。  

JAVA_HOME に Amazon corretto 17 の JDK が設定された環境で動作を確認しています。  
https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html  

# 開発環境セットアップ
Google の Palm2 の SDK をダウロードして、ローカルリポジトリにインストールしないと、ビルドが通りません。  
下記コマンドを実行してください。
```
> gradlew.bat downloadPalm2BetaSDK 
```

# アプリの使い方
[HowToUse-ja.md](documents/HowToUse-ja.md)を参照してください。  

# OSS
このアプリケーションで使用されているOSS。  

- org.jetbrains.compose.desktop:desktop-jvm-windows-x64  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- org.jetbrains.compose.material3:material3-desktop  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- org.jetbrains.kotlin:kotlin-stdlib-common  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- org.jetbrains.kotlin:kotlin-stdlib-jdk8  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- org.jetbrains.kotlinx:kotlinx-coroutines-core  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- org.jetbrains.kotlinx:kotlinx-serialization-json  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- com.squareup.okhttp3:okhttp  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- realm/realm-kotlin  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- io.grpc:grpc-okhttp  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
