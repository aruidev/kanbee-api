package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CardMoveDTO {

    @NotNull(message = "List ID cannot be null")
    private Long listId;

    @NotNull(message = "Position cannot be null")
    @Min(value = 0, message = "Position must be non-negative")
    private Integer position;

    // Constructores
    public CardMoveDTO() {}

    public CardMoveDTO(Long listId, Integer position) {
        this.listId = listId;
        this.position = position;
    }

    // Getters y setters
    public Long getListId() { return listId; }
    public void setListId(Long listId) { this.listId = listId; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}