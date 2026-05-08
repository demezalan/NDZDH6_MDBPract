# NDZDH6 API Java

A Java-based REST API for restaurant management system using Spring Boot.

## Project Structure

```
NDZDH6_APIjava/
├── src/
│   ├── main/
│   │   └── java/com/ndzdh6/
│   │       ├── models/         # Data models
│   │       └── Application.java
│   └── test/
├── pom.xml                      # Maven configuration
└── README.md
```

## Models

- **Cim**: Customer information
- **Etterem**: Restaurant details
- **Foszakacs**: Head chef information
- **Gyakornok**: Apprentice/Intern information

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## Building the Project

```bash
mvn clean package
```

## Running the Application

```bash
mvn spring-boot:run
```

## API Endpoints

The application will start on `http://localhost:8080`

## Development

This project uses:
- Spring Boot for framework
- Spring Data JPA for database access
- Lombok for reducing boilerplate code
- H2 Database for development
