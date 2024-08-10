package ru.hogwarts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.model.Student;
import ru.hogwarts.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentSqlController {
    private final StudentService studentService;

    public StudentSqlController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getStudentCount(){
        return ResponseEntity.ok(studentService.getCountOfAllStudents());
    }

    @GetMapping("/get-age-average")
    public ResponseEntity<Double> getStudentAgeAverage(){
        return ResponseEntity.ok(studentService.getStudentAgeAverage());
    }

    @GetMapping("/get-last-five-student")
    public ResponseEntity<List<Student>> getLastFiveStudents(){
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }
}
