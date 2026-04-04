package com.taskmanager.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.taskmanager.models.Project;
import com.taskmanager.models.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProjectTaskRepository implements IRepository<Object> {

    private final ConcurrentHashMap<String, Project> projects = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Task> tasks = new ConcurrentHashMap<>();
    private static final String DATA_FILE = "taskmanager_data.json";
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new com.google.gson.TypeAdapter<LocalDateTime>() {
                @Override
                public void write(com.google.gson.stream.JsonWriter out, LocalDateTime value) throws IOException {
                    out.value(value != null ? value.toString() : null);
                }
                @Override
                public LocalDateTime read(com.google.gson.stream.JsonReader in) throws IOException {
                    String str = in.nextString();
                    return str != null ? LocalDateTime.parse(str) : null;
                }
            }).create();

    public ProjectTaskRepository() {
        loadFromStorage();
    }

    @Override
    public void save(Object entity) {
        if (entity instanceof Project p) {
            projects.put(p.getId(), p);
        } else if (entity instanceof Task t) {
            tasks.put(t.getId(), t);
        }
        persistToJson();
    }

    @Override
    public Optional<Object> findById(String id) {
        if (projects.containsKey(id)) return Optional.of(projects.get(id));
        if (tasks.containsKey(id)) return Optional.of(tasks.get(id));
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public List<Project> findAllProjects() {
        return new ArrayList<>(projects.values());
    }

    @SuppressWarnings("unchecked")
    public List<Task> findAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Task> findTasksByProjectId(String projectId) {
        return tasks.values().stream()
                .filter(t -> projectId.equals(t.getProjectId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> findAll() {
        List<Object> all = new ArrayList<>();
        all.addAll(projects.values());
        all.addAll(tasks.values());
        return all;
    }

    @Override
    public void deleteById(String id) {
        projects.remove(id);
        tasks.remove(id);
        persistToJson();
    }

    @Override
    public void saveAll(List<Object> entities) {
        for (Object e : entities) save(e);
    }

    @Override
    public void loadFromStorage() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (Reader reader = Files.newBufferedReader(Paths.get(DATA_FILE))) {
            Type type = new TypeToken<StorageData>(){}.getType();
            StorageData data = gson.fromJson(reader, type);
            if (data != null) {
                data.projects.forEach(p -> projects.put(p.getId(), p));
                data.tasks.forEach(t -> tasks.put(t.getId(), t));
            }
        } catch (Exception e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }

    private void persistToJson() {
        try (Writer writer = Files.newBufferedWriter(Paths.get(DATA_FILE))) {
            StorageData data = new StorageData(new ArrayList<>(projects.values()), new ArrayList<>(tasks.values()));
            gson.toJson(data, writer);
        } catch (Exception e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }

    // Inner class for JSON serialization
    private static class StorageData {
        List<Project> projects;
        List<Task> tasks;

        public StorageData(List<Project> projects, List<Task> tasks) {
            this.projects = projects;
            this.tasks = tasks;
        }
    }
}
