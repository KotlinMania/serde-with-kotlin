# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 17/72 (23.6%)
- **Function parity:** 11/1392 matched (target 83) — 0.8%
- **Class/type parity:** 15/511 matched (target 102) — 2.9%
- **Combined symbol parity:** 26/1903 matched (target 185) — 1.4%
- **Average inline-code cosine:** 0.07 (function body across 16 matched files)
- **Average documentation cosine:** 0.23 (doc text across 16 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 16 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. json

- **Target:** `serdewith.Json [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3070710.0
- **Functions:** 0/4 matched (target 2)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_str`
- **Types:** 0/3 matched (target 1)
- **Missing types:** `JsonString`, `Helper`, `Value`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `json.rs` vs expected `json.rs`
- **Proposed provenance header:** `// port-lint: source json.rs` (current: `// port-lint: source json.rs`)
- **Lint issues:** 1

### 2. enum_map

- **Target:** `serdewith.EnumMap [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2697010.0
- **Functions:** 1/49 matched (target 2)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_map`, `is_human_readable`, `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_i128`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_u128`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_element`, `end`, `serialize_field`, `deserialize_seq`, `deserialize_any`, `next_element_seed`, `size_hint`, `deserialize_enum`, `variant_seed`, `unit_variant`, `newtype_variant_seed`, `tuple_variant`, `struct_variant`
- **Types:** 0/21 matched (target 1)
- **Missing types:** `EnumMap`, `EnumMapVisitor`, `Value`, `SeqAsMapSerializer`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `SerializeSeqElement`, `EnumAsMapElementSerializer`, `SerializeVariant`, `SeqDeserializer`, `EnumDeserializer`, `Variant`, `SeedTupleVariant`, `SeedStructVariant`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `enum_map.rs` vs expected `enum_map.rs`
- **Proposed provenance header:** `// port-lint: source enum_map.rs` (current: `// port-lint: source enum_map.rs`)
- **Lint issues:** 1

### 3. key_value_map

- **Target:** `serdewith.KeyValueMap [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1808210.0
- **Functions:** 1/52 matched (target 2)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_map`, `is_human_readable`, `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_i128`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_u128`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_element`, `end`, `serialize_field`, `serialize_key`, `serialize_value`, `deserialize_seq`, `deserialize_any`, `next_element_seed`, `size_hint`, `deserialize_tuple`, `deserialize_tuple_struct`, `deserialize_map`, `deserialize_struct`, `visit_seq`, `next_key_seed`, `next_value_seed`
- **Types:** 1/30 matched (target 2)
- **Missing types:** `KeyValueMap`, `KeyValueMapVisitor`, `Value`, `SeqAsMapSerializer`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `SerializeSeqElement`, `ElementAsKeyValueSerializer`, `KeyValueSeqSerializer`, `KeyValueTupleSerializer`, `KeyValueTupleStructSerializer`, `KeyValueStructSerializer`, `SeqDeserializer`, `MapKeyDeserializer`, `KeyValueSeqDeserialize`, `KeyValueTupleDeserialize`, `KeyValueTupleStructDeserialize`, `KeyValueMapDeserialize`, `KeyValueStructDeserialize`, `VisitorWrapper`, `MapAccessWrapper`, `SeqAccessWrapper`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `key_value_map.rs` vs expected `key_value_map.rs`
- **Proposed provenance header:** `// port-lint: source key_value_map.rs` (current: `// port-lint: source key_value_map.rs`)
- **Lint issues:** 1

### 4. with_suffix

- **Target:** `serdewith.WithSuffix [PROVENANCE-FALLBACK]`
- **Similarity:** 0.02
- **Dependents:** 1
- **Priority Score:** 1525509.9
- **Functions:** 2/43 matched (target 6)
- **Missing functions:** `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_key`, `serialize_value`, `serialize_entry`, `end`, `serialize_field`, `deserialize_any`, `deserialize_option`, `deserialize_identifier`, `expecting`, `visit_map`, `next_key_seed`, `next_value_seed`, `visit_unit`
- **Types:** 1/12 matched (target 2)
- **Missing types:** `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `Value`, `WithSuffixOption`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `with_suffix.rs` vs expected `with_suffix.rs`
- **Proposed provenance header:** `// port-lint: source with_suffix.rs` (current: `// port-lint: source with_suffix.rs`)
- **Lint issues:** 1

### 5. with_prefix

- **Target:** `serdewith.WithPrefix [PROVENANCE-FALLBACK]`
- **Similarity:** 0.02
- **Dependents:** 1
- **Priority Score:** 1525509.9
- **Functions:** 2/43 matched (target 6)
- **Missing functions:** `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_key`, `serialize_value`, `serialize_entry`, `end`, `serialize_field`, `deserialize_any`, `deserialize_option`, `deserialize_identifier`, `expecting`, `visit_map`, `next_key_seed`, `next_value_seed`, `visit_unit`
- **Types:** 1/12 matched (target 2)
- **Missing types:** `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `Value`, `WithPrefixOption`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `with_prefix.rs` vs expected `with_prefix.rs`
- **Proposed provenance header:** `// port-lint: source with_prefix.rs` (current: `// port-lint: source with_prefix.rs`)
- **Lint issues:** 1

### 6. utils.duration

- **Target:** `serdewith.Duration [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1262610.0
- **Functions:** 0/20 matched (target 16)
- **Missing functions:** `is_positive`, `is_negative`, `apply_f64`, `apply_i64`, `new`, `checked_mul`, `checked_div`, `with_duration`, `to_system_time`, `to_std_duration`, `from`, `serialize_as`, `expecting`, `visit_i64`, `visit_u64`, `visit_f64`, `visit_str`, `deserialize_as`, `parse_float_into_time_parts`, `test_parse_float_into_time_parts`
- **Types:** 0/6 matched (target 8)
- **Missing types:** `Sign`, `DurationSigned`, `DurationVisitorFlexible`, `Value`, `DurationDeserializationVisitor`, `ParseFloatError`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `utils/duration.rs` vs expected `utils/duration.rs`
- **Proposed provenance header:** `// port-lint: source utils/duration.rs` (current: `// port-lint: source utils/duration.rs`)
- **Lint issues:** 1

### 7. serde_as.lib

- **Target:** `serdewith.BoolFromInt [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 676710.0
- **Functions:** 0/29 matched (target 2)
- **Missing functions:** `test_basic_wrappers`, `test_option`, `test_bound`, `test_range`, `test_rangefrom`, `test_rangeto`, `test_rangeinclusive`, `test_result`, `test_display_fromstr`, `test_if_is_human_readable`, `test_tuples`, `test_arrays`, `test_sequence_like_types`, `test_none_as_empty_string`, `test_bytes_or_string`, `string_with_separator`, `test_vec_skip_error`, `test_map_skip_error_btreemap`, `test_map_skip_error_btreemap_flatten`, `test_map_skip_error_hashmap`, `test_map_skip_error_hashmap_flatten`, `test_serialize_reference`, `test_big_arrays`, `test_bytes`, `test_one_or_many_prefer_one`, `test_one_or_many_prefer_many`, `test_borrow_cow_str`, `into_deserializer`, `test_boolfromint`
- **Types:** 0/38 matched (target 3)
- **Missing types:** `SBox`, `SPin`, `SPinBox`, `SRc`, `SPinRc`, `SRcWeak`, `SArc`, `SPinArc`, `SArcWeak`, `SCell`, `SRefCell`, `SMutex`, `SRwLock`, `S`, `Struct`, `S1`, `S2a`, `S2b`, `S2c`, `S6`, `S0`, `S2`, `S32`, `S3`, `S4`, `S5`, `SVec`, `A`, `S1a`, `S1Mut`, `S1aMut`, `Test`, `S1Vec`, `S2Vec`, `BorrowedStr`, `Deserializer`, `SStrict`, `SFlexible`
- **Tests:** 0/28 matched
- **Provenance warning:** port-lint provenance header matched only by basename: `lib.rs` vs expected `tests/serde_as/lib.rs`
- **Proposed provenance header:** `// port-lint: source tests/serde_as/lib.rs` (current: `// port-lint: source lib.rs`)
- **Lint issues:** 1

### 8. lib

- **Target:** `serdewith.Mod [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 454510.0
- **Functions:** 0/1 matched (target 10)
- **Missing functions:** `inspect_error`
- **Types:** 0/44 matched (target 19)
- **Missing types:** `As`, `Same`, `DisplayFromStr`, `IfIsHumanReadable`, `NoneAsEmptyString`, `DefaultOnError`, `DefaultOnNull`, `BytesOrString`, `DurationSeconds`, `DurationSecondsWithFrac`, `DurationMilliSeconds`, `DurationMilliSecondsWithFrac`, `DurationMicroSeconds`, `DurationMicroSecondsWithFrac`, `DurationNanoSeconds`, `DurationNanoSecondsWithFrac`, `TimestampSeconds`, `TimestampSecondsWithFrac`, `TimestampMilliSeconds`, `TimestampMilliSecondsWithFrac`, `TimestampMicroSeconds`, `TimestampMicroSecondsWithFrac`, `TimestampNanoSeconds`, `TimestampNanoSecondsWithFrac`, `Bytes`, `OneOrMany`, `PickFirst`, `FromInto`, `FromIntoRef`, `TryFromInto`, `TryFromIntoRef`, `BorrowCow`, `InspectError`, `VecSkipError`, `MapSkipError`, `BoolFromInt`, `StringWithSeparator`, `Map`, `Seq`, `MapPreventDuplicates`, `MapFirstKeyWins`, `SetPreventDuplicates`, `SetLastValueWins`, `Schema`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Lint issues:** 5

### 9. flatten_maybe

- **Target:** `serdewith.FlattenMaybe [PROVENANCE-FALLBACK]`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 262709.9
- **Functions:** 1/22 matched (target 2)
- **Missing functions:** `expecting`, `visit_bool`, `visit_i8`, `visit_i16`, `visit_i32`, `visit_i64`, `visit_i128`, `visit_u8`, `visit_u16`, `visit_u32`, `visit_u64`, `visit_u128`, `visit_f32`, `visit_f64`, `visit_char`, `visit_unit`, `visit_str`, `visit_bytes`, `visit_borrowed_str`, `visit_borrowed_bytes`, `visit_map`
- **Types:** 0/5 matched (target 1)
- **Missing types:** `FlattenedMaybe`, `Value`, `Field`, `FieldVisitor`, `FlattenedMaybeVisitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `flatten_maybe.rs` vs expected `flatten_maybe.rs`
- **Proposed provenance header:** `// port-lint: source flatten_maybe.rs` (current: `// port-lint: source flatten_maybe.rs`)
- **Lint issues:** 1

### 10. base64

- **Target:** `serdewith.Base64 [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 161610.0
- **Functions:** 0/5 matched (target 4)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_str`, `charset`
- **Types:** 0/11 matched (target 14)
- **Missing types:** `Base64`, `Helper`, `Value`, `Sealed`, `Alphabet`, `Standard`, `UrlSafe`, `Crypt`, `Bcrypt`, `ImapMutf7`, `BinHex`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `base64.rs` vs expected `base64.rs`
- **Proposed provenance header:** `// port-lint: source base64.rs` (current: `// port-lint: source base64.rs`)
- **Lint issues:** 1

### 11. de.skip_error

- **Target:** `serdewith.SkipError [PROVENANCE-FALLBACK]`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 91009.5
- **Functions:** 1/5 matched (target 4)
- **Missing functions:** `deserialize_as`, `expecting`, `visit_seq`, `visit_map`
- **Types:** 0/5 matched (target 2)
- **Missing types:** `GoodOrError`, `SeqVisitor`, `Value`, `MapSkipErrorVisitor`, `KVPair`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `de/skip_error.rs` vs expected `de/skip_error.rs`
- **Proposed provenance header:** `// port-lint: source de/skip_error.rs` (current: `// port-lint: source de/skip_error.rs`)
- **Lint issues:** 1

### 12. tests.with_prefix

- **Target:** `serdewith.PrefixSuffix [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 70710.0
- **Functions:** 0/3 matched (target 4)
- **Missing functions:** `test_flatten_with_prefix`, `test_plain_with_prefix`, `test_enum_unit_variant_with_prefix`
- **Types:** 0/4 matched (target 2)
- **Missing types:** `Match`, `Player`, `Foo`, `Data`
- **Tests:** 0/3 matched
- **Provenance warning:** port-lint provenance header matched only by basename: `with_prefix.rs` vs expected `tests/with_prefix.rs`
- **Proposed provenance header:** `// port-lint: source tests/with_prefix.rs` (current: `// port-lint: source with_prefix.rs`)
- **Lint issues:** 1

### 13. rust

- **Target:** `serdewith.Rust [PROVENANCE-FALLBACK]`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 60909.3
- **Functions:** 2/6 matched (target 5)
- **Missing functions:** `expecting`, `visit_seq`, `visit_map`, `deserialize_ignore_any`
- **Types:** 1/3 matched (target 6)
- **Missing types:** `SeqVisitor`, `MapVisitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rust.rs` vs expected `rust.rs`
- **Proposed provenance header:** `// port-lint: source rust.rs` (current: `// port-lint: source rust.rs`)
- **Lint issues:** 2

### 14. hex

- **Target:** `serdewith.Hex [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/2 matched (target 5)
- **Missing functions:** `serialize_as`, `deserialize_as`
- **Types:** 0/1 matched (target 4)
- **Missing types:** `Hex`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `hex.rs` vs expected `hex.rs`
- **Proposed provenance header:** `// port-lint: source hex.rs` (current: `// port-lint: source hex.rs`)
- **Lint issues:** 1

### 15. formats

- **Target:** `serdewith.Formats [PROVENANCE-FALLBACK]`
- **Similarity:** 0.95
- **Dependents:** 0
- **Priority Score:** 1200.5
- **Functions:** 1/1 matched (target 9)
- **Missing functions:** _none_
- **Types:** 11/11 matched (target 32)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `formats.rs` vs expected `formats.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `formats.rs` vs expected `formats.rs`
- **Proposed provenance header:** `// port-lint: source formats.rs` (current: `// port-lint: source formats.rs`)
- **Proposed provenance header:** `// port-lint: source formats.rs` (current: `// port-lint: source formats.rs`)
- **Lint issues:** 2

### 16. duplicate_key_impls.mod

- **Target:** `serdewith.DuplicateKey [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `duplicate_key_impls/mod.rs` vs expected `duplicate_key_impls/mod.rs`
- **Proposed provenance header:** `// port-lint: source duplicate_key_impls/mod.rs` (current: `// port-lint: source duplicate_key_impls/mod.rs`)
- **Lint issues:** 1

### 17. derives.lib

- **Target:** `serdewith.OneOrMany [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only by basename: `lib.rs` vs expected `tests/derives/lib.rs`
- **Proposed provenance header:** `// port-lint: source tests/derives/lib.rs` (current: `// port-lint: source lib.rs`)
- **Lint issues:** 1

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

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `content.mod` | `content.Mod` | 0 | `src/content/mod.rs` | `content/Mod.kt` |
| `de.mod` | `de.Mod` | 0 | `src/de/mod.rs` | `de/Mod.kt` |
| `ser.mod` | `ser.Mod` | 0 | `src/ser/mod.rs` | `ser/Mod.kt` |

