package com.student.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.sms.entity.Student;
import com.student.sms.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student("Alice Johnson", "alice@example.com", "Computer Science", 20);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Alice Johnson"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.course").value("Computer Science"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    void testGetAllStudents() throws Exception {
        studentRepository.save(new Student("Alice", "alice@example.com", "CS", 20));
        studentRepository.save(new Student("Bob", "bob@example.com", "IT", 22));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetStudentById() throws Exception {
        Student saved = studentRepository.save(new Student("Charlie", "charlie@example.com", "Math", 21));

        mockMvc.perform(get("/api/students/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Charlie"))
                .andExpect(jsonPath("$.email").value("charlie@example.com"));
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString("not found")));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student saved = studentRepository.save(new Student("Diana", "diana@example.com", "Physics", 23));
        Student updateDetails = new Student("Diana Prince", "diana.prince@example.com", "Astrophysics", 24);

        mockMvc.perform(put("/api/students/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Diana Prince"))
                .andExpect(jsonPath("$.email").value("diana.prince@example.com"))
                .andExpect(jsonPath("$.course").value("Astrophysics"))
                .andExpect(jsonPath("$.age").value(24));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Student saved = studentRepository.save(new Student("Evan", "evan@example.com", "Chemistry", 19));

        mockMvc.perform(delete("/api/students/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(get("/api/students/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStudentsByCourse() throws Exception {
        studentRepository.save(new Student("Frank", "frank@example.com", "CS", 20));
        studentRepository.save(new Student("Grace", "grace@example.com", "cs", 21));
        studentRepository.save(new Student("Heidi", "heidi@example.com", "Biology", 22));

        mockMvc.perform(get("/api/students/course/{course}", "CS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].course", equalToIgnoringCase("CS")))
                .andExpect(jsonPath("$[1].course", equalToIgnoringCase("CS")));
    }

    @Test
    void testCreateStudent_ValidationError() throws Exception {
        Student invalid = new Student("", "invalid-email", "", -5);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors").exists());
    }
}
