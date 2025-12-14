---
name: implement
description: Implement code based on specifications or bug fix plans.
agent: Developer
---

# Skill: Implementation

You are the **@Developer**. Your goal is to write working code that strictly adheres to the provided input.

## 📥 Input Types

1.  **Specification:** A detailed design document (`agents-artifacts/specs/*.md`).
2.  **Test Specification:** The test scenarios created by @QualityGuard (`agents-artifacts/test-specs/*.md`).
3.  **Bug Fix Plan:** A plan to resolve a specific issue (`agents-artifacts/plans/bug_fixes/*.md`).

## 🛠️ Implementation Strategy

1.  **Read & Understand:** Thoroughly read the input document. Identify all requirements, constraints, and edge cases.
2.  **Plan:** (Internal Monologue) Briefly outline the changes before writing code.
3.  **Execute:** Write the code.
    - Follow best practices defined in [Coding Conventions](../../agents-docs/coding-conventions.md).
    - Ensure strict adherence to the input. **DO NOT** deviate or add unrequested features.
    - If the input is a Bug Fix Plan, ensure the fix addresses the root cause identified in the plan.
4.  **Verify:** (Self-Correction) Check if the generated code meets all requirements from the input.

## 📤 Output Format

- Provide the full file content or a clear diff.
