package com.aruidev.kanbeeapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tasks",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_tasks_list_position", columnNames = {"list_id", "position"})
        }
)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer position = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "list_id", nullable = false)
    @JsonIgnore
    private BoardList boardList;

    public Task() {}
    public Task(String title, String description, Integer position) {
        this.title = title;
        this.description = description;
        this.position = position;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public BoardList getBoardList() { return boardList; }
    public void setBoardList(BoardList boardList) { this.boardList = boardList; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        return id != null && id.equals(((Task) o).id);
    }
    @Override
    public int hashCode() { return id != null ? id.hashCode() : 0; }
    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', position=" + position + "}";
    }
}
