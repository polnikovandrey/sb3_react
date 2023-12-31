# Features:

## Infrastructure (Deploy and Orchestration):

    Ansible, Docker [+ Compose, + Swarm], Vagrant, VirtualBox

## Services:

    [Backend] (+ MVC Frontend): Form Login + Signup User + Edit User Profile + List & CRUD Users by Admin.
    [Frontend] (+ React): JWT Login + Signup User + Edit User Profile + List & CRUD Users by Admin.
    [MySQL]
    [Nginx]

## \[Backend\] (+ MVC Frontend):

    Spring Boot 3 [Bootstrap, Spring Data JPA, Flyway, Java 20, JUnit, Lombok, Maven, Mockito, MySQL, Spring Security 6, Thymeleaf, Validation, Web].

## React Frontend:

    React [Bootstrap, JWT, Redux/Redux Toolkit, TypeScript]

# Notes:

## Database init script location:

    services/backend/src/main/resources/db/script/mysql-setup.sql

## MVC Frontend location:

    localhost:8080

## React Frontend location:

    localhost:3000 (development), localhost:8081 (production).

## Credentials:

    admin:admin

# Todos:

    + Bootstrap themes for Both MVC & React frontend apps
    + Health-Check for services deployment with Ansible:
       * MySQL <- [ Backend ]
       * Backend <- [ Frontend ]
       * Frontend <- [ Nginx ]
    + User Avatar Upload feature: 
        * Image Converter microservice
        * RabbitMQ for Image Converter <-> Backend communication
        * WebSocket for Backend -> React Frontend communication
