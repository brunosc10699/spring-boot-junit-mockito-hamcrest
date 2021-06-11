package com.bruno.studentsmanagement.services;

import com.bruno.studentsmanagement.dto.StudentDTO;
import com.bruno.studentsmanagement.entities.Student;
import com.bruno.studentsmanagement.repositories.StudentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    private Student givenStudent = new Student(1L, "Pedro Álvares Cabral", sdf.parse("01-011467"), "pedroac@gmail.com", "(11) 98741-3652", 0);
    private StudentDTO expectedStudent = new StudentDTO(givenStudent);

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    public StudentServiceTest() throws ParseException {
    }
}
