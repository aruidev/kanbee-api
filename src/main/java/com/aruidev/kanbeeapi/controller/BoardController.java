package com.aruidev.kanbeeapi.controller;

import com.aruidev.kanbeeapi.dto.BoardCreateDTO;
import com.aruidev.kanbeeapi.dto.BoardResponseDTO;
import com.aruidev.kanbeeapi.dto.TitleUpdateDTO;
import com.aruidev.kanbeeapi.service.BoardService;
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
@Tag(name = "Boards", description = "Operaciones para gestionar tableros")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @Operation(summary = "Crear un tablero", description = "Crea un nuevo tablero y devuelve 201 con Location")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tablero creado",
                    content = @Content(schema = @Schema(implementation = BoardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content)
    })
    @PostMapping(value = "/boards", consumes = "application/json")
    public ResponseEntity<BoardResponseDTO> create(@Valid @RequestBody BoardCreateDTO dto) {
        BoardResponseDTO created = boardService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/boards/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Obtener un tablero", description = "Devuelve un tablero por id; soporta expand=lists,cards")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = BoardResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDTO> get(@PathVariable UUID id,
                                                @RequestParam(name = "expand", required = false) String expand) {
        boolean includeChildren = expand != null && (expand.contains("lists") || expand.contains("cards"));
        return ResponseEntity.ok(boardService.get(id, includeChildren));
    }

    @Operation(summary = "Actualizar título del tablero", description = "PATCH parcial del título del tablero")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado",
                    content = @Content(schema = @Schema(implementation = BoardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @PatchMapping(value = "/boards/{id}", consumes = "application/json")
    public ResponseEntity<BoardResponseDTO> update(@PathVariable UUID id,
                                                   @Valid @RequestBody TitleUpdateDTO dto) {
        return ResponseEntity.ok(boardService.updateTitle(id, dto.getTitle()));
    }

    @Operation(summary = "Eliminar tablero", description = "Elimina un tablero por id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
