# Device API

A RESTful Spring Boot application to manage devices with full CRUD functionality, domain-level validation, Swagger documentation, test coverage, PostgreSQL persistence, and Docker containerization.

---

## 🚀 Features

* Create, update, delete, and retrieve device resources
* Filter devices by brand and state
* Business rules:

    * `creationTime` is immutable
    * `name` and `brand` cannot be updated if device is `IN_USE`
    * Devices in `IN_USE` state cannot be deleted
* API documentation with Swagger (OpenAPI 3)
* Unit tests using JUnit and Mockito
* Integration test collection using Postman + Newman
* Fully containerized using Docker and Docker Compose

---

## 🛠 Technologies

* Java 21
* Spring Boot 3.x
* Spring Data JPA
* PostgreSQL
* Maven
* Swagger (springdoc-openapi)
* Docker & Docker Compose
* JUnit 5 / Mockito
* Postman & Newman

---

## 📦 How to Run Locally

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd device-api
```

### 2. Build the JAR

```bash
./mvnw clean package -DskipTests
```

### 3. Run with Docker Compose

```bash
docker-compose up --build
```

> This starts PostgreSQL and the app together. Spring Boot will connect to `jdbc:postgresql://postgres:5432/devicedb`

---

## 🔗 API Endpoints

| Method | Endpoint                 | Description                     |
| ------ | ------------------------ | ------------------------------- |
| POST   | `/devices`               | Create a new device             |
| GET    | `/devices`               | Get all devices                 |
| GET    | `/devices/{id}`          | Get device by ID                |
| PUT    | `/devices/{id}`          | Full update of a device         |
| PATCH  | `/devices/{id}/state`    | Update only the state of device |
| DELETE | `/devices/{id}`          | Delete a device                 |
| GET    | `/devices/brand/{brand}` | Filter by brand                 |
| GET    | `/devices/state/{state}` | Filter by state                 |

---

## 📚 Swagger UI

Once the application is running, open:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Testing

### ✅ Unit Tests

```bash
./mvnw test
```

### ✅ Integration Tests (Postman + Newman)

```bash
newman run Device-Api-Testable.postman_collection.json
```

> This verifies that all endpoints work and validations are enforced

---

## 📁 Docker Overview

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/device-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: devicedb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: your_password
    ports:
      - "5432:5432"

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/devicedb
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: your_password
```

---

## 📌 Notes

* Ensure PostgreSQL container is up before Spring Boot starts
* Add `-parameters` flag in `build.gradle` to support path variable binding:

```groovy
tasks.withType(JavaCompile) {
    options.compilerArgs << "-parameters"
}
```

* Lombok is used, so enable annotation processing in your IDE

---

## 👨‍💻 Author & License

Created by \[Your Name]. MIT License or your preferred license.
