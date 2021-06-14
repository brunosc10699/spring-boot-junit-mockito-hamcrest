# A simple REST API to manage students

First of all, I would like to thank all the employees of [Digital Innovation One](https://web.digitalinnovation.one), represented by Mr. Iglá Generoso and the entire software developer community. Thanks also to [Prof. Rodrigo Peleias](https://github.com/rpeleias), who inspired me and helped me in this work.



#### Objective

Build a simple Rest API to manage students applying TDD (Test Driven Development)



#### Stack

Java 11

Spring Boot version 2.5.1

- Project dependencies
   - WEB
   - Validation
   - H2 Database
   - JPA
   - Lombok
   - Mockito Core - version 3.11.0
   - Hamcrest All - version1.3
   - JUnit Jupiter - version 5.7.2

Gradle

Intellj IDEA Community Edition

Postman

GIT / GITHUB (obviously rsrsrs)



#### TDD

Tests for all service and resource methods 



#### Entity "Student"

I used lombok to write less and produce more

JPA annotations



#### Layers

 - Resource

   	- returning ResponseEntity
   	- JSON

 - Service

   	- Custom Exceptions

 - Repository

    - @Transactional(readOnly=true)

      Query Method



#### DTO (Data Transfer Object)

Attribute validation was here

#### 