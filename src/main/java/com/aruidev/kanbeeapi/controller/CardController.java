package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@Validated
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping(value = "/lists/{listId}/cards", consumes = "application/json")
    public ResponseEntity<CardResponseDTO> create(@PathVariable Long listId,
                                                  @Valid @RequestBody CardCreateDTO dto) {
        CardResponseDTO created = cardService.create(listId, dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/cards/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<CardResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.get(id));
    }

    @PatchMapping(value = "/cards/{id}", consumes = "application/json")
    public ResponseEntity<CardResponseDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody CardUpdateDTO dto) {
        return ResponseEntity.ok(cardService.update(id, dto));
    }

    @PatchMapping(value = "/cards/{id}/move", consumes = "application/json")
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
