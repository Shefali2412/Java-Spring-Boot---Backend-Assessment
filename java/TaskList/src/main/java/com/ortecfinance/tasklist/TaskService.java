package com.ortecfinance.tasklist;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
// import java.util.ArrayList;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;

@Service
public class TaskService {
    private final Map<String, List<Task>> tasks = new LinkedHashMap<>();
    private final Map<String, String> projectNames = new HashMap<>();
    private long lastProjectId = 0;
    private long lastTaskId = 0;

    public String addProject(String name) {
        String projectId = "P" + (++lastProjectId);
        projectNames.put(projectId, name);
        tasks.put(projectId, new ArrayList<>());
        return projectId;
    }

    public long addTask(String project, String description) {
        List<Task> projectTasks = tasks.get(project);
        if (projectTasks == null) {
            throw new IllegalArgumentException(String.format("Could not find a project with the ID \"%s\".", project));
        }
        Task newTask = new Task(nextTaskId(), description, false);
        projectTasks.add(newTask);
        return newTask.getId();
    }
    

    public void setDone(long taskId, boolean done) {
        for (List<Task> projectTasks : tasks.values()) {
            for (Task task : projectTasks) {
                if (task.getId() == taskId) {
                    task.setDone(done);
                    return;
                }
            }
        }
        throw new IllegalArgumentException(String.format("Could not find a task with an ID of %d.", taskId));
    }

    public void setDeadline(long taskId, String deadline) { 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
        LocalDate parsedDeadline = LocalDate.parse(deadline, formatter); 
        for (List<Task> projectTasks : tasks.values()) { 
            for (Task task : projectTasks) { 
                if (task.getId() == taskId) { 
                    task.setDeadline(parsedDeadline); 
                    return; 
                } 
            } 
        } 
        throw new IllegalArgumentException(String.format("Task %d not found.", taskId)); 
    }
    
  public Map<String, Map<String, List<Map<String, Object>>>> viewByDeadline() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    Map<String, Map<String, List<Map<String, Object>>>> tasksByDeadlineAndProject = new LinkedHashMap<>();

    for (Map.Entry<String, List<Task>> projectEntry : tasks.entrySet()) {
        String projectId = projectEntry.getKey();
        String projectName = projectNames.get(projectId);

        for (Task task : projectEntry.getValue()) {
            LocalDate deadline = task.getDeadline();
            String formattedDeadline = (deadline != null) ? deadline.format(formatter) : "No Deadline";

            Map<String, Object> taskDetails = new LinkedHashMap<>();
            taskDetails.put("id", task.getId());
            taskDetails.put("description", task.getDescription());
            taskDetails.put("done", task.isDone());

            tasksByDeadlineAndProject
                .computeIfAbsent(formattedDeadline, k -> new LinkedHashMap<>())
                .computeIfAbsent(projectName, k -> new ArrayList<>())
                .add(taskDetails);
        }
    }
    return tasksByDeadlineAndProject;
}


    private long nextTaskId() {
        return ++lastTaskId;
    }

    public Map<String, List<Task>> getTasks() {
        return tasks;
    }
}



