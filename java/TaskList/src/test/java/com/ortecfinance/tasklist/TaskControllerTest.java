package com.ortecfinance.tasklist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateProject() throws Exception {
        Map<String, String> projectPayload = new HashMap<>();
        projectPayload.put("projectName", "New Project");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Project created successfully");
        response.put("projectId", "P1");

        when(taskService.addProject(any(String.class))).thenReturn("P1");

        mockMvc.perform(post("/tasks/projects")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Project created successfully"))
                .andExpect(jsonPath("$.projectId").value("P1"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        Map<String, String> taskPayload = new HashMap<>();
        taskPayload.put("description", "New Task");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Task created successfully");
        response.put("taskId", "1");

        when(taskService.addTask(any(String.class), any(String.class))).thenReturn(1L);

        mockMvc.perform(post("/tasks/projects/P1/tasks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(taskPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Task created successfully"))
                .andExpect(jsonPath("$.taskId").value("1"));
    }

    @Test
    void shouldUpdateTaskDeadline() throws Exception {
        String formattedDate = "31-12-2024";

        mockMvc.perform(put("/tasks/projects/P1/tasks/1")
                .param("deadline", formattedDate))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deadline updated successfully"));
    }

    @Test
    void shouldViewTasksByDeadline() throws Exception {
        Map<String, Map<String, List<Map<String, Object>>>> projectTasks = new HashMap<>();
        Map<String, List<Map<String, Object>>> tasks = new HashMap<>();

        Map<String, Object> taskDetails = new HashMap<>();
        taskDetails.put("id", 1);
        taskDetails.put("description", "New Task");
        taskDetails.put("done", false);

        tasks.put("New Project", List.of(taskDetails));
        projectTasks.put("31-12-2024", tasks);

        when(taskService.viewByDeadline()).thenReturn(projectTasks);

        mockMvc.perform(get("/tasks/view_by_deadline"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['31-12-2024'].['New Project'][0].id").value(1))
                .andExpect(jsonPath("$.['31-12-2024'].['New Project'][0].description").value("New Task"))
                .andExpect(jsonPath("$.['31-12-2024'].['New Project'][0].done").value(false));
    }
}
