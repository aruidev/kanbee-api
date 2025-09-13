package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class BoardListMoveDTO {

    @NotNull(message = "Board ID cannot be null")
    private UUID boardId;

    @NotNull(message = "Position cannot be null")
    @Min(value = 0, message = "Position must be non-negative")
    private Integer position;

    public BoardListMoveDTO() {}

    public BoardListMoveDTO(UUID boardId, Integer position) {
        this.boardId = boardId;
        this.position = position;
    }

    public UUID getBoardId() { return boardId; }
    public void setBoardId(UUID boardId) { this.boardId = boardId; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}

