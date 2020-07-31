# Trello Automation / UI and API  
Automation Framework to solve Trello Challenge

## Requirements
  
You will need Windows or Linux OS
[Open JDK 8](https://jdk.java.net/8/)
[Maven 3](https://maven.apache.org/download.cgi) 
Chrome Browser (last version)

```  
$ git clone git@github.com:cesaralobo/trello-ui-automation.git 
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
      "USER": "your_account@gmail.com",
      "PASS": "pass",
      "FullName": "Cesar",
      "BASE_URI": "https://api.trello.com/1/",
      "KEY": "",
      "TOKEN": ""
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
[Download video](https://github.com/cesaralobo/trello-ui-automation/blob/master/CHALHOUB_CASE.mp4)


