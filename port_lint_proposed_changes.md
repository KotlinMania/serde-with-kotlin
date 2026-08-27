# port-lint Proposed Changes

**Generated:** 2026-08-26
**Source:** tmp
**Target:** src/commonMain/kotlin

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/serdewith/PrefixSuffix.kt` | `// port-lint: source serde_with/tests/with_prefix.rs` | `// port-lint: source with_prefix.rs` | `with_prefix.rs` | `port-lint provenance header matched only by basename: 'serde_with/tests/with_prefix.rs' vs expected 'with_prefix.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/serdewith/BoolFromInt.kt` | `// port-lint: source serde_with/src/lib.rs` | `// port-lint: source serde_with/tests/serde_as/lib.rs` | `serde_with/tests/serde_as/lib.rs` | `port-lint provenance header matched only by basename: 'serde_with/src/lib.rs' vs expected 'serde_with/tests/serde_as/lib.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/serdewith/OneOrMany.kt` | `// port-lint: source serde_with/src/lib.rs` | `// port-lint: source serde_with/tests/derives/lib.rs` | `serde_with/tests/derives/lib.rs` | `port-lint provenance header matched only by basename: 'serde_with/src/lib.rs' vs expected 'serde_with/tests/derives/lib.rs'` |
