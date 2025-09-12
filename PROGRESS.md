# Kanbee Backend - Progreso del Proyecto

## 📋 Resumen del Proyecto
**Kanbee** es una aplicación web tipo Trello que permite gestionar tareas en tableros colaborativos mediante URLs abiertas, inspirada en la organización de las abejas. Este README documenta todo el progreso realizado hasta ahora.

## 🎯 Stack Tecnológico Decidido
- **Backend**: Spring Boot + Maven + Java 21
- **Base de datos**: Supabase (PostgreSQL)
- **Deploy**: Render.com (plan gratuito)
- **IDE**: IntelliJ IDEA
- **Documentación API**: Swagger/OpenAPI

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

-- Create table tasks
CREATE TABLE tasks (
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
CREATE INDEX idx_tasks_list_id ON tasks(list_id);
CREATE INDEX idx_tasks_position ON tasks(list_id, position);
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "boards")
public class Board {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardList> boardLists;
    
    // Constructor vacío (requerido por JPA)
    public Board() {}
    
    // Constructor con título
    public Board(String title) {
        this.title = title;
    }
    
    // Getters y setters...
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<BoardList> getBoardLists() { return boardLists; }
    public void setBoardLists(List<BoardList> boardLists) { this.boardLists = boardLists; }
}
```  

#### 2. Crear BoardList.java:

```java
package com.aruidev.kanbeeapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board_lists")
public class BoardList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private Integer position = 0;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column (name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relación Many-to-One con Board
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
    
    // Relación One-to-Many con Tasks
    @OneToMany(mappedBy = "boardList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
    
    // Constructores
    public BoardList() {}
    
    public BoardList(String title, Integer position) {
        this.title = title;
        this.position = position;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
```  

#### 3. Crear Task.java:

```java
package com.aruidev.kanbeeapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Integer position = 0;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relación Many-to-One con BoardList
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private BoardList boardList;
    
    // Constructores
    public Task() {}
    
    public Task(String title, String description, Integer position) {
        this.title = title;
        this.description = description;
        this.position = position;
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
    
    public BoardList getBoardList() { return boardList; }
    public void setBoardList(BoardList boardList) { this.boardList = boardList; }
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
    private List<TaskResponseDTO> tasks;
    private Integer taskCount; // Para el contador de tareas
    
    // Constructor vacío
    public BoardListResponseDTO() {}
    
    public BoardListResponseDTO(Long id, String title, Integer position, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<TaskResponseDTO> getTasks() { return tasks; }
    public void setTasks(List<TaskResponseDTO> tasks) { 
        this.tasks = tasks;
        this.taskCount = tasks != null ? tasks.size() : 0;
    }
    
    public Integer getTaskCount() { return taskCount; }
    public void setTaskCount(Integer taskCount) { this.taskCount = taskCount; }
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

## 3. DTOs para Task

### `TaskCreateDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public class TaskCreateDTO {
    
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;
    
    @Min(value = 0, message = "Position must be non-negative")
    private Integer position = 0;
    
    // Constructores
    public TaskCreateDTO() {}
    
    public TaskCreateDTO(String title, String description, Integer position) {
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

### `TaskResponseDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import java.time.LocalDateTime;

public class TaskResponseDTO {
    
    private Long id;
    private String title;
    private String description;
    private Integer position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor vacío
    public TaskResponseDTO() {}
    
    public TaskResponseDTO(Long id, String title, String description, Integer position, 
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

### `TaskMoveDTO.java`
```java
package com.aruidev.kanbeeapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class TaskMoveDTO {
    
    @NotNull(message = "List ID cannot be null")
    private Long listId;
    
    @NotNull(message = "Position cannot be null")
    @Min(value = 0, message = "Position must be non-negative")
    private Integer position;
    
    // Constructores
    public TaskMoveDTO() {}
    
    public TaskMoveDTO(Long listId, Integer position) {
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
- ✅ **Task counter** en `BoardListResponseDTO`
- ✅ **DTOs específicos** para drag-and-drop (`TaskMoveDTO`, `BoardListMoveDTO`)
- ✅ **Separación clara** entre Create/Response DTOs

### Próximo paso:
Crear repositorios JPA para acceso a datos.

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
- [X] Crear clases `Board`, `List`, `Task`
- [X] Configurar relaciones JPA (`@OneToMany`, `@ManyToOne`)
- [X] Agregar timestamps automáticos (`@CreationTimestamp`, `@UpdateTimestamp`)

### Tarea 5 (DTOs):
- [ ] `BoardCreateDTO`, `BoardResponseDTO`
- [ ] `ListCreateDTO`, `TaskCreateDTO`
- [ ] `TaskMoveDTO` para drag-and-drop


### **Tarea 6: Crear Repositorios JPA**
  - [ ] Crear `BoardRepository` extendiendo `JpaRepository<Board, UUID>`
  - [ ] Crear `BoardListRepository` extendiendo `JpaRepository<BoardList, Long>`
  - [ ] Crear `TaskRepository` extendiendo `JpaRepository<Task, Long>`
  - [ ] Añadir queries personalizadas si se necesita ordenamiento (ej. `findByBoardIdOrderByPositionAsc`)

### **Tarea 7: Crear Servicios (lógica de negocio)**
  - [ ] Implementar `BoardService`, `BoardListService`, `TaskService`
  - [ ] Mapear **Entidades ↔ DTOs**
  - [ ] Incluir validaciones de negocio (board existe, lista existe, etc.)

### **Tarea 8: Crear Controladores REST**
  - [ ] `BoardController`: endpoints CRUD para tableros
  - [ ] `BoardListController`: endpoints CRUD para listas
  - [ ] `TaskController`: endpoints CRUD para tareas
  - [ ] Endpoints de movimiento drag-and-drop (`PATCH /tasks/{id}/move`, `PATCH /lists/{id}/move`)
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

## 📊 Tiempo Invertido Hasta Ahora
- **Tarea 1** (Spring Boot setup): ~1.5h (incluyó resolución de problemas Maven)
- **Tarea 2** (Supabase config): ~1h (incluyó configuración de seguridad)
- **Documentación**: ~0.5h
- **Total**: ~3h (dentro del estimado de 13-15h total para Milestone 1)

---
*Última actualización: 11-09-2025 - Proyecto progresando según roadmap*