package com.aruidev.kanbeeapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
        name = "board_lists",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_board_lists_board_position", columnNames = {"board_id", "position"})
        }
)
public class BoardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private Integer position = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIgnore
    private Board board;

    @JsonIgnore
    @OneToMany(mappedBy = "boardList", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private Set<Task> tasks = new LinkedHashSet<>();

    public BoardList() {}
    public BoardList(String title, Integer position) {
        this.title = title;
        this.position = position;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public Set<Task> getTasks() { return tasks; }

    public void addTask(Task task) {
        tasks.add(task);
        task.setBoardList(this);
    }
    public void removeTask(Task task) {
        tasks.remove(task);
        task.setBoardList(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardList)) return false;
        return id != null && id.equals(((BoardList) o).id);
    }
    @Override
    public int hashCode() { return id != null ? id.hashCode() : 0; }
    @Override
    public String toString() {
        return "BoardList{id=" + id + ", title='" + title + "', position=" + position + "}";
    }
}
