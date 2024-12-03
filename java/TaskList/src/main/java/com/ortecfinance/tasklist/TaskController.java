package com.ortecfinance.tasklist;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;

    // @GetMapping
    // public List<String> getTasks() {
    //     return Arrays.asList("Task 1", "Task 2", "Task 3");
//      }
    }

    @PostMapping("/projects")
    public ResponseEntity<Map<String, String>> createProject(@RequestBody Map<String, String> payload) {
    String projectName = payload.get("projectName");
    String projectId = taskService.addProject(projectName);

    // Create a response map to hold the project ID and the success message
    Map<String, String> response = new HashMap<>();
    response.put("message", "Project created successfully");
    response.put("projectId", projectId);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
}

    @PostMapping("/projects/{project_id}/tasks")
    public ResponseEntity<Map<String, String>> createTask(@PathVariable String project_id, @RequestBody Map<String, String> payload) {
    System.out.println("Received project_id: " + project_id);
    System.out.println("Received description: " + payload.get("description"));
    
    String description = payload.get("description");
    long taskId = taskService.addTask(project_id, description);

    // Create a response map to hold the task ID and the success message
    Map<String, String> response = new HashMap<>();
    response.put("message", "Task created successfully");
    response.put("taskId", String.valueOf(taskId));

    return new ResponseEntity<>(response, HttpStatus.CREATED);
}

    @PutMapping("/projects/{project_id}/tasks/{task_id}") 
    public ResponseEntity<String> updateTaskDeadline(@PathVariable String project_id, @PathVariable long task_id, @RequestParam String deadline) { 
        taskService.setDeadline(task_id, deadline); 
        return new ResponseEntity<>("Task deadline updated successfully", HttpStatus.OK); 
    }

    @GetMapping("/view_by_deadline")
    public ResponseEntity<Map<String, Map<String, List<Map<String, Object>>>>> viewByDeadline() {
    Map<String, Map<String, List<Map<String, Object>>>> tasksByDeadline = taskService.viewByDeadline();
    return new ResponseEntity<>(tasksByDeadline, HttpStatus.OK);
}

}


