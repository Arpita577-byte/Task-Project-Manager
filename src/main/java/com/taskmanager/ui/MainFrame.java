

package com.taskmanager.ui;

import com.taskmanager.models.Project;
import com.taskmanager.models.Task;
import com.taskmanager.enums.Priority;
import com.taskmanager.enums.Status;
import com.taskmanager.repository.ProjectTaskRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class MainFrame extends JFrame {

    private ProjectTaskRepository repo = new ProjectTaskRepository();
    private JTable projectTable;
    private JTable taskTable;

    public MainFrame() {
        setTitle("Task Manager");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadProjects();
    }

    private void initComponents() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(224, 242, 241)); // light teal

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(255, 248, 220)); // light yellow

        // Projects Table
        projectTable = new JTable();
        projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectTable.setFillsViewportHeight(true);
        projectTable.getSelectionModel().addListSelectionListener(this::onProjectSelected);
        projectTable.setBackground(new Color(204, 229, 255));
        JScrollPane projectScroll = new JScrollPane(projectTable);
        projectScroll.setBorder(BorderFactory.createTitledBorder("Projects"));
        leftPanel.add(projectScroll, BorderLayout.CENTER);

        // Project Buttons
        JPanel projectBtnPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        projectBtnPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        projectBtnPanel.setBackground(new Color(178, 223, 219)); // darker teal

        JButton addProjectBtn = styledButton("Add Project", new Color(0, 128, 128), Color.WHITE);
        addProjectBtn.addActionListener(e -> addProject());

        JButton editProjectBtn = styledButton("Edit Project", new Color(0, 153, 153), Color.WHITE);
        editProjectBtn.addActionListener(e -> editProject());

        JButton deleteProjectBtn = styledButton("Delete Project", new Color(204, 0, 0), Color.WHITE);
        deleteProjectBtn.addActionListener(e -> deleteProject());

        projectBtnPanel.add(addProjectBtn);
        projectBtnPanel.add(editProjectBtn);
        projectBtnPanel.add(deleteProjectBtn);
        leftPanel.add(projectBtnPanel, BorderLayout.SOUTH);

        // Tasks Table
        taskTable = new JTable();
        taskTable.setFillsViewportHeight(true);
        taskTable.setBackground(new Color(255, 240, 245)); // light pink
        JScrollPane taskScroll = new JScrollPane(taskTable);
        taskScroll.setBorder(BorderFactory.createTitledBorder("Tasks"));
        rightPanel.add(taskScroll, BorderLayout.CENTER);

        // Task Buttons
        JPanel taskBtnPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        taskBtnPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        taskBtnPanel.setBackground(new Color(255, 228, 196)); // bisque

        JButton addTaskBtn = styledButton("Add Task", new Color(0, 128, 0), Color.WHITE);
        addTaskBtn.addActionListener(e -> addTask());

        JButton completeTaskBtn = styledButton("Mark Completed", new Color(0, 102, 51), Color.WHITE);
        completeTaskBtn.addActionListener(e -> markTaskCompleted());

        JButton editTaskBtn = styledButton("Edit Task Priority", new Color(255, 165, 0), Color.WHITE);
        editTaskBtn.addActionListener(e -> editTaskPriority());

        JButton deleteTaskBtn = styledButton("Delete Task", new Color(204, 0, 0), Color.WHITE);
        deleteTaskBtn.addActionListener(e -> deleteTask());

        taskBtnPanel.add(addTaskBtn);
        taskBtnPanel.add(completeTaskBtn);
        taskBtnPanel.add(editTaskBtn);
        taskBtnPanel.add(deleteTaskBtn);
        rightPanel.add(taskBtnPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);
        getContentPane().add(splitPane);
    }

    private JButton styledButton(String text, Color bg, Color fg){
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
    }

    private void onProjectSelected(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) showSelectedProjectTasks();
    }

    private void loadProjects() {
        List<Project> projects = repo.findAllProjects();
        String[] columnNames = {"ID", "Name", "Description", "Completion %", "Tasks"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Project p : projects) {
            model.addRow(new Object[]{
                    p.getId().substring(0, 8),
                    p.getName(),
                    p.getDescription(),
                    String.format("%.1f", p.getCompletionPercentage()),
                    p.getTasks().size()
            });
        }
        projectTable.setModel(model);
        showSelectedProjectTasks();
    }

    private void showSelectedProjectTasks() {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow == -1) {
            taskTable.setModel(new DefaultTableModel());
            return;
        }

        String projectShortId = (String) projectTable.getValueAt(selectedRow, 0);
        Project selectedProject = repo.findAllProjects().stream()
                .filter(p -> p.getId().startsWith(projectShortId))
                .findFirst().orElse(null);

        if (selectedProject == null) return;

        List<Task> tasks = selectedProject.getTasks();
        String[] columnNames = {"ID", "Title", "Status", "Priority", "Due Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Task t : tasks) {
            model.addRow(new Object[]{
                    t.getId().substring(0, 8),
                    t.getTitle(),
                    t.getStatus(),
                    t.getPriority(),
                    t.getDueDate() != null ? t.getDueDate().toLocalDate() : "No due date"
            });
        }
        taskTable.setModel(model);
    }

    // --- Project Operations ---
    private void addProject() {
        String name = JOptionPane.showInputDialog(this, "Enter Project Name:");
        if (name == null || name.isEmpty()) return;

        String desc = JOptionPane.showInputDialog(this, "Enter Project Description:");
        if (desc == null) desc = "";

        Project p = new Project(name, desc);
        repo.save(p);
        loadProjects();
    }

    private void editProject() {
        int row = projectTable.getSelectedRow();
        if (row == -1) return;

        String projectShortId = (String) projectTable.getValueAt(row, 0);
        Project p = repo.findAllProjects().stream()
                .filter(pr -> pr.getId().startsWith(projectShortId))
                .findFirst().orElse(null);
        if (p == null) return;

        String newName = JOptionPane.showInputDialog(this, "Edit Name:", p.getName());
        if (newName != null && !newName.isEmpty()) p.setName(newName);

        String newDesc = JOptionPane.showInputDialog(this, "Edit Description:", p.getDescription());
        if (newDesc != null) p.setDescription(newDesc);

        repo.save(p);
        loadProjects();
    }

    private void deleteProject() {
        int row = projectTable.getSelectedRow();
        if (row == -1) return;

        String projectShortId = (String) projectTable.getValueAt(row, 0);
        Project p = repo.findAllProjects().stream()
                .filter(pr -> pr.getId().startsWith(projectShortId))
                .findFirst().orElse(null);
        if (p == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this project?");
        if (confirm == JOptionPane.YES_OPTION) {
            repo.deleteById(p.getId());
            loadProjects();
        }
    }

    // --- Task Operations ---
    private void addTask() {
        int row = projectTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a project first!");
            return;
        }

        String projectShortId = (String) projectTable.getValueAt(row, 0);
        Project project = repo.findAllProjects().stream()
                .filter(p -> p.getId().startsWith(projectShortId))
                .findFirst().orElse(null);
        if (project == null) return;

        String title = JOptionPane.showInputDialog(this, "Enter Task Title:");
        if (title == null || title.isEmpty()) return;

        String desc = JOptionPane.showInputDialog(this, "Enter Task Description:");
        if (desc == null) desc = "";

        Priority priority = (Priority) JOptionPane.showInputDialog(this,
                "Select Priority:", "Priority",
                JOptionPane.QUESTION_MESSAGE, null,
                Priority.values(), Priority.MEDIUM);

        Task task = new Task(title, desc, priority, LocalDateTime.now().plusDays(7), project.getId(), title);
        project.addTask(task);
        repo.save(project);
        loadProjects();
    }

    private void markTaskCompleted() {
        int row = taskTable.getSelectedRow();
        if (row == -1) return;

        String taskShortId = (String) taskTable.getValueAt(row, 0);
        Project project = getSelectedProject();
        if (project == null) return;

        Task task = project.getTasks().stream()
                .filter(t -> t.getId().startsWith(taskShortId))
                .findFirst().orElse(null);
        if (task == null) return;

        task.markCompleted();
        repo.save(project);
        loadProjects();
    }

    private void editTaskPriority() {
        int row = taskTable.getSelectedRow();
        if (row == -1) return;

        String taskShortId = (String) taskTable.getValueAt(row, 0);
        Project project = getSelectedProject();
        if (project == null) return;

        Task task = project.getTasks().stream()
                .filter(t -> t.getId().startsWith(taskShortId))
                .findFirst().orElse(null);
        if (task == null) return;

        Priority priority = (Priority) JOptionPane.showInputDialog(this,
                "Select Priority:", "Edit Priority",
                JOptionPane.QUESTION_MESSAGE, null,
                Priority.values(), task.getPriority());
        if (priority != null) task.setPriority(priority);

        repo.save(project);
        loadProjects();
    }

    private void deleteTask() {
        int row = taskTable.getSelectedRow();
        if (row == -1) return;

        String taskShortId = (String) taskTable.getValueAt(row, 0);
        Project project = getSelectedProject();
        if (project == null) return;

        Task task = project.getTasks().stream()
                .filter(t -> t.getId().startsWith(taskShortId))
                .findFirst().orElse(null);
        if (task == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this task?");
        if (confirm == JOptionPane.YES_OPTION) {
            project.removeTask(task);
            repo.save(project);
            loadProjects();
        }
    }

    private Project getSelectedProject() {
        int row = projectTable.getSelectedRow();
        if (row == -1) return null;
        String projectShortId = (String) projectTable.getValueAt(row, 0);
        return repo.findAllProjects().stream()
                .filter(p -> p.getId().startsWith(projectShortId))
                .findFirst().orElse(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}