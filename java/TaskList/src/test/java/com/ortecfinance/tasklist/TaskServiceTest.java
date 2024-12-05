package com.ortecfinance.tasklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void testAddProject() {
        String projectId = taskService.addProject("Project A");
        assertNotNull(projectId);
        assertEquals("P1", projectId);

        assertTrue(taskService.getTasks().containsKey(projectId));
    }

    @Test
    void testAddTaskToValidProject() {
        String projectId = taskService.addProject("Project A");
        long taskId = taskService.addTask(projectId, "Task 1");

        assertNotNull(taskId);
        assertEquals(1, taskId);

        Task task = taskService.getTasks().get(projectId).get(0);
        assertEquals("Task 1", task.getDescription());
        assertFalse(task.isDone());
    }

    @Test
    void testAddTaskToInvalidProject() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.addTask("INVALID_PROJECT", "Task 1");
        });
        assertEquals("Could not find a project with the ID \"INVALID_PROJECT\".", exception.getMessage());
    }

    @Test
    void testSetDoneForValidTask() {
        String projectId = taskService.addProject("Project A");
        long taskId = taskService.addTask(projectId, "Task 1");

        assertDoesNotThrow(() -> taskService.setDone(taskId, true));

        Task task = taskService.getTasks().get(projectId).get(0);
        assertTrue(task.isDone());
    }

    @Test
    void testSetDoneForInvalidTask() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.setDone(999L, true);
        });
        assertEquals("Task with ID '999' not found.", exception.getMessage());
    }

    @Test
    void testSetDeadlineForValidTask() {
        String projectId = taskService.addProject("Project A");
        long taskId = taskService.addTask(projectId, "Task 1");

        assertDoesNotThrow(() -> taskService.setDeadline(projectId, taskId, "25-12-2024"));

        Task task = taskService.getTasks().get(projectId).get(0);
        assertEquals(LocalDate.of(2024, 12, 25), task.getDeadline());
    }

    @Test
    void testSetDeadlineForInvalidProject() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.setDeadline("INVALID_PROJECT", 101L, "25-12-2024");
        });
        assertEquals("Could not find a project with the ID \"INVALID_PROJECT\".", exception.getMessage());
    }

    @Test
    void testSetDeadlineForInvalidTaskInValidProject() {
        String projectId = taskService.addProject("Project A");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.setDeadline(projectId, 999L, "25-12-2024");
        });
        assertEquals(String.format("Task with ID '%d' not found in project '%s'.", 999L, projectId), exception.getMessage());
    }

    @Test
    void testViewByDeadline() {
        String projectId = taskService.addProject("Project A");
        long task1Id = taskService.addTask(projectId, "Task 1");
        long task2Id = taskService.addTask(projectId, "Task 2");

        taskService.setDeadline(projectId, task1Id, "25-12-2024");
        taskService.setDeadline(projectId, task2Id, "25-12-2024");

        var result = taskService.viewByDeadline();
        assertEquals(1, result.size());
        assertTrue(result.containsKey("25-12-2024"));

        var tasksByProject = result.get("25-12-2024");
        assertTrue(tasksByProject.containsKey("Project A"));
        assertEquals(2, tasksByProject.get("Project A").size());
    }

    @Test
    void testViewByDeadlineWithNoTasksButProjectExists() {
         taskService.addProject("Project A");

          var result = taskService.viewByDeadline();
          assertTrue(result.isEmpty());
    }
}
