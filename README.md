# Shop cart

[![Build Status](https://travis-ci.com/davidgfolch/shopCart.svg?branch=master)](https://travis-ci.com/davidgfolch/shopCart)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=bugs)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=coverage)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=com.dgf%3AshopCart&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart)
## Description

Fully reactive shop cart REST API micro-service.  Also see the related [front-end ReactJs project](https://github.com/davidgfolch/shop-web)

- Allows to add an Item at once to the Cart.  Keeps items in the Cart in the current session.

- Allows to list cart's items.

### Prerequisites
- Enable annotation processors (in your IDE for lombok)

### Architecture
- Reactive Layers architecture (spring-framework reactor).
- Java non-blocking functional programming: reactor+streams.
- REST API with Spring-WebFlux.
    - Using [Functional Programming Model](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html#_functional_programming_model)

Notes:
- BeanValidation implementation.
- Reactive junit tests.
- Integration test.
- Basic auth security implementation.
- Sonar gradle plugin
    - you can run Sonarqube gradle task if you have sonar installed on localhost:9000
    - or see the [Sonarcloud.io](https://sonarcloud.io/dashboard?id=com.dgf%3AshopCart) dashboard for this project (through travis-ci integration).
  
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

    curl -v -b cookies.txt -c cookies.txt -u user:user -d '{"item": {"id": 1,"name": "product4"}}' -H 'Content-Type: application/json' http://localhost:8080/cart/add
    curl -v -b cookies.txt -c cookies.txt -u user:user http://localhost:8080/cart/list
#### With postman
- Use `postman_collection.json` (importing the json file in Postman client):
    - `add`: to add an item to the cart
    - `list`: to see all items in the cart

# Initial setup reference

## Travis & SonarCloud setup

Create a token for this project: https://sonarcloud.io/account/security/

Encrypt key (in project folder)
    
    travis encrypt --pro <keyCreatedInSonarCloud>

# Microservice
## Size optimization
### With spring-boot-thin-launcher
https://github.com/spring-projects-experimental/spring-boot-thin-launcher

Original/thin generated jars:

    -rwxrwxrwx 1 slks slks 19683502 mar 15 09:13 shopCart-0.0.1-SNAPSHOT.jar
    -rwxrwxrwx 1 slks slks 25610 mar 15 16:55 build/libs/shopCart-0.0.1-SNAPSHOT.jar

Original/thin docker image (techtests/shopcart): 125MB/105MB

**Using `FROM openjdk:8-jre-alpine` (instead of `FROM openjdk:8-jdk-alpine`) final size: 84.9MB**

### Attempt to minimize docker image size with jdk11
This is **not a solution** as it generates a bigger image

    docker build -f Dockerfile-optimized -t techtests/shopcart-optimized .
    docker images | grep techtests/shopcart 

Generated images:

    $ docker images | grep techtests/shopcart
    techtests/shopcart                               latest               36234d830c12        14 hours ago        125MB
    techtests/shopcart-optimized                     latest               3e22c1446dd8        15 hours ago        151MB

# TODO
- Full micro-service architecture.

