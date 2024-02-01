# Spring Boot store database REST API implementation
Simple REST API for managing and retrieving information from a relational database using HTTP requests. Project created to learn the basics of Spring Boot. The project also contains basic integration tests.
## Overview
In this section, the basics of the created project are described.
### Functionality
Currently the project allows for basic CRUD operations on each database entity. Project was created with modularity in mind which allows to easily swap implementations for all application layers.
### Technologies
This project uses the following technologies:
- Spring Boot - base of the project;
- Spring Web - creation of REST controllers;
- Spring Data JPA (on top of Hibernate) - data access;
- PostgreSQL Driver - communication with database;
- H2 Database - creation of an in memory database used in the testing enviroment;
- Lombok - boilerplate code automatic generation (constructors, getters, setter, etc.).
### Database
The database that is accessed during runtime is located in a Docker container that uses the official PostgreSQL image. The database can be described by the following diagram:

![postgres - database](https://github.com/rdelgiudi/spring-boot-restapi-store/assets/83218453/962cfa7e-7e4a-482f-926b-993c57a58d96)

## Avaiable endpoints

### Endpoints avaiable for each entity (CRUD operations)
```
POST */{entity} - creates new Entity (mapped from DTO)
GET */{entity} - lists all Entities (mapped to DTO)
GET */{entity}/{id} - returns Entity (mapped to DTO) whose id equals the path variable "id"
PUT */{entity}/{id} - fully updates Entity (mapped from DTO) whose id equals the path variable "id"
PATCH */{entity}/{id} - partially updates Entity (mapped from DTO) whose id equals the path variable "id"
DELETE */{entity}/{id} - deletes Entity (mapped from DTO) whose id equals the path variable "id"
```
