package ru.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String color, String name);

    List<Faculty> findByNameIgnoreCaseAndColorIgnoreCase(String name, String color);
}
