---
agent: speckit.constitution
description: Create or update the project constitution from interactive or provided principle inputs, ensuring all dependent templates stay in sync.
---

# SpecKit Constitution Management

## Goal

Manage the project's engineering constitution (`.specify/memory/constitution.md`) - a foundational document that defines non-negotiable principles, standards, and guidelines for all feature specifications, plans, and implementations.

## User Input

```text
$ARGUMENTS
```

You **MUST** consider the user input before proceeding (if not empty).

## Operating Constraints

**Constitution is Authoritative**: The constitution serves as the ultimate source of truth for quality standards, architectural principles, and process requirements. All specs, plans, and tasks must align with constitution principles.

**Versioned and Immutable**: Each constitution update creates a new version. Previous versions are preserved for audit trail.

**Explicit User Approval Required**: Never modify the constitution without explicit user confirmation, as changes cascade to all features.

## Constitution Structure

The constitution file must contain these sections:

### 1. Project Identity

```markdown
# Project Constitution

**Project Name**: [Project Name]  
**Version**: [Semantic Version]  
**Last Updated**: [Date]  
**Maintainers**: [List of maintainers]

## Purpose

[Brief description of what this project does and its primary objectives]

## Scope

[What is in scope and out of scope for this project]
```

### 2. Core Principles

List of normative principles using RFC 2119 keywords (MUST, MUST NOT, SHOULD, SHOULD NOT, MAY):

```markdown
## Core Principles

### P1: [Principle Name]

**Category**: [Architecture | Quality | Process | Security | Performance | Testing]

**Statement**: [Normative statement using MUST/SHOULD]

**Rationale**: [Why this principle exists]

**Examples**:
- ✅ Good: [Concrete example of compliance]
- ❌ Bad: [Concrete example of violation]

**Exceptions**: [If any, under what conditions this can be relaxed]
```

### 3. Mandatory Artifacts

```markdown
## Mandatory Artifacts

All features MUST include:

1. **spec.md** - Feature specification with:
   - Overview/Context
   - Functional Requirements
   - Non-Functional Requirements
   - User Stories
   - Edge Cases

2. **plan.md** - Implementation plan with:
   - Architecture decisions
   - Data model
   - Phases with tasks
   - Technical constraints

3. **tasks.md** - Executable task list with:
   - Unique task IDs
   - Phase grouping
   - Dependencies
   - File paths
```

### 4. Quality Gates

```markdown
## Quality Gates

### Specification Phase
- [ ] All functional requirements have measurable acceptance criteria
- [ ] Non-functional requirements specify numeric thresholds
- [ ] User stories follow "As a [role], I want [goal], so that [benefit]" format
- [ ] Edge cases identified and documented

### Planning Phase
- [ ] Architecture aligns with constitution principles
- [ ] Data model defined for all entities
- [ ] Tasks cover all requirements (100% coverage)
- [ ] Dependencies explicitly mapped

### Implementation Phase
- [ ] All tasks completed
- [ ] Tests written and passing
- [ ] Documentation updated
- [ ] Code review completed
```

### 5. Standards and Guidelines

```markdown
## Standards and Guidelines

### Naming Conventions
[Project-specific naming rules]

### Code Style
[Project-specific code style requirements]

### Documentation
[Documentation standards]

### Testing
[Test coverage and quality requirements]

### Security
[Security requirements and practices]

### Performance
[Performance benchmarks and requirements]
```

## Execution Steps

### 1. Initialize Constitution Context

Check if constitution already exists:

```bash
if [ -f .specify/memory/constitution.md ]; then
  echo "Constitution exists - will update"
else
  echo "Constitution does not exist - will create"
fi
```

### 2. Interactive Constitution Builder (if creating new)

If no constitution exists and user didn't provide full constitution, run interactive builder:

**Ask user sequentially:**

1. "What is the project name?"
2. "What is the primary purpose of this project? (1-2 sentences)"
3. "What is explicitly OUT of scope for this project?"
4. "What are your top 3-5 non-negotiable engineering principles? (e.g., 'All APIs must be versioned', 'Zero-trust security model', 'Test coverage must exceed 80%')"
5. "Are there specific architectural constraints? (e.g., microservices, monolith, serverless)"
6. "What quality gates must every feature pass before merging?"
7. "Are there mandatory naming conventions or code style guides?"

**For each principle provided:**
- Assign stable ID (P1, P2, P3...)
- Categorize (Architecture, Quality, Process, Security, Performance, Testing)
- Ensure normative language (MUST/SHOULD)
- Request rationale if not provided
- Request concrete examples

### 3. Update Existing Constitution (if modifying)

If constitution exists and user wants to update:

1. Load current constitution
2. Parse existing principles (P1, P2, etc.)
3. Present current state to user
4. Ask: "What would you like to change? (add principle, modify principle, remove principle, update standards)"
5. For modifications:
   - Show before/after diff
   - Explain cascade impact (which specs/plans may be affected)
   - Request explicit confirmation

### 4. Validate Constitution Structure

Before writing, validate:

- [ ] All required sections present
- [ ] Principles use normative keywords (MUST/SHOULD)
- [ ] Each principle has category and rationale
- [ ] Quality gates are measurable
- [ ] Standards are specific (not vague)
- [ ] Version incremented if updating existing

### 5. Write Constitution File

Create/update `.specify/memory/constitution.md`:

```bash
mkdir -p .specify/memory
# Write constitution content
```

**Version Management:**
- If creating: Version 1.0.0
- If updating: Increment according to change type
  - Major (X.0.0): Breaking principle changes
  - Minor (x.Y.0): New principles added
  - Patch (x.y.Z): Clarifications, examples, rationale updates

### 6. Archive Previous Version (if updating)

If updating existing constitution:

```bash
# Archive previous version
cp .specify/memory/constitution.md .specify/memory/constitution-v[PREVIOUS_VERSION].md
```

### 7. Cascade Analysis

Analyze impact of constitution changes on existing features:

```bash
# Find all feature directories
find .specify/features -name "spec.md" -o -name "plan.md" -o -name "tasks.md"
```

For each feature:
- Report if new principles may require spec/plan updates
- Identify potential violations
- Suggest remediation commands

### 8. Generate Constitution Summary

Create human-readable summary document (`.specify/memory/constitution-summary.md`):

```markdown
# Constitution Quick Reference

## Principles by Category

### Architecture
- P1: [Principle statement]
- P3: [Principle statement]

### Quality  
- P2: [Principle statement]
- P5: [Principle statement]

### Security
- P4: [Principle statement]

## Quick Checklist

Before submitting any feature:
- [ ] Principle 1 compliance
- [ ] Principle 2 compliance
- [ ] ...
```

### 9. Update Template Files (if they exist)

If template files exist, ensure they reflect constitution requirements:

- `.specify/templates/spec-template.md`
- `.specify/templates/plan-template.md`
- `.specify/templates/tasks-template.md`

Add comments/sections enforcing constitution principles.

### 10. Output Confirmation

Provide user with:

```markdown
## Constitution Update Summary

**Version**: [New Version]  
**Previous Version**: [Old Version] (if applicable)  
**Changes**: [High-level summary]

### Added Principles
- P[N]: [Statement]

### Modified Principles
- P[N]: [Old] → [New]

### Removed Principles
- P[N]: [Statement]

### Affected Features
[List of features that may need review]

### Next Steps
1. Review constitution at `.specify/memory/constitution.md`
2. Run `/speckit.analyze` on existing features to check compliance
3. Update affected specs/plans as needed
```

## Constitution Principle Guidelines

### Good Principles

✅ **Specific and Measurable**
- "All API responses MUST complete within 200ms for 95th percentile"
- "Unit test coverage MUST exceed 80% for all business logic"

✅ **Enforceable**
- "All database migrations MUST be reversible"
- "Public APIs MUST use semantic versioning"

✅ **Valuable Constraints**
- "Authentication MUST use OAuth2 or OpenID Connect"
- "All user inputs MUST be validated and sanitized"

### Bad Principles (Avoid)

❌ **Vague or Subjective**
- "Code should be clean and maintainable" (no measurable criteria)
- "Performance should be good" (undefined threshold)

❌ **Unenforceable**
- "Developers should try their best" (not verifiable)
- "Consider security when possible" (too permissive)

❌ **Process Over Outcome**
- "Must have daily standups" (process, not principle)
- "Use Jira for all tracking" (tool mandate, not principle)

### Normative Language (RFC 2119)

Use precise keywords:

- **MUST** / **REQUIRED** / **SHALL**: Absolute requirement
- **MUST NOT** / **SHALL NOT**: Absolute prohibition  
- **SHOULD** / **RECOMMENDED**: Strong recommendation, exceptions allowed with justification
- **SHOULD NOT**: Strong recommendation against, exceptions allowed with justification
- **MAY** / **OPTIONAL**: Truly optional, left to implementer discretion

## Operating Principles

### Collaborative Process

- **Interactive by default**: Don't assume; ask clarifying questions
- **Incremental refinement**: Start with core principles, iterate
- **Example-driven**: Provide concrete examples for each principle
- **User-approved changes**: Never auto-commit constitution changes

### Version Control

- **Semantic versioning**: Major.Minor.Patch
- **Changelog**: Document all changes with rationale
- **Archival**: Keep previous versions for audit trail

### Impact Analysis

- **Cascade awareness**: Constitution changes affect all features
- **Proactive notification**: Alert users to affected features
- **Remediation guidance**: Suggest specific commands to fix violations

## Error Handling

**Missing .specify directory**:
```markdown
❌ Error: SpecKit not initialized in this repository.

Run the following to initialize:
```
mkdir -p .specify/memory .specify/templates .specify/features
```
```

**Invalid principle format**:
```markdown
❌ Error: Principle "Code should be good" is too vague.

Principles must:
- Use normative keywords (MUST/SHOULD)
- Be measurable or verifiable
- Include category and rationale

Example:
P1: All public APIs MUST return responses within 200ms at 95th percentile
Category: Performance
Rationale: Ensures responsive user experience for web and mobile clients
```

## Context

$ARGUMENTS

