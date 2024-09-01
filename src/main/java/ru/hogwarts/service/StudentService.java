package ru.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.exceptions.StudentNotFoundException;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private int count = 1;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;

    }

    public Student createStudent (Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student getStudent(long studentId) {
        logger.debug("Requesting student by id: {}", studentId);
        logger.info("Was invoked method for get student by ID");
        logger.warn("The object you are looking for may not be found");
        logger.error("There is not student with id = " + studentId); // если сработает исключение, то на экран выведется
        // месседж который прописан в StudentNotFoundException. Тогда куда добавлять эту строку? в сервис или в
        // эксепшен? и если в эксепшен зачем дублировать месседж?
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public Student updateStudent(long studentId, Student student) {
        logger.debug("Updating student: {}", studentId);
        logger.info("Was invoked method for update student");
        logger.warn("The object you are looking for may not be found");
        logger.error("There is not student with id = " + studentId);
        return studentRepository.findById(studentId)
                .map(oldStudent-> {
                    oldStudent.setName(student.getName());
                    oldStudent.setAge(student.getAge());
                    return studentRepository.save(oldStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public Student deleteStudent(long studentId) {
        logger.debug("Deleting student by id: {}", studentId);
        logger.info("Was invoked method for delete student by ID");
        logger.warn("The object you are deleting may not be found");
        logger.error("There is not student with id = " + studentId);
        return studentRepository.findById(studentId)
                .map(student-> {
                    studentRepository.delete(student);
                    return student;
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get student by age");
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for get student by age between");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findFaculty (long studentId) {
        logger.info("Was invoked method for get faculty by student ID");
        return getStudent(studentId).getFaculty();
    }

    public Integer getCountOfAllStudents() {
        logger.info("Was invoked method for get count of students");
        return studentRepository.getCountOfAllStudents();
    }

    public Double getStudentAgeAverage() {
        logger.info("Was invoked method for get average age student");
        return studentRepository.getStudentAgeAverage();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five student");
        Integer pageSize = 5;
        int pageNumber = getCountOfAllStudents() / pageSize;

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);  // не уверен, что нужно отнимать единицу при такой реализации
        return studentRepository.findAll(pageRequest).getContent();
    }

    public List<Student> getStudentsByName(String name) {
        logger.info("Was invoked method for get student by name");
        return studentRepository.getStudentsByName(name);
    }

    public List<Student> getSortedStudents() {
        logger.info("Was invoked method for get sorted names begins with the letter a");
        return studentRepository.findAll().stream().
                filter(e -> e.getName().toUpperCase().startsWith("A")).
                sorted().
                collect(Collectors.toList());
    }

    public Double getStudentAgeAverageByStream() {
        logger.info("Was invoked method for get average age students");
        return studentRepository.findAll().stream().
                mapToInt(Student :: getAge).
                average().
                orElseThrow(NullPointerException::new);
    }

    public void printParallel() {
        logger.info("Was invoked method for print parallel students names");
        List <Student> allStudents = studentRepository.findAll();
        String names = allStudents.stream().
                filter(e -> e.getId() == 1 || e.getId() == 2).
                map(Student :: getName).
                toList().
                toString();
        logger.info("Main thread " + names);

        new Thread(() -> {
            List <String> names2 = allStudents.stream().
                    filter(e -> e.getId() == 3 || e.getId() == 4).
                    map(Student :: getName).
                    toList();
            logger.info("2 thread " + names2);;
        }).start();

        new Thread(() -> {
            List <String> names3 = allStudents.stream().
                    filter(e -> e.getId() == 5 || e.getId() == 6).
                    map(Student :: getName).
                    toList();
            logger.info("3 thread " + names3);;
        }).start();
    }

    public synchronized void printStudentNamesInPairs(long name1, long name2) {

        System.out.println(studentRepository.findById(name1).
                map(Student ::getName).
                orElseThrow(NullPointerException::new) + " count " + count);
        System.out.println(studentRepository.findById(name2).
                map(Student ::getName).
                orElseThrow(NullPointerException::new) + " count " + count);
        count ++;
    }
    public void printSynchronized() {
        logger.info("Was invoked method for print synchronized students names");

        printStudentNamesInPairs(1L, 2L);

        new Thread(() -> {
            printStudentNamesInPairs(3L, 4L);
        }).start();

        new Thread(() -> {
            printStudentNamesInPairs(5L, 6L);
        }).start();
    }
}
