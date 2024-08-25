package ru.hogwarts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")

public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty (@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(createdFaculty);
    }

    @GetMapping("/{facultyId}")
    public ResponseEntity<Faculty> getFaculty (@PathVariable int facultyId) {
        Faculty faculty = facultyService.getFaculty(facultyId);
        return ResponseEntity.ok(faculty);
    }

    @PutMapping("/{facultyId}")
    public ResponseEntity<Faculty> updateFaculty (@PathVariable int facultyId, @RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.updateFaculty(facultyId, faculty);
        if (updatedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("/{facultyId}")
    public Faculty deleteFaculty (@PathVariable int facultyId) {
        return facultyService.deleteFaculty(facultyId);
    }

    @GetMapping("/colorOrName/{colorOrName}")
    public ResponseEntity<List<Faculty>> getFacultyByColorOrName (@PathVariable String colorOrName) {
        List<Faculty> faculties = facultyService.findByColorOrName(colorOrName);
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{facultyId}/students")
    public ResponseEntity<List<Student>> getStudentsByFaculty (@PathVariable int facultyId) {
        List<Student> students = facultyService.findStudentsByFaculty(facultyId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/name-and-color")
    public ResponseEntity<List<Faculty>> getFacultyByNameAndColor (@RequestParam String name, @RequestParam String color) {
        List<Faculty> faculties = facultyService.findByNameAndColor(name, color);
        return ResponseEntity.ok(faculties);
    }
}
