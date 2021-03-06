package com.bruno.studentsmanagement.services;

import com.bruno.studentsmanagement.dto.StudentDTO;
import com.bruno.studentsmanagement.entities.Student;
import com.bruno.studentsmanagement.repositories.StudentRepository;
import com.bruno.studentsmanagement.services.exceptions.EmailAlreadyRegisteredException;
import com.bruno.studentsmanagement.services.exceptions.StudentInconsistencyException;
import com.bruno.studentsmanagement.services.exceptions.StudentNotFoundException;
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
        checkStudentEmail(studentDTO.getEmail());
        Student student = fromDTO(studentDTO);
        student = studentRepository.save(student);
        return new StudentDTO(student);
    }

    public List<StudentDTO> findAll(){
        return studentRepository.findAll().stream().map(StudentDTO::new).collect(Collectors.toList());
    }

    public StudentDTO findById(Long id){
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        return new StudentDTO(student);
    }

    public StudentDTO findByEmail(String email){
        Student student = studentRepository.findByEmail(email).orElseThrow(() -> new StudentNotFoundException(email));
        return new StudentDTO(student);
    }

    public void deleteById(Long id){
        findById(id);
        studentRepository.deleteById(id);
    }

    public void deleteByEmail(String email){
        StudentDTO studentDTO = findByEmail(email);
        studentRepository.deleteByEmail(studentDTO.getEmail());
    }

    public StudentDTO updateById(Long id, StudentDTO studentDTO){
        StudentDTO savedStudent = findById(id);
        if(!studentDTO.getEmail().equals(savedStudent.getEmail())) checkStudentEmail(studentDTO.getEmail());
        studentDTO.setId(id);
        if(studentDTO.getAttendance() == null) studentDTO.setAttendance(savedStudent.getAttendance());
        Student student = fromDTO(studentDTO);
        return new StudentDTO(studentRepository.save(student));
    }

    public StudentDTO updateByEmail(StudentDTO studentDTO){
        StudentDTO savedStudent = findByEmail(studentDTO.getEmail());
        studentDTO.setId(savedStudent.getId());
        if(!studentDTO.getEmail().equals(savedStudent.getEmail())) checkStudentEmail(studentDTO.getEmail());
        if(studentDTO.getAttendance() == null) studentDTO.setAttendance(savedStudent.getAttendance());
        Student student = fromDTO(studentDTO);
        return new StudentDTO(studentRepository.save(student));
    }

    public StudentDTO updateEmail(Long id, String email, String newEmail){
        StudentDTO studentDTO = findById(id);
        if(!studentDTO.getEmail().equals(email)) throw new StudentInconsistencyException(email, id);
        studentDTO.setEmail(newEmail);
        Student student = fromDTO(studentDTO);
        student = studentRepository.save(student);
        return new StudentDTO(student);
    }

    public StudentDTO increaseAttendance(Long id){
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        student.setAttendance(student.getAttendance() + 1);
        return new StudentDTO(studentRepository.save(student));
    }

    public List<StudentDTO> findByNameContainingIgnoreCase(String name){
        return studentRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }

    private Optional<Void> checkStudentEmail(String email){
        Optional<Student> student = studentRepository.findByEmail(email);
        if(student.isPresent()) throw new EmailAlreadyRegisteredException(email);
        return null;
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

}
