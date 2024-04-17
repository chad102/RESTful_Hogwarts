package ru.hogwarts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.service.StudentService;
import java.util.List;


@RestController
@RequestMapping("/student")

public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent (@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("{studentId}")
    public ResponseEntity<Student> getStudent (@PathVariable int studentId) {
        Student student = studentService.getStudent(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("{studentId}")
    public ResponseEntity<Student> updateStudent (@RequestBody long studentId, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(studentId, student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping
    public ResponseEntity<Student> deleteStudent (@RequestParam int studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = "/age")
    public ResponseEntity<List<Student>> getStudentsByAge (@PathVariable int age) {
        if (studentService.getStudentsByAge(age).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentService.getStudentsByAge(age));
    }

    @GetMapping(params = {"minAge", "maxAge"})
    public ResponseEntity<List<Student>> findByAgeBetween (@PathVariable int minAge, @PathVariable int maxAge) {
        List<Student> students = studentService.findByAgeBetween(minAge, maxAge);
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}/faculty")
    public ResponseEntity<Faculty> findFaculty (@PathVariable long studentId) {
        Faculty faculty = studentService.findFaculty(studentId);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }
}
