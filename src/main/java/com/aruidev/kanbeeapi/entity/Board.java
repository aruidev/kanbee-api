package com.aruidev.kanbeeapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private Set<BoardList> boardLists = new LinkedHashSet<>();

    public Board() {}
    public Board(String title) { this.title = title; }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Set<BoardList> getBoardLists() { return boardLists; }

    public void addBoardList(BoardList list) {
        boardLists.add(list);
        list.setBoard(this);
    }
    public void removeBoardList(BoardList list) {
        boardLists.remove(list);
        list.setBoard(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        return id != null && id.equals(((Board) o).id);
    }
    @Override
    public int hashCode() { return id != null ? id.hashCode() : 0; }
    @Override
    public String toString() {
        return "Board{id=" + id + ", title='" + title + "'}";
    }
}
