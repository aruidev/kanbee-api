package com.aruidev.kanbeeapi.repository;

import com.aruidev.kanbeeapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Tareas de una lista ordenadas
    List<Task> findByBoardList_IdOrderByPositionAsc(Long listId);

    // Última posición en la lista
    Optional<Task> findTopByBoardList_IdOrderByPositionDesc(Long listId);

    // Desplazar posiciones al insertar
    @Modifying
    @Query("UPDATE Task t SET t.position = t.position + 1 " +
           "WHERE t.boardList.id = :listId AND t.position >= :startPosition")
    int shiftPositionsUpFrom(Long listId, int startPosition);

    // Reordenar rango (movimiento hacia adelante)
    @Modifying
    @Query("UPDATE Task t SET t.position = t.position - 1 " +
           "WHERE t.boardList.id = :listId AND t.position > :from AND t.position <= :to")
    int closeGapAfterMoveDown(Long listId, int from, int to);

    // Reordenar rango (movimiento hacia atrás)
    @Modifying
    @Query("UPDATE Task t SET t.position = t.position + 1 " +
           "WHERE t.boardList.id = :listId AND t.position >= :to AND t.position < :from")
    int closeGapAfterMoveUp(Long listId, int from, int to);
}