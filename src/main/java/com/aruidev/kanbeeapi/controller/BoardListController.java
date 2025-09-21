package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.service.BoardListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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
@Tag(name = "Lists", description = "Operaciones para gestionar listas del tablero")
public class BoardListController {

    private final BoardListService boardListService;

    public BoardListController(BoardListService boardListService) {
        this.boardListService = boardListService;
    }

    @Operation(summary = "Crear una lista", description = "Crea una lista en un tablero y devuelve 201 con Location")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lista creada",
                    content = @Content(schema = @Schema(implementation = BoardListResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tablero no encontrado", content = @Content)
    })
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

    @Operation(summary = "Obtener una lista", description = "Devuelve una lista por id; soporta expand=cards")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = BoardListResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @GetMapping("/lists/{id}")
    public ResponseEntity<BoardListResponseDTO> get(@PathVariable Long id,
                                                    @RequestParam(name = "expand", required = false) String expand) {
        boolean includeCards = expand != null && expand.contains("cards");
        return ResponseEntity.ok(boardListService.get(id, includeCards));
    }

    @Operation(summary = "Actualizar título de la lista", description = "PATCH parcial del título de la lista")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado",
                    content = @Content(schema = @Schema(implementation = BoardListResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @PatchMapping(value = "/lists/{id}", consumes = "application/json")
    public ResponseEntity<BoardListResponseDTO> update(@PathVariable Long id,
                                                       @Valid @RequestBody TitleUpdateDTO dto) {
        return ResponseEntity.ok(boardListService.updateTitle(id, dto.getTitle()));
    }

    @Operation(summary = "Mover una lista", description = "Cambia la posición de una lista dentro de un tablero")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movida",
                    content = @Content(schema = @Schema(implementation = BoardListResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @PatchMapping(value = "/lists/{id}/move", consumes = "application/json")
    public ResponseEntity<BoardListResponseDTO> move(@PathVariable Long id,
                                                     @Valid @RequestBody BoardListMoveDTO moveDto) {
        return ResponseEntity.ok(boardListService.move(id, moveDto));
    }

    @Operation(summary = "Eliminar lista", description = "Elimina una lista por id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @DeleteMapping("/lists/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardListService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
