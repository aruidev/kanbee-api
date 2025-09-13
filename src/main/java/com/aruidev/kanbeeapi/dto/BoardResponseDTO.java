package com.aruidev.kanbeeapi.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BoardResponseDTO {

    private UUID id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BoardListResponseDTO> boardLists;

    // Constructor vacío
    public BoardResponseDTO() {}

    public BoardResponseDTO(UUID id, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<BoardListResponseDTO> getBoardLists() { return boardLists; }
    public void setBoardLists(List<BoardListResponseDTO> boardLists) { this.boardLists = boardLists; }
}