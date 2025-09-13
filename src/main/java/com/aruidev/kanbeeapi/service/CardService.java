package com.aruidev.kanbeeapi.service;

import com.aruidev.kanbeeapi.dto.*;
import com.aruidev.kanbeeapi.entity.BoardList;
import com.aruidev.kanbeeapi.entity.Card;
import com.aruidev.kanbeeapi.exception.NotFoundException;
import com.aruidev.kanbeeapi.repository.BoardListRepository;
import com.aruidev.kanbeeapi.repository.CardRepository;
import com.aruidev.kanbeeapi.service.mapper.EntityDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final BoardListRepository boardListRepository;

    public CardService(CardRepository cardRepository, BoardListRepository boardListRepository) {
        this.cardRepository = cardRepository;
        this.boardListRepository = boardListRepository;
    }

    @Transactional
    public CardResponseDTO create(Long listId, CardCreateDTO dto) {
        BoardList list = boardListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found: " + listId));

        int position = resolveInsertionPositionForCard(listId, dto.getPosition());

        cardRepository.shiftPositionsUpFrom(listId, position);

        Card card = new Card(dto.getTitle(), dto.getDescription(), position);
        card.setBoardList(list);
        cardRepository.save(card);
        return EntityDtoMapper.toCardResponse(card);
    }

    private int resolveInsertionPositionForCard(Long listId, Integer requested) {
        int max = cardRepository.findTopByBoardList_IdOrderByPositionDesc(listId)
                .map(c -> c.getPosition() + 1)
                .orElse(0);
        if (requested == null) return max;
        return Math.min(requested, max);
    }

    public CardResponseDTO get(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card not found: " + id));
        return EntityDtoMapper.toCardResponse(card);
    }

    @Transactional
    public CardResponseDTO update(Long id, CardCreateDTO dto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card not found: " + id));
        card.setTitle(dto.getTitle());
        card.setDescription(dto.getDescription());
        return EntityDtoMapper.toCardResponse(card);
    }

    @Transactional
    public CardResponseDTO move(Long cardId, CardMoveDTO moveDto) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card not found: " + cardId));

        Long sourceListId = card.getBoardList().getId();
        Long targetListId = moveDto.getListId();
        int newPosRequested = moveDto.getPosition();
        boolean listChange = !sourceListId.equals(targetListId);

        if (listChange) {
            // Cerrar hueco en lista origen
            int oldPos = card.getPosition();
            cardRepository.closeGapAfterMoveDown(sourceListId, oldPos, oldPos + 1);

            // Insertar en lista destino
            BoardList targetList = boardListRepository.findById(targetListId)
                    .orElseThrow(() -> new NotFoundException("Target list not found: " + targetListId));

            int newPos = resolveInsertionPositionForCard(targetListId, newPosRequested);
            cardRepository.shiftPositionsUpFrom(targetListId, newPos);

            card.setBoardList(targetList);
            card.setPosition(newPos);
        } else {
            int oldPos = card.getPosition();
            if (oldPos == newPosRequested) {
                return EntityDtoMapper.toCardResponse(card);
            }
            int maxPos = cardRepository.findTopByBoardList_IdOrderByPositionDesc(sourceListId)
                    .map(Card::getPosition)
                    .orElse(0);
            int newPos = Math.min(newPosRequested, maxPos);
            if (oldPos < newPos) {
                cardRepository.closeGapAfterMoveDown(sourceListId, oldPos, newPos);
            } else {
                cardRepository.closeGapAfterMoveUp(sourceListId, oldPos, newPos);
            }
            card.setPosition(newPos);
        }
        return EntityDtoMapper.toCardResponse(card);
    }

    @Transactional
    public void delete(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card not found: " + id));
        Long listId = card.getBoardList().getId();
        int oldPos = card.getPosition();
        cardRepository.delete(card);
        cardRepository.closeGapAfterMoveDown(listId, oldPos, oldPos + 1);
    }
}