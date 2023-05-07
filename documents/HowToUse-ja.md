Playground の使い方
===
OpenAI の Chat API を呼び出して、AIを試すための Playground です。  
Chat API については[公式ドキュメント](https://platform.openai.com/docs/guides/chat)を確認してください。  

# 動作環境
アプリの動作には JAVA 17 が必要になります。  

JAVA_HOME に Amazon corretto 17 の JDK が設定された環境で動作を確認しています。  
https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html  

# 事前準備
[OpenAI](https://openai.com/) の API KEY を取得する（有料）必要があります。  
1. OpenAI の アカウントを作成    
   認証時にショートメッセージが送られるので、SMSを受け取れる電話番号が必要になります。
2. Manage account の Billing で Payment methods を登録  
   支払いに使うクレジットカードなどを登録します。
3. API keys で API Key を発行  
   発行時しか表示されないので、安全な場所に保存してください。
4. Billing の Usage limits で上限を設定

# ファイルの説明
- run.bat  
  アプリを起動するための Windows Batch ファイル。  
- PlaygroundL-xxx.jar  
  アプリ本体の jar ファイル。  
- playgroundApp.properties  
  アプリの設定ファイル。アプリにより自動生成されます。  
- chat.properties  
  CHAT 関連の設定ファイル。アプリにより自動生成されます。
- image.properties  
  IMAGE 関連の設定ファイル。アプリにより自動生成されます。
- chatPrompt ディレクトリ  
  プロンプトのサンプルです。  
  ここに置かれたファイルは CHAT SYSTEM の QUICK LOAD から呼び出せます。  
- chatMessage ディレクトリ  
  ユーザメッセージのテンプレートサンプルです。  
  ここに置かれたファイルは CHAT MESSAGE の QUICK LOAD から呼び出せます。
- log ディレクトリ  
  アプリのログファイル。アプリにより自動生成されます。   

# アプリの使い方

## アプリの起動
1. run.bat を起動  
2. SETTINGS の OPENAI_API_KEY に API Key を設定し、Saveを押す  
   セキュリティのため、Saveすると入力欄は消えます。  

## CHAT 機能の使い方
1. BottomBar で CHAT を選択します  
2. SYSTEM に プロンプト を入力します  
   LOAD SYSTEM PROMPT を押下することで、ファイルから読み込むこともできます。  
3. USER の CONTENT に ユーザのメッセージを入力します  
4. SUBMIT を押下すると ASSISTANT の応答が表示されます  

## IMAGE 機能の使い方

### 画像生成
1. BottomBar で IMAGE を選択します  
2. PROMPT を英語で入力します  
3. CREATE を押下して数秒待つと、画像が表示されます  
4. 生成された画像はログに出力されています  

### 画像編集
1. 生成された画像のリストから編集したい画像を選択します
2. 編集したい画像の上をドラッグすることで、AIに編集してほしい領域を塗りつぶします
3. 編集してほしい内容を PROMPT に英語で入力します
4. EDIT を押下して数秒待つと、画像が表示されます
5. 生成された画像はログに出力されています

### 画像バリエーション作成
1. 生成された画像のリストから編集したい画像を選択します
2. VARIATION を押下して数秒待つと、画像が表示されます
3. 生成された画像はログに出力されています

# GPT-4
OpenAI の API で GPT-4 は限定メンバーのみ公開されています。  
GPT-4 を使うためには [waitlist](https://openai.com/waitlist/gpt-4-api) に登録する必要があります。  

# OSS
本アプリが利用している OSS。  

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
