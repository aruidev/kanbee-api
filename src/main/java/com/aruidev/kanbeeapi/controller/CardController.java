package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/lists/{listId}/cards")
    public ResponseEntity<CardResponseDTO> create(@PathVariable Long listId,
                                                  @Valid @RequestBody CardCreateDTO dto) {
        CardResponseDTO created = cardService.create(listId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<CardResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.get(id));
    }

    @PatchMapping("/cards/{id}")
    public ResponseEntity<CardResponseDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody CardCreateDTO dto) {
        return ResponseEntity.ok(cardService.update(id, dto));
    }

    @PatchMapping("/cards/{id}/move")
    public ResponseEntity<CardResponseDTO> move(@PathVariable Long id,
                                                @Valid @RequestBody CardMoveDTO moveDto) {
        return ResponseEntity.ok(cardService.move(id, moveDto));
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

