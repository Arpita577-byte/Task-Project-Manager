package com.taskmanager.service;

import com.taskmanager.enums.Priority;
import com.taskmanager.enums.Status;
import com.taskmanager.models.Project;
import com.taskmanager.models.Task;
import com.taskmanager.repository.ProjectTaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectService {
    private final ProjectTaskRepository repository;

    public ProjectService() {
        this.repository = new ProjectTaskRepository();
    }

    // Project operations (25+ methods possible - here are key ones)
    public Project createProject(String name, String description) {
        Project project = new Project(name, description);
        repository.save(project);
        return project;
    }

    public Optional<Project> getProjectById(String id) {
        return repository.findById(id).filter(p -> p instanceof Project).map(p -> (Project) p);
    }

    public List<Project> getAllProjects() {
        return repository.findAllProjects();
    }

    public void deleteProject(String id) {
        repository.deleteById(id);
    }

    public void updateProjectName(String id, String newName) {
        getProjectById(id).ifPresent(p -> {
            p.setName(newName);
            repository.save(p);
        });
    }

    // Task operations
    public Task createTask(String projectId, String title, String description, Priority priority, LocalDateTime dueDate) {
        Project project = getProjectById(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Task task = new Task(title, description, priority, dueDate, projectId);
        project.addTask(task);
        repository.save(task);
        repository.save(project);
        return task;
    }

    public List<Task> getTasksByProject(String projectId) {
        return repository.findTasksByProjectId(projectId);
    }

    public void updateTaskStatus(String taskId, Status status) {
        repository.findById(taskId).filter(t -> t instanceof Task).map(t -> (Task) t).ifPresent(task -> {
            task.setStatus(status);
            repository.save(task);
        });
    }

    public List<Task> getOverdueTasks() {
        return repository.findAllTasks().stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return repository.findAllTasks().stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    // Additional business methods
    public double getProjectProgress(String projectId) {
        return getProjectById(projectId).map(Project::getCompletionPercentage).orElse(0.0);
    }

    public void markTaskCompleted(String taskId) {
        repository.findById(taskId).filter(t -> t instanceof Task).map(t -> (Task) t).ifPresent(Task::markCompleted);
    }

    // ... (you can extend with 20+ more methods like filtering, statistics, etc.)
}
