package com.bruno.studentsmanagement.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InconsistencyStudentException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public InconsistencyStudentException(String email, Long id){
        super(String.format("The email address (%s) provided may not belong to the student identified by this ID: %s", email, id));
    }
}
