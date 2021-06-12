package com.bruno.studentsmanagement.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public StudentNotFoundException(Long id){
        super(String.format("There is no student registered with this ID: %s", id));
    }

    public StudentNotFoundException(String email){
        super(String.format("There is no student registered with this email address: %s", email));
    }
}
