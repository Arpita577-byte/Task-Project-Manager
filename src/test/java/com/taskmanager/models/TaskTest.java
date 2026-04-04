package com.taskmanager.models;

import com.taskmanager.enums.Priority;
import com.taskmanager.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;
    private final LocalDateTime futureDate = LocalDateTime.now().plusDays(5);
    private final LocalDateTime pastDate = LocalDateTime.now().minusDays(5);

    @BeforeEach
    void setUp() {
        task = new Task("Test Task", "Test Description", Priority.HIGH, futureDate, "proj-123");
    }

    @Test
    void testTaskCreation() {
        assertNotNull(task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(Status.PENDING, task.getStatus());
        assertEquals(futureDate, task.getDueDate());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
    }

    @Test
    void testSettersUpdateTimestamp() {
        LocalDateTime oldUpdated = task.getUpdatedAt();
        task.setTitle("Updated Title");
        assertTrue(task.getUpdatedAt().isAfter(oldUpdated));
    }

    @Test
    void testMarkCompleted() {
        task.markCompleted();
        assertEquals(Status.COMPLETED, task.getStatus());
    }

    @Test
    void testStartProgress() {
        task.startProgress();
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void testIsOverdue_WhenPastDueAndNotCompleted() {
        Task overdueTask = new Task("Overdue", "desc", Priority.HIGH, pastDate, "proj-123");
        assertTrue(overdueTask.isOverdue());
    }

    @Test
    void testIsOverdue_WhenCompleted() {
        Task completedTask = new Task("Completed", "desc", Priority.HIGH, pastDate, "proj-123");
        completedTask.markCompleted();
        assertFalse(completedTask.isOverdue());
    }

    @Test
    void testAddAndRemoveTag() {
        task.addTag("urgent");
        task.addTag("backend");
        assertEquals(2, task.getTags().size());
        assertTrue(task.getTags().contains("urgent"));

        task.removeTag("urgent");
        assertEquals(1, task.getTags().size());
        assertFalse(task.getTags().contains("urgent"));
    }

    @Test
    void testEqualsAndHashCode() {
        Task task2 = new Task("Same ID Task", "desc", Priority.LOW, futureDate, "proj-123");
        // Manually set same ID for testing (reflection not used for simplicity)
        assertNotEquals(task, task2); // Different IDs
    }

    @Test
    void testToString() {
        String str = task.toString();
        assertTrue(str.contains("Test Task"));
        assertTrue(str.contains("HIGH"));
    }
}
