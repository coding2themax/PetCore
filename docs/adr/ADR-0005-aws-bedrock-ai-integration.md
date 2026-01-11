# ADR-0005: Use AWS Bedrock for AI/LLM Integration

## Status

Accepted

## Context

AI augmentation features require access to large language models for generating pet personality summaries, explaining match scores, and providing adoption guidance. We need a managed AI service that is cost-effective, secure, and easy to integrate.

## Decision

We will use AWS Bedrock as our AI provider, leveraging Claude or other foundation models available through Bedrock's API for natural language generation tasks.

## Alternatives Considered

- **OpenAI API**: Excellent models but data leaves AWS, higher cost, and external dependency
- **Azure OpenAI**: Requires multi-cloud complexity
- **Self-hosted LLM**: Significant infrastructure and ML ops overhead
- **Anthropic direct**: Similar to OpenAI concerns with data residency
- **SageMaker with custom models**: Too complex for our initial needs

## Consequences

### Positive

- Stays within AWS ecosystem (same VPC, IAM, billing)
- Pay-per-use pricing with no infrastructure management
- Multiple model choices (Claude, Titan, etc.)
- Built-in content filtering and safety guardrails
- Data privacy and compliance (data doesn't leave AWS)
- Easy integration with existing AWS services
- Access to latest foundation models without retraining

### Negative

- AWS vendor lock-in for AI capabilities
- Model availability varies by region
- Potentially higher per-token costs than self-hosted at scale
- Less control over model fine-tuning
- API rate limits may require request throttling

## Implementation Notes

- Use AWS SDK for Java to integrate with Bedrock
- Implement caching layer for common prompts
- Set up CloudWatch alerts for cost and rate limit monitoring
- Design prompts to be provider-agnostic for future portability
