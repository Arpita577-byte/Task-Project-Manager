# Task & Project Manager CLI

A powerful command-line application for managing Tasks and Projects built with Java 17 and Maven.

## Features

- Create and manage multiple Projects
- Add Tasks with Priority, Due Dates, and Status
- Track progress with completion percentage
- Overdue task detection
- Persistent storage using JSON
- Comprehensive test coverage

## Tech Stack

- Java 17
- Maven
- Gson (JSON serialization)
- JUnit 5

## How to Run

```bash
# 1. Clone the repository
git clone <your-repo-url>
cd task-project-manager

# 2. Build the project
mvn clean package

# 3. Run the application
java -jar target/task-project-manager-1.0.0-jar-with-dependencies.jar
