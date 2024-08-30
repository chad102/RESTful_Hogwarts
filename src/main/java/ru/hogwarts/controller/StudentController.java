package ru.hogwarts.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.model.Avatar;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.service.AvatarService;
import ru.hogwarts.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@RestController
@RequestMapping("/student")

public class StudentController {
    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService,
                             AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent (@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        System.out.println(createdStudent);
        return ResponseEntity.ok(createdStudent);
    }

    @PostMapping(value = "{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar (@PathVariable long studentId, @RequestPart MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 300) {
            ResponseEntity.badRequest().body("Слишком большой размер файла");
        }
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{studentId}")
    public ResponseEntity<Student> getStudent (@PathVariable long studentId) {
        Student student = studentService.getStudent(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent (@PathVariable long studentId, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(studentId, student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Student> deleteStudent (@PathVariable int studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = "age")
    public ResponseEntity<List<Student>> getStudentsByAge (@RequestParam int age) {
        if (studentService.getStudentsByAge(age).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentService.getStudentsByAge(age));
    }

    @GetMapping(params = {"minAge", "maxAge"})
    public ResponseEntity<List<Student>> findByAgeBetween (@RequestParam int minAge, @RequestParam int maxAge) {
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

    @GetMapping("{studentId}/avatar/from-db")
    public ResponseEntity<byte[]> downloadAvatar (@PathVariable long studentId) {
        Avatar avatar = avatarService.findStudentAvatar(studentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping("{studentId}/avatar/from-fs")
    public void downloadAvatar (@PathVariable long studentId, HttpServletResponse response) throws IOException{
        Avatar avatar = avatarService.findStudentAvatar(studentId);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
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

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Student>> getStudentsByName(@PathVariable String name) {
        return ResponseEntity.ok(studentService.getStudentsByName(name));
    }

    @GetMapping("/names/sorted/a")
    public ResponseEntity<List<Student>> getSortedStudents() {
        return ResponseEntity.ok(studentService.getSortedStudents());
    }

    @GetMapping("/get-age-average-stream")
    public ResponseEntity<Double> getStudentAgeAverageByStream() {
        return ResponseEntity.ok(studentService.getStudentAgeAverageByStream());
    }

}
