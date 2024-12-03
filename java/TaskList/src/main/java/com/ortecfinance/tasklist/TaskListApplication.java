package com.ortecfinance.tasklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class TaskListApplication {

//     public static void main(String[] args) {
//         if (args.length == 0) {
//             System.out.println("Starting console Application");
//             TaskList.startConsole();
//         }
//         else {
//             SpringApplication.run(TaskListApplication.class, args);
//             System.out.println("localhost:8080/tasks");
//         }
//     }

// }

@SpringBootApplication public class TaskListApplication { 
    public static void main(String[] args) { 
        if (args.length > 0 && "console".equals(args[0])) { 
            System.out.println("Starting console Application"); 
            TaskList.startConsole(); 
        } else { 
            SpringApplication.run(TaskListApplication.class, args); 
            System.out.println("Server started at http://localhost:8080/tasks"); 
        } 
    } 
}
