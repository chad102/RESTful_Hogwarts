package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.exceptions.FacultyNotFoundException;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;

import java.util.List;

@Service

public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty (Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(int facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
    }

    public Faculty updateFaculty(int facultyId, Faculty faculty) {
        return facultyRepository.findById(facultyId)
                .map(oldFaculty -> {
                    oldFaculty.setName(faculty.getName());
                    oldFaculty.setColor(faculty.getColor());
                return facultyRepository.save(oldFaculty);
                })
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
    }

    public void deleteFaculty(int facultyId) {
        facultyRepository.findById(facultyId)
                .map(faculty -> {
                     facultyRepository.delete(faculty);
                     return faculty;
                })
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
    }

    public List<Faculty> findByColorOrName(String colorOrName){
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }

    public List<Student> findStudentsByFaculty (int facultyId) {
        return studentRepository.findByFaculty_Id(facultyId);
    }
}
