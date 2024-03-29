# PlaygroundL

This is a playground for testing AI such as OpenAI and Google Palm2.  
Please refer to the documentation for each API: [OpenAI Documentation](https://platform.openai.com/docs/introduction) and [Google Documentation](https://developers.generativeai.google/api/python/google/generativeai).

## Environment
It runs on Windows 11 environment.  
JAVA 17 is required for the application to run.

The application has been tested in an environment where [Amazon Corretto 17 JDK](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html) is set in JAVA_HOME.  

## Development Environment Setup
Download and install Google Palm2 SDK to the local repository, otherwise the build will not pass.  
Please run the following command:
```
> gradlew.bat downloadPalm2BetaSDK 
```

## OSS
Open-source software used in this application.

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
- org.jsoup:jsoup  
  [The MIT License](https://github.com/jhy/jsoup/blob/master/LICENSE)
- com.google.api-client  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
- com.google.apis:google-api-services-customsearch  
  [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)

This document was translated using OpenAI.
