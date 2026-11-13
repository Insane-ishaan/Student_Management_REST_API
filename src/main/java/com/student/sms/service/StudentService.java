package com.student.sms.service;

import com.student.sms.entity.Student;

import java.util.List;

public interface StudentService {

    // Add a new student
    Student saveStudent(Student student);

    // Retrieve all students
    List<Student> getAllStudents();

    // Retrieve student by ID
    Student getStudentById(Long id);

    // Update student details
    Student updateStudent(Long id, Student studentDetails);

    // Delete a student by ID
    void deleteStudent(Long id);

    // Retrieve students by course
    List<Student> getStudentsByCourse(String course);
}
