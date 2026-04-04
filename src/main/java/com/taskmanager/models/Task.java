package com.taskmanager.models;

import com.taskmanager.enums.Priority;
import com.taskmanager.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Task {
    private final String id;
    private String name;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String projectId;
    private List<String> tags;
    private int estimatedHours;

    // Constructor
    public Task(String title, String description, Priority priority, LocalDateTime dueDate, String projectId, String name) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.status = Status.PENDING;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.projectId = projectId;
        this.tags = new ArrayList<>();
        this.estimatedHours = 0;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getProjectId() { return projectId; }
    public List<String> getTags() { return new ArrayList<>(tags); }
    public int getEstimatedHours() { return estimatedHours; }

    // Setters with timestamp update
    public void setTitle(String title) { this.title = title; updateTimestamp(); }
    public void setDescription(String description) { this.description = description; updateTimestamp(); }
    public void setStatus(Status status) { this.status = status; updateTimestamp(); }
    public void setPriority(Priority priority) { this.priority = priority; updateTimestamp(); }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; updateTimestamp(); }
    public void setProjectId(String projectId) { this.projectId = projectId; updateTimestamp(); }
    public void setEstimatedHours(int estimatedHours) { this.estimatedHours = estimatedHours; updateTimestamp(); }

    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            updateTimestamp();
        }
    }

    public void removeTag(String tag) {
        tags.remove(tag);
        updateTimestamp();
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean isOverdue() {
        return dueDate != null && dueDate.isBefore(LocalDateTime.now()) && status != Status.COMPLETED;
    }

    public void markCompleted() {
        this.status = Status.COMPLETED;
        updateTimestamp();
    }

    public void startProgress() {
        this.status = Status.IN_PROGRESS;
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | Priority: %s | Due: %s",
                id.substring(0, 8), title, status, priority,
                dueDate != null ? dueDate.toLocalDate() : "No due date");
    }
}
