package com.example.demo.students;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")

public class StudentManagementController {
    private static final List<Student> students = Arrays.asList(
            new Student(1, "James"),
            new Student(2, "Lana"),
            new Student(3, "Anna")
    );
    @GetMapping
    public List<Student> getAllStudents(){
        return students;
    }
    @PostMapping
    public void registerNewStudent(@RequestBody Student student){
        System.out.println(student);
    }
    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable Integer studentId){
        System.out.println(studentId);
    }
    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student){
        System.out.println(studentId);
        System.out.println(student);
    }
}
