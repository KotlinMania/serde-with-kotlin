# port-lint Proposed Changes

**Generated:** 2026-08-26
**Source:** tmp/serde_with/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/serdewith

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonTest/kotlin/io/github/kotlinmania/serdewith/KeyValueMapTest.kt` | `// port-lint: tests serde_as/key_value_map.rs` | `// port-lint: tests key_value_map.rs` | `key_value_map.rs` | `port-lint provenance header matched only by basename: 'tests:serde_as/key_value_map.rs' vs expected 'key_value_map.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/serdewith/EnumMapTest.kt` | `// port-lint: tests serde_as/enum_map.rs` | `// port-lint: tests enum_map.rs` | `enum_map.rs` | `port-lint provenance header matched only by basename: 'tests:serde_as/enum_map.rs' vs expected 'enum_map.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/serdewith/BoolFromIntTest.kt` | `// port-lint: tests serde_as/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only by basename: 'tests:serde_as/lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/serdewith/DisplayFromStrTest.kt` | `// port-lint: tests serde_as/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only by basename: 'tests:serde_as/lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/serdewith/NoneAsEmptyStringTest.kt` | `// port-lint: tests serde_as/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only by basename: 'tests:serde_as/lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/serdewith/OneOrManyTest.kt` | `// port-lint: tests serde_as/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only by basename: 'tests:serde_as/lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/serdewith/SkipErrorTest.kt` | `// port-lint: tests serde_as/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only by basename: 'tests:serde_as/lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/serdewith/StringWithSeparatorTest.kt` | `// port-lint: tests serde_as/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only by basename: 'tests:serde_as/lib.rs' vs expected 'lib.rs'` |
