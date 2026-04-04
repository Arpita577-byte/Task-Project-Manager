# Architecture Documentation

## Design Patterns Used

- **Layered Architecture**: Presentation (CLI) → Service → Repository → Model
- **Repository Pattern**: Abstracts data access
- **Dependency Injection**: Service depends on Repository
- **Builder-like fluent setters** with timestamp tracking
- **Enum usage** for Status and Priority

## Package Structure

- `enums/`          → Status and Priority
- `models/`         → Task and Project domain models
- `repository/`     → Data access layer with JSON persistence
- `service/`        → Business logic
- `TaskProjectManagerApp.java` → CLI entry point

## Key Design Decisions

- In-memory storage with `ConcurrentHashMap` for thread-safety
- Automatic JSON persistence on every save
- Rich domain models with business methods (`isOverdue()`, `getCompletionPercentage()`, etc.)
- Immutable IDs using UUID

## Future Improvements

- Add user authentication
- Support for subtasks
- Export to CSV/PDF
- Web UI using Spring Boot
