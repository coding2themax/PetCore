# ADR-0008: Use AWS Application Load Balancer as API Gateway

## Status

Accepted

## Context

We need a routing layer between the frontend and backend services to handle SSL termination, path-based routing, health checks, and future service discovery. This layer should be simple to configure and cost-effective for our initial scale.

## Decision

We will use AWS Application Load Balancer (ALB) as our API gateway/edge routing layer for the initial deployment.

## Alternatives Considered

- **AWS API Gateway**: More features (throttling, API keys) but higher cost and complexity for our simple routing needs
- **Kong/Nginx**: Requires managing additional infrastructure
- **Service Mesh (Istio)**: Over-engineered for monolith; useful later for microservices
- **Direct exposure**: No SSL termination, health checks, or future flexibility

## Consequences

### Positive

- Simple configuration for single backend target
- Built-in SSL/TLS termination
- Health checks and auto-recovery
- Path-based routing ready for future service extraction
- WebSocket support for real-time features
- Integrates with AWS WAF for security
- Supports blue-green deployments
- Low cost for our traffic volume

### Negative

- Less sophisticated features than API Gateway (no request/response transformation)
- Limited rate limiting capabilities (need application-level throttling)
- Basic authentication options (need to implement auth in backend)
- AWS-specific (migration complexity if leaving AWS)

## Implementation Notes

- Configure target group with Spring Boot Actuator health endpoint
- Set up HTTPS listener with ACM certificate
- Configure path rules: `/api/*` → backend, `/` → frontend (S3/CloudFront)
- Enable access logs for debugging
- Set connection draining for graceful shutdowns
