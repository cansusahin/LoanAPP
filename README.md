# **Loan APP**

*Loan API for a bank so that their employees can create, list and pay loans for their customers. Also customers can manage their loans & payments.*

## **Tech Stack**

1. Core Framework
   
- Spring Boot:

-- spring-boot-starter-parent (version 3.4.5)

-- spring-boot (version 3.4.5)

-- spring-boot-starter-web (for web development with Spring MVC)

-- spring-boot-starter-data-jpa (for JPA support)

-- spring-boot-starter-validation (for validation support)

3. Testing Frameworks
   
JUnit 5:

junit-bom (version 5.10.1)

spring-boot-starter-test (includes JUnit, Mockito, etc., for testing)

mockito-core (for mocking dependencies in tests)

spring-security-test (for security-related tests)

4. Database & Persistence
   
H2 Database:

com.h2database:h2 (version 2.3.232) – an in-memory database for testing or development purposes

Spring Data JPA:

spring-boot-starter-data-jpa (for interacting with databases using JPA)

5. Security
   
Spring Security:

spring-security-core (version 6.4.5)

spring-security-web (version 6.4.5)

spring-security-config (version 6.4.5) – configuration for Spring Security

spring-security-test (version 6.4.5) – test support for Spring Security

6. JSON & Jackson
   
Jackson:

jackson-databind (version 2.19.0)

jackson-core (version 2.19.0)

jackson-annotations (version 2.19.0)

7. OpenAPI Documentation
   
SpringDoc OpenAPI:

springdoc-openapi-starter-webmvc-ui (version 2.8.6) – for generating OpenAPI documentation for REST APIs with Swagger UI integration.

8. Other Libraries
Lombok:

lombok (version 1.18.38) – used to reduce boilerplate code (e.g., getters, setters, constructors).

Jakarta Servlet API:

jakarta.servlet-api (version 6.1.0) – provides the Jakarta Servlet API, which is required for web applications.


## **Summary of Tech Stack**

- Spring Boot (for building web applications)

- Spring Data JPA (for interacting with databases using JPA)

- Spring Security (for securing web applications)

- H2 Database (in-memory database)

- JUnit 5 and Mockito (for testing)

- Lombok (to reduce boilerplate code)

- SpringDoc OpenAPI (for API documentation with Swagger UI)


## **Run The Application**

 ./mvnw spring-boot:run
*Application is running on http://localhost:8081/. Port is defined in application.yaml.*
server.port: 8081

## **User Roles**

-ADMIN
-CUSTOMER

## **Test users**

1- username: admin pass: admin123
2- username: cansusahin pass: cansu123 (customer id 1)
3- username: ozgesahin pass: ozge123 (customer id 2)

## **H2 Database Configuration & Tables**

You can access h2 database from http://localhost:8081/h2-console. Database username is loanAdmin and password loan2025*. Initial customer and user info is added as sql script. It will be loaded while running the app.

*Configuration in application.yaml:*

spring:
  h2:
    console:
      enabled: true
      path: /h2-console
      settings.trace: false
      settings.web-allow-others: false
  datasource:
    url: jdbc:h2:mem:loanapp
    username: loanAdmin
    password: loan2025*
    driverClassName: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    defer-datasource-initialization: true

<img width="336" alt="Screenshot 2025-05-13 at 12 30 13" src="https://github.com/user-attachments/assets/e16bff94-1137-4c69-9793-65f005f7bd44" />

<img width="327" alt="Screenshot 2025-05-13 at 12 30 27" src="https://github.com/user-attachments/assets/2aed5586-1d98-406a-86fe-f933c8a21ce5" />

## **Swagger**

You can access swagger from http://localhost:8081/swagger-ui/index.html#/.
Swagger api doc:

{"openapi":"3.1.0","info":{"title":"OpenAPI definition","version":"v0"},"servers":[{"url":"http://localhost:8081","description":"Generated server url"}],"paths":{"/loan/create":{"post":{"tags":["loan-controller"],"operationId":"createLoan","parameters":[{"name":"customerId","in":"query","required":true,"schema":{"type":"integer","format":"int64"}},{"name":"amount","in":"query","required":true,"schema":{"type":"number"}},{"name":"interestRate","in":"query","required":true,"schema":{"type":"number"}},{"name":"numberOfInstallments","in":"query","required":true,"schema":{"type":"integer","format":"int32"}}],"responses":{"200":{"description":"OK","content":{"*/*":{"schema":{"type":"object"}}}}}}},"/installment/pay":{"post":{"tags":["installment-controller"],"operationId":"payLoanInstallments","parameters":[{"name":"loanId","in":"query","required":true,"schema":{"type":"integer","format":"int64"}},{"name":"paymentAmount","in":"query","required":true,"schema":{"type":"number"}}],"responses":{"200":{"description":"OK","content":{"*/*":{"schema":{"type":"object"}}}}}}},"/loan/list":{"get":{"tags":["loan-controller"],"operationId":"listLoans","parameters":[{"name":"customerId","in":"query","required":true,"schema":{"type":"integer","format":"int64"}},{"name":"isPaid","in":"query","required":false,"schema":{"type":"boolean"}}],"responses":{"200":{"description":"OK","content":{"*/*":{"schema":{"type":"object"}}}}}}},"/installment/list":{"get":{"tags":["installment-controller"],"operationId":"listInstallments","parameters":[{"name":"loanId","in":"query","required":true,"schema":{"type":"integer","format":"int64"}}],"responses":{"200":{"description":"OK","content":{"*/*":{"schema":{"type":"object"}}}}}}}},"components":{}}

## **Postman Collection**

Postman collection is added under project directory, named: Loan APP Collection.postman_collection.json. Much easy to use for testing endpoints. Don't forget to use basic auth!

## **API Endpoints**

- Create Loan --> Method: POST, Path: http://localhost:8081/loan/create
- List Loan --> Method: GET, Path: http://localhost:8081/loan/list
- List Loan Installments --> Method: GET, Path: http://localhost:8081/installment/list
- Pay Loan Installments --> Method: POST, Path: http://localhost:8081/installment/pay
