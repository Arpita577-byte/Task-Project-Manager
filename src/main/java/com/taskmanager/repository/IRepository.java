package com.taskmanager.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    void save(T entity);
    Optional<T> findById(String id);
    List<T> findAll();
    void deleteById(String id);
    void saveAll(List<T> entities);
    void loadFromStorage();  // for JSON persistence
}
