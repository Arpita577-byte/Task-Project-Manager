package com.taskmanager.models;


import com.taskmanager.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Project {
    private final String id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Task> tasks;

    public Project(String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.startDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.tasks = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<Task> getTasks() { return new ArrayList<>(tasks); }

    // Setters
    public void setName(String name) { this.name = name; updateTimestamp(); }
    public void setDescription(String description) { this.description = description; updateTimestamp(); }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; updateTimestamp(); }

    public void addTask(Task task) {
        task.setProjectId(this.id);
        if (!tasks.contains(task)) {
            tasks.add(task);
            updateTimestamp();
        }
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        updateTimestamp();
    }

    public int getCompletedTasksCount() {
        return (int) tasks.stream().filter(t -> t.getStatus() == Status.COMPLETED).count();
    }

    public double getCompletionPercentage() {
        if (tasks.isEmpty()) return 0.0;
        return (getCompletedTasksCount() * 100.0) / tasks.size();
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Project[%s] %s (%.1f%% completed | %d tasks)",
                id.substring(0, 8), name, getCompletionPercentage(), tasks.size());
    }
}
