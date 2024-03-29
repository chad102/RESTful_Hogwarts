package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    Map<Integer, Student> studentMap = new HashMap<>();
    int studentIdCount = 0;
    public Student createStudent (Student student) {
        student.setId(++studentIdCount);
        studentMap.put(student.getId(), student);
        System.out.println(studentMap);
        return student;
    }

    public Student getStudent(int studentId) {
        return studentMap.get(studentId);
    }

    public Student updateStudent(int studentId, Student student) {
        studentMap.put(studentId, student);
        return student;
    }

    public Student deleteStudent(int studentId) {
        return studentMap.remove(studentId);
    }

    public List<Student> getStudentsByAge(int age){
        List<Student> studentListByAge = new ArrayList<>();
        studentListByAge = studentMap.values().stream().
                filter(student -> student.getAge() == age).collect(Collectors.toList());
        return  studentListByAge;
    }
}
