package com.taskmanager;

import com.taskmanager.enums.Priority;
import com.taskmanager.enums.Status;
import com.taskmanager.models.Project;
import com.taskmanager.models.Task;
import com.taskmanager.service.ProjectService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TaskProjectManagerApp {
    private static final ProjectService service = new ProjectService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        System.out.println("=== Task & Project Manager CLI ===");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput("Enter choice: ");

            switch (choice) {
                case 1 -> createProject();
                case 2 -> listProjects();
                case 3 -> createTask();
                case 4 -> listTasks();
                case 5 -> updateTaskStatus();
                case 6 -> showOverdueTasks();
                case 7 -> showProjectProgress();
                case 0 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Create Project");
        System.out.println("2. List All Projects");
        System.out.println("3. Create Task");
        System.out.println("4. List Tasks in Project");
        System.out.println("5. Update Task Status");
        System.out.println("6. Show Overdue Tasks");
        System.out.println("7. Show Project Progress");
        System.out.println("0. Exit");
    }

    private static void createProject() {
        System.out.print("Project Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        Project p = service.createProject(name, desc);
        System.out.println("Project created: " + p);
    }

    private static void listProjects() {
        List<Project> projects = service.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects yet.");
            return;
        }
        projects.forEach(System.out::println);
    }

    private static void createTask() {
        listProjects();
        System.out.print("Enter Project ID: ");
        String projectId = scanner.nextLine();

        System.out.print("Task Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();

        System.out.println("Priority (LOW/MEDIUM/HIGH/CRITICAL): ");
        Priority priority = Priority.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Due Date (yyyy-MM-dd HH:mm) or leave blank: ");
        String dateStr = scanner.nextLine();
        LocalDateTime dueDate = dateStr.isBlank() ? null : LocalDateTime.parse(dateStr, formatter);

        Task task = service.createTask(projectId, title, desc, priority, dueDate);
        System.out.println("Task created: " + task);
    }

    private static void listTasks() {
        listProjects();
        System.out.print("Enter Project ID: ");
        String projectId = scanner.nextLine();
        List<Task> tasks = service.getTasksByProject(projectId);
        tasks.forEach(System.out::println);
    }

    private static void updateTaskStatus() {
        // Simple implementation - list all tasks first
        List<Task> allTasks = service.getAllTasks(); // add this method if needed in service
        // ... (implement similarly)
        System.out.println("Update task status feature - extend as needed");
    }

    private static void showOverdueTasks() {
        List<Task> overdue = service.getOverdueTasks();
        System.out.println("Overdue Tasks:");
        overdue.forEach(System.out::println);
    }

    private static void showProjectProgress() {
        listProjects();
        System.out.print("Enter Project ID: ");
        String id = scanner.nextLine();
        double progress = service.getProjectProgress(id);
        System.out.printf("Project Progress: %.1f%%\n", progress);
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Invalid input. " + prompt);
        }
        return scanner.nextInt();
    }
}
