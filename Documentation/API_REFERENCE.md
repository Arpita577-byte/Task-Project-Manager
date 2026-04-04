# API Reference

## ProjectService Key Methods

- `createProject(String name, String description)`
- `createTask(...)`
- `getAllProjects()`
- `getTasksByProject(String projectId)`
- `updateTaskStatus(String taskId, Status status)`
- `getProjectProgress(String projectId)`
- `getOverdueTasks()`

## Model Methods

**Task:**
- `markCompleted()`, `startProgress()`, `isOverdue()`

**Project:**
- `addTask(Task task)`, `getCompletionPercentage()`
