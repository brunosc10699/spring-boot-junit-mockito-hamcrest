package com.bruno.studentsmanagement.services;

import com.bruno.studentsmanagement.dto.StudentDTO;
import com.bruno.studentsmanagement.entities.Student;
import com.bruno.studentsmanagement.repositories.StudentRepository;
import com.bruno.studentsmanagement.services.exceptions.EmailAlreadyRegisteredException;
import com.bruno.studentsmanagement.services.exceptions.InconsistencyStudentException;
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
        getStudentEmail(studentDTO.getEmail());
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
        findByEmail(email);
        studentRepository.deleteByEmail(email);
    }

    public StudentDTO updateById(Long id, StudentDTO studentDTO){
        StudentDTO savedStudent = findById(id);
        if(!savedStudent.getEmail().equals(studentDTO.getEmail())){
            throw new InconsistencyStudentException(studentDTO.getEmail(), id);
        }
        studentDTO.setId(id);
        studentDTO.setEmail(savedStudent.getEmail());
        Student student = fromDTO(studentDTO);
        return new StudentDTO(studentRepository.save(student));
    }

    private void getStudentEmail(String email){
        Optional<Student> student = studentRepository.findByEmail(email);
        if(student.isPresent()){
            throw new EmailAlreadyRegisteredException(email);
        }
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
