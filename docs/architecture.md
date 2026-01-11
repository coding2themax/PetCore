# Package Architecture

### Package-Level Overview

#### Package-Level Overview (PetCore Modular Monolith)

```mermaid
flowchart LR
    subgraph PetCore["PetCore (Spring Boot Modular Monolith)"]
        API[api<br/>Controllers & Routing]
        Profile[profile<br/>Pet Profiles]
        Matching[matching<br/>Rule-Based Matching]
        Search[search<br/>Search & Discovery]
        Augmentation["augmentation<br/>AI (Optional)"]
        Security[security]
        Config[config]
        Observability[observability]

        API --> Profile
        API --> Matching
        API --> Search
        API --> Augmentation

        Matching --> Profile
        Search --> Profile

        Augmentation -. Optional .-> Matching
        Augmentation -. Optional .-> Search

        Profile --> Config
        Matching --> Config
        Search --> Config
        Augmentation --> Config

        API --> Security
        Profile --> Observability
        Matching --> Observability
        Search --> Observability
    end
```

## Internal Structure of a Domain Module

```mermaid
flowchart TB
    subgraph Profile["profile module"]
        API[api<br/>REST Controllers]
        App[application<br/>Use Cases]
        Domain[domain<br/>Entities & Rules]
        Infra[infrastructure<br/>Persistence & Adapters]
    end

    API --> App
    App --> Domain
    App --> Infra
    Infra --> Domain
```

#### Inter-Module Dependency Rules (Critical Diagram)

```mermaid
flowchart LR
    Profile[profile]
    Matching[matching]
    Search[search]
    Augmentation[augmentation]

    Matching -->|read-only| Profile
    Search -->|read-only| Profile

    Augmentation -.-> Matching
    Augmentation -.-> Search

    Profile -.->|NO DEPENDENCIES| Matching
    Profile -.->|NO DEPENDENCIES| Search
    Profile -.->|NO DEPENDENCIES| Augmentation
```

#### Optional: Future Service Extraction Mapping

```mermaid
flowchart LR
    ProfileMono[profile module] --> ProfileSvc[pet-profile-service]
    MatchingMono[matching module] --> MatchingSvc[pet-matching-service]
    SearchMono[search module] --> SearchSvc[pet-search-service]
    AugMono[augmentation module] --> AugSvc[pet-augmentation-service]
```
