package com.bruno.studentsmanagement.services.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException(String email){
        super(String.format("The address email provided (%s) is already registered by another student!", email));
    }
}
