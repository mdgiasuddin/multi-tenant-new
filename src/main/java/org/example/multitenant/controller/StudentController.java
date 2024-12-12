package org.example.multitenant.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.multitenant.model.entity.Student;
import org.example.multitenant.repository.StudentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PostMapping
    public Student createNewStudent(@RequestBody @Valid Student student) {
        return studentRepository.save(student);
    }
}