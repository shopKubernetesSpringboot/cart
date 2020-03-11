# Getting Started

## Description

Fully reactive shop cart REST API.

### Prerequisites
- Enable annotation processors (in your IDE for lombok)

### Architecture
- Reactive Layers architecture (spring-framework reactor).
- Java non-blocking functional programming: reactor+streams.
- REST API's with Spring-WebFlux.
    - Using [Functional Programming Model](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html#_functional_programming_model)

Notes:
- BeanValidation implementation.
- Reactive junit tests (of course).
- No security implementation at all.
- No persistence implementation.
- Sonar gradle plugin
    - you can run sonarqube gradle task if you have sonar installed on localhost:9000
    - or see the [Sonarcloud.io](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart) dashboard for this project.
  
### Run
#### Spring boot application
- Run App as a spring-boot app:
    - command line: `gradlew :bootRun`
    - intellij: right button on `App.java` & Run...
#### With docker

    docker build -t techtests/shopcart .
    docker run -p 8080:8080 -t techtests/shopcart

### Try
#### With curl

    curl -v -b cookies.txt -c cookies.txt -d '{"item": {"id": 1,"name": "product4"}}' -H 'Content-Type: application/json' http://localhost:8080/cart/add
    curl -v -b cookies.txt -c cookies.txt -v http://localhost:8080/cart/list
#### With postman
- Use `postman_collection.json` (importing the json file in Postman client):
    - `add`: to add an item to the cart
    - `list`: to see all items in the cart

# Initial setup reference

## Travis & sonarcloud setup

Create a token for this project: https://sonarcloud.io/account/security/

Encrypt key (in project folder)
    
    travis encrypt --pro <keyCreatedInSonarCloud>
    