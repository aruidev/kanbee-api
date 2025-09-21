package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.BoardCreateDTO;
import com.aruidev.kanbeeapi.dto.BoardResponseDTO;
import com.aruidev.kanbeeapi.dto.TitleUpdateDTO;
import com.aruidev.kanbeeapi.service.BoardService;
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
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping(value = "/boards", consumes = "application/json")
    public ResponseEntity<BoardResponseDTO> create(@Valid @RequestBody BoardCreateDTO dto) {
        BoardResponseDTO created = boardService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/boards/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDTO> get(@PathVariable UUID id,
                                                @RequestParam(name = "expand", required = false) String expand) {
        boolean includeChildren = expand != null && (expand.contains("lists") || expand.contains("cards"));
        return ResponseEntity.ok(boardService.get(id, includeChildren));
    }

    @PatchMapping(value = "/boards/{id}", consumes = "application/json")
    public ResponseEntity<BoardResponseDTO> update(@PathVariable UUID id,
                                                   @Valid @RequestBody TitleUpdateDTO dto) {
        return ResponseEntity.ok(boardService.updateTitle(id, dto.getTitle()));
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
