# How to Use Playground

This is a Playground for testing AI.

## Operating Environment
It runs on Windows 11.  
JAVA 17 is required for the application to run.  

It has been tested in an environment where [Amazon Corretto 17 JDK](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html) is set in JAVA_HOME.  

## Preparations

### OpenAI
You need to obtain an API KEY from [OpenAI](https://openai.com/) (paid).
1. Create an OpenAI account.  
   You will need a phone number that can receive SMS messages for authentication.
2. Register a payment method in Billing under Manage account.  
   Register the credit card or other payment method you will use for payment.
3. Generate an API key under API keys.  
   It is only displayed at the time of generation, so please save it in a secure location.
4. Set the limits under Usage limits in Billing.

### Google generative AI
Register for the waitlist at [Google generative AI](https://developers.generativeai.google/) and, once you have obtained the license, generate an [API Key](https://developers.generativeai.google/products/palm).

### Google Custom Search API
If you want to use OpenAI Chat's function to work with Google search, use the API Key and Search Engine ID of Google Cloud Platform's [Custom Search API](https://developers.google.com/custom-search/v1/overview). will become necessary.

## Description of Files
### Directory with Exe
- PlaygroundL.exe  
  Exe file to launch the application.
- chatPrompt directory  
  Sample prompts. Files placed here can be called from QUICK LOAD in CHAT SYSTEM.
- chatMessage directory  
  Sample user message templates. Files placed here can be called from QUICK LOAD in CHAT MESSAGE.
- chatFunction directory  
  Sample function definitions. Functions defined in files placed here are called from CHAT.

### AppData directory
App configuration data will be created in `<UserDir>/AppData/Roaming/PlaygroundL`.  
- playgroundApp.properties  
  The application's configuration file. It is automatically generated by the application.
- chat.properties  
  Configuration file related to CHAT (Open AI). It is automatically generated by the application.
- discuss.properties  
  Configuration file related to DISCUSS (Google generative AI). It is automatically generated by the application.
- image.properties  
  Configuration file related to IMAGE. It is automatically generated by the application.
- db directory  
  DataBase files used by the application. They are automatically generated by the application.
- log directory  
  Log files of the application. They are automatically generated by the application.

## How to Use the Application

### Starting the Application
1. Start run.bat.
2. Set the API Key in SETTINGS under OPENAI_API_KEY and click Save.  
   For security reasons, the input field disappears when you save it.  

<div style="width: 60%;"><video controls style="width: 100%;" src="images/00-Setup-API-Key.mp4"></video></div>

## How to Use the CHAT Feature

<div style="width: 60%;"><video controls style="width: 100%;" src="images/10-OpenAI-Chat.mp4"></video></div>
<div style="width: 60%;"><video controls style="width: 100%;" src="images/11_OpenAI-Chat-Function-1.mp4"></video></div>
<div style="width: 60%;"><video controls style="width: 100%;" src="images/12_OpenAI-Chat-Function-2.mp4"></video></div>
<div style="width: 60%;"><video controls style="width: 100%;" src="images/15_OpenAI-Chat-Restore-Old-Session.mp4"></video></div>

### Interacting with the ASSISTANT
1. Select CHAT in the BottomBar.
2. Enter the prompt in SYSTEM.  
   You can also load it from a file by clicking LOAD SYSTEM PROMPT.
3. Enter the user's message in USER's CONTENT.
4. Click SUBMIT to see the response from the ASSISTANT.

### Starting a New Session
1. Click NEW SESSION.

### Restoring an Old Chat Session
1. Click RESTORE OLD CHAT...
2. Select the session you want to restore from the list.

## How to Use the DISCUSS Feature
It is similar to CHAT, but only English can be used.  

<div style="width: 60%;"><video controls style="width: 100%;" src="images/20_PaLM2-Discuss-Service.mp4"></video></div>

## How to Use the IMAGE Feature

<div style="width: 60%;"><video controls style="width: 100%;" src="images/30_OpenAI-Image.mp4"></video></div>

### Image Generation
1. Select IMAGE in the BottomBar.
2. Enter the prompt in PROMPT.
3. Click CREATE and wait a few seconds to see the generated image.
4. The generated image is also output in the log.

### Image Editing
1. Select the image you want to edit from the list of generated images.
2. Drag over the area you want AI to edit to fill it.
3. Enter the desired editing content in PROMPT.
4. Click EDIT and wait a few seconds to see the edited image.
5. The generated image is also output in the log.

### Creating Image Variations
1. Select the image you want to edit from the list of generated images.
2. Click VARIATION and wait a few seconds to see the image.
3. The generated image is also output in the log.

## GPT-4
GPT-4 is only available to ChatGPT Plus members through the OpenAI API.  

**This document was translated using OpenAI.**
