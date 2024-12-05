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

import java.util.*;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Default endpoint - additional endpoint 
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> defaultEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the Task API");
        response.put("instructions", "Refer to the API documentation for available endpoints.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Create Project 
    @PostMapping("/projects")
    public ResponseEntity<Map<String, String>> createProject(@RequestBody Map<String, String> payload) {
        String projectName = payload.get("projectName");
        String projectId = taskService.addProject(projectName);

       // show project ID and the success message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Project created successfully");
        response.put("projectId", projectId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
}
    //Create Task
    @PostMapping("/projects/{project_id}/tasks")
    public ResponseEntity<Map<String, String>> createTask(@PathVariable String project_id, @RequestBody Map<String, String> payload) {
         System.out.println("project_id: " + project_id);
         System.out.println("description: " + payload.get("description"));
    
         String description = payload.get("description");
         long taskId = taskService.addTask(project_id, description);

         // show task ID and the success message
         Map<String, String> response = new HashMap<>();
         response.put("message", "Task created successfully");
         response.put("taskId", String.valueOf(taskId));

         return new ResponseEntity<>(response, HttpStatus.CREATED);
}
    //add Task deadline
    @PutMapping("/projects/{project_id}/tasks/{task_id}") 
    public ResponseEntity<String> updateTaskDeadline(@PathVariable String project_id, @PathVariable long task_id, @RequestParam String deadline) { 
        taskService.setDeadline(project_id, task_id, deadline); 
        return new ResponseEntity<>("Task deadline updated successfully", HttpStatus.OK); 
    }
    //View tasks by deadline
    @GetMapping("/projects/view_by_deadline")
    public ResponseEntity<Map<String, Map<String, List<Map<String, Object>>>>> viewByDeadline() {
        Map<String, Map<String, List<Map<String, Object>>>> tasksByDeadline = taskService.viewByDeadline();
        return new ResponseEntity<>(tasksByDeadline, HttpStatus.OK);
}

    //NOTE: Below are additional endpoint added by me, as I am experimenting and practicing - this endpoints are not mentioned in README file

    // get tasks - show all tasks witout deadline
    @GetMapping("/all_tasks")
    public ResponseEntity<List<Map<String, Object>>> getAllTasks() {
    List<Map<String, Object>> tasks = taskService.getTasks()
        .entrySet()
        .stream()
        .flatMap(entry -> entry.getValue().stream().map(task -> {
            Map<String, Object> taskDetails = new HashMap<>();
            taskDetails.put("project", entry.getKey()); // add project name
            taskDetails.put("description", task.getDescription()); // add task description
            taskDetails.put("done", task.isDone()); // indicate if the task is done
            return taskDetails;
        }))
        .toList();
      return new ResponseEntity<>(tasks, HttpStatus.OK);
     }

    //end point for done status
   @PutMapping("/projects/{project_id}/tasks/{task_id}/done")
   public ResponseEntity<String> updateTaskStatus(@PathVariable String project_id, @PathVariable long task_id, @RequestParam boolean done) {
        taskService.setDone(task_id, done);
        return new ResponseEntity<>("Task status updated successfully", HttpStatus.OK);

        }


}




