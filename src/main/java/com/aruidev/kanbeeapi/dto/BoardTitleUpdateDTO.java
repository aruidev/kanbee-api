package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BoardTitleUpdateDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    public BoardTitleUpdateDTO() {}
    public BoardTitleUpdateDTO(String title) { this.title = title; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}

