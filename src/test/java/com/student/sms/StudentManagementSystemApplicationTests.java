package com.student.sms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentManagementSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}


/* 
Sample commands to test
# Retrieve all students
./mvnw spring-boot:run

curl http://localhost:8080/api/students

# Add a student
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Rahul Sharma","email":"rahul@example.com","course":"Computer Science","age":21}' */