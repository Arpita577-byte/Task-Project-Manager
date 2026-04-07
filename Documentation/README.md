# Task & Project Manager

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.9.14-blue?style=flat-square)
![Swing](https://img.shields.io/badge/Swing-UI%20%26%20UX-blueviolet?style=flat-square)
![JUnit](https://img.shields.io/badge/JUnit-5-green?style=flat-square)

## GITHUB REPOSITORY :
https://github.com/Arpita577-byte/Task-Project-Manager

A modern **Desktop Application** with **Swing GUI** for efficient Task and Project Management.


## 📖 Introduction

**Task & Project Manager** is a feature-rich desktop application built in Java with **Swing** for a clean and user-friendly graphical interface. It helps individuals and teams manage projects and tasks effectively with an intuitive UI/UX.

Now upgraded with **Swing-based Graphical User Interface** for better user experience.

---

## ✨ Key Features

### Project Management
- Create, view, edit, and delete projects
- Project details with completion percentage

### Task Management
- Add, update, and delete tasks with rich form UI
- Task status management (`PENDING`, `IN_PROGRESS`, `COMPLETED`, `BLOCKED`, `ON_HOLD`)
- Priority levels: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`
- Due date picker support
- Add tags to tasks

### Smart Features
- Real-time project progress visualization
- Overdue tasks highlighting
- Responsive and modern Swing UI
- Persistent data storage (JSON)
- Clean separation of UI and business logic

---

## 🛠️ Tech Stack

- **Language**: Java 17
- **GUI Framework**: Java Swing (for modern UI/UX)
- **Build Tool**: Apache Maven
- **JSON Processing**: Google Gson
- **Testing**: JUnit 5
- **Architecture**: Layered Architecture (Model - Repository - Service - UI)

---

## 🚀 Quick Start

### Prerequisites
- Java Development Kit (JDK) 17 or higher

### Steps to Run
1. Open terminal / PowerShell in the project root folder.

2. Build the project:
   ```powershell
   mvn clean package

### Run the application:
 
java -jar target/task-project-manager-1.0.0-jar-with-dependencies.jar

### User Interface Highlights

1) Modern Swing-based GUI with improved UX

2) Easy-to-use forms for adding projects and tasks

3) Progress bars for visual project tracking

4) Tables to display tasks and projects

5) Buttons, labels, and dialogs for smooth interaction

6) Responsive layout for better usability

### Data Persistence

-All projects and tasks are automatically saved in taskmanager_data.json
-Data persists even after closing the application
-No external database required

### Testing
-Comprehensive test coverage included:
mvn test

## Project Structure

```plaintext
task-project-manager/
├── src/main/java/com/taskmanager/
│   ├── TaskProjectManagerApp.java          ← Main Swing UI Entry Point
│   ├── enums/
│   │   ├── Status.java
│   │   └── Priority.java
│   ├── models/
│   │   ├── Task.java
│   │   └── Project.java
│   ├── repository/
│   │   ├── IRepository.java
│   │   └── ProjectTaskRepository.java
│   └── service/
│       └── ProjectService.java
├── src/test/java/com/taskmanager/
│   ├── models/
│   │   ├── TaskTest.java
│   │   └── ProjectTest.java
│   └── service/
│       └── ProjectServiceTest.java
├── Documentation/
│   ├── README.md
│   ├── ARCHITECTURE.md
│   ├── API_REFERENCE.md
│   ├── INSTALLATION.md
│   ├── DEVELOPMENT.md
│   └── CONTRIBUTING.md
├── pom.xml
├── .gitignore
└── taskmanager_data.json (auto-generated)
```
---

### Documentation

- **ARCHITECTURE.md** — Application Architecture & Design Patterns  
- **INSTALLATION.md** — Setup Guide & Troubleshooting  
- **DEVELOPMENT.md** — Developer Guide  
- **CONTRIBUTING.md** — Contribution Guidelines  
- **API_REFERENCE.md** — API and Class Reference

---

### Future Enhancements

- Dark/Light theme support in Swing
- Drag & Drop task management
- Export to PDF, Excel or CSV
- Sub-tasks and hierarchical task structure
- Advanced search and filtering
- Reminder and notification system
- Multiple workspaces
- Web version using Spring Boot + Thymeleaf or JavaFX
- Calendar view and analytics reports
- User authentication and team collaboration

---

### License
This project is licensed under the MIT License.
  

