package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public class BoardListCreateDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Min(value = 0, message = "Position must be non-negative")
    private Integer position = 0;

    // Constructores
    public BoardListCreateDTO() {}

    public BoardListCreateDTO(String title, Integer position) {
        this.title = title;
        this.position = position;
    }

    // Getters y setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}