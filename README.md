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
 ![Alt text](/doc/images/process.png)
 
# Task_Process
Each function is run as a task. All output is encapsulated as Infos. And input consists of the user's form input and infos

 ![Alt text](/doc/images/task_process.png)

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
- database: mysql run sda-app/src/main/resources/db/schema.sql
- modify sda-app/src/main/resources/application-sda.properties
  -
``sda.path=/tmp/pinpoint``
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
- run sda-app/src/main/java/cn/edu/nju/software/sda/app/MyApplicationRunner.main
- In the sda-web   npm run start
- Visit http://localhost:4444

