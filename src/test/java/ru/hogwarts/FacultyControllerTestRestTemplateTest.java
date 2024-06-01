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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import ru.hogwarts.model.Faculty;
import ru.hogwarts.model.Student;
import ru.hogwarts.repository.AvatarRepository;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;


import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplateTest {

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
    }

    private Faculty createFaculty(){
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return facultyRepository.save(faculty);
    }


    public String buildUrl(String uriStartsWithSlash){
        return "http://localhost:%d%s".formatted(port, uriStartsWithSlash);
    }

    @Test
    public void createFacultyTest() throws JsonProcessingException {
        Faculty faculty = new Faculty();

        faculty.setColor("Красный");
        faculty.setName("Гриффиндор");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(faculty), headers);


        ResponseEntity<Faculty> responseEntity = testRestTemplate.postForEntity(
                buildUrl("/faculty"),
                request,
                Faculty.class
        );

        Faculty created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();
        assertThat(created).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
        assertThat(created.getId()).isNotNull();

        Optional<Faculty> fromDb = facultyRepository.findById(created.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(created);
    }

    @Test
    public void getFacultyTest() throws JsonProcessingException {
        Faculty faculty = new Faculty();

        faculty.setColor("Красный");
        faculty.setName("Гриффиндор");

        faculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> responseEntity = testRestTemplate.getForEntity(
                buildUrl("/faculty/" + faculty.getId()),
                Faculty.class
        );

        Faculty created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();

        Optional<Faculty> fromDb = facultyRepository.findById(created.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(created);
    }

    @Test
    public void updateFacultyTest() throws JsonProcessingException {
        Faculty faculty = new Faculty();

        faculty.setColor("Красный");
        faculty.setName("Гриффиндор");
        faculty.setId(1);

        faculty = facultyRepository.save(faculty);

        HttpEntity<Faculty> request = new HttpEntity<>(faculty);

        ResponseEntity<Faculty> responseEntity = testRestTemplate.exchange(
                buildUrl("/faculty/" + faculty.getId()),
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        Faculty updated = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated).isNotNull();
        assertThat(updated).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
        assertThat(updated.getId()).isNotNull();

        Optional<Faculty> fromDb = facultyRepository.findById(updated.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    public void deleteFacultyTest() throws JsonProcessingException {
        Faculty faculty = new Faculty();

        faculty.setColor("Красный");
        faculty.setName("Гриффиндор");
        faculty.setId(1);

        faculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> responseEntity = testRestTemplate.exchange(
                buildUrl("/faculty/" + faculty.getId()),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyRepository.findById(faculty.getId())).isNotPresent();
    }
}