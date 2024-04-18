package ru.hogwarts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.exceptions.StudentNotFoundException;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent (Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public Student updateStudent(long studentId, Student student) {
        return studentRepository.findById(studentId)
                .map(oldStudent-> {
                    oldStudent.setName(student.getName());
                    oldStudent.setAge(student.getAge());
                    return studentRepository.save(oldStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public Student deleteStudent(long studentId) {
        return studentRepository.findById(studentId)
                .map(student-> {
                    studentRepository.delete(student);
                    return student;
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public List<Student> getStudentsByAge(int age){
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge){
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findFaculty (long studentId) {
        return getStudent(studentId).getFaculty();
    }
}
