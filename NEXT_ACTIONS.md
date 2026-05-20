# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 1/35 (2.9%)
- **Function parity:** 1/1040 matched (target 9) — 0.1%
- **Class/type parity:** 11/205 matched (target 32) — 5.4%
- **Combined symbol parity:** 12/1245 matched (target 41) — 1.0%
- **Average inline-code cosine:** 0.95 (function body across 1 matched files)
- **Average documentation cosine:** 0.79 (doc text across 1 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 0 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. formats

- **Target:** `serdewith.Formats`
- **Similarity:** 0.95
- **Dependents:** 0
- **Priority Score:** 1200.5
- **Functions:** 1/1 matched (target 9)
- **Missing functions:** _none_
- **Types:** 11/11 matched (target 32)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Next Commands

```bash
# Initialize task queue for systematic porting
cd tools/ast_distance
./ast_distance --init-tasks ../../tmp/serde_with/src rust ../../src/commonMain/kotlin/io/github/kotlinmania/serdewith kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `content.mod` | `content.Mod` | 0 | `content/mod.rs` | `content/Mod.kt` |
| `de.mod` | `de.Mod` | 0 | `de/mod.rs` | `de/Mod.kt` |
| `duplicate_key_impls.mod` | `duplicatekeyimpls.Mod` | 0 | `duplicate_key_impls/mod.rs` | `duplicatekeyimpls/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `ser.mod` | `ser.Mod` | 0 | `ser/mod.rs` | `ser/Mod.kt` |
