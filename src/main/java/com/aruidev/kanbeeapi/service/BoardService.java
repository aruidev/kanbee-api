package com.aruidev.kanbeeapi.service;

import com.aruidev.kanbeeapi.dto.BoardCreateDTO;
import com.aruidev.kanbeeapi.dto.BoardResponseDTO;
import com.aruidev.kanbeeapi.entity.Board;
import com.aruidev.kanbeeapi.exception.NotFoundException;
import com.aruidev.kanbeeapi.repository.BoardRepository;
import com.aruidev.kanbeeapi.service.mapper.EntityDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public BoardResponseDTO create(BoardCreateDTO dto) {
        Board board = new Board(dto.getTitle());
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
        board.setTitle(newTitle);
        return EntityDtoMapper.toBoardResponse(board, false);
    }

    @Transactional
    public void delete(UUID id) {
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException("Board not found: " + id);
        }
        boardRepository.deleteById(id);
    }
}