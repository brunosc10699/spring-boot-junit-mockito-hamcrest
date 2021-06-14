package com.bruno.studentsmanagement.resources;

import com.bruno.studentsmanagement.dto.StudentDTO;
import com.bruno.studentsmanagement.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<StudentDTO>> findAll(){
        List<StudentDTO> list = studentService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<StudentDTO> findById(@PathVariable Long id){
        StudentDTO studentDTO = studentService.findById(id);
        return ResponseEntity.ok(studentDTO);
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<StudentDTO> findByEmail(@PathVariable String email){
        StudentDTO studentDTO = studentService.findByEmail(email);
        return ResponseEntity.ok(studentDTO);
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
