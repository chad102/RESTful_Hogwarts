package ru.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int minAge, int maxAge);
    List<Student> findByFaculty_Id(int facultyId);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getCountOfAllStudents();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double getStudentAgeAverage();

}
