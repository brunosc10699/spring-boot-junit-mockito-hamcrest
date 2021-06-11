package com.bruno.studentsmanagement.resources;

import com.bruno.studentsmanagement.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentResource {

    @Autowired
    private StudentService studentService;

}
