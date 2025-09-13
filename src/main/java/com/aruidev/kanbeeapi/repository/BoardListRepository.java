package com.aruidev.kanbeeapi.repository;

import com.aruidev.kanbeeapi.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {

    // Listas de un board ordenadas
    List<BoardList> findByBoard_IdOrderByPositionAsc(UUID boardId);

    // Última (mayor) posición para insertar al final
    Optional<BoardList> findTopByBoard_IdOrderByPositionDesc(UUID boardId);

    // Desplaza +1 todas las posiciones >= start (usar al insertar en medio)
    @Modifying
    @Query("UPDATE BoardList bl SET bl.position = bl.position + 1 " +
           "WHERE bl.board.id = :boardId AND bl.position >= :startPosition")
    int shiftPositionsUpFrom(UUID boardId, int startPosition);

    // Mueve hacia abajo (posicion -1) dentro de un rango (cuando se extrae un elemento hacia adelante)
    @Modifying
    @Query("UPDATE BoardList bl SET bl.position = bl.position - 1 " +
           "WHERE bl.board.id = :boardId AND bl.position > :from AND bl.position <= :to")
    int closeGapAfterMoveDown(UUID boardId, int from, int to);

    // Mueve hacia arriba (posicion +1) dentro de un rango (cuando se extrae un elemento hacia atrás)
    @Modifying
    @Query("UPDATE BoardList bl SET bl.position = bl.position + 1 " +
           "WHERE bl.board.id = :boardId AND bl.position >= :to AND bl.position < :from")
    int closeGapAfterMoveUp(UUID boardId, int from, int to);

    boolean existsById(Long id);
}