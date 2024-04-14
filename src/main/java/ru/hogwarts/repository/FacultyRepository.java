package ru.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    List<Faculty> findByColorIgnoreCase(String color);
    @Query(value = "SELECT f.* "
            + " from Student facultyId"
            + " where f.id=s.facultyId"
            , nativeQuery = true)
    Faculty findByStudent(long id);
}
