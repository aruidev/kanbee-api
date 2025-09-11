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
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
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

### Tarea 4: Definir entidades JPA ⏭️ EN PROGRESO

Paso 1: Crear las clases de entidad  
Vamos a crear las clases Java que mapean las tablas. Crear estas clases en `src/main/java/com/aruidev/kanbeeapi/entity/:

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
package com.example.kanbee.entity;

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
    
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
```  

#### 3. Crear Task.java:

```java
package com.example.kanbee.entity;

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

## 🚧 Próximos Pasos (Pendientes)

### Inmediato:
- [ ] Ejecutar SQL en Supabase SQL Editor
- [ ] Verificar tablas creadas correctamente
- [ ] Testear conexión Spring Boot ↔ Supabase

### Tarea 4 (Definir entidades JPA):
- [ ] Crear clases `Board`, `List`, `Task`
- [ ] Configurar relaciones JPA (`@OneToMany`, `@ManyToOne`)
- [ ] Agregar timestamps automáticos (`@CreationTimestamp`, `@UpdateTimestamp`)

### Tarea 5 (DTOs):
- [ ] `BoardCreateDTO`, `BoardResponseDTO`
- [ ] `ListCreateDTO`, `TaskCreateDTO`
- [ ] `TaskMoveDTO` para drag-and-drop

## 💡 Lecciones Aprendidas

1. **Maven Wrapper es esencial** - No todos los sistemas tienen Maven instalado globalmente
2. **Separar entornos desde el inicio** - Evita commits accidentales de credenciales
3. **Direct Connection > Pooler** para aplicaciones persistentes como Spring Boot
4. **IntelliJ IDEA facilita mucho** el desarrollo comparado con terminal puro
5. **Java LTS > Bleeding edge** para proyectos de portfolio

## 📊 Tiempo Invertido Hasta Ahora
- **Tarea 1** (Spring Boot setup): ~1.5h (incluyó resolución de problemas Maven)
- **Tarea 2** (Supabase config): ~1h (incluyó configuración de seguridad)
- **Documentación**: ~0.5h
- **Total**: ~3h (dentro del estimado de 13-15h total para Milestone 1)

---
*Última actualización: [Fecha actual] - Proyecto progresando según roadmap*