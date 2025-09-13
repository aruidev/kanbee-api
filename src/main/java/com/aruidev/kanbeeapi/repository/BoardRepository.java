package com.aruidev.kanbeeapi.repository;

import com.aruidev.kanbeeapi.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {

    // Carga eager controlada de listas y tareas (evita N+1)
    @EntityGraph(attributePaths = {"boardLists", "boardLists.tasks"})
    Optional<Board> findWithBoardListsById(UUID id);

    boolean existsById(UUID id);
}
