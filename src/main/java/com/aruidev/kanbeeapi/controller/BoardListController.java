package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.service.BoardListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@Validated
public class BoardListController {

    private final BoardListService boardListService;

    public BoardListController(BoardListService boardListService) {
        this.boardListService = boardListService;
    }

    @PostMapping(value = "/boards/{boardId}/lists", consumes = "application/json")
    public ResponseEntity<BoardListResponseDTO> create(@PathVariable UUID boardId,
                                                       @Valid @RequestBody BoardListCreateDTO dto) {
        BoardListResponseDTO created = boardListService.create(boardId, dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/lists/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/lists/{id}")
    public ResponseEntity<BoardListResponseDTO> get(@PathVariable Long id,
                                                    @RequestParam(name = "expand", required = false) String expand) {
        boolean includeCards = expand != null && expand.contains("cards");
        return ResponseEntity.ok(boardListService.get(id, includeCards));
    }

    @PatchMapping(value = "/lists/{id}", consumes = "application/json")
    public ResponseEntity<BoardListResponseDTO> update(@PathVariable Long id,
                                                       @Valid @RequestBody TitleUpdateDTO dto) {
        return ResponseEntity.ok(boardListService.updateTitle(id, dto.getTitle()));
    }

    @PatchMapping(value = "/lists/{id}/move", consumes = "application/json")
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
