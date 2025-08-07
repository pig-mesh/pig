# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PigX is a proprietary enterprise-grade microservices platform built on Spring Cloud Alibaba, focusing on AI knowledge
management, document processing, and intelligent Q&A capabilities. The current version is 5.10.2-SNAPSHOT.

**Important**: This is NOT an open-source project. All rights reserved by the author. Commercial use requires proper
licensing.

## Technology Stack

- **Java Version**: Java 17
- **Spring Boot**: 3.5.6
- **Spring Cloud**: 2025.0.0
- **Spring Cloud Alibaba**: 2023.0.3.3
- **AI/ML Libraries**:
    - LangChain4j: 1.6.0
    - Spring AI: 1.0.2
- **Database**:
    - MySQL (for business data)
    - PostgreSQL with pgvector extension (for vector storage)
    - Support for Qdrant, Milvus, Chroma, Neo4j
- **ORM**: MyBatis-Plus
- **Container**: Undertow
- **Service Discovery**: Nacos
- **Build Tool**: Maven

## Project Structure

### Module Organization

```
pigx/
├── pigx-boot/              # Monolithic deployment starter (combines all modules)
├── pigx-knowledge/         # AI knowledge base module (core AI capabilities)
├── pigx-auth/              # Authentication module (dependency)
├── pigx-upms-biz/          # User management module (dependency)
├── pigx-gateway/           # API gateway (microservices mode)
├── pigx-register/          # Nacos registry (microservices mode)
├── pigx-visual/            # Visual management modules (monitoring, codegen, etc.)
├── db/                     # Database initialization scripts
└── docker-compose.yml      # Docker compose configuration
```

### Key Module: pigx-knowledge

The knowledge module follows a standard layered architecture:

```
pigx-knowledge/src/main/java/com/pig4cloud/pigx/knowledge/
├── controller/           # REST API endpoints
├── service/             # Business logic layer
│   └── impl/           # Service implementations
├── mapper/             # MyBatis mapper interfaces
├── entity/             # JPA/MyBatis entities
├── dto/                # Data transfer objects
├── config/             # Spring configuration classes
└── support/            # Support utilities and handlers
    ├── constant/       # Enums and constants
    ├── handler/        # Specialized handlers
    │   ├── rag/       # RAG (Retrieval Augmented Generation) handlers
    │   ├── parse/     # Document parsing handlers
    │   ├── model/     # AI model handlers
    │   └── websearch/ # Web search handlers
    ├── provider/       # External service providers
    ├── rule/          # LiteFlow rule definitions
    ├── function/      # LangChain4j functions
    └── util/          # Utility classes
```

### Vector Storage Architecture

The system uses a factory pattern for vector store implementations:

- **Interface**: `EmbeddingStoreFactory` (pigx-knowledge/support/handler/rag/)
- **Supported Stores**: Qdrant, Milvus, Chroma, PGVector, Neo4j
- **Factory Method**: `createEmbeddingStore(embedStoreEntity, datasetEntity)`
- Each store type has its own factory implementation that checks `support(storeType)`

## Build and Run Commands

### Maven Profiles

The project has two main profiles:

- **cloud** (default): Microservices mode with Spring Cloud
- **boot**: Monolithic mode using pigx-boot

### Build Commands

```bash
# Build entire project (microservices mode)
mvn clean install

# Build monolithic version
mvn clean install -P boot

# Build specific module
mvn clean install -pl pigx-knowledge

# Skip tests
mvn clean install -DskipTests

# Build Docker image for a module
mvn clean install docker:build -pl pigx-knowledge

# Format code according to Spring Java Format
mvn spring-javaformat:apply
```

### Run Commands

#### Monolithic Mode (Recommended for Development)

```bash
# Run pigx-boot module
cd pigx-boot
mvn spring-boot:run

# Or run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Default port**: 8080 (configured in pigx-boot)

#### Microservices Mode

```bash
# Start all services using Docker Compose
docker-compose up

# Or start infrastructure first, then individual services
# 1. Start Nacos (service discovery)
cd pigx-register && mvn spring-boot:run

# 2. Start gateway
cd pigx-gateway && mvn spring-boot:run

# 3. Start knowledge service
cd pigx-knowledge && mvn spring-boot:run
```

**Knowledge service port**: 9010

### Test Commands

```bash
# Run all tests
mvn test

# Run tests for specific module
mvn test -pl pigx-knowledge

# Run specific test class
mvn test -pl pigx-knowledge -Dtest=YourTestClass

# Run tests with coverage
mvn clean test jacoco:report
```

## Development Configuration

### Database Setup

The project uses PostgreSQL with pgvector for the boot module:

- **Host**: localhost (or pgvector in Docker)
- **Port**: 5432
- **Database**: pigxx_boot
- **Username**: postgres
- **Password**: postgres

Database initialization scripts are in `db/` directory:

- `0pigxx_boot.sql` - Boot module schema
- `9pigxx_ai.sql` - AI knowledge base schema

### Redis Configuration

- **Host**: 127.0.0.1
- **Database**: 5

### Nacos Configuration

For microservices mode:

- **Server**: pigx-register:8848
- **Username**: nacos
- **Password**: nacos
- Configuration files are loaded from Nacos dynamically

### File Storage

Local file storage (configurable):

- **Path**: /Users/lengleng/Downloads/img (development)
- Can be configured in `application-dev.yml`

## Code Style and Conventions

### Code Formatting

The project uses Spring Java Format. Run before committing:

```bash
mvn spring-javaformat:apply
```

### Package Naming Conventions

- **Controllers**: Suffix with `Controller` (e.g., `AiDatasetController`)
- **Services**: Interface without suffix, impl with `Impl` (e.g., `EmbeddingStoreService`, `EmbeddingStoreServiceImpl`)
- **Mappers**: Suffix with `Mapper` for both interface and XML (e.g., `AiDatasetMapper.java`, `AiDatasetMapper.xml`)
- **Entities**: Suffix with `Entity` (e.g., `AiDatasetEntity`)
- **DTOs**: Suffix with `DTO` (e.g., `AiMultimodalEmbeddingArkDTO`)
- **Enums**: Suffix with `Enums` (e.g., `EmbedBizTypeEnums`, `ModelSupportEnums`)

### MyBatis Mapper XML Location

All MyBatis mapper XMLs are in: `src/main/resources/mapper/*.xml`

### Constants and Enums

Located in `support/constant/` package. Key enums:

- `EmbedBizTypeEnums` - Embedding business types
- `ModelSupportEnums` - Supported AI models
- `EmbedStoreSupportEnums` - Supported vector stores
- `FileTypeEnums` - Supported file types
- `ChatTypeEnums` - Chat interaction types

## AI/RAG Implementation Details

### Embedding Store Factory Pattern

When working with vector stores:

1. Create a factory class implementing `EmbeddingStoreFactory`
2. Implement `support(String storeType)` to declare supported types
3. Implement `createEmbeddingStore(...)` to instantiate the store
4. Register as a Spring `@Component`

### LangChain4j Integration

- Document parsers: Apache PDFBox, Apache POI
- Embedding models: Support for OpenAI, custom models via factory
- Vector stores: Qdrant, Milvus, Chroma, PGVector
- Document splitters and transformers in handler/rag package

### LiteFlow Rule Engine

Business rules are defined in the `support/rule/` package using LiteFlow DSL.

## API Documentation

API documentation is available via Swagger/Knife4j:

- **Swagger UI**: http://localhost:9010/doc.html (knowledge service)
- **OpenAPI**: Enabled via `@EnableOpenApi` annotation
- Token URL for auth: Configured in swagger.token-url property

## Git Workflow

**Current branch**: ai-leng-dev
**Main branch**: dev (use this for PRs)

## Important Notes

### Security

- Configuration encryption: Uses Jasypt with password "pigx"
- Login encryption: Uses AES with key "pigxpigxpigxpigx" (16 chars)
- OAuth2 resource server protection enabled

### Docker

Docker Maven plugin is configured but disabled by default (`docker.skip=false` in pom).
Registry: registry.cn-hangzhou.aliyuncs.com/pigcloudx

### Magic API

Low-code API platform is integrated for dynamic API creation:

- Configuration: `src/main/resources/lowcode/magic-api.yml`
- Requires additional setup for production use

### File Parsing

Supports multiple formats via Tika, Torchv, and custom parsers:

- PDF, Word, Excel, PowerPoint (Apache POI)
- HTML to DOCX conversion (Aspose Words)
- Unstructured data (Torchv)

## Common Development Tasks

### Adding a New Vector Store

1. Create factory class in `pigx-knowledge/support/handler/rag/`
2. Implement `EmbeddingStoreFactory` interface
3. Add store type to `EmbedStoreSupportEnums`
4. Add dependencies in `pigx-knowledge/pom.xml`

### Adding a New AI Model

1. Add model configuration to `ModelSupportEnums`
2. Create model provider in `support/provider/` if needed
3. Update embedding service implementation

### Adding a New Document Parser

1. Create parser handler in `support/handler/parse/`
2. Register file type in `FileTypeEnums`
3. Integrate with document processing pipeline

### Modifying Database Schema

1. Update SQL in `db/` directory
2. Update corresponding entity classes
3. Update MyBatis mapper interfaces and XMLs
4. Run database migration scripts