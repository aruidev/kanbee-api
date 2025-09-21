package com.aruidev.kanbeeapi.service.mapper;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.entity.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class EntityDtoMapper {

    private EntityDtoMapper() {}

    public static BoardResponseDTO toBoardResponse(Board board, boolean includeChildren) {
        BoardResponseDTO dto = new BoardResponseDTO(
                board.getId(),
                board.getTitle(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
        if (includeChildren) {
            List<BoardListResponseDTO> listDtos = board.getBoardLists().stream()
                    .sorted(Comparator.comparing(BoardList::getPosition))
                    .map(EntityDtoMapper::toBoardListResponseWithCards)
                    .collect(Collectors.toList());
            dto.setBoardLists(listDtos);
        }
        return dto;
    }

    public static BoardListResponseDTO toBoardListResponse(BoardList list, boolean includeCards) {
        BoardListResponseDTO dto = new BoardListResponseDTO(
                list.getId(),
                list.getTitle(),
                list.getPosition(),
                list.getCreatedAt(),
                list.getUpdatedAt()
        );
        if (includeCards) {
            List<CardResponseDTO> cards = list.getCards().stream()
                    .sorted(Comparator.comparing(Card::getPosition))
                    .map(EntityDtoMapper::toCardResponse)
                    .collect(Collectors.toList());
            dto.setCards(cards);
        }
        return dto;
    }

    public static BoardListResponseDTO toBoardListResponseWithCards(BoardList list) {
        return toBoardListResponse(list, true);
    }

    public static CardResponseDTO toCardResponse(Card card) {
        return new CardResponseDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getPosition(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}