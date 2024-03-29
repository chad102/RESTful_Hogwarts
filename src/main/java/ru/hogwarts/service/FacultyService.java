package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class FacultyService {
    Map<Integer, Faculty> facultyMap = new HashMap<>();
    int facultyId = 1;
    public Faculty createFaculty (Faculty faculty) {
        faculty.setId(++facultyId);
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFaculty(int facultyId) {
        return facultyMap.get(facultyId);
    }

    public Faculty updateFaculty(int facultyId, Faculty faculty) {
        facultyMap.put(facultyId, faculty);
        return faculty;
    }

    public Faculty deleteFaculty(int facultyId) {
        return facultyMap.remove(facultyId);
    }

    public List<Faculty> getAllFaculties(){
        return facultyMap.values().stream().toList();
    }

    public List<Faculty> getFacultyByColor(String color){
        List<Faculty> facultyListByColor = new ArrayList<>();
        facultyListByColor = facultyMap.values().stream().
                filter(faculty -> faculty.getColor().equals(color)).collect(Collectors.toList());
        return facultyListByColor;
    }
}
