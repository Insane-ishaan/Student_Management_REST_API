# Student Management System - REST API

> A Spring Boot-based REST API project developed as part of my Java backend learning journey. It provides a complete CRUD management system for student records utilizing Spring Data JPA, Hibernate, and MySQL.

---

## 1. Project Overview & Architecture

This project implements a complete **Student Management System** adhering to clean layered architecture:

```
src/main/java/com/student/sms/
├── StudentManagementSystemApplication.java    # Application Entry Point
├── entity/
│   └── Student.java                           # JPA Entity with Bean Validation
├── repository/
│   └── StudentRepository.java                 # Spring Data JPA Repository
├── service/
│   ├── StudentService.java                    # Business Logic Interface
│   └── impl/
│       └── StudentServiceImpl.java            # Service Implementation & Transactions
├── controller/
│   └── StudentController.java                 # REST Controller Endpoints
└── exception/
    ├── ResourceNotFoundException.java         # 404 Custom Exception
    ├── DuplicateResourceException.java        # 409 Conflict Exception
    ├── ErrorResponse.java                     # Standardized Error DTO
    └── GlobalExceptionHandler.java            # @RestControllerAdvice
```

---

## 2. Entity Requirements

The `Student` entity (`students` table in MySQL) contains the following fields:

| Field | Type | Constraint | Description |
|---|---|---|---|
| `id` | `Long` | Primary Key, Auto-increment | Unique identifier |
| `name` | `String` | Non-null, 2-100 chars | Student's full name |
| `email` | `String` | Non-null, Unique, Valid Email | Student email address |
| `course` | `String` | Non-null, 2-100 chars | Enrolled course name |
| `age` | `Integer` | Non-null, Min value 1 | Student age |

---

## 3. REST API Endpoints

All endpoints are prefixed under `/api/students`:

| HTTP Method | Endpoint | Description | Status Code |
|---|---|---|---|
| `POST` | `/api/students` | Add a new student | `201 Created` |
| `GET` | `/api/students` | Retrieve all students | `200 OK` |
| `GET` | `/api/students/{id}` | Retrieve student by ID | `200 OK` / `404 Not Found` |
| `PUT` | `/api/students/{id}` | Update student details | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/students/{id}` | Delete a student | `200 OK` / `404 Not Found` |
| `GET` | `/api/students/course/{course}` | Retrieve students by course | `200 OK` |

---

## 4. Database Configuration (MySQL)

Configuration located in `src/main/resources/application.properties`:

```properties
spring.application.name=student-management-system
server.port=8080

# MySQL DataSource
spring.datasource.url=jdbc:mysql://localhost:3306/student_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=sharma@2007
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA & Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### MySQL Database Setup
If you need to create the database manually in MySQL Workbench or CLI:
```sql
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;
```
*(The application will automatically generate the `students` table on startup via Hibernate DDL update).*

---

## 5. Build and Run Instructions

### Prerequisites
- Java 17 or Java 21
- MySQL Server 8.0 running locally on port 3306

### Compile & Run
Using the included Maven wrapper (no local Maven install needed):

```powershell
# Run the application
.\mvnw.cmd spring-boot:run

# Or package as a JAR and run
.\mvnw.cmd clean package
java -jar target/student-management-system-0.0.1-SNAPSHOT.jar
```

### Run Automated Tests
Includes comprehensive unit and integration tests covering all 6 endpoints and validation/exception scenarios:
```powershell
.\mvnw.cmd test
```

---

## 6. Testing All Endpoints with Postman

A ready-to-import Postman Collection is included in the project:
📁 `Student_Management_System.postman_collection.json`

### How to Import & Test in Postman:
1. Open **Postman**.
2. Click **Import** (top left).
3. Drag and drop `Student_Management_System.postman_collection.json` from this folder.
4. Make sure the Spring Boot application is running (`http://localhost:8080`).
5. Execute each request in order and take screenshots of the responses:

### Detailed Request / Response Guide for Screenshots:

#### 1. Add Student (POST `/api/students`)
- **Request Body (JSON):**
  ```json
  {
    "name": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```
- **Response:** `201 Created`
  ```json
  {
    "id": 1,
    "name": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```

#### 2. Retrieve All Students (GET `/api/students`)
- **Response:** `200 OK`
  ```json
  [
    {
      "id": 1,
      "name": "Alice Johnson",
      "email": "alice.johnson@example.com",
      "course": "Computer Science",
      "age": 21
    }
  ]
  ```

#### 3. Retrieve Student by ID (GET `/api/students/1`)
- **Response:** `200 OK`
  ```json
  {
    "id": 1,
    "name": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```

#### 4. Update Student Details (PUT `/api/students/1`)
- **Request Body (JSON):**
  ```json
  {
    "name": "Alice J. Williams",
    "email": "alice.williams@example.com",
    "course": "Artificial Intelligence",
    "age": 22
  }
  ```
- **Response:** `200 OK`
  ```json
  {
    "id": 1,
    "name": "Alice J. Williams",
    "email": "alice.williams@example.com",
    "course": "Artificial Intelligence",
    "age": 22
  }
  ```

#### 5. Retrieve Students by Course (GET `/api/students/course/Artificial Intelligence`)
- **Response:** `200 OK`
  ```json
  [
    {
      "id": 1,
      "name": "Alice J. Williams",
      "email": "alice.williams@example.com",
      "course": "Artificial Intelligence",
      "age": 22
    }
  ]
  ```

#### 6. Delete a Student (DELETE `/api/students/1`)
- **Response:** `200 OK`
  ```json
  {
    "success": true,
    "message": "Student deleted successfully with id: 1"
  }
  ```

#### 7. Exception Handling - Student Not Found (GET `/api/students/9999`)
- **Response:** `404 Not Found`
  ```json
  {
    "timestamp": "2026-11-13T19:26:13.9451766",
    "status": 404,
    "error": "Not Found",
    "message": "Student not found with id : '9999'",
    "path": "/api/students/9999"
  }
  ```

#### 8. Validation Error Handling (POST `/api/students`)
- **Invalid Payload:**
  ```json
  {
    "name": "",
    "email": "not-an-email",
    "course": "",
    "age": -5
  }
  ```
- **Response:** `400 Bad Request`
  ```json
  {
    "timestamp": "2026-11-13T19:26:44.3611788",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed for one or more fields",
    "path": "/api/students",
    "validationErrors": {
      "name": "Name is required",
      "email": "Email must be a valid email address",
      "course": "Course is required",
      "age": "Age must be greater than 0"
    }
  }
  ```

---

## 7. Git & GitHub Repository Setup

To commit and push this repository to GitHub:

```powershell
git init
git add .
git commit -m "feat: complete Student Management REST API implementation"
git branch -M main
git remote add origin https://github.com/<your-username>/<your-repo-name>.git
git push -u origin main
```
