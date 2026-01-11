# ADR-0002: Use PostgreSQL as Primary Database

## Status

Accepted

## Context

The PetCore platform requires a robust, relational database to store pet profiles, shelter information, user preferences, and matching data. We need ACID compliance, complex querying capabilities, and support for future data analytics.

## Decision

We will use PostgreSQL as our primary database, deployed on AWS RDS for managed operations, automated backups, and high availability.

## Alternatives Considered

- **MySQL/MariaDB**: Good performance but lacks some advanced features like JSONB and full-text search capabilities
- **MongoDB**: Flexible schema but lacks strong consistency guarantees needed for adoption workflows
- **DynamoDB**: Excellent for key-value access but complex queries and relational data modeling would be challenging

## Consequences

### Positive

- Strong ACID guarantees for critical adoption workflows
- Rich querying capabilities with SQL
- JSONB support for flexible pet attribute storage
- Built-in full-text search for pet descriptions
- Mature ecosystem and extensive Spring Boot integration
- AWS RDS provides managed backups, patching, and scaling

### Negative

- Vertical scaling limitations compared to NoSQL alternatives
- Higher operational costs than serverless options
- Requires careful index management for performance at scale
