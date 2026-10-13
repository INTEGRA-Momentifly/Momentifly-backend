# Momentifly-backend
## API Documentation

When running the application locally, the API documentation is available at:

http://localhost:8080/swagger-ui

The raw OpenAPI specification is available at:

http://localhost:8080/v3/api-docs


# Set up the database:
Make sure you have a database running.  
Update your credentials in src/main/resources/application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/momentifly_db
spring.datasource.username=your_username  
spring.datasource.password=your_password


# Start the app:
Use Gradle to build and run the project:  
./gradlew bootRun  
The API will be available at http://localhost:8080.

# Running Integration Tests

This project uses **Testcontainers** for integration tests against a real PostgreSQL instance.
To run these tests locally, you must have **Docker installed and running** on your machine.

To run all tests (both unit and integration tests), use the following Gradle command:

```bash
./gradlew test