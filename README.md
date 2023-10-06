# reactive
Kotlin PoC with Spring WebFlux

### Testing

Summary list of frameworks used in this PoC:
* Kotlin: 1.8.22
* Java: 17
* Gradle: 8.2.1
* Netty: 4.1.97
* Spring Boot: 3.1.4
* Spring Framework: 6.0.2
* Spring WebFlux: 6.0.2
* Spring Data R2DBC: 6.0.2
* Project Reactor: 3.5.10
* Runtime DB Driver (PostgreSQL): r2dbc-postgresql: 1.0.2.RELEASE

Test:
* Spring Boot Test: 3.1.4
* JUnit: 5.9.3
* MockK: 1.13.3 (springmockk: 4.0.2)
* Project Reactor Test: 3.5.10
* MockWebServer: 4.11.0
* Test DB Driver (H2): r2dbc-h2: 1.0.0.RELEASE

### Testing

To run the tests using embedded gradle:
```console
$ ./gradlew clean test
```

### Running the app

To run the Spring Boot app simply run the command:

```console
$ ./gradlew bootRun
```

### Database

Database parameters must be set, currently using PostgreSQL

To create a database locally, simply run:
```console
$ createdb reactive
```

The configuration file can be found [here](https://github.com/atilla8huno/reactive/blob/main/src/main/resources/application.yml#L3-L4):
```
src/main/resources/application.yml
```
