# PetCore

## Architecture (Stage 1 — Modular Monolith)

```mermaid
flowchart TB
    User[User Browser]

    subgraph FE[Frontend]
        React["React SPA<br/>React + TypeScript<br/>React Router"]
    end

    subgraph Edge[Edge / Routing]
        Gateway[API Gateway / ALB]
    end

    subgraph BE["PetCore Backend Spring Boot"]



        Profile["Pet Profile Module<br/>- Pets & Shelters<br/>- Intake Records"]
        Matching["Matching & Classification Module<br/>- Rule-Based Scoring<br/>- Compatibility Logic"]
        Search["Search Module<br/>- Filtering & Ranking<br/>- Explainable Results"]
        AI["AI Augmentation Module<br/>(Optional, Feature-Flagged)"]
    end

    DB[("PostgreSQL (RDS)<br/>Single Schema")]

    AIProvider["AI Provider<br/>(Bedrock / LLM)"]

    User --> React
    React -->|HTTPS| Gateway
    Gateway --> BE

    BE --> Profile
    BE --> Matching
    BE --> Search
    BE --> AI

    Profile --> DB
    Matching --> DB
    Search --> DB

    AI -. Optional .-> AIProvider
```

## Architecture (Stage 2 — Selective Microservices)

```mermaid
flowchart TB
    User[User Browser]

    subgraph FE[Frontend]
        React["React SPA<br/>React + TypeScript<br/>React Router"]
    end

    subgraph Edge[Edge / Routing]
        Gateway[API Gateway]
    end

    React -->|HTTPS| Gateway

    subgraph Core[Core Services]
        Profile["pet-profile-service<br/>Java + Spring Boot"]
    end

    subgraph Domain[Extracted Domain Services]
        Matching["pet-matching-service<br/>Java + Spring Boot"]
        Search["pet-search-service<br/>Java + Spring Boot"]
    end

    subgraph AIBlock[AI Services]
        AIService["pet-augmentation-service<br/>(Optional)"]
    end

    ProfileDB[(PostgreSQL RDS)]
    MatchingDB[(DynamoDB)]
    SearchDB[(OpenSearch)]

    AIProvider["AI Provider<br/>(Bedrock / LLM)"]

    Gateway --> Profile
    Gateway --> Matching
    Gateway --> Search
    Gateway --> AIService

    Profile --> ProfileDB
    Matching --> MatchingDB
    Search --> SearchDB

    AIService -. Controlled .-> AIProvider

```

## Service Interaction Flows

### Pet Intake Flow

Purpose: Demonstrate end-to-end pet intake process with validation and persistence

```mermaid
sequenceDiagram
    participant Staff as Shelter Staff
    participant FE as React SPA
    participant GW as API Gateway
    participant Auth as Security Module
    participant API as API Controller
    participant Profile as Profile Module
    participant DB as PostgreSQL (RDS)
    participant Obs as Observability

    Staff ->> FE: Fill out pet intake form
    Staff ->> FE: Submit intake request

    FE ->> GW: POST /api/pets/intake<br/>{petData, shelterId}
    GW ->> Auth: Validate JWT token
    Auth -->> GW: Authenticated user

    GW ->> API: Forward intake request
    API ->> Obs: Log intake attempt

    API ->> API: Validate request payload

    alt Validation fails
        API -->> GW: 400 Bad Request<br/>Validation errors
        GW -->> FE: Error response
        FE -->> Staff: Show validation errors
    else Validation succeeds
        API ->> Profile: Create pet profile
        Profile ->> Profile: Generate unique pet ID
        Profile ->> Profile: Apply business rules

        Profile ->> DB: INSERT INTO pets
        DB -->> Profile: Persisted record

        Profile ->> DB: INSERT INTO intake_records
        DB -->> Profile: Intake record saved

        Profile -->> API: Pet profile created
        API ->> Obs: Log successful intake

        API -->> GW: 201 Created<br/>{petId, profile}
        GW -->> FE: Success response
        FE -->> Staff: Show confirmation<br/>Display pet ID
    end

    Note over Staff,Obs: System operates fully without AI dependency

```

### Pet Profile Creation

```mermaid

sequenceDiagram
    participant U as User
    participant FE as React SPA
    participant GW as API Gateway
    participant PS as pet-profile-service
    participant DB as PostgreSQL (RDS)

    U ->> FE: Submit pet intake form
    FE ->> GW: POST /pets
    GW ->> PS: Forward request
    PS ->> DB: Persist pet profile
    DB -->> PS: Saved record
    PS -->> GW: 201 Created
    GW -->> FE: Response
    FE -->> U: Confirmation UI

```

### Matching & Classification Flow (Rules First)

Purpose: Demonstrate deterministic logic + optional AI

```mermaid
sequenceDiagram
    participant U as User
    participant FE as React SPA
    participant GW as API Gateway
    participant MS as pet-matching-service
    participant PS as pet-profile-service
    participant MDB as DynamoDB
    participant AI as pet-augmentation-service
    participant LLM as AI Provider

    U ->> FE: Request pet match
    FE ->> GW: GET /match?petId=123
    GW ->> MS: Forward request

    MS ->> PS: GET /pets/123
    PS -->> MS: Pet profile data

    MS ->> MS: Run rule-based scoring
    MS ->> MDB: Store match result

    alt Low confidence & AI enabled
        MS ->> AI: Request explanation
        AI ->> LLM: Summarize reasoning
        LLM -->> AI: Explanation
        AI -->> MS: AI response
    end

    MS -->> GW: Match result (+ explanation)
    GW -->> FE: Response
    FE -->> U: Explainable results UI
```

### Search Flow with Optional Semantic Re-Ranking

Purpose: Show performance-aware AI usage

```mermaid
sequenceDiagram
    participant U as User
    participant FE as React SPA
    participant GW as API Gateway
    participant SS as pet-search-service
    participant OS as OpenSearch
    participant AI as pet-augmentation-service
    participant LLM as AI Provider

    U ->> FE: Search pets
    FE ->> GW: GET /search?q=family+dog
    GW ->> SS: Forward request

    SS ->> OS: Keyword search (BM25)
    OS -->> SS: Ranked results

    alt Semantic re-ranking enabled
        SS ->> AI: Re-rank results
        AI ->> LLM: Generate embeddings
        LLM -->> AI: Semantic scores
        AI -->> SS: Re-ranked list
    end

    SS -->> GW: Search results
    GW -->> FE: Response
    FE -->> U: Search UI
```
