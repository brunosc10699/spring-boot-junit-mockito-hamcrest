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
    @Size(max = 50, message = "Student name may have 50 characters maximum!")
    private String name;

    @NotBlank(message = "We need to know your birth date, please!")
    @Size(max = 10, message = "Your birth date must have this format: xx/xx/xxxx")
    @Past
    private Date birthDate;

    @Email(message = "Inform us your best email!")
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
