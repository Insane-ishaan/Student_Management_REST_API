package com.student.sms.service.impl;

import com.student.sms.entity.Student;
import com.student.sms.exception.DuplicateResourceException;
import com.student.sms.exception.ResourceNotFoundException;
import com.student.sms.repository.StudentRepository;
import com.student.sms.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student saveStudent(Student student) {
        // Check for duplicate email
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DuplicateResourceException("Student already exists with email: " + student.getEmail());
        }
        // Ensure new student gets an auto-generated ID
        student.setId(null);
        return studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    @Override
    public Student updateStudent(Long id, Student studentDetails) {
        Student existingStudent = getStudentById(id);

        // Check if updated email is already taken by another student
        if (studentRepository.existsByEmailAndIdNot(studentDetails.getEmail(), id)) {
            throw new DuplicateResourceException("Another student already exists with email: " + studentDetails.getEmail());
        }

        existingStudent.setName(studentDetails.getName());
        existingStudent.setEmail(studentDetails.getEmail());
        existingStudent.setCourse(studentDetails.getCourse());
        existingStudent.setAge(studentDetails.getAge());

        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getStudentsByCourse(String course) {
        return studentRepository.findByCourseIgnoreCase(course);
    }
}
