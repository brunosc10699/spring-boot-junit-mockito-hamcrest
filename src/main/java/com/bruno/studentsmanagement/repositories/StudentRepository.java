package com.bruno.studentsmanagement.repositories;

import com.bruno.studentsmanagement.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Transactional(readOnly=true)
    Optional<Student> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    @Transactional(readOnly=true)
    List<Student> findByNameContainingIgnoreCase(String name);

}
