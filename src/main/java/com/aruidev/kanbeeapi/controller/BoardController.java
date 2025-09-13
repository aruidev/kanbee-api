package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.BoardCreateDTO;
import com.aruidev.kanbeeapi.dto.BoardResponseDTO;
import com.aruidev.kanbeeapi.dto.BoardTitleUpdateDTO;
import com.aruidev.kanbeeapi.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/boards")
@Validated
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<BoardResponseDTO> create(@Valid @RequestBody BoardCreateDTO dto) {
        BoardResponseDTO created = boardService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDTO> get(@PathVariable UUID id,
                                                @RequestParam(name = "children", defaultValue = "false") boolean includeChildren) {
        return ResponseEntity.ok(boardService.get(id, includeChildren));
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<BoardResponseDTO> updateTitle(@PathVariable UUID id,
                                                         @Valid @RequestBody BoardTitleUpdateDTO dto) {
        return ResponseEntity.ok(boardService.updateTitle(id, dto.getTitle()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

