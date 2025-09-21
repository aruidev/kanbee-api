package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@Validated
@Tag(name = "Cards", description = "Operaciones para gestionar tarjetas")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Crear una tarjeta", description = "Crea una tarjeta en una lista y devuelve 201 con Location")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarjeta creada",
                    content = @Content(schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada", content = @Content)
    })
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

    @Operation(summary = "Obtener una tarjeta", description = "Devuelve una tarjeta por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @GetMapping("/cards/{id}")
    public ResponseEntity<CardResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.get(id));
    }

    @Operation(summary = "Actualizar una tarjeta (parcial)", description = "PATCH de campos opcionales como title/description")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado",
                    content = @Content(schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @PatchMapping(value = "/cards/{id}", consumes = "application/json")
    public ResponseEntity<CardResponseDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody CardUpdateDTO dto) {
        return ResponseEntity.ok(cardService.update(id, dto));
    }

    @Operation(summary = "Mover una tarjeta", description = "Cambia lista y/o posición de una tarjeta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movida",
                    content = @Content(schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @PatchMapping(value = "/cards/{id}/move", consumes = "application/json")
    public ResponseEntity<CardResponseDTO> move(@PathVariable Long id,
                                                @Valid @RequestBody CardMoveDTO moveDto) {
        return ResponseEntity.ok(cardService.move(id, moveDto));
    }

    @Operation(summary = "Eliminar tarjeta", description = "Elimina una tarjeta por id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
