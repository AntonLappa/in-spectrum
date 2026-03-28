# InSpectrum

InSpectrum is a backend application designed to support children on the autism spectrum through personalized developmental plans. The system collects structured assessment data from parents or educators, analyzes responses using a category-based scoring algorithm, and generates individualized learning plans composed of curated educational resources.

The primary users of the platform are parents and educators who work with children on the autism spectrum and need a structured, data-driven approach to skill development.

---

## Core Features


### Authentication
User registration and login with JWT-based authentication. Tokens are issued on sign-up and login, and must be included as a Bearer token in the `Authorization` header for all subsequent requests. Token TTL is configurable via environment variables.

### Assessment
A questionnaire system that allows users to fill out structured assessments. The backend provides a JSON-based assessment template containing categorized questions. Users submit their answers, which are stored as a JSON map of question IDs to numeric scores. Assessments can be retrieved individually or by fetching the user's latest submission (`GET /assessments/latest`).

### Plan Generation
The core feature of the application. After an assessment is submitted, the system generates a personalized development plan by analyzing the user's answers. Plans inherently load and enrich full resource details using optimized batch-fetching to prevent database bottlenecks. The generation algorithm is described in detail in the [Plan Generation Logic](#plan-generation-logic) section below.

### Resources
Educational materials stored in the system, supporting two content types: `TEXT` and `VIDEO`. Each resource is linked to a specific skill and can be filtered by type, audience (HOME, CLASS, BOTH), skill, and publication status. Resources serve as the building blocks of generated plans.

### Progress Tracking
Users can log progress entries against individual plan items. Each entry is tied to a specific date, and the system prevents duplicate entries for the same plan item on the same day. Progress history can be retrieved per plan item.

---

## Example Flow

Typical user journey:

1. User signs up and logs in
2. User retrieves assessment template
3. User submits answers
4. Backend generates personalized plan
5. User views plan and fully enriched resources
6. User tracks progress over time

---

## Plan Generation Logic

The plan generation process is the central algorithm of the application. It transforms raw assessment answers into a structured, actionable development plan through the following pipeline:

```
answers -> categories -> score aggregation -> top categories -> skills -> resources -> plan
```

**Step-by-step breakdown:**

1. **Retrieve assessment answers** -- The system loads the completed assessment and parses the stored JSON into a map of question IDs to numeric scores.

2. **Map questions to categories** -- The assessment template is loaded, and each question ID is mapped to its corresponding developmental category ((e.g. TACTILE, AUDITORY, VISUAL).

3. **Aggregate scores by category** -- For each category, the individual question scores are summed to produce a total category score. Higher scores indicate areas that require more attention.

4. **Select top categories** -- The categories are sorted by score in descending order, and the top 2 categories are selected as focus areas for the plan.

5. **Resolve skills** -- Each selected category is matched to a `Skill` entity in the database using its category code.

6. **Fetch resources** -- For each resolved skill, the system retrieves all published resources associated with that skill.

7. **Build the plan** -- A new `Plan` entity is created and linked to the user and assessment. Each selected resource becomes a `PlanItem` within the plan, ordered sequentially. Following creation, resources are dynamically batched and hydrated into the plan payload.

The result is a personalized plan containing targeted resources that address the child's specific developmental needs.

---

## Tech Stack

| Technology       | Purpose                                      |
|------------------|----------------------------------------------|
| Java 25          | Primary programming language                 |
| Spring Boot 4.0  | Application framework                        |
| Spring Security  | Authentication and authorization             |
| Spring Data JPA  | Database access and ORM                      |
| PostgreSQL 16    | Relational database                          |
| Liquibase        | Database schema migration management         |
| JWT (jjwt)       | Stateless token-based authentication         |
| SpringDoc OpenAPI| API documentation and Swagger UI             |
| Jackson 3        | Advanced object serialization parsing        |
| Lombok           | Boilerplate code reduction                   |
| Maven            | Build and dependency management              |
| Docker Compose   | Local PostgreSQL provisioning                |

---

## Architecture

The application follows a layered architecture with clear separation of concerns:

```
Controller -> Service -> Repository
```

### Layers

- **Controller** -- Handles HTTP requests, delegates to services, and returns DTOs. Each domain area has its own controller: `AuthController`, `UserController`, `AssessmentController`, `PlanController`, `ResourceController`, `ProgressController`.

- **Service** -- Contains all business logic. Each service is defined by an interface and a corresponding implementation class, enabling loose coupling and testability. Includes optimized techniques to load large resource trees in batch mappings.

- **Repository** -- Spring Data JPA repositories providing database access. Entity classes map directly to PostgreSQL tables.

- **Mapper** -- Dedicated mapper classes handle conversion between entity, domain model, and DTO layers. This keeps transformation logic out of services and controllers.

- **DTO** -- Data Transfer Objects are used for all API input and output. Request and response DTOs are separated and organized by domain area. The application natively translates Java's inner `camelCase` naming conventions to strict frontend `snake_case` payloads via explicit Jackson mappings. This guarantees UI resilience and allows generous payload evaluation (e.g. gracefully accepting both `userType` and `user_type` from API consumers).

- **GlobalExceptionHandler** -- A centralized `@ControllerAdvice` that catches all application exceptions and maps them to appropriate HTTP status codes with a consistent `ErrorDto` response body. Actively manages unreadable JSON formats (returning `400 Bad Request`), custom authorization barriers (returning `403 Forbidden`), not-found queries, validation warnings, and internal mapping issues.

### Entity Model

The core entities are:

| Entity              | Description                                   |
|---------------------|-----------------------------------------------|
| `UserEntity`        | User account with role and user type           |
| `AssessmentEntity`  | Submitted assessment with answers as JSON      |
| `PlanEntity`        | Generated development plan linked to a user    |
| `PlanItemEntity`    | Individual item in a plan, linked to a resource|
| `ResourceEntity`    | Educational material (TEXT or VIDEO)            |
| `SkillEntity`       | Developmental skill category                   |
| `ProgressEntryEntity`| Progress log entry for a plan item            |

---

## API Documentation

All API endpoints are aligned 100% with the robust OpenAPI specification file located at `src/main/resources/api.yml`. The specification covers the following endpoint groups:

| Tag          | Base Path                       | Description                        |
|--------------|----------------------------------|------------------------------------|
| Auth         | `/auth/login`, `/auth/sign-up`   | User authentication                |
| Users        | `/users/**`                      | User profile and account management|
| Assessments  | `/assessments/**`                | Assessment template and submissions|
| Plans        | `/plans/**`                      | Plan generation and retrieval      |
| Resources    | `/resources/**`                  | Educational resource library       |
| Progress     | `/plan-items/**/progress`        | Progress tracking                  |

When the application is running, the heavily synchronized Swagger UI dynamically documents `snake_case` operations via SpringDoc auto-generation and explicit object mappers at `http://localhost:8080/swagger-ui.html`.

All endpoints except `/auth/login` and `/auth/sign-up` require authentication via a Bearer token in the `Authorization` header:

```
Authorization: Bearer <token>
```

---

## Security

- JWT-based authentication
- All endpoints (except auth) require Bearer token
- Sensitive data such as JWT tokens are not logged
- Secure error trapping shields application logic from external consumers 

## How to Run

### Prerequisites

- Java 25
- PostgreSQL 16 (or Docker)
- Maven 3.9+

### Database Setup

Start a PostgreSQL instance using Docker Compose:

```bash
docker-compose up -d
```

This creates a PostgreSQL container with the following defaults:

| Parameter  | Value            |
|------------|------------------|
| Host       | `127.0.0.1`      |
| Port       | `5432`           |
| Database   | `neuro_app`      |
| Username   | `neuro`          |
| Password   | `neuro_password` |

### Running the Application

```bash
./mvnw spring-boot:run
```

On startup, Liquibase automatically applies all pending database migrations defined in `src/main/resources/db/changelog/db.changelog-master.yaml`. No manual schema setup is required.

### Configuration

The following environment variables can be used to override defaults:

| Variable            | Default                            | Description               |
|---------------------|------------------------------------|---------------------------|
| `DATABASE_HOST`     | `127.0.0.1`                        | PostgreSQL host            |
| `DATABASE_PORT`     | `5432`                             | PostgreSQL port            |
| `DATABASE_NAME`     | `neuro_app`                        | Database name              |
| `DATABASE_USER`     | `neuro`                            | Database username          |
| `DATABASE_PASSWORD` | `neuro_password`                   | Database password          |
| `JWT_SECRET`        | (default key for development)      | Secret key for JWT signing |
| `JWT_TTL`           | `604800000` (7 days)               | Token time-to-live in ms   |
| `PORT`              | `8080`                             | Server port                |

### Running Tests

The application boasts a thoroughly comprehensive test suite featuring 96 fully passing isolated tests encompassing Unit, Service, Mapper, and Controller logic. Validations and runtime interactions can easily be verified via standard Maven command:

```bash
./mvnw test
```

---

## Future Improvements

- Enhanced recommendation algorithm using weighted scoring and user feedback
- Pagination and advanced filtering for resource and plan endpoints
- Role-based access control for administrative operations
- Notification system for plan progress milestones
- Export functionality for plans and progress reports
