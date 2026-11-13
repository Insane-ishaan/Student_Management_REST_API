package com.student.sms.repository;

import com.student.sms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Retrieve students by course (case-insensitive for better usability)
    List<Student> findByCourseIgnoreCase(String course);

    // Retrieve students by exact course
    List<Student> findByCourse(String course);

    // Find student by email
    Optional<Student> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Check if email already exists for another student (useful for update)
    boolean existsByEmailAndIdNot(String email, Long id);
}
