package com.bruno.studentsmanagement.services;

import com.bruno.studentsmanagement.dto.StudentDTO;
import com.bruno.studentsmanagement.entities.Student;
import com.bruno.studentsmanagement.repositories.StudentRepository;
import com.bruno.studentsmanagement.services.exceptions.EmailAlreadyRegisteredException;
import com.bruno.studentsmanagement.services.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    private Student givenStudent = new Student(
            1L, "Pedro Álvares Cabral",
            sdf.parse("01-01-1467"),
            "pedroac@gmail.com",
            "(11) 98741-3652",
            0
    );
    private StudentDTO expectedStudent = new StudentDTO(givenStudent);

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    public StudentServiceTest() throws ParseException {
    }

    @Test
    void whenANewStudentIsGivenThenItMustBeCreated() {
        when(studentRepository.save(givenStudent)).thenReturn(givenStudent);
        StudentDTO studentDTO = studentService.save(expectedStudent);
        assertThat(studentDTO.getId(), is(equalTo(expectedStudent.getId())));
        assertThat(studentDTO.getName(), is(equalTo(expectedStudent.getName())));
        assertThat(studentDTO.getBirthDate(), is(equalTo(expectedStudent.getBirthDate())));
        assertThat(studentDTO.getEmail(), is(equalTo(expectedStudent.getEmail())));
        assertThat(studentDTO.getPhone(), is(equalTo(expectedStudent.getPhone())));
        assertThat(studentDTO.getAttendance(), is(equalTo(0)));
    }

    @Test
    void whenCreatingANewStudentAnEmailAccountIsAlreadyRegisteredThenThrowAnException() {
        when(studentRepository.findByEmail(givenStudent.getEmail())).thenReturn(Optional.of(givenStudent));
        assertThrows(EmailAlreadyRegisteredException.class, () -> studentService.save(expectedStudent));
    }

    @Test
    void whenFindAllMethodIsCalledThenReturnAListOfStudents() {
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(givenStudent));
        List<StudentDTO> list = studentService.findAll();
        assertThat(list.get(0), is(equalTo(expectedStudent)));
    }

    @Test
    void whenFindAllMethodIsCalledThenReturnAnEmptyListOfStudents(){
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
        List<StudentDTO> list = studentService.findAll();
        assertThat(list.size(), is(equalTo(0)));
    }

    @Test
    void whenFindByIdIsCalledWithARegisteredIdThenReturnTheStudent() {
        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.of(givenStudent));
        StudentDTO studentDTO = studentService.findById(givenStudent.getId());
        assertThat(studentDTO.getId(), is(equalTo(expectedStudent.getId())));
        assertThat(studentDTO.getName(), is(equalTo(expectedStudent.getName())));
        assertThat(studentDTO.getBirthDate(), is(equalTo(expectedStudent.getBirthDate())));
        assertThat(studentDTO.getEmail(), is(equalTo(expectedStudent.getEmail())));
        assertThat(studentDTO.getPhone(), is(equalTo(expectedStudent.getPhone())));
        assertThat(studentDTO.getAttendance(), is(equalTo(expectedStudent.getAttendance())));
    }

    @Test
    void whenFindByIdIsCalledWithAnUnregisteredIdThenThrowAnException() {
        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.findById(givenStudent.getId()));
    }

    @Test
    void whenFindByEmailIsCalledWithARegisteredEmailThenReturnTheStudent() {
        when(studentRepository.findByEmail(givenStudent.getEmail())).thenReturn(Optional.of(givenStudent));
        StudentDTO studentDTO = studentService.findByEmail(givenStudent.getEmail());
        assertThat(studentDTO.getId(), is(equalTo(expectedStudent.getId())));
        assertThat(studentDTO.getName(), is(equalTo(expectedStudent.getName())));
        assertThat(studentDTO.getBirthDate(), is(equalTo(expectedStudent.getBirthDate())));
        assertThat(studentDTO.getEmail(), is(equalTo(expectedStudent.getEmail())));
        assertThat(studentDTO.getPhone(), is(equalTo(expectedStudent.getPhone())));
        assertThat(studentDTO.getAttendance(), is(equalTo(expectedStudent.getAttendance())));
    }

    @Test
    void whenFindByEmailIsCalledWithAnUnregisteredEmailThenThrowAnException() {
        when(studentRepository.findByEmail(givenStudent.getEmail())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.findByEmail(givenStudent.getEmail()));
    }

    @Test
    void whenDeleteByIdMethodIsCalledWithARegisteredIdThenTheStudentMustBeExcluded() {
//        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.of(givenStudent));
//        doNothing().when(studentRepository).deleteById(givenStudent.getId());
//        studentService.deleteById(givenStudent.getId());
//        verify(studentRepository, times(1)).findById(givenStudent.getId());
//        verify(studentRepository, times(1)).deleteById(givenStudent.getId());
    }

    @Test
    void whenDeleteByIdMethodIsCalledWithAnUnegisteredIdThenThrowAnException() {
    }

    @Test
    void whenDeleteByEmailMethodIsCalledWithARegisteredEmailThenTheStudentMustBeExcluded() {
    }

    @Test
    void whenDeleteByEmailMethodIsCalledWithAnUnregisteredEmailThenThrowAnException() {
    }
}
