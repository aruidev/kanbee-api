# Kanbee – A Collaborative Kanban Board with a Bee-Inspired Design

**Kanbee** es una aplicación web tipo Trello que permite gestionar tareas en tableros colaborativos mediante URLs abiertas, inspirada en la organización de las abejas. Diseñada como un MVP para un portfolio junior, combina un frontend moderno, un backend robusto, y una base de datos en la nube, con una UI temática de abeja que usa colores vibrantes y patrones hexagonales.

**[Demo Frontend](https://kanbee-frontend.vercel.app/)** | **[API Docs (Swagger)](https://kanbee-backend.onrender.com/swagger-ui.html)** | **[Backend Repo](https://claude.ai/chat/2727206b-61a6-45bb-94ad-a20b7d89036f#)** | **[Frontend Repo](https://claude.ai/chat/2727206b-61a6-45bb-94ad-a20b7d89036f#)**

---

## Funcionalidades

- Crear, leer, actualizar y eliminar tableros, listas y tareas.
- URLs abiertas para acceso público (sin autenticación, intencional para simplicidad).
- Drag-and-drop para mover tareas entre listas, con persistencia de posición.
- Persistencia del orden de listas con drag-and-drop.
- Timestamps automáticos (creado/modificado) para mostrar actividad reciente.
- Contador de tareas por lista para visión rápida del progreso.
- Spinner temático ("Las abejas están organizando tus tareas...") para manejar el spin-down del backend.
- UI inspirada en abejas: colores (#FFC107, #1A1A1A, #FFFFFF, #A8E6CF), hexágonos, íconos.
- API REST documentada con Swagger.

## Tecnologías

- **Frontend**: Angular, Angular CDK (drag-and-drop), Angular Material (spinner), Vercel.
- **Backend**: Spring Boot, Spring Data JPA, DTOs para separación de capas, Render.
- **Base de datos**: Supabase (PostgreSQL, plan gratuito).
- **Documentación**: Swagger (OpenAPI).

## Hoja de ruta

### Milestone 1: Backend – Spring Boot + Supabase (13-15 horas)

|Tarea|Descripción|Horas estimadas|Entregables|
|---|---|---|---|
|Crear proyecto Spring Boot|Configurar proyecto con Maven, dependencias: Spring Web, Spring Data JPA, PostgreSQL Driver, Springdoc OpenAPI, Bean Validation|1.5-2h|Proyecto Maven listo|
|Configurar Supabase|Crear proyecto, obtener URL JDBC, configurar `application.properties` con conexión SSL, pool de conexiones|1.5h|Conexión a Supabase establecida y optimizada|
|Crear tablas|SQL en Supabase: `boards` (id: UUID, title, created_at, updated_at), `lists` (id: SERIAL, board_id, title, position, created_at), `tasks` (id: SERIAL, list_id, title, description, position, created_at, updated_at)|1.5h|Tablas creadas con timestamps|
|Definir entidades JPA|Clases `Board`, `List`, `Task` con relaciones (`@OneToMany`, `mappedBy`, `orphanRemoval`), campos `position` y timestamps con `@CreationTimestamp`, `@UpdateTimestamp`|2.5h|Entidades Java con audit trail|
|Crear DTOs|DTOs para request/response: `BoardCreateDTO`, `BoardResponseDTO`, `ListCreateDTO`, `TaskCreateDTO`, `TaskMoveDTO`|1.5h|DTOs implementados|
|Crear repositorios|`BoardRepository`, `ListRepository`, `TaskRepository` extendiendo `JpaRepository` con queries personalizadas para ordenamiento|1.5h|Repositorios con queries optimizadas|
|Crear controladores REST|Endpoints CRUD: `POST /boards`, `GET /boards/{id}`, `POST /boards/{id}/lists`, `POST /lists/{id}/tasks`, `PATCH /tasks/{id}/move`, `PATCH /lists/{id}/move`. Validación de existencia de board_id. Usar `@Valid` con `@NotNull`, `@Size`|3-4h|Endpoints funcionales con validaciones y manejo de concurrencia|
|Configurar CORS|Permitir acceso desde frontend Angular (`https://kanbee-frontend.vercel.app`) y localhost|0.5h|CORS funcionando|
|Documentar con Swagger|Anotar endpoints con `@Operation`, `@ApiResponse`, ejemplos de DTOs, probar UI Swagger|1h|Swagger completo y accesible|
|Pruebas unitarias|Tests con JUnit para controladores principales y validaciones|1h|Test coverage > 70%|
|Test local + debugging|Probar CRUD con Postman/Swagger, verificar timestamps, concurrencia, datos en Supabase|1h|Backend estable y probado|
|Deploy en Render|Subir a GitHub, configurar variables de entorno (`SPRING_DATASOURCE_URL`, etc.), probar Swagger en deploy, configurar health check|1.5-2h|Backend en `kanbee-backend.onrender.com`|

**Entregables**: API REST funcional con DTOs, timestamps, conectada a Supabase, documentada con Swagger, desplegada en Render.

---

### Milestone 2: Frontend – Angular + CDK + Material (13-15 horas)

|Tarea|Descripción|Horas estimadas|Entregables|
|---|---|---|---|
|Crear proyecto Angular|`ng new kanbee-frontend`, instalar Angular CDK y Angular Material (tema personalizado con #FFC107), configurar ESLint|1.5h|Proyecto Angular listo|
|Configurar entornos|`environment.ts` (local: `http://localhost:8080`), `environment.prod.ts` (`https://kanbee-backend.onrender.com`), interceptor para loading|1h|Entornos configurados con interceptors|
|Crear componentes|`BoardComponent`, `ListComponent`, `TaskComponent`, `TaskCounterComponent` con estructura responsive|2h|Componentes estructurados|
|Crear servicios HTTP|`BoardService` con métodos CRUD: `createBoard`, `getBoard`, `createList`, `createTask`, `moveTask`, `moveList`. Tipado fuerte con interfaces|2h|Servicios tipados y optimizados|
|Implementar drag-and-drop tareas|Angular CDK para mover tareas, enviar `PATCH /tasks/{id}/move` con `list_id` y `position`, manejar conflictos con timestamps|3-4h|Drag-and-drop de tareas funcional y robusto|
|Implementar drag-and-drop listas|Angular CDK para reordenar listas, persistir con `PATCH /lists/{id}/move`|1.5h|Drag-and-drop de listas funcional|
|Contador de tareas|Componente que muestre total de tareas por lista con estado visual (vacía, pocas, muchas)|1h|Contador visual implementado|
|Mostrar timestamps|Formatear y mostrar fecha de creación/modificación en tareas de forma amigable ("hace 2 horas")|1h|Timestamps user-friendly|
|Implementar spinner|`mat-spinner` (color #FFC107) con mensaje "Las abejas están organizando tus tareas..." para peticiones HTTP|1h|Spinner funcional y temático|
|UI temática abeja|Colores (#FFC107, #1A1A1A, #FFFFFF, #A8E6CF), hexágonos (CSS/SVG), bordes redondeados, hover en tareas, íconos de abeja, animaciones sutiles|2.5h|UI atractiva y profesional|
|Responsive design|Asegurar funcionamiento en móvil y tablet, collapse de listas en pantallas pequeñas|1.5h|Design responsive|
|Pruebas de integración|Verificar comunicación con backend, persistencia de drag-and-drop, spinner en spin-down, timestamps|1.5h|Frontend funcional y probado|
|Deploy en Vercel|Subir a GitHub, configurar build (`ng build`, output: `dist/kanbee-frontend`), probar URLs abiertas, configurar analytics básicos|1.5-2h|Frontend en `kanbee-frontend.vercel.app`|

**Entregables**: Frontend Angular con drag-and-drop completo, timestamps, contadores, UI de abeja responsive, desplegado en Vercel.

---

### Milestone 3: Documentación y Portfolio (4-5 horas)

|Tarea|Descripción|Horas estimadas|Entregables|
|---|---|---|---|
|README Backend y Frontend|Descripción técnica, stack, arquitectura, funcionalidades, enlaces a deploys, limitaciones (URLs abiertas, spin-down), mejoras futuras, métricas de performance|2h|README profesional y completo|
|Capturas / GIF / Video|GIF de 15-20s mostrando: crear board → agregar listas → crear tareas → drag-and-drop → timestamps. Video demo de 2-3 min para LinkedIn|1.5h|Material visual profesional|
|Métricas técnicas|Lighthouse scores del frontend, tiempo de respuesta promedio de la API, estadísticas de build|0.5h|Métricas documentadas|
|Pruebas finales|Probar URLs abiertas en múltiples pestañas/navegadores, verificar Swagger, UI, spinner, responsive, accesibilidad básica|1h|Proyecto validado completamente|
|Portfolio|Añadir entrada detallada: título, descripción técnica, stack, desafíos superados, GIF/video, enlaces a deploys/repos, métricas|1h|Entrada profesional destacada|

**Entregables**: README completo, material visual profesional, métricas técnicas, proyecto desplegado y validado, entrada destacada en portfolio.

---

## Limitaciones y consideraciones técnicas

- **URLs abiertas**: Cualquiera con la URL puede editar tableros, intencional para simplicidad del MVP.
- **Spin-down de Render**: Retraso inicial (5-15s) en el plan gratuito, mitigado con spinner temático y health check.
- **Concurrencia**: Conflictos de drag-and-drop resueltos usando timestamps de última modificación.
- **Sin autenticación**: Diseñado como MVP, pero arquitectura preparada para JWT o Supabase Auth.

## Mejoras futuras (roadmap extendido)

- **Autenticación**: Implementar JWT/Supabase Auth con roles de usuario.
- **Tiempo real**: WebSockets para actualizaciones simultáneas entre usuarios.
- **Tests completos**: Coverage > 90% (JUnit backend, Jasmine/Cypress frontend).
- **Funcionalidades avanzadas**: Etiquetas, fechas límite, comentarios, archivos adjuntos.
- **Exportación**: PDF/JSON de tableros para backup.
- **PWA**: Convertir en Progressive Web App para uso offline.
- **Analytics**: Dashboard de productividad y métricas de uso.

## Desafíos técnicos superados

- **Spin-down mitigation**: Spinner temático con mensaje personalizado para mejorar UX durante cold starts.
- **Drag-and-drop persistence**: Sistema de posiciones con resolución de conflictos usando timestamps.
- **Arquitectura limpia**: Separación de capas con DTOs, evitando exposición directa de entidades JPA.
- **Performance**: Queries optimizadas y lazy loading para tableros con muchas tareas.
- **Responsive UX**: Interfaz adaptable que funciona tanto en desktop como móvil.

## Métricas objetivo

- **Performance**: Lighthouse Score > 90, API response time < 200ms
- **Code Quality**: SonarQube Grade A, Test Coverage > 80%
- **UX**: Mobile-first design, accesibilidad WCAG 2.1 AA básica

## Instalación local

1. **Backend**:

    ```bash
    git clone <backend-repo>
    cd kanbee-backend
    # Configurar application-local.properties con credenciales de Supabase
    mvn spring-boot:run
    # Probar: http://localhost:8080/swagger-ui.html
    ```

2. **Frontend**:

    ```bash
    git clone <frontend-repo>
    cd kanbee-frontend
    npm install
    ng serve
    # Acceder: http://localhost:4200
    ```


## Tips para la demo

- **Pre-demo**: Cargar el backend 5 minutos antes para evitar spin-down.
- **Storytelling**: Usar URLs de ejemplo (`/board/<uuid>`) en navegadores diferentes para mostrar colaboración.
- **Material visual**: Tener el GIF de 15-20s listo para captar atención inmediatamente.
- **Métricas**: Mencionar Lighthouse scores y performance para destacar calidad técnica.
- **Diferenciación**: Enfatizar la temática de abejas y la atención a detalles UX como elementos diferenciadores.