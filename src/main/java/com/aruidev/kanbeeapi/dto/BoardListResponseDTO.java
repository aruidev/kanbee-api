package com.aruidev.kanbeeapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BoardListResponseDTO {

    private Long id;
    private String title;
    private Integer position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TaskResponseDTO> tasks;
    private Integer taskCount; // Para el contador de tareas

    // Constructor vacío
    public BoardListResponseDTO() {}

    public BoardListResponseDTO(Long id, String title, Integer position, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<TaskResponseDTO> getTasks() { return tasks; }
    public void setTasks(List<TaskResponseDTO> tasks) {
        this.tasks = tasks;
        this.taskCount = tasks != null ? tasks.size() : 0;
    }

    public Integer getTaskCount() { return taskCount; }
    public void setTaskCount(Integer taskCount) { this.taskCount = taskCount; }
}