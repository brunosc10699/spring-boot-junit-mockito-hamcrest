package com.bruno.studentsmanagement.resources;

import com.bruno.studentsmanagement.dto.StudentDTO;
import com.bruno.studentsmanagement.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentResource {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> save(@Valid @RequestBody StudentDTO studentDTO){
        studentDTO = studentService.save(studentDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/id")
                .buildAndExpand(studentDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(studentDTO);
    }

}
