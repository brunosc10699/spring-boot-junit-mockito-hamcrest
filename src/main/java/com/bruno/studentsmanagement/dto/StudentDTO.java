package com.bruno.studentsmanagement.dto;

import com.bruno.studentsmanagement.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Student name is mandatory!")
    private String name;

    @NotBlank(message = "We need to know your birth date, please!")
    @Past
    private Date birthDate;

    @Email
    private String email;

    @NotBlank(message = "Your phone number is required!")
    @Size(min = 14, max = 15, message = "Your phone number must have this format: (xx) xxxxx-xxxx")
    private String phone;

    private Integer attendance;

    public StudentDTO (Student student){
        id = student.getId();
        name = student.getName();
        birthDate = student.getBirthDate();
        email = student.getEmail();
        phone = student.getPhone();
        attendance = student.getAttendance();
    }
}
