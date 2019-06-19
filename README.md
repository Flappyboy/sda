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
Each function is run as a task. All output is encapsulated as Infos. And input consists of the user's form input and infos

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
- database: mysql run sda-app/src/main/resources/db/schema.sql
- modify sda-app/src/main/resources/application-sda.properties

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




