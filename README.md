# QA Automation Engineer Challenge  
** Automation Framework to solve Trello Challenge  **

## Requirements
  
You will need Windows or Linux OS
[Open JDK 12](https://jdk.java.net/12/)
[Maven 3](https://maven.apache.org/download.cgi) 
Chrome or Firefox Browser (last version)

 Clone project from [Gitlab](https://gitlab.com/)  
```  
$ git clone https://gitlab.com/git_clobo/chalhoub-test.git  
```  
### What is included?

Chrome or Gecko driver.
TestNG and Rest Assured, Reports and Logs.
Hamcrest, JsonPath, Page Object Pattern.
Data independent file (DATA_CASES.json)

### Config Data
Independent data file can be configured with new users (Json objects) by adding more data and the script will execute as many users as it has.
``` json
{
  "Cases": [
    {
      "caseID": "CHALLENGE",
      "USER": "automation.tae@gmail.com",
      "PASS": "TrellAuto",
      "FullName": "Cesar",
      "BASE_URI": "https://api.trello.com/1/",
      "KEY": "ac5eb725096fb0af97e78327e727cee8",
      "TOKEN": "b1d5279fae96a36eb72e0210f379c553dcaabf24dc2853d45368565de35afb54"
    },
    {
      "caseID": "CHALLENGE_2",
      "USER": "other_user@gmail.com",
      "PASS": "otherPass",
      "FullName": "Other",
      "BASE_URI": "https://api.trello.com/1/",
      "KEY": "ac5eb725096fb0af97e78327e727cee8",
      "TOKEN": "b1d5279fae96a36eb72e0210f379c553dcaabf24dc2853d45368565de35afb54"
    }
  ]
}
```  
### Console commands
Execute UI mode
```  
$ mvn test  
```  
  Execute headless mode
```  
$ mvn test -DheadlessM=y
```
### Screenshots
[Click to see video](https://gitlab.com/git_clobo/chalhoub-test/blob/master/CHALHOUB_CASE.mp4)


Developed by [Cesar Lobo](https://www.linkedin.com/in/cesar-lobo/)

