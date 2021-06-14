package com.bruno.studentsmanagement.resources;

import com.bruno.studentsmanagement.dto.StudentDTO;
import com.bruno.studentsmanagement.entities.Student;
import com.bruno.studentsmanagement.services.StudentService;
import com.bruno.studentsmanagement.services.exceptions.EmailAlreadyRegisteredException;
import com.bruno.studentsmanagement.services.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.bruno.studentsmanagement.utils.DateConverterUtil.convertDate;
import static com.bruno.studentsmanagement.utils.JsonConvertionUtil.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StudentResourceTest {

    private static final String URL = "/api/v1/students";

    private Student givenStudent = new Student(
            1L, "Pedro Álvares Cabral",
            convertDate("1467-01-01"),
            "pedroac@gmail.com",
            "(11) 98741-3652",
            0
    );

    private StudentDTO expectedStudent = new StudentDTO(givenStudent);

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentResource studentResource;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentResource)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenCreatedStatusIsReturned() throws Exception {
        when(studentService.save(expectedStudent)).thenReturn(expectedStudent);
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(givenStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(expectedStudent.getName())))
                .andExpect(jsonPath("$.birthDate", is(expectedStudent.getBirthDate().getTime())))
                .andExpect(jsonPath("$.email", is(expectedStudent.getEmail())))
                .andExpect(jsonPath("$.phone", is(expectedStudent.getPhone())))
                .andExpect(jsonPath("$.attendance", is(expectedStudent.getAttendance())));
    }

    @Test
    void whenPOSTIsCalledWithARegisteredStudentEmailThenThrowsEmailAlreadyRegisteredException() throws Exception {
        when(studentService.save(expectedStudent)).thenThrow(EmailAlreadyRegisteredException.class);
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(givenStudent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledToFindAllStudentsThenReturnOkStatus() throws Exception {
        when(studentService.findAll()).thenReturn(Collections.singletonList(expectedStudent));
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGETIsCalledToFindAStudentByARegisteredIdThenReturnOkStatus() throws Exception {
        when(studentService.findById(givenStudent.getId())).thenReturn(expectedStudent);
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/id/" + givenStudent.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(expectedStudent.getName())))
                .andExpect(jsonPath("$.birthDate", is(expectedStudent.getBirthDate().getTime())))
                .andExpect(jsonPath("$.email", is(expectedStudent.getEmail())))
                .andExpect(jsonPath("$.phone", is(expectedStudent.getPhone())))
                .andExpect(jsonPath("$.attendance", is(expectedStudent.getAttendance())));
    }

    @Test
    void whenGETIsCalledToFindAStudentByAnUnregisteredIdThenThrowStudentNotFoundException() throws Exception {
        when(studentService.findById(givenStudent.getId())).thenThrow(StudentNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/id/" + givenStudent.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETIsCalledToFindAStudentByEmailWithARegisteredEmailThenReturnOkStatus() throws Exception {
        when(studentService.findByEmail(givenStudent.getEmail())).thenReturn(expectedStudent);
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/email/" + givenStudent.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(expectedStudent.getName())))
                .andExpect(jsonPath("$.birthDate", is(expectedStudent.getBirthDate().getTime())))
                .andExpect(jsonPath("$.email", is(expectedStudent.getEmail())))
                .andExpect(jsonPath("$.phone", is(expectedStudent.getPhone())))
                .andExpect(jsonPath("$.attendance", is(expectedStudent.getAttendance())));
    }

    @Test
    void whenGETIsCalledToFindAStudentByEmailWithAnUnregisteredEmailThenThrowStudentNotFoundException() throws Exception {
        when(studentService.findByEmail(givenStudent.getEmail())).thenThrow(StudentNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/email/" + givenStudent.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDELETEIsCalledWithARegisteredIdThenReturnNoContentStatus() throws Exception {
        doNothing().when(studentService).deleteById(givenStudent.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/id/" + givenStudent.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithARegisteredIdThenThrowStudentNotFoundException() throws Exception {
        doThrow(StudentNotFoundException.class).when(studentService).deleteById(givenStudent.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/id/" + givenStudent.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDELETEIsCalledWithARegisteredEmailThenReturnNoContentStatus() throws Exception {
        doNothing().when(studentService).deleteByEmail(givenStudent.getEmail());
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/email/" + givenStudent.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithARegisteredEmailThenThrowStudentNotFoundException() throws Exception {
        doThrow(StudentNotFoundException.class).when(studentService).deleteByEmail(givenStudent.getEmail());
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/email/" + givenStudent.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPUTIsCalledToUpdateDataStudentByARegisteredIdThenReturnOkStatus() throws Exception {
        when(studentService.updateById(givenStudent.getId(), expectedStudent)).thenReturn(expectedStudent);
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/id/" + givenStudent.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(expectedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(expectedStudent.getName())))
                .andExpect(jsonPath("$.birthDate", is(expectedStudent.getBirthDate().getTime())))
                .andExpect(jsonPath("$.email", is(expectedStudent.getEmail())))
                .andExpect(jsonPath("$.phone", is(expectedStudent.getPhone())))
                .andExpect(jsonPath("$.attendance", is(expectedStudent.getAttendance())));
    }

    @Test
    void whenPUTIsCalledToUpdateDataStudentByAnUnregisteredIdThenThrowStudentNotFoundException() throws Exception {
        when(studentService.updateById(givenStudent.getId(), expectedStudent)).thenThrow(StudentNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/id/" + givenStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedStudent)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPUTIsCalledToUpdateDataStudentByARegisteredEmailThenReturnOkStatus() throws Exception {
        when(studentService.updateByEmail(expectedStudent)).thenReturn(expectedStudent);
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(expectedStudent.getName())))
                .andExpect(jsonPath("$.birthDate", is(expectedStudent.getBirthDate().getTime())))
                .andExpect(jsonPath("$.email", is(expectedStudent.getEmail())))
                .andExpect(jsonPath("$.phone", is(expectedStudent.getPhone())))
                .andExpect(jsonPath("$.attendance", is(expectedStudent.getAttendance())));
    }

    @Test
    void whenPUTIsCalledToUpdateDataStudentByAnUnregisteredEmailThenThrowStudentNotFoundException() throws Exception {
        when(studentService.updateByEmail(expectedStudent)).thenThrow(StudentNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedStudent)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToUpdateStudentEmailWithARegisteredIdThenReturnOkStatus() throws Exception {
        when(
                studentService.updateEmail(
                        givenStudent.getId(),
                        givenStudent.getEmail(),
                        expectedStudent.getEmail()
                )
        ).thenReturn(expectedStudent);
        mockMvc.perform(MockMvcRequestBuilders.patch(
                URL + "/" + givenStudent.getId() + "/" + givenStudent.getEmail() + "/" + expectedStudent.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPATCHIsCalledToUpdateStudentEmailWithAnUnregisteredIdThenThrowStudentNotFoundException() throws Exception {
        doThrow(StudentNotFoundException.class).when(studentService).updateEmail(
                givenStudent.getId(),
                givenStudent.getEmail(),
                expectedStudent.getEmail()
        );
        mockMvc.perform(MockMvcRequestBuilders.patch(
                URL + "/" + givenStudent.getId() + "/" + givenStudent.getEmail() + "/" + expectedStudent.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncreaseStudentAttendanceWithARegisteredIdThenOkStatusIsReturned() throws Exception {
        when(studentService.increaseAttendance(givenStudent.getId())).thenReturn(expectedStudent);
        mockMvc.perform(MockMvcRequestBuilders.patch(URL + "/" + givenStudent.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPATCHIsCalledToIncreaseStudentAttendanceWithAnUnregisteredIdThenThrowStudentNotFoundException() throws Exception {
        doThrow(StudentNotFoundException.class).when(studentService).increaseAttendance(givenStudent.getId());
        mockMvc.perform(MockMvcRequestBuilders.patch(URL + "/" + givenStudent.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETIsCalledToFindByPartOfANameThenReturnOkStatus() throws Exception {
        when(studentService.findByNameContainingIgnoreCase(givenStudent.getName()))
                .thenReturn(Collections.singletonList(expectedStudent));
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "?name=" + givenStudent.getName())
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
