package com.aruidev.kanbeeapi.service;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.entity.Board;
import com.aruidev.kanbeeapi.entity.BoardList;
import com.aruidev.kanbeeapi.exception.NotFoundException;
import com.aruidev.kanbeeapi.exception.BadRequestException;
import com.aruidev.kanbeeapi.repository.BoardListRepository;
import com.aruidev.kanbeeapi.repository.BoardRepository;
import com.aruidev.kanbeeapi.service.mapper.EntityDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BoardListService {

    private static final int MAX_TITLE_LENGTH = 255;

    private final BoardRepository boardRepository;
    private final BoardListRepository boardListRepository;

    public BoardListService(BoardRepository boardRepository,
                            BoardListRepository boardListRepository) {
        this.boardRepository = boardRepository;
        this.boardListRepository = boardListRepository;
    }

    @Transactional
    public BoardListResponseDTO create(UUID boardId, BoardListCreateDTO dto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board not found: " + boardId));

        int position = resolveInsertionPositionForList(boardId, dto.getPosition());

        // Shift si inserta en el medio
        boardListRepository.shiftPositionsUpFrom(boardId, position);

        String sanitizedTitle = sanitizeAndValidateTitle(dto.getTitle());
        BoardList list = new BoardList(sanitizedTitle, position);
        list.setBoard(board);
        boardListRepository.save(list);
        return EntityDtoMapper.toBoardListResponse(list, true);
    }

    private int resolveInsertionPositionForList(UUID boardId, Integer requested) {
        int max = boardListRepository.findTopByBoard_IdOrderByPositionDesc(boardId)
                .map(bl -> bl.getPosition() + 1)
                .orElse(0);
        if (requested == null) return max;
        return Math.min(requested, max);
    }

    public BoardListResponseDTO get(Long id, boolean includeCards) {
        BoardList list = boardListRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("List no encontrada: " + id));
        return EntityDtoMapper.toBoardListResponse(list, includeCards);
    }

    @Transactional
    public BoardListResponseDTO updateTitle(Long id, String newTitle) {
        BoardList list = boardListRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("List no encontrada: " + id));
        list.setTitle(sanitizeAndValidateTitle(newTitle));
        return EntityDtoMapper.toBoardListResponse(list, false);
    }

    @Transactional
    public BoardListResponseDTO move(Long listId, BoardListMoveDTO moveDto) {
        BoardList list = boardListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found: " + listId));

        UUID targetBoardId = moveDto.getBoardId();
        int newPosRequested = moveDto.getPosition();

        // Cambio de board?
        boolean boardChange = !list.getBoard().getId().equals(targetBoardId);

        if (boardChange) {
            Board targetBoard = boardRepository.findById(targetBoardId)
                    .orElseThrow(() -> new NotFoundException("Target board not found: " + targetBoardId));

            // Cerrar hueco en board original
            int oldPos = list.getPosition();
            boardListRepository.closeGapAfterMoveDown(list.getBoard().getId(), oldPos, oldPos + 1);

            // Resolver nueva posición en board destino
            int newPos = resolveInsertionPositionForList(targetBoardId, newPosRequested);
            boardListRepository.shiftPositionsUpFrom(targetBoardId, newPos);

            list.setBoard(targetBoard);
            list.setPosition(newPos);
            return EntityDtoMapper.toBoardListResponse(list, false);
        } else {
            int oldPos = list.getPosition();
            if (oldPos == newPosRequested) {
                return EntityDtoMapper.toBoardListResponse(list, false);
            }
            int maxPos = boardListRepository.findTopByBoard_IdOrderByPositionDesc(targetBoardId)
                    .map(BoardList::getPosition)
                    .orElse(0);
            int newPos = Math.min(newPosRequested, maxPos);

            if (oldPos < newPos) {
                // Movimiento hacia adelante
                boardListRepository.closeGapAfterMoveDown(targetBoardId, oldPos, newPos);
            } else {
                // Movimiento hacia atrás
                boardListRepository.closeGapAfterMoveUp(targetBoardId, oldPos, newPos);
            }
            list.setPosition(newPos);
            return EntityDtoMapper.toBoardListResponse(list, false);
        }
    }

    @Transactional
    public void delete(Long id) {
        BoardList list = boardListRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("List not found: " + id));
        UUID boardId = list.getBoard().getId();
        int oldPos = list.getPosition();
        boardListRepository.delete(list);
        // Compactar hueco (oldPos a oldPos) usando rango consistente
        boardListRepository.closeGapAfterMoveDown(boardId, oldPos, oldPos + 1);
    }

    private String sanitizeAndValidateTitle(String raw) {
        if (raw == null) {
            throw new BadRequestException("Title cannot be null");
        }
        String sanitized = raw.trim().replaceAll("\\s+", " ");
        if (sanitized.isEmpty()) {
            throw new BadRequestException("Title cannot be blank");
        }
        if (sanitized.length() > MAX_TITLE_LENGTH) {
            throw new BadRequestException("Title length must be <= " + MAX_TITLE_LENGTH);
        }
        return sanitized;
    }
}