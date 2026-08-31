# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 14/35 (40.0%)
- **Function parity:** 13/907 matched (target 121) — 1.4%
- **Class/type parity:** 17/209 matched (target 103) — 8.1%
- **Combined symbol parity:** 30/1116 matched (target 224) — 2.7%
- **Average inline-code cosine:** 0.10 (function body across 12 matched files)
- **Average documentation cosine:** 0.29 (doc text across 12 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 13 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. key_value_map

- **Target:** `serdewith.KeyValueMap [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1808210.0
- **Functions:** 1/52 matched (target 4)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_map`, `is_human_readable`, `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_i128`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_u128`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_element`, `end`, `serialize_field`, `serialize_key`, `serialize_value`, `deserialize_seq`, `deserialize_any`, `next_element_seed`, `size_hint`, `deserialize_tuple`, `deserialize_tuple_struct`, `deserialize_map`, `deserialize_struct`, `visit_seq`, `next_key_seed`, `next_value_seed`
- **Types:** 1/30 matched (target 3)
- **Missing types:** `KeyValueMap`, `KeyValueMapVisitor`, `Value`, `SeqAsMapSerializer`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `SerializeSeqElement`, `ElementAsKeyValueSerializer`, `KeyValueSeqSerializer`, `KeyValueTupleSerializer`, `KeyValueTupleStructSerializer`, `KeyValueStructSerializer`, `SeqDeserializer`, `MapKeyDeserializer`, `KeyValueSeqDeserialize`, `KeyValueTupleDeserialize`, `KeyValueTupleStructDeserialize`, `KeyValueMapDeserialize`, `KeyValueStructDeserialize`, `VisitorWrapper`, `MapAccessWrapper`, `SeqAccessWrapper`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/key_value_map.rs` vs expected `key_value_map.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/key_value_map.rs` vs expected `key_value_map.rs`
- **Proposed provenance header:** `// port-lint: source key_value_map.rs` (current: `// port-lint: source serde_with/src/key_value_map.rs`)
- **Proposed provenance header:** `// port-lint: tests key_value_map.rs` (current: `// port-lint: tests serde_with/src/key_value_map.rs`)
- **Lint issues:** 2

### 2. enum_map

- **Target:** `serdewith.EnumMap [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1697010.0
- **Functions:** 1/49 matched (target 4)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_map`, `is_human_readable`, `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_i128`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_u128`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_element`, `end`, `serialize_field`, `deserialize_seq`, `deserialize_any`, `next_element_seed`, `size_hint`, `deserialize_enum`, `variant_seed`, `unit_variant`, `newtype_variant_seed`, `tuple_variant`, `struct_variant`
- **Types:** 0/21 matched (target 3)
- **Missing types:** `EnumMap`, `EnumMapVisitor`, `Value`, `SeqAsMapSerializer`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `SerializeSeqElement`, `EnumAsMapElementSerializer`, `SerializeVariant`, `SeqDeserializer`, `EnumDeserializer`, `Variant`, `SeedTupleVariant`, `SeedStructVariant`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/enum_map.rs` vs expected `enum_map.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/enum_map.rs` vs expected `enum_map.rs`
- **Proposed provenance header:** `// port-lint: source enum_map.rs` (current: `// port-lint: source serde_with/src/enum_map.rs`)
- **Proposed provenance header:** `// port-lint: tests enum_map.rs` (current: `// port-lint: tests serde_with/src/enum_map.rs`)
- **Lint issues:** 2

### 3. with_prefix

- **Target:** `serdewith.WithPrefix [PROVENANCE-FALLBACK]`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 525509.8
- **Functions:** 2/43 matched (target 15)
- **Missing functions:** `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_key`, `serialize_value`, `serialize_entry`, `end`, `serialize_field`, `deserialize_any`, `deserialize_option`, `deserialize_identifier`, `expecting`, `visit_map`, `next_key_seed`, `next_value_seed`, `visit_unit`
- **Types:** 1/12 matched (target 5)
- **Missing types:** `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `Value`, `WithPrefixOption`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/with_prefix.rs` vs expected `with_prefix.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/with_prefix.rs` vs expected `with_prefix.rs`
- **Proposed provenance header:** `// port-lint: source with_prefix.rs` (current: `// port-lint: source serde_with/src/with_prefix.rs`)
- **Proposed provenance header:** `// port-lint: tests with_prefix.rs` (current: `// port-lint: tests serde_with/src/with_prefix.rs`)
- **Lint issues:** 2

### 4. with_suffix

- **Target:** `serdewith.WithSuffix [PROVENANCE-FALLBACK]`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 525509.8
- **Functions:** 2/43 matched (target 15)
- **Missing functions:** `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_key`, `serialize_value`, `serialize_entry`, `end`, `serialize_field`, `deserialize_any`, `deserialize_option`, `deserialize_identifier`, `expecting`, `visit_map`, `next_key_seed`, `next_value_seed`, `visit_unit`
- **Types:** 1/12 matched (target 5)
- **Missing types:** `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `Value`, `WithSuffixOption`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/with_suffix.rs` vs expected `with_suffix.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/with_suffix.rs` vs expected `with_suffix.rs`
- **Proposed provenance header:** `// port-lint: source with_suffix.rs` (current: `// port-lint: source serde_with/src/with_suffix.rs`)
- **Proposed provenance header:** `// port-lint: tests with_suffix.rs` (current: `// port-lint: tests serde_with/src/with_suffix.rs`)
- **Lint issues:** 2

### 5. flatten_maybe

- **Target:** `serdewith.FlattenMaybe [PROVENANCE-FALLBACK]`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 262709.9
- **Functions:** 1/22 matched (target 3)
- **Missing functions:** `expecting`, `visit_bool`, `visit_i8`, `visit_i16`, `visit_i32`, `visit_i64`, `visit_i128`, `visit_u8`, `visit_u16`, `visit_u32`, `visit_u64`, `visit_u128`, `visit_f32`, `visit_f64`, `visit_char`, `visit_unit`, `visit_str`, `visit_bytes`, `visit_borrowed_str`, `visit_borrowed_bytes`, `visit_map`
- **Types:** 0/5 matched (target 3)
- **Missing types:** `FlattenedMaybe`, `Value`, `Field`, `FieldVisitor`, `FlattenedMaybeVisitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/flatten_maybe.rs` vs expected `flatten_maybe.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/flatten_maybe.rs` vs expected `flatten_maybe.rs`
- **Proposed provenance header:** `// port-lint: source flatten_maybe.rs` (current: `// port-lint: source serde_with/src/flatten_maybe.rs`)
- **Proposed provenance header:** `// port-lint: tests flatten_maybe.rs` (current: `// port-lint: tests serde_with/src/flatten_maybe.rs`)
- **Lint issues:** 2

### 6. utils.duration

- **Target:** `serdewith.Duration [PROVENANCE-FALLBACK]`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 222609.6
- **Functions:** 2/20 matched (target 22)
- **Missing functions:** `is_positive`, `is_negative`, `apply_f64`, `apply_i64`, `new`, `checked_mul`, `checked_div`, `with_duration`, `to_system_time`, `to_std_duration`, `from`, `serialize_as`, `expecting`, `visit_i64`, `visit_u64`, `visit_f64`, `visit_str`, `deserialize_as`
- **Types:** 2/6 matched (target 14)
- **Missing types:** `DurationSigned`, `DurationVisitorFlexible`, `Value`, `DurationDeserializationVisitor`
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/utils/duration.rs` vs expected `utils/duration.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/utils/duration.rs` vs expected `utils/duration.rs`
- **Proposed provenance header:** `// port-lint: source utils/duration.rs` (current: `// port-lint: source serde_with/src/utils/duration.rs`)
- **Proposed provenance header:** `// port-lint: tests utils/duration.rs` (current: `// port-lint: tests serde_with/src/utils/duration.rs`)
- **Lint issues:** 2

### 7. base64

- **Target:** `serdewith.Base64 [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 161610.0
- **Functions:** 0/5 matched (target 9)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_str`, `charset`
- **Types:** 0/11 matched (target 16)
- **Missing types:** `Base64`, `Helper`, `Value`, `Sealed`, `Alphabet`, `Standard`, `UrlSafe`, `Crypt`, `Bcrypt`, `ImapMutf7`, `BinHex`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/base64.rs` vs expected `base64.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/base64.rs` vs expected `base64.rs`
- **Proposed provenance header:** `// port-lint: source base64.rs` (current: `// port-lint: source serde_with/src/base64.rs`)
- **Proposed provenance header:** `// port-lint: tests base64.rs` (current: `// port-lint: tests serde_with/src/base64.rs`)
- **Lint issues:** 2

### 8. de.skip_error

- **Target:** `serdewith.SkipError [PROVENANCE-FALLBACK]`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 91009.5
- **Functions:** 1/5 matched (target 4)
- **Missing functions:** `deserialize_as`, `expecting`, `visit_seq`, `visit_map`
- **Types:** 0/5 matched (target 2)
- **Missing types:** `GoodOrError`, `SeqVisitor`, `Value`, `MapSkipErrorVisitor`, `KVPair`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/de/skip_error.rs` vs expected `de/skip_error.rs`
- **Proposed provenance header:** `// port-lint: source de/skip_error.rs` (current: `// port-lint: source serde_with/src/de/skip_error.rs`)
- **Lint issues:** 1

### 9. json

- **Target:** `serdewith.Json [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 70710.0
- **Functions:** 0/4 matched (target 5)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_str`
- **Types:** 0/3 matched (target 8)
- **Missing types:** `JsonString`, `Helper`, `Value`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/json.rs` vs expected `json.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/json.rs` vs expected `json.rs`
- **Proposed provenance header:** `// port-lint: source json.rs` (current: `// port-lint: source serde_with/src/json.rs`)
- **Proposed provenance header:** `// port-lint: tests json.rs` (current: `// port-lint: tests serde_with/src/json.rs`)
- **Lint issues:** 2

### 10. rust

- **Target:** `serdewith.Rust [PROVENANCE-FALLBACK]`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 60909.3
- **Functions:** 2/6 matched (target 20)
- **Missing functions:** `expecting`, `visit_seq`, `visit_map`, `deserialize_ignore_any`
- **Types:** 1/3 matched (target 7)
- **Missing types:** `SeqVisitor`, `MapVisitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/rust.rs` vs expected `rust.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/rust.rs` vs expected `rust.rs`
- **Proposed provenance header:** `// port-lint: source rust.rs` (current: `// port-lint: source serde_with/src/rust.rs`)
- **Proposed provenance header:** `// port-lint: tests rust.rs` (current: `// port-lint: tests serde_with/src/rust.rs`)
- **Lint issues:** 2

### 11. hex

- **Target:** `serdewith.Hex [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/2 matched (target 11)
- **Missing functions:** `serialize_as`, `deserialize_as`
- **Types:** 0/1 matched (target 5)
- **Missing types:** `Hex`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/hex.rs` vs expected `hex.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/hex.rs` vs expected `hex.rs`
- **Proposed provenance header:** `// port-lint: source hex.rs` (current: `// port-lint: source serde_with/src/hex.rs`)
- **Proposed provenance header:** `// port-lint: tests hex.rs` (current: `// port-lint: tests serde_with/src/hex.rs`)
- **Lint issues:** 2

### 12. formats

- **Target:** `serdewith.Formats [PROVENANCE-FALLBACK]`
- **Similarity:** 0.95
- **Dependents:** 0
- **Priority Score:** 1200.5
- **Functions:** 1/1 matched (target 9)
- **Missing functions:** _none_
- **Types:** 11/11 matched (target 32)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde_with/src/formats.rs` vs expected `formats.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde_with/src/formats.rs` vs expected `formats.rs`
- **Proposed provenance header:** `// port-lint: source formats.rs` (current: `// port-lint: source serde_with/src/formats.rs`)
- **Proposed provenance header:** `// port-lint: tests formats.rs` (current: `// port-lint: tests serde_with/src/formats.rs`)
- **Lint issues:** 2

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Matched

| Source | Target | Path |
|--------|--------|------|
| `lib` | `serdewith.Mod` | `lib` |
| `duplicate_key_impls.mod` | `serdewith.DuplicateKey` | `duplicate_key_impls/mod` |

