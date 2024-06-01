package ru.hogwarts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.controller.FacultyController;
import ru.hogwarts.model.Faculty;

import ru.hogwarts.service.FacultyService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @Test
    public void createFacultyWebMvcTest() throws Exception {

        int facultyId = 1;
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        faculty.setId(facultyId);

        Faculty savedFaculty = new Faculty();
        savedFaculty.setName("Гриффиндор");
        savedFaculty.setColor("Красный");
        savedFaculty.setId(facultyId);

        when(facultyService.createFaculty(faculty)).thenReturn(savedFaculty);

        // when
        ResultActions perform = mockMvc.perform(post("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        // then
        perform
                .andExpect(jsonPath("$.id").value(savedFaculty.getId()))
                .andExpect(jsonPath("$.name").value(savedFaculty.getName()))
                .andExpect(jsonPath("$.color").value(savedFaculty.getColor()))
                .andDo(print());
    }

    @Test
    public void getFacultyWebMvcTest() throws Exception {

        int facultyId = 1;
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        faculty.setId(facultyId);

        when(facultyService.getFaculty(faculty.getId())).thenReturn(faculty);

        // when
        ResultActions perform = mockMvc.perform(get("/faculty/{facultyId}", facultyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        // then
        perform
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    public void updateFacultyWebMvcTest() throws Exception {

        int facultyId = 1;
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        faculty.setId(facultyId);

        when(facultyService.updateFaculty(faculty.getId(), faculty)).thenReturn(faculty);

        // when
        ResultActions perform = mockMvc.perform(put("/faculty/{facultyId}", facultyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        // then
        perform
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    public void deleteFacultyWebMvcTest() throws Exception {

        int facultyId = 1;
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        faculty.setId(facultyId);

        when(facultyService.getFaculty(faculty.getId())).thenReturn(faculty);

        // when
        ResultActions perform = mockMvc.perform(delete("/faculty/{facultyId}", facultyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print());
    }
}