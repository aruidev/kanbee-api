package com.aruidev.kanbeeapi.repository;

import com.aruidev.kanbeeapi.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

  // Tareas de una lista ordenadas
  List<Card> findByBoardList_IdOrderByPositionAsc(Long listId);

  // Última posición en la lista
  Optional<Card> findTopByBoardList_IdOrderByPositionDesc(Long listId);

  // Desplazar posiciones al insertar
  @Modifying
  @Transactional
  @Query("UPDATE Card t SET t.position = t.position + 1 WHERE t.boardList.id = :listId AND t.position >= :startPosition")
  int shiftPositionsUpFrom(@Param("listId") Long listId, @Param("startPosition") int startPosition);

  // Reordenar rango (movimiento hacia adelante)
  @Modifying
  @Transactional
  @Query("UPDATE Card t SET t.position = t.position - 1 WHERE t.boardList.id = :listId AND t.position > :from AND t.position <= :to")
  int closeGapAfterMoveDown(@Param("listId") Long listId, @Param("from") int from, @Param("to") int to);

  // Reordenar rango (movimiento hacia atrás)
  @Modifying
  @Transactional
  @Query("UPDATE Card t SET t.position = t.position + 1 WHERE t.boardList.id = :listId AND t.position >= :to AND t.position < :from")
  int closeGapAfterMoveUp(@Param("listId") Long listId, @Param("from") int from, @Param("to") int to);
}