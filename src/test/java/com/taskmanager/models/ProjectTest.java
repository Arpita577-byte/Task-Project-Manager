package com.taskmanager.models;

import com.taskmanager.enums.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    private Project project;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        project = new Project("Test Project", "Test Description");
        task1 = new Task("Task 1", "Desc 1", Priority.HIGH, LocalDateTime.now().plusDays(3), project.getId());
        task2 = new Task("Task 2", "Desc 2", Priority.MEDIUM, LocalDateTime.now().plusDays(5), project.getId());
    }

    @Test
    void testProjectCreation() {
        assertNotNull(project.getId());
        assertEquals("Test Project", project.getName());
        assertEquals("Test Description", project.getDescription());
        assertNotNull(project.getStartDate());
        assertNotNull(project.getCreatedAt());
        assertTrue(project.getTasks().isEmpty());
    }

    @Test
    void testAddTask() {
        project.addTask(task1);
        assertEquals(1, project.getTasks().size());
        assertEquals(project.getId(), task1.getProjectId());
    }

    @Test
    void testRemoveTask() {
        project.addTask(task1);
        project.addTask(task2);
        project.removeTask(task1);
        assertEquals(1, project.getTasks().size());
    }

    @Test
    void testGetCompletedTasksCount() {
        project.addTask(task1);
        project.addTask(task2);
        assertEquals(0, project.getCompletedTasksCount());

        task1.markCompleted();
        assertEquals(1, project.getCompletedTasksCount());
    }

    @Test
    void testGetCompletionPercentage() {
        project.addTask(task1);
        project.addTask(task2);
        assertEquals(0.0, project.getCompletionPercentage(), 0.001);

        task1.markCompleted();
        assertEquals(50.0, project.getCompletionPercentage(), 0.001);

        task2.markCompleted();
        assertEquals(100.0, project.getCompletionPercentage(), 0.001);
    }

    @Test
    void testEmptyProjectCompletion() {
        assertEquals(0.0, project.getCompletionPercentage());
    }

    @Test
    void testToString() {
        project.addTask(task1);
        String str = project.toString();
        assertTrue(str.contains("Test Project"));
        assertTrue(str.contains("0.0%"));
    }
}
