# reactive
Kotlin PoC with WebFlux

## Testing

To run the tests using embedded gradle:
```console
$ ./gradlew clean test
```

## Running the app

To run the Spring Boot app simply run the command:

```console
$ ./gradlew bootRun
```

## Database

Database parameters must be set, currently using PostgreSQL

To create a database locally, simply run:
```console
$ createdb reactive
```
The configuration file can be found [here](https://github.com/atilla8huno/reactive/blob/main/src/main/resources/application.yml#L3-L4):
```
src/main/resources/application.yml
```
