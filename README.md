# M2M
This is a microservice cutting tool. This tool can help you to divide your service. Our goal is to provide a platform that is easy to extend. You can easily implement your ideas on this platform, and conduct micro-service segmentation.

# Introduction
The process is divided into three steps, info collection, partition and evaluation. We provide some implementations by default. 
- Info collection: 
  - static java source code analysis
  - dynamic java call information
  - git commit information
- Partition: 
  - Louvain
  - MST-Cluster
- Evaluation: 
  - Metrics

# Use Flow
 ![process](/doc/images/process.png)
 
# Task_Process
Each function (Info collection, Partition, Evaluation) is run as a task. All output is encapsulated as Infos. And input consists of the user's form input and infos (from other task output). 
- **UI:** The user enters a parameter or selects the desired Info.
- **Instantiate Form Data:** Convert the form data entered by the user. For example, Convert the File address to a File object.
- **Instantiate Infos:** Convert the Info Id selected by the user to specific Info data.
- **FunctionManager:** The specific FunctionService is called based on the selected function name. The colored rectangles represent each FunctionService.
  - **Static Java Info Collection:** Analyze the jar package using ASM to obtain classes, methods, and their dependencies.
  - **Pinpoint Plugin Info Collection:** Automatically generate plug-ins from class information. The plug-in is used to tell which information pinpoint collects.
  - **Pinpoint Dynamic Java Info Collection:** Pinpoint acquisition target program operating time dynamic data.
  - **Git Commit Info Collection:** Get commit data from git logs to establish relationships between classes.
- **Info Dao Manager:** The corresponding InfoDao is called based on the type of the Info output.
- **Info Dao:** Persistent operation for Info.

 

 ![task_process](/doc/images/task_process.png)

# Module
- core
  - This module defines the basic data types and functional interfaces in the system.
- plugin
  - This module manages all FunctionServices using serviceloader.
- app
  - Provide interfaces to the front end.
  - Implement some InfoDao.
  - Implemented the running process of the task.
- web
  - Front End.

# Install
- Environment
  - App: 
    - JDK8 
    - MAVEN 3.6.0
    - MYSQL 5.7
  - web:
    - Node 10.16.0
    - NPM 6.9.0
  - other:
    - Pinpoint Plugin Info Collection: Gradle 5.4.1
    - Pinpoint Dynamic Java Info Collection: Pinpoint Tool (https://github.com/Flappyboy/pinpoint  The official tool does not provide an interface, so you need to use our version, which adds an interface to get data), Installation Instructions (http://naver.github.io/pinpoint/1.8.1/installation.html)
    
- database: mysql, run sda-app/src/main/resources/db/schema.sql
- modify sda-app/src/main/resources/application-sda.properties
````
sda.path=/tmp/sda
````
- modify sda-app/src/main/resources/application-dev.yml
````
server:
    port: 8019
  
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/sda?useUnicode=true&characterEncoding=utf-8
      driver-class-name: com.mysql.jdbc.Driver
      username: root
      password: root
  
  logging:
    level:
      cn:
        edu:
          nju:
            software:
              sda:
                app:
                  dao: debug
  sda:
    properties: application-sda.properties
````
- run app
  - You can run sda-app/src/main/java/cn/edu/nju/software/sda/app/Application.main in IDE.
  - You can run "mvn clean install -Dmaven.test.skip=true", and run "java -jar sda-app/target/sda-app-1.0.0.jar"
- In the sda-web   
  - npm install
  - npm run start
- Visit http://localhost:4444

# 1. Usage
> This chapter introduces the basic functions and usage of M2M.

## 1.1 App Management
> Add, delete, modify application information.

 ![App](/doc/images/usage/app.png)
 
## 1.2 Info Collection
> Click the Detail button of the application to enter the Detail page, select NewInfo, and the corresponding information can be added as the basis for division.

![Info](/doc/images/usage/info1.png)
 
![Info](/doc/images/usage/info2.png)
 
## 1.3 Partition
> Select Partition, you can carry out corresponding Partition operation.

![Partition](/doc/images/usage/partition1.png)
 
![Partition](/doc/images/usage/partition2.png)
 
> Visually view the partition results and modify the partition results.This includes adding and deleting services and moving classes from one service to another in bulk.The current partitioned state can be saved by a copy operation.

![Partition Detail](/doc/images/usage/partition3.png)
 
![Partition Detail](/doc/images/usage/partition4.png)
 
![Partition Detail](/doc/images/usage/partition5.png)
 
## 1.4 Evaluation
> The corresponding assessment results can be viewed below each of the partition results.

![Evaluation](/doc/images/usage/evaluation1.png)

> Batch selection and partitioning results can also be used for comparison of evaluation data.

![Evaluation](/doc/images/usage/evaluation2.png)

## 1.5 Task query

> Check the status of the task execution, including Doing, Complete, Error.

![Task](/doc/images/usage/task.png)




