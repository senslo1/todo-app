# todo-backend-springboot
This TODO app backend is written in Spring Boot and uses SpringMVC, SpringJPA, Kotlin, Maven, Logback, Liquibase, and Swagger Request Validator.
For now the data is persisted using an in-memory H2 database, but I am considering using Postgres in the future.

## Requirements
* JDK 1.8 or higher
* Maven

## Building the project
Build the app using the command below:
```bash
$ ./mvnw clean install
```

Then run it using:
```bash
$ ./mvnw spring-boot:run
```

## Usage
The app has an endpoint serving an OpenAPI spec that tells you all you need to know about the app's endpoints
and the schemas of the requests and responses.
When running the app, you can get the OpenAPI spec by running the following command:
```bash
$ curl -X GET http://localhost:5000/api-doc
```
In the future I may attempt to create a SwaggerUI endpoint for simplicity.
