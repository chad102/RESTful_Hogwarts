package ru.hogwarts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.controller.StudentController;
import ru.hogwarts.model.Student;
import ru.hogwarts.service.AvatarService;
import ru.hogwarts.service.StudentService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @MockBean
    private AvatarService avatarService;

    @Test
    public void createStudentWebMvcTest() throws Exception {

        int studentId = 1;
        Student student = new Student();
        student.setName("Vasya");
        student.setAge(25);
        student.setId(studentId);

        Student savedStudent = new Student();
        savedStudent.setName("Vasya");
        savedStudent.setAge(25);
        savedStudent.setId(studentId);

        when(studentService.createStudent(student)).thenReturn(savedStudent);

        // when
        ResultActions perform = mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        // then
        perform
                .andExpect(jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.name").value(savedStudent.getName()))
                .andExpect(jsonPath("$.age").value(savedStudent.getAge()))
                .andDo(print());
    }

    @Test
    public void getStudentWebMvcTest() throws Exception {

        int studentId = 1;
        Student student = new Student();
        student.setName("Vasya");
        student.setAge(25);
        student.setId(studentId);

        when(studentService.getStudent(student.getId())).thenReturn(student);

        // when
        ResultActions perform = mockMvc.perform(get("/student/{studentId}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        // then
        perform
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    public void updateStudentWebMvcTest() throws Exception {

        int studentId = 1;
        Student student = new Student();
        student.setName("Vasya");
        student.setAge(25);
        student.setId(studentId);

        when(studentService.updateStudent(studentId, student)).thenReturn(student);

        // when
        ResultActions perform = mockMvc.perform(put("/student/{studentId}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        // then
        perform
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    public void deleteStudentWebMvcTest() throws Exception {

        int studentId = 1;
        Student student = new Student();
        student.setName("Vasya");
        student.setAge(25);
        student.setId(studentId);

        when(studentService.deleteStudent(studentId)).thenReturn(student);

        // when
        ResultActions perform = mockMvc.perform(delete("/student/{studentId}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print());
    }
}
