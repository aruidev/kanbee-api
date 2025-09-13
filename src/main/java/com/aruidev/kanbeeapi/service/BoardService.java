package com.aruidev.kanbeeapi.service;

import com.aruidev.kanbeeapi.dto.BoardCreateDTO;
import com.aruidev.kanbeeapi.dto.BoardResponseDTO;
import com.aruidev.kanbeeapi.entity.Board;
import com.aruidev.kanbeeapi.exception.BadRequestException;
import com.aruidev.kanbeeapi.exception.NotFoundException;
import com.aruidev.kanbeeapi.repository.BoardRepository;
import com.aruidev.kanbeeapi.service.mapper.EntityDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BoardService {

    private static final int MAX_TITLE_LENGTH = 255;

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public BoardResponseDTO create(BoardCreateDTO dto) {
        String sanitized = sanitizeAndValidateTitle(dto.getTitle());
        Board board = new Board(sanitized);
        boardRepository.save(board);
        return EntityDtoMapper.toBoardResponse(board, false);
    }

    public BoardResponseDTO get(UUID id, boolean includeChildren) {
        Board board = includeChildren
                ? boardRepository.findWithBoardListsById(id)
                  .orElseThrow(() -> new NotFoundException("Board not found: " + id))
                : boardRepository.findById(id)
                  .orElseThrow(() -> new NotFoundException("Board not found: " + id));
        return EntityDtoMapper.toBoardResponse(board, includeChildren);
    }

    @Transactional
    public BoardResponseDTO updateTitle(UUID id, String newTitle) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Board not found: " + id));
        String sanitized = sanitizeAndValidateTitle(newTitle);
        board.setTitle(sanitized);
        return EntityDtoMapper.toBoardResponse(board, false);
    }

    @Transactional
    public void delete(UUID id) {
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException("Board not found: " + id);
        }
        boardRepository.deleteById(id);
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