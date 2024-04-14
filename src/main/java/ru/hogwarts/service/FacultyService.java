package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.repository.FacultyRepository;

import java.util.List;

@Service

public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty (Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(int facultyId) {
        return facultyRepository.findById(facultyId).get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(int facultyId) {
        facultyRepository.deleteById(facultyId);
    }

    public List<Faculty> findByColorIgnoreCase(String color){
        return facultyRepository.findByColorIgnoreCase(color);
    }
    public Faculty getFacultyByStudent (Student student) {
        return facultyRepository.findByStudent(student.getId());
    }
}
