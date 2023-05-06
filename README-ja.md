PlaygroundL
===
OpenAI の Chat API を呼び出して、AIを試すための Playground です。  
Chat API については[公式ドキュメント](https://platform.openai.com/docs/guides/chat)を確認してください。  

# 環境
Windows 環境で動作します。  
アプリの動作には JAVA 17 が必要になります。  

JAVA_HOME に Amazon corretto 17 の JDK が設定された環境で動作を確認しています。  
https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html  

# アプリの使い方
[HowToUse-ja.md](documents/HowToUse-ja.md)を参照してください。  

<video width="320" height="240" controls>
  <source src="./PLAYGROUND-Movie.mp4" type="video/mp4">
</video>

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
