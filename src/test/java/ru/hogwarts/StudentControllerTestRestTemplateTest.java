package ru.hogwarts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import ru.hogwarts.model.Avatar;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.repository.AvatarRepository;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.swing.UIManager.get;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @AfterEach
    public void afterEach(){
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @BeforeEach
    public void beforeEach() {
        Faculty faculty1 = createFaculty();
        Faculty faculty2 = createFaculty();

        createStudents(faculty1);
        createStudents(faculty2);
    }

    private Faculty createFaculty(){
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return facultyRepository.save(faculty);
    }

    private void createStudents(Faculty faculty) {
        studentRepository.saveAll(Stream.generate(()->{
                            Student student = new Student();
                            student.setFaculty(faculty);
                            student.setName(faker.harryPotter().character());
                            student.setAge(faker.random().nextInt(11,18));
                            return student;
                        })
                        .limit(5)
                        .collect(Collectors.toList())
        );
    }


    public String buildUrl(String uriStartsWithSlash){
        return "http://localhost:%d%s".formatted(port, uriStartsWithSlash);
    }

    @Test
    public void createStudentTest() throws JsonProcessingException {
        Student student = new Student();

        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());
        student.setFaculty(facultyRepository.findAll(PageRequest.of(faker.random().nextInt(0,1), 1))
                .getContent()
                .get(0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(student), headers);


        ResponseEntity<Student> responseEntity = testRestTemplate.postForEntity(
                buildUrl("/student"),
                request,
                Student.class
                );

        Student created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();
        assertThat(created).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
        assertThat(created.getId()).isNotNull();

        Optional<Student> fromDb = studentRepository.findById(created.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(created);
    }

    @Test
    public void getStudentTest() throws JsonProcessingException {
        Student student = new Student();

        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());
        student.setId(faker.random().nextInt(1,999));
        student.setFaculty(facultyRepository.findAll(PageRequest.of(faker.random().nextInt(0,1), 1))
                .getContent()
                .get(0));
        student = studentRepository.save(student);

        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity(
                buildUrl("/student/" + student.getId()),
                Student.class
        );

        Student created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();

        Optional<Student> fromDb = studentRepository.findById(created.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(created);
    }

    @Test
    public void updateStudentTest() throws JsonProcessingException {
        Student student = new Student();

        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());
        student.setFaculty(facultyRepository.findAll(PageRequest.of(faker.random().nextInt(0,1), 1))
                .getContent()
                .get(0));
        student = studentRepository.save(student);

        HttpEntity<Student> request = new HttpEntity<>(student);

        ResponseEntity<Student> responseEntity = testRestTemplate.exchange(
                buildUrl("/student/" + student.getId()),
                HttpMethod.PUT,
                request,
                Student.class
        );

        Student updated = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated).isNotNull();
        assertThat(updated).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
        assertThat(updated.getId()).isNotNull();

        Optional<Student> fromDb = studentRepository.findById(updated.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    public void deleteStudentTest() throws JsonProcessingException {
        Student student = new Student();

        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());
        student.setFaculty(facultyRepository.findAll(PageRequest.of(faker.random().nextInt(0,1), 1))
                .getContent()
                .get(0));
        student = studentRepository.save(student);

        ResponseEntity<Student> responseEntity = testRestTemplate.exchange(
                buildUrl("/student/" + student.getId()),
                HttpMethod.DELETE,
                null,
                Student.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRepository.findById(student.getId())).isNotPresent();
    }
}
