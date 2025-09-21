package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public class CardCreateDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    @Min(value = 0, message = "Position must be non-negative")
    private Integer position = 0;

    // Constructores
    public CardCreateDTO() {}

    public CardCreateDTO(String title, String description, Integer position) {
        this.title = title;
        this.description = description;
        this.position = position;
    }

    // Getters y setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}