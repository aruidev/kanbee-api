# Kanbee Backend - Progreso del Proyecto

## 📋 Resumen del Proyecto
**Kanbee** es una aplicación web tipo Trello que permite gestionar cards en tableros colaborativos mediante URLs abiertas, inspirada en la organización de las abejas. Este README documenta todo el progreso realizado hasta ahora.

## 🎯 Stack Tecnológico Decidido
- **Backend**: Spring Boot + Maven + Java 21
- **Base de datos**: Supabase (PostgreSQL)
- **Deploy**: Render.com (plan gratuito)
- **IDE**: IntelliJ IDEA
- **Documentación API**: Swagger/OpenAPI

## 🔧 Configuración de Desarrollo

### IntelliJ IDEA Setup:
1. ✅ Proyecto importado correctamente como proyecto Maven
2. ✅ Dependencies descargadas automáticamente
3. ✅ Maven Wrapper disponible (archivos `mvnw.cmd`, `.mvn/`)
4. ✅ Run Configuration configurada con profile `local`

### Git Setup:
- ✅ Repositorio Git inicializado
- ✅ `.gitignore` configurado para proteger credenciales
- ✅ Archivos sensibles excluidos del control de versiones

## 🎯 Decisiones Técnicas Tomadas

### ¿Por qué Java 21 y no Java 24?
- ✅ **Java 21 es LTS** (Long Term Support)
- ✅ **Totalmente compatible** con Spring Boot 3.x
- ✅ **Estable y probado** en producción
- ❌ Java 24 es bleeding edge y no es LTS

### ¿Por qué Direct Connection en Supabase?
- ✅ **Spring Boot es persistente** (no serverless)
- ✅ **Mejor performance** para apps tradicionales
- ✅ **Soporta PREPARE statements** (usado por JPA/Hibernate)
- ✅ **Render.com soporta** conexiones de larga duración

### ¿Por qué Render + Supabase y no otras opciones?
- ✅ **Render**: 750 horas gratis/mes, deploy fácil desde GitHub
- ✅ **Supabase**: Mejor PostgreSQL managed gratuito, dashboard excelente
- ❌ **Railway**: Requiere $5/mes después del trial
- ❌ **Fly.io**: Solo 160GB-hours gratis (menos horas que Render)

### ¿Por qué profiles de Spring?
- ✅ **Seguridad**: Credenciales no van al repositorio
- ✅ **Flexibilidad**: Fácil switch entre local/producción
- ✅ **Best practices**: Patrón estándar en Spring Boot

### VM Options en IntelliJ
####  Ejecutar localmente:
```bash
Run Configuration con VM options: -Dspring.profiles.active=local
```

## 📝 Comandos Útiles

### Verificar Git:
```bash
git status
git log --oneline
```

## 🚧 Pipeline de Tareas
Entidades → DTOs → Repositorios → Servicios → Controladores → Swagger → Tests.

### Inmediato:
- [X] Ejecutar SQL en Supabase SQL Editor
- [X] Verificar tablas creadas correctamente
- [X] Testear conexión Spring Boot ↔ Supabase

### Tarea 4 (Definir entidades JPA):
- [X] Crear clases `Board`, `List`, `Card`
- [X] Configurar relaciones JPA (`@OneToMany`, `@ManyToOne`)
- [X] Agregar timestamps automáticos (`@CreationTimestamp`, `@UpdateTimestamp`)

### Tarea 5 (DTOs):
- [X] `BoardCreateDTO`, `BoardResponseDTO`
- [X] `ListCreateDTO`, `CardCreateDTO`
- [X] `CardMoveDTO` para drag-and-drop

### **Tarea 6: Crear Repositorios JPA**
- [X] Crear `BoardRepository` extendiendo `JpaRepository<Board, UUID>`
- [X] Crear `BoardListRepository` extendiendo `JpaRepository<BoardList, Long>`
- [X] Crear `CardRepository` extendiendo `JpaRepository<Card, Long>`
- [X] Añadir queries personalizadas si se necesita ordenamiento (ej. `findByBoardIdOrderByPositionAsc`)

### **Tarea 7: Crear Servicios (lógica de negocio)**
- [ ] Implementar `BoardService`, `BoardListService`, `CardService`
- [ ] Mapear **Entidades ↔ DTOs**
- [ ] Incluir validaciones de negocio (board existe, lista existe, etc.)

### **Tarea 8: Crear Controladores REST**
- [ ] `BoardController`: endpoints CRUD para tableros
- [ ] `BoardListController`: endpoints CRUD para listas
- [ ] `CardController`: endpoints CRUD para cards
- [ ] Endpoints de movimiento drag-and-drop (`PATCH /cards/{id}/move`, `PATCH /lists/{id}/move`)
- [ ] Usar `@Valid` y `@RequestBody` para validar DTOs

### **Tarea 9: Configurar CORS**
- [ ] Permitir acceso desde:
  - `http://localhost:4200` (desarrollo local)
  - `https://kanbee-frontend.vercel.app` (deploy en Vercel)

### **Tarea 10: Documentar con Swagger**
- [ ] Anotar controladores con `@Operation`, `@ApiResponse`
- [ ] Probar en `/swagger-ui.html`

### **Tarea 11: Pruebas unitarias**
- [ ] Crear tests con JUnit para controladores y servicios
- [ ] Verificar validaciones (`@NotNull`, `@Size`, etc.)
- [ ] Cobertura mínima >70%

### **Tarea 12: Test local + Debugging**
- [ ] Probar CRUD con Swagger/Postman
- [ ] Verificar timestamps automáticos
- [ ] Verificar concurrencia en drag-and-drop

### **Tarea 13: Deploy en Render**
- [ ] Subir repo a GitHub
- [ ] Configurar variables de entorno (`SPRING_DATASOURCE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`)
- [ ] Probar Swagger en `kanbee-backend.onrender.com`
- [ ] Configurar health check

## 💡 Notas

1. **Maven Wrapper es esencial** - Iniciar proyecto en pom.xml, no en el directorio. IntelliJ IDEA instalará todas las dependencias maven automáticamente.
2. **Separar entornos desde el inicio** - Evita commits accidentales de credenciales.
3. **Direct Connection > Pooler** para aplicaciones persistentes como Spring Boot.
4. **Git** - Asegurarse de inicializar repositorio en la raíz del proyecto.
5. **Java LTS > Bleeding edge** - Versiones **no LTS** tienen soporte limitado.

---

## Observaciones técnicas

### Entidades
- Considerar `Set<>` en colecciones para evitar duplicados y mejorar `equals`/`hashCode`.
- Añadir `@JsonIgnore` o mapear siempre vía DTO para evitar problemas de serialización con `LAZY`.
- Añadir `equals`/`hashCode` basados en `id` (cuando no sea null) y quizá `toString` seguro (sin relaciones).
- Unificar tipos: tablas usan `SERIAL` (int) pero entidades usan `Long`; consistente, pero podrías migrar a `BIGSERIAL` o ajustar a `Integer`.

### Modelo relacional
- Falta `ON UPDATE` triggers para mantener `updated_at` si quieres consistencia también a nivel SQL (opcional; Hibernate ya lo maneja).
- Podrías agregar una restricción única (`board_id`, `position`) y (`list_id`, `position`) para garantizar orden estable.

### DTOs
- Con Java 21 puedes migrar muchos DTOs a `record` para reducir código (solo en petición/respuesta).
- `cardCount` se deriva: marcarlo como calculado en el mapper, evitar setter público.

### Capa de persistencia (pendiente)
- Repositorios con métodos:
  - `List<BoardList> findByBoardIdOrderByPositionAsc(UUID boardId)`
  - `List<Card> findByBoardListIdOrderByPositionAsc(Long listId)`
- Añadir métodos para obtener máximo `position` al insertar al final.

### Servicios
- Definir transacciones: `@Transactional(readOnly = true)` por defecto y mutaciones con `@Transactional`.
- Encapsular lógica de reordenamiento (shift de posiciones) en métodos dedicados para evitar inconsistencias.

### Configuración
- Añadir `spring.jpa.open-in-view=false` para forzar carga controlada en servicios.
- Considerar `springdoc.api-docs.path=/api-docs` y agrupar endpoints bajo `/api/v1`.

### Errores y manejo
- Planear `@ControllerAdvice` + `@ExceptionHandler` para `EntityNotFound`, validaciones y mensajes uniformes JSON.

### Performance / futuro
- Si reordenamientos son frecuentes, podrías usar técnica de spacing (posiciones tipo 100, 200...) para minimizar recalculos.
- Añadir auditoría futura (`createdBy`) si luego agregas usuarios.

### Seguridad
- Aunque ahora sea público, dejar preparado un filtro CORS global + futura capa auth (JWT o API key simple).

### Testing (cuando llegues)
- Usar `@DataJpaTest` para validar orden y cascadas.
- Tests de servicio para reordenar listas/cards (incluye colisiones y huecos).

---

## ✅ Milestone 1: Backend Setup - PROGRESO ACTUAL

### Tarea 1: Crear proyecto Spring Boot ✅ COMPLETADO
- **Herramienta**: Spring Initializr (start.spring.io)
- **Configuración**:
    - Project: Maven
    - Language: Java
    - Spring Boot: 3.3.x (versión estable)
    - Java: 21 (LTS - recomendado sobre Java 24)
    - Packaging: Jar

- **Dependencies seleccionadas en Initializr**:
    - ✅ Spring Web (spring-boot-starter-web)
    - ✅ Spring Data JPA (spring-boot-starter-data-jpa)
    - ✅ PostgreSQL Driver (postgresql)
    - ✅ Validation (spring-boot-starter-validation)

- **Dependencies a agregar manualmente al pom.xml**:
  ```xml
  <!-- Springdoc OpenAPI (Swagger) -->
  <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>2.2.0</version>
  </dependency>
  ```

- **Descripción del package**: "API for Kanbee – A Collaborative Kanban Board"
- **Resultado**: ✅ Proyecto Maven funcional con todas las dependencias descargadas

### Tarea 2: Configurar Supabase ✅ COMPLETADO

#### Creación del proyecto en Supabase:
- **Nombre del proyecto**: `kanbee-api-db`
- **Tipo de conexión elegida**: **Direct Connection** (recomendada para Spring Boot)
- **Connection String obtenida**:
  ```
  postgresql://postgres:[PASSWORD]@db.fwcjffdorjrssjkkwyhl.supabase.co:5432/postgres
  ```

#### Configuración de archivos de propiedades:

**`application.properties`** (versionado en Git):
```properties
# Database configuration
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Server configuration
server.port=8080
```

**`application-local.properties`** (NO versionado, para desarrollo local):
```properties
# Local development credentials - DO NOT COMMIT
spring.datasource.url=postgresql://postgres:[REAL-PASSWORD]@db.fwcjffdorjrssjkkwyhl.supabase.co:5432/postgres?sslmode=require
spring.datasource.username=postgres
spring.datasource.password=[REAL-PASSWORD]
```

#### Configuración en IntelliJ IDEA:
- **Edit Configurations** → **VM options**: `-Dspring.profiles.active=local`
- Esto hace que Spring Boot cargue automáticamente `application-local.properties`

#### Seguridad configurada:
- **`.gitignore` actualizado**:
  ```gitignore
  # Local environment files (IMPORTANTE)
  application-local.properties
  .env
  
  # Standard Java/Maven ignores
  *.class
  target/
  *.log
  *.jar
  .idea/
  *.iml
  ```

### Tarea 3: Crear tablas ✅ COMPLETADO

#### SQL preparado para ejecutar en Supabase:
```sql
-- Create table boards
CREATE TABLE boards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create table board_lists
CREATE TABLE board_lists (
    id SERIAL PRIMARY KEY,
    board_id UUID NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    position INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create table cards
CREATE TABLE cards (
    id SERIAL PRIMARY KEY,
    list_id INTEGER NOT NULL REFERENCES board_lists(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    position INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create indexes for performance optimization
CREATE INDEX idx_board_lists_board_id ON board_lists(board_id);
CREATE INDEX idx_board_lists_position ON board_lists(board_id, position);
CREATE INDEX idx_cards_list_id ON cards(list_id);
CREATE INDEX idx_cards_position ON cards(list_id, position);
```

> Se guarda seed SQL en `sql/supabase/seed.sql`.

### Tarea 4: Definir entidades JPA ✅ COMPLETADO

#### Crear las clases de entidad

Vamos a crear las clases Java que mapean las tablas. Crear estas clases en `src/main/java/com/aruidev/kanbeeapi/entity/`:

#### 1. Crear `Board.java`:

```java
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
```

#### 2. Crear BoardList.java:

```java
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
  private Set<Card> cards = new LinkedHashSet<>();

  public BoardList() {
  }

  public BoardList(String title, Integer position) {
    this.title = title;
    this.position = position;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public Set<Card> getCards() {
    return cards;
  }

  public void addCard(Card card) {
    cards.add(card);
    card.setBoardList(this);
  }

  public void removeCard(Card card) {
    cards.remove(card);
    card.setBoardList(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BoardList)) return false;
    return id != null && id.equals(((BoardList) o).id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "BoardList{id=" + id + ", title='" + title + "', position=" + position + "}";
  }
}
```

#### 3. Crear Card.java:

```java
package com.aruidev.kanbeeapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "cards",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_cards_list_position", columnNames = {"list_id", "position"})
        }
)
public class Card {

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

    public Card() {}
    public Card(String title, String description, Integer position) {
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
        if (!(o instanceof Card)) return false;
        return id != null && id.equals(((Card) o).id);
    }
    @Override
}
```

### Tarea 5: Kanbee DTOs - Data Transfer Objects 🚧 EN PROGRESO

Crear: `src/main/java/com/aruidev/kanbeeapi/dto/`

## 1. DTOs para Board

### `BoardCreateDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BoardCreateDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    // Constructor vacío
    public BoardCreateDTO() {}

    public BoardCreateDTO(String title) {
        this.title = title;
    }

    // Getters y setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
```

### `BoardResponseDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BoardResponseDTO {

    private UUID id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BoardListResponseDTO> boardLists;

    // Constructor vacío
    public BoardResponseDTO() {}

    public BoardResponseDTO(UUID id, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<BoardListResponseDTO> getBoardLists() { return boardLists; }
    public void setBoardLists(List<BoardListResponseDTO> boardLists) { this.boardLists = boardLists; }
}
```

## 2. DTOs para BoardList

### `BoardListCreateDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public class BoardListCreateDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Min(value = 0, message = "Position must be non-negative")
    private Integer position = 0;

    // Constructores
    public BoardListCreateDTO() {}

    public BoardListCreateDTO(String title, Integer position) {
        this.title = title;
        this.position = position;
    }

    // Getters y setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
```

### `BoardListResponseDTO.java`

```java
package com.aruidev.kanbeeapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BoardListResponseDTO {

  private Long id;
  private String title;
  private Integer position;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<CardResponseDTO> cards;
  private Integer cardCount; // Para el contador de tareas

  // Constructor vacío
  public BoardListResponseDTO() {
  }

  public BoardListResponseDTO(Long id, String title, Integer position, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.position = position;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // Getters y setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public List<CardResponseDTO> getCards() {
    return cards;
  }

  public void setCards(List<CardResponseDTO> cards) {
    this.cards = cards;
    this.cardCount = cards != null ? cards.size() : 0;
  }

  public Integer getCardCount() {
    return cardCount;
  }

  public void setCardCount(Integer cardCount) {
    this.cardCount = cardCount;
  }
}
```

### `BoardListMoveDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.util.UUID;

public class BoardListMoveDTO {

    @NotNull(message = "Board ID cannot be null")
    private UUID boardId;

    @NotNull(message = "Position cannot be null")
    @Min(value = 0, message = "Position must be non-negative")
    private Integer position;

    // Constructores
    public BoardListMoveDTO() {}

    public BoardListMoveDTO(UUID boardId, Integer position) {
        this.boardId = boardId;
        this.position = position;
    }

    // Getters y setters
    public UUID getBoardId() { return boardId; }
    public void setBoardId(UUID boardId) { this.boardId = boardId; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
```

## 3. DTOs para Card

### `CardCreateDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public class CardCreateDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    @Min(value = 0, message = "Position must be non-negative")
    private Integer position = 0;

    // Constructores
    public CardCreateDTO() {}

    public CardCreateDTO(String title, String description, Integer position) {
        this.title = title;
        this.description = description;
        this.position = position;
    }

    // Getters y setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
```

### `CardResponseDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import java.time.LocalDateTime;

public class CardResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Integer position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public CardResponseDTO() {}

    public CardResponseDTO(Long id, String title, String description, Integer position,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```

### `CardMoveDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CardMoveDTO {

    @NotNull(message = "List ID cannot be null")
    private Long listId;

    @NotNull(message = "Position cannot be null")
    @Min(value = 0, message = "Position must be non-negative")
    private Integer position;

    // Constructores
    public CardMoveDTO() {}

    public CardMoveDTO(Long listId, Integer position) {
        this.listId = listId;
        this.position = position;
    }

    // Getters y setters
    public Long getListId() { return listId; }
    public void setListId(Long listId) { this.listId = listId; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
```

## Notas importantes:

### Validaciones incluidas:
- ✅ `@NotBlank` para campos obligatorios
- ✅ `@Size` para límites de caracteres
- ✅ `@Min` para posiciones no negativas
- ✅ `@NotNull` para campos requeridos

### Features agregadas:
- ✅ **Card counter** en `BoardListResponseDTO`
- ✅ **DTOs específicos** para drag-and-drop (`CardMoveDTO`, `BoardListMoveDTO`)
- ✅ **Separación clara** entre Create/Response DTOs

### Próximos pasos recomendados (prioridad)
- Repositorios con métodos ordenados.
- Mappers entidad ↔ DTO (manual o MapStruct).
- Servicios con transacciones y lógica de movimiento.
- Controladores + validaciones + manejo de errores global.
- Desactivar Open Session in View y verificar cargas necesarias.
- Swagger y prueba end-to-end con datos semilla.

---
*Última actualización: 11-09-2025 - Proyecto progresando según roadmap*

---

# Modificaciones 1
> 13-09-2024

SQL con restricciones únicas y triggers de update_at (para editar datos fuera de Hibernate):

### REBUILD DESTRUCTIVA

```sqL
-- Rebuild limpio (usar solo si puedes perder datos)

BEGIN;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Drop previo (idempotente)
DROP TRIGGER IF EXISTS trg_cards_updated_at ON cards;
DROP TRIGGER IF EXISTS trg_board_lists_updated_at ON board_lists;
DROP TRIGGER IF EXISTS trg_boards_updated_at ON boards;
DROP FUNCTION IF EXISTS set_updated_at();
DROP TABLE IF EXISTS cards CASCADE;
DROP TABLE IF EXISTS board_lists CASCADE;
DROP TABLE IF EXISTS boards CASCADE;

-- Función para mantener updated_at
CREATE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Tablas
CREATE TABLE boards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE board_lists (
    id SERIAL PRIMARY KEY,
    board_id UUID NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_board_lists_board_position UNIQUE (board_id, position)
);

CREATE TABLE cards (
    id SERIAL PRIMARY KEY,
    list_id INTEGER NOT NULL REFERENCES board_lists(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_cards_list_position UNIQUE (list_id, position)
);

-- Índices adicionales solo sobre FKs (los UNIQUE ya crean índice compuesto)
CREATE INDEX idx_board_lists_board_id ON board_lists(board_id);
CREATE INDEX idx_cards_list_id ON cards(list_id);

-- Triggers updated_at
CREATE TRIGGER trg_boards_updated_at
BEFORE UPDATE ON boards
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_board_lists_updated_at
BEFORE UPDATE ON board_lists
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_cards_updated_at
BEFORE UPDATE ON cards
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

COMMIT;
```

### REBUILD INCREMENTAL (si no quieres perder datos)

```sql
-- Rebuild limpio (usar solo si puedes perder datos)

BEGIN;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Drop previo (idempotente)
DROP TRIGGER IF EXISTS trg_cards_updated_at ON cards;
DROP TRIGGER IF EXISTS trg_board_lists_updated_at ON board_lists;
DROP TRIGGER IF EXISTS trg_boards_updated_at ON boards;
DROP FUNCTION IF EXISTS set_updated_at();
DROP TABLE IF EXISTS cards CASCADE;
DROP TABLE IF EXISTS board_lists CASCADE;
DROP TABLE IF EXISTS boards CASCADE;

-- Función para mantener updated_at
CREATE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Tablas
CREATE TABLE boards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE board_lists (
    id SERIAL PRIMARY KEY,
    board_id UUID NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_board_lists_board_position UNIQUE (board_id, position)
);

CREATE TABLE cards (
    id SERIAL PRIMARY KEY,
    list_id INTEGER NOT NULL REFERENCES board_lists(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_cards_list_position UNIQUE (list_id, position)
);

-- Índices adicionales solo sobre FKs (los UNIQUE ya crean índice compuesto)
CREATE INDEX idx_board_lists_board_id ON board_lists(board_id);
CREATE INDEX idx_cards_list_id ON cards(list_id);

-- Triggers updated_at
CREATE TRIGGER trg_boards_updated_at
BEFORE UPDATE ON boards
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_board_lists_updated_at
BEFORE UPDATE ON board_lists
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_cards_updated_at
BEFORE UPDATE ON cards
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

COMMIT;
```

## Cambios en entidades: Se usa Set en lugar de List para evitar duplicados y mejorar equals/hashCode.

`Board.java`

```java
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
```

`BoardList.java`

```java
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
  private Set<Card> cards = new LinkedHashSet<>();

  public BoardList() {
  }

  public BoardList(String title, Integer position) {
    this.title = title;
    this.position = position;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public Set<Card> getCards() {
    return cards;
  }

  public void addCard(Card card) {
    cards.add(card);
    card.setBoardList(this);
  }

  public void removeCard(Card card) {
    cards.remove(card);
    card.setBoardList(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BoardList)) return false;
    return id != null && id.equals(((BoardList) o).id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "BoardList{id=" + id + ", title='" + title + "', position=" + position + "}";
  }
}
```

`Card.java`

```java
package com.aruidev.kanbeeapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "cards",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_cards_list_position", columnNames = {"list_id", "position"})
        }
)
public class Card {

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

    public Card() {}
    public Card(String title, String description, Integer position) {
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
        if (!(o instanceof Card)) return false;
        return id != null && id.equals(((Card) o).id);
    }
    @Override
    public int hashCode() { return id != null ? id.hashCode() : 0; }
    @Override
    public String toString() {
        return "Card{id=" + id + ", title='" + title + "', position=" + position + "}";
    }
}
```


## TAREA 6: Repositorios JPA ✅ COMPLETADO
Repositorios JPA con métodos de consulta ordenada, obtención de última posición y utilidades para reordenar (drag&drop). Se usan métodos derivados y queries @Modifying para desplazar posiciones respetando las UNIQUE (board_id, position) y UNIQUE (list_id, position).
Archivos en src/main/java/com/aruidev/kanbeeapi/repository/

`BoardRepository.java`
```java
package com.aruidev.kanbeeapi.repository;

import com.aruidev.kanbeeapi.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {

    // Carga eager controlada de listas y tareas (evita N+1)
    @EntityGraph(attributePaths = {"boardLists", "boardLists.cards"})
    Optional<Board> findWithBoardListsById(UUID id);

    boolean existsById(UUID id);
}
```

`BoardListRepository.java`
```java
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
```

`CardRepository.java`

```java
package com.aruidev.kanbeeapi.repository;

import com.aruidev.kanbeeapi.entity.Card;
import com.aruidev.kanbeeapi.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

  // Tareas de una lista ordenadas
  List<Card> findByBoardList_IdOrderByPositionAsc(Long listId);

  // Última posición en la lista
  Optional<Card> findTopByBoardList_IdOrderByPositionDesc(Long listId);

  // Desplazar posiciones al insertar
  @Modifying
  @Query("UPDATE Card t SET t.position = t.position + 1 " +
          "WHERE t.boardList.id = :listId AND t.position >= :startPosition")
  int shiftPositionsUpFrom(Long listId, int startPosition);

  // Reordenar rango (movimiento hacia adelante)
  @Modifying
  @Query("UPDATE Card t SET t.position = t.position - 1 " +
          "WHERE t.boardList.id = :listId AND t.position > :from AND t.position <= :to")
  int closeGapAfterMoveDown(Long listId, int from, int to);

  // Reordenar rango (movimiento hacia atrás)
  @Modifying
  @Query("UPDATE Card t SET t.position = t.position + 1 " +
          "WHERE t.boardList.id = :listId AND t.position >= :to AND t.position < :from")
  int closeGapAfterMoveUp(Long listId, int from, int to);
}
```

### Notas de uso
 
> - En servicios anotar métodos mutadores con @Transactional. 
> - Ajustar orden de reordenamiento: 1) desplazar rango 2) set nueva posición del elemento movido.
> - Recomendado en application.properties: spring.jpa.open-in-view=false para controlar cargas en servicio.
> - Validar conflictos DataIntegrityViolationException por las UNIQUE en posición.