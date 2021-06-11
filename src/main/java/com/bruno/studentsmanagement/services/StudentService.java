package com.bruno.studentsmanagement.services;

import com.bruno.studentsmanagement.dto.StudentDTO;
import com.bruno.studentsmanagement.entities.Student;
import com.bruno.studentsmanagement.repositories.StudentRepository;
import com.bruno.studentsmanagement.services.exceptions.EmailAlreadyRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public StudentDTO save(StudentDTO studentDTO){
        findByEmail(studentDTO.getEmail());
        Student student = fromDTO(studentDTO);
        student = studentRepository.save(student);
        return new StudentDTO(student);
    }

    public List<StudentDTO> findAll(){
        return studentRepository.findAll().stream().map(StudentDTO::new).collect(Collectors.toList());
    }

    private Student fromDTO(StudentDTO studentDTO){
        return new Student(
                studentDTO.getId(),
                studentDTO.getName(),
                studentDTO.getBirthDate(),
                studentDTO.getEmail(),
                studentDTO.getPhone(),
                studentDTO.getAttendance()
        );
    }

    private void findByEmail(String email){
        Optional<Student> student = studentRepository.findByEmail(email);
        if(student.isPresent()){
            throw new EmailAlreadyRegisteredException(email);
        }
    }

}
