package ru.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty (Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(int facultyId) {
        logger.debug("Requesting faculty by id: {}", facultyId);
        logger.info("Was invoked method for get faculty by id");
        logger.warn("The object you are looking for may not be found");
        logger.error("There is not faculty with id = " + facultyId);
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
    }

    public Faculty updateFaculty(int facultyId, Faculty faculty) {
        logger.debug("Requesting faculty by id: {}", facultyId);
        logger.info("Was invoked method for update faculty");
        logger.warn("The object you are updating may not be found");
        logger.error("There is not faculty with id = " + facultyId);
        return facultyRepository.findById(facultyId)
                .map(oldFaculty -> {
                    oldFaculty.setName(faculty.getName());
                    oldFaculty.setColor(faculty.getColor());
                return facultyRepository.save(oldFaculty);
                })
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
    }

    public Faculty deleteFaculty(int facultyId) {
        logger.debug("Requesting faculty by id: {}", facultyId);
        logger.info("Was invoked method for delete faculty");
        logger.warn("The object you are deleting may not be found");
        logger.error("There is not faculty with id = " + facultyId);
        return facultyRepository.findById(facultyId)
                .map(faculty -> {
                     facultyRepository.delete(faculty);
                     return faculty;
                })
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
    }

    public List<Faculty> findByColorOrName(String colorOrName){
        logger.info("Was invoked method for get faculty by color or name");
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }

    public List<Student> findStudentsByFaculty (int facultyId) {
        logger.info("Was invoked method for get students by faculty id");
        return studentRepository.findByFaculty_Id(facultyId);
    }

    public List<Faculty> findByNameAndColor(String name, String color) {
        logger.info("Was invoked method for get faculty by name and color");
        return facultyRepository.findByNameIgnoreCaseAndColorIgnoreCase(name, color);
    }
}
