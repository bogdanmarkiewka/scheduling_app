# Author: Bogdan Markiewka

# Description

This is a Maven project using Spring Boot, created for the purpose of an assignment.

## Prerequisites

Before running this application, you need to have the following installed on your machine:

- Git
- Java 17
- Maven
- Docker

## Setting Up

1. Clone the repository:

```bash
git clone https://github.com/bogdanmarkiewka/scheduling_app.git
cd scheduling_app
mvn clean install
```

## Running the Application

### Local Environment

To run the application in a local environment, use the following commands:

1. run database
- make sure your docker server is up and running

```
docker compose up
```

2. run the app

```
mvn spring-boot:run
```

### Tests

To run tests, you can execute the following command:

```
mvn test
```

- unit tests
```
com/bogdanmarkiewka/sassignment
```

## API

All available request are collected in file:

```
src/test/http_requests.http
```
