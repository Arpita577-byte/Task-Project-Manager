package com.taskmanager;

import com.taskmanager.ui.MainFrame;
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
        new MainFrame();
        System.out.println("=====================================");
        System.out.println("   Task & Project Manager CLI v1.0   ");
        System.out.println("=====================================");

        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> createProject();
                case 2 -> listAllProjects();
                case 3 -> createTask();
                case 4 -> listTasksInProject();
                case 5 -> updateTaskStatus();
                case 6 -> markTaskCompleted();
                case 7 -> showOverdueTasks();
                case 8 -> showProjectProgress();
                case 9 -> deleteProject();
                case 0 -> {
                    running = false;
                    System.out.println("Thank you for using Task & Project Manager. Goodbye!");
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Create New Project");
        System.out.println("2. List All Projects");
        System.out.println("3. Create New Task");
        System.out.println("4. List Tasks in a Project");
        System.out.println("5. Update Task Status");
        System.out.println("6. Mark Task as Completed");
        System.out.println("7. Show Overdue Tasks");
        System.out.println("8. Show Project Progress");
        System.out.println("9. Delete a Project");
        System.out.println("0. Exit");
        System.out.println("-----------------");
    }

    private static void createProject() {
        System.out.print("Enter Project Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        Project project = service.createProject(name, description);
        System.out.println("✅ Project created successfully!");
        System.out.println(project);
    }

    private static void listAllProjects() {
        List<Project> projects = service.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }
        System.out.println("\n--- All Projects ---");
        projects.forEach(System.out::println);
    }

    private static void createTask() {
        listAllProjects();
        if (service.getAllProjects().isEmpty()) {
            System.out.println("Please create a project first.");
            return;
        }

        System.out.print("\nEnter Project ID: ");
        String projectId = scanner.nextLine();

        System.out.print("Enter Task Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Task Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Priority (LOW / MEDIUM / HIGH / CRITICAL): ");
        Priority priority = Priority.valueOf(scanner.nextLine().toUpperCase().trim());

        System.out.print("Enter Due Date (yyyy-MM-dd HH:mm) or press Enter to skip: ");
        String dateInput = scanner.nextLine().trim();
        LocalDateTime dueDate = dateInput.isEmpty() ? null : LocalDateTime.parse(dateInput, formatter);

        try {
            Task task = service.createTask(projectId, title, description, priority, dueDate);
            System.out.println("✅ Task created successfully!");
            System.out.println(task);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void listTasksInProject() {
        listAllProjects();
        System.out.print("\nEnter Project ID: ");
        String projectId = scanner.nextLine();

        List<Task> tasks = service.getTasksByProject(projectId);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found in this project.");
            return;
        }
        System.out.println("\n--- Tasks in Project ---");
        tasks.forEach(System.out::println);
    }

    private static void updateTaskStatus() {
        System.out.print("Enter Task ID: ");
        String taskId = scanner.nextLine();
        System.out.println("Available Status: PENDING, IN_PROGRESS, COMPLETED, BLOCKED, ON_HOLD");
        System.out.print("Enter New Status: ");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase().trim());

        service.updateTaskStatus(taskId, status);
        System.out.println("✅ Task status updated successfully!");
    }

    private static void markTaskCompleted() {
        System.out.print("Enter Task ID: ");
        String taskId = scanner.nextLine();
        service.markTaskCompleted(taskId);
        System.out.println("✅ Task marked as COMPLETED!");
    }

    private static void showOverdueTasks() {
        List<Task> overdue = service.getOverdueTasks();
        if (overdue.isEmpty()) {
            System.out.println("No overdue tasks.");
            return;
        }
        System.out.println("\n--- Overdue Tasks ---");
        overdue.forEach(System.out::println);
    }

    private static void showProjectProgress() {
        listAllProjects();
        System.out.print("\nEnter Project ID: ");
        String projectId = scanner.nextLine();
        double progress = service.getProjectProgress(projectId);
        System.out.printf("Project Progress: %.1f%%\n", progress);
    }

    private static void deleteProject() {
        listAllProjects();
        System.out.print("\nEnter Project ID to delete: ");
        String projectId = scanner.nextLine();
        service.deleteProject(projectId);
        System.out.println("✅ Project deleted successfully.");
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Invalid input. " + prompt);
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return choice;
    }
}
