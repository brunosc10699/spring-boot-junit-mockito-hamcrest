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

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.of(givenStudent));
        doNothing().when(studentRepository).deleteById(givenStudent.getId());
        studentService.deleteById(givenStudent.getId());
        verify(studentRepository, times(1)).findById(givenStudent.getId());
        verify(studentRepository, times(1)).deleteById(givenStudent.getId());
    }

    @Test
    void whenDeleteByIdMethodIsCalledWithAnUnregisteredIdThenThrowAnException() {
        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteById(givenStudent.getId()));
    }

    @Test
    void whenDeleteByEmailMethodIsCalledWithARegisteredEmailThenTheStudentMustBeExcluded() {
        when(studentRepository.findByEmail(givenStudent.getEmail())).thenReturn(Optional.of(givenStudent));
        doNothing().when(studentRepository).deleteByEmail(givenStudent.getEmail());
        studentService.deleteByEmail(givenStudent.getEmail());
        verify(studentRepository, times(1)).findByEmail(givenStudent.getEmail());
        verify(studentRepository, times(1)).deleteByEmail(givenStudent.getEmail());
    }

    @Test
    void whenDeleteByEmailMethodIsCalledWithAnUnregisteredEmailThenThrowAnException() {
        when(studentRepository.findByEmail(givenStudent.getEmail())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteByEmail(givenStudent.getEmail()));
    }

    @Test
    void whenUpdateByIdMethodIsCalledWithARegisteredIdThenDataStudentMustBeUpdated() {
        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.of(givenStudent));
        when(studentRepository.save(givenStudent)).thenReturn(givenStudent);
        StudentDTO updatedStudent = studentService.updateById(givenStudent.getId(), expectedStudent);
        assertThat(updatedStudent.getId(), is(equalTo(expectedStudent.getId())));
        assertThat(updatedStudent.getName(), is(equalTo(expectedStudent.getName())));
        assertThat(updatedStudent.getBirthDate(), is(equalTo(expectedStudent.getBirthDate())));
        assertThat(updatedStudent.getPhone(), is(equalTo(expectedStudent.getPhone())));
        assertThat(updatedStudent.getAttendance(), is(equalTo(expectedStudent.getAttendance())));
    }

    @Test
    void whenUpdateByIdMethodIsCalledWithAnUnregisteredIdThenThrowException() {
        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.updateById(givenStudent.getId(), expectedStudent));
    }

    @Test
    void whenUpdateByEmailMethodIsCalledWithARegisteredEmailThenDataStudentMustBeUpdated() {
        when(studentRepository.findByEmail(givenStudent.getEmail())).thenReturn(Optional.of(givenStudent));
        when(studentRepository.save(givenStudent)).thenReturn(givenStudent);
        StudentDTO updatedStudent = studentService.updateByEmail(expectedStudent);
        assertThat(updatedStudent.getId(), is(equalTo(expectedStudent.getId())));
        assertThat(updatedStudent.getName(), is(equalTo(expectedStudent.getName())));
        assertThat(updatedStudent.getBirthDate(), is(equalTo(expectedStudent.getBirthDate())));
        assertThat(updatedStudent.getEmail(), is(equalTo(expectedStudent.getEmail())));
        assertThat(updatedStudent.getPhone(), is(equalTo(expectedStudent.getPhone())));
        assertThat(updatedStudent.getAttendance(), is(equalTo(expectedStudent.getAttendance())));
    }

    @Test
    void whenUpdateByEmailMethodIsCalledWithAnUnregisteredEmailThenThrowException() {
        when(studentRepository.findByEmail(givenStudent.getEmail())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.updateByEmail(expectedStudent));
    }

    @Test
    void whenUpdateEmailMethodIsCalledWithRegisteredIdThenUpdateStudentEmail() {
        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.of(givenStudent));
        StudentDTO updatedStudent = studentService.updateEmail(
                givenStudent.getId(),
                givenStudent.getEmail(),
                expectedStudent.getEmail()
        );
        assertThat(updatedStudent.getId(), is(equalTo(expectedStudent.getId())));
        assertThat(updatedStudent.getEmail(), is(equalTo(expectedStudent.getEmail())));
    }

    @Test
    void whenUpdateEmailMethodIsCalledWithUnregisteredIdThenThrowException() {
        when(studentRepository.findById(givenStudent.getId())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class,
                () -> studentService.updateEmail(
                        givenStudent.getId(),
                        givenStudent.getEmail(),
                        expectedStudent.getEmail()
                )
        );
    }

    @Test
    void whenIncreaseAttendanceMethodIsCalledWithARegisteredIdThenIncreaseStudentAttendance() {
        when(studentRepository.getById(givenStudent.getId())).thenReturn(givenStudent);
        StudentDTO updatedStudent = studentService.increaseAttendance(givenStudent.getId());
        assertThat(updatedStudent.getId(), is(equalTo(expectedStudent.getId())));
        assertThat(updatedStudent.getAttendance(), is(equalTo(expectedStudent.getAttendance() + 1)));
    }

    @Test
    void whenIncreaseAttendanceMethodIsCalledWithAnUnregisteredIdThenThrowException() {
        when(studentRepository.getById(givenStudent.getId())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> studentService.increaseAttendance(givenStudent.getId()));
    }
}
