package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.service.BoardListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@Validated
public class BoardListController {

    private final BoardListService boardListService;

    public BoardListController(BoardListService boardListService) {
        this.boardListService = boardListService;
    }

    @PostMapping("/boards/{boardId}/lists")
    public ResponseEntity<BoardListResponseDTO> create(@PathVariable UUID boardId,
                                                       @Valid @RequestBody BoardListCreateDTO dto) {
        BoardListResponseDTO created = boardListService.create(boardId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/lists/{id}")
    public ResponseEntity<BoardListResponseDTO> get(@PathVariable Long id,
                                                    @RequestParam(name = "cards", defaultValue = "false") boolean includeCards) {
        return ResponseEntity.ok(boardListService.get(id, includeCards));
    }

    @PatchMapping("/lists/{id}/title")
    public ResponseEntity<BoardListResponseDTO> updateTitle(@PathVariable Long id,
                                                            @Valid @RequestBody BoardTitleUpdateDTO dto) {
        return ResponseEntity.ok(boardListService.updateTitle(id, dto.getTitle()));
    }

    @PatchMapping("/lists/{id}/move")
    public ResponseEntity<BoardListResponseDTO> move(@PathVariable Long id,
                                                     @Valid @RequestBody BoardListMoveDTO moveDto) {
        return ResponseEntity.ok(boardListService.move(id, moveDto));
    }

    @DeleteMapping("/lists/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardListService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

