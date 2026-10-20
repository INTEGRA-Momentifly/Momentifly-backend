# Momentifly

Momentifly is an app built to help you organize your time, track tasks, and stay motivated. It combines a calendar, a to-do list, and some fun game features to keep things interesting.

# What it does

Tasks & Appointments: Manage your daily schedule and get reminders.

Health Tracking: Log your daily calories and workouts.

Games: Complete daily quests to earn points and climb the leaderboard.

Social: Add friends and keep up with them.

# Tech Stack

Java 25

Spring Boot

PostgreSQL (or your preferred SQL DB)

Gradle

# How to run it locally

Clone the repo:

git clone <repository-url>
cd <project-directory>


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