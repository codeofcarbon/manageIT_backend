# ManageIT

A project management tool that supports the agile methodology (modeled on Jira).

## Table of contents

* [Overview](#overview)
* [Technologies](#technologies)
* [How to start](#how-to-start)

## Overview
SDA course final project - a program modeled on the Jira management tool, supporting agile work methodology. 


It is a group graduate project carried out at the final stage of the SDA course. Working on a group project, and thus
the opportunity to work in a team while creating the application allowed us to take a broader look at the problems being solved, 
and above all, it allowed us to learn more about working in the gitflow system. 
The project is based on MVC architectural design pattern and implements a REST web service structure divided into the back and front-end parts.

## Technologies

* JAVA 11
* Spring Boot 2.5.2
* Lombok 1.18.20
* Junit5 5.7.2
* AssertJ 2.2.10
* Mockito 3.9
* Rest Assured 4.4
* MySQL 8.0.25

## How to start

```
git clone https://github.com/codeofcarbon/manageIT_backend.git
```

### After you clone repository

You need to create the appropriate tables in database with help of
*database_preparation.sql* (./src/main/resources)
and add credentials in *application.properties* file

### Once you have prepared database

* Run command line in the project directory and type following command:

```
mvn clean compile package && cd target && java -jar ManageIT-0.7.jar
```
