# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 17/72 (23.6%)
- **Function parity:** 35/1424 matched (target 159) — 2.5%
- **Class/type parity:** 16/515 matched (target 138) — 3.1%
- **Combined symbol parity:** 51/1939 matched (target 297) — 2.6%
- **Average inline-code cosine:** 0.20 (function body across 13 matched files)
- **Average documentation cosine:** 0.17 (doc text across 13 matched files)
- **Cheat-zeroed Files:** 4
- **Critical Issues:** 16 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. serde_with.enum_map

- **Target:** `serdewith.EnumMap`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2697010.0
- **Functions:** 1/49 matched (target 4)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_map`, `is_human_readable`, `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_i128`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_u128`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_element`, `end`, `serialize_field`, `deserialize_seq`, `deserialize_any`, `next_element_seed`, `size_hint`, `deserialize_enum`, `variant_seed`, `unit_variant`, `newtype_variant_seed`, `tuple_variant`, `struct_variant`
- **Types:** 0/21 matched (target 3)
- **Missing types:** `EnumMap`, `EnumMapVisitor`, `Value`, `SeqAsMapSerializer`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `SerializeSeqElement`, `EnumAsMapElementSerializer`, `SerializeVariant`, `SeqDeserializer`, `EnumDeserializer`, `Variant`, `SeedTupleVariant`, `SeedStructVariant`

### 2. serde_with.key_value_map

- **Target:** `serdewith.KeyValueMap`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1808210.0
- **Functions:** 1/52 matched (target 4)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_map`, `is_human_readable`, `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_i128`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_u128`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_element`, `end`, `serialize_field`, `serialize_key`, `serialize_value`, `deserialize_seq`, `deserialize_any`, `next_element_seed`, `size_hint`, `deserialize_tuple`, `deserialize_tuple_struct`, `deserialize_map`, `deserialize_struct`, `visit_seq`, `next_key_seed`, `next_value_seed`
- **Types:** 1/30 matched (target 3)
- **Missing types:** `KeyValueMap`, `KeyValueMapVisitor`, `Value`, `SeqAsMapSerializer`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `SerializeSeqElement`, `ElementAsKeyValueSerializer`, `KeyValueSeqSerializer`, `KeyValueTupleSerializer`, `KeyValueTupleStructSerializer`, `KeyValueStructSerializer`, `SeqDeserializer`, `MapKeyDeserializer`, `KeyValueSeqDeserialize`, `KeyValueTupleDeserialize`, `KeyValueTupleStructDeserialize`, `KeyValueMapDeserialize`, `KeyValueStructDeserialize`, `VisitorWrapper`, `MapAccessWrapper`, `SeqAccessWrapper`

### 3. serde_with.with_prefix

- **Target:** `serdewith.PrefixSuffix [PROVENANCE-FALLBACK]`
- **Similarity:** 0.01
- **Dependents:** 1
- **Priority Score:** 1535509.9
- **Functions:** 2/43 matched (target 6)
- **Missing functions:** `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_key`, `serialize_value`, `serialize_entry`, `end`, `serialize_field`, `deserialize_any`, `deserialize_option`, `deserialize_identifier`, `expecting`, `visit_map`, `next_key_seed`, `next_value_seed`, `visit_unit`
- **Types:** 0/12 matched (target 4)
- **Missing types:** `WithPrefix`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `Value`, `WithPrefixOption`
- **Provenance warning:** port-lint provenance header matched only by basename: `serde_with/tests/with_prefix.rs` vs expected `with_prefix.rs`
- **Proposed provenance header:** `// port-lint: source with_prefix.rs` (current: `// port-lint: source serde_with/tests/with_prefix.rs`)
- **Lint issues:** 1

### 4. utils.duration

- **Target:** `serdewith.Duration`
- **Similarity:** 0.04
- **Dependents:** 1
- **Priority Score:** 1222609.6
- **Functions:** 2/20 matched (target 22)
- **Missing functions:** `is_positive`, `is_negative`, `apply_f64`, `apply_i64`, `new`, `checked_mul`, `checked_div`, `with_duration`, `to_system_time`, `to_std_duration`, `from`, `serialize_as`, `expecting`, `visit_i64`, `visit_u64`, `visit_f64`, `visit_str`, `deserialize_as`
- **Types:** 2/6 matched (target 14)
- **Missing types:** `DurationSigned`, `DurationVisitorFlexible`, `Value`, `DurationDeserializationVisitor`
- **Tests:** 1/1 matched

### 5. serde_as.lib

- **Target:** `serdewith.BoolFromInt [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 676710.0
- **Functions:** 0/29 matched (target 2)
- **Missing functions:** `test_basic_wrappers`, `test_option`, `test_bound`, `test_range`, `test_rangefrom`, `test_rangeto`, `test_rangeinclusive`, `test_result`, `test_display_fromstr`, `test_if_is_human_readable`, `test_tuples`, `test_arrays`, `test_sequence_like_types`, `test_none_as_empty_string`, `test_bytes_or_string`, `string_with_separator`, `test_vec_skip_error`, `test_map_skip_error_btreemap`, `test_map_skip_error_btreemap_flatten`, `test_map_skip_error_hashmap`, `test_map_skip_error_hashmap_flatten`, `test_serialize_reference`, `test_big_arrays`, `test_bytes`, `test_one_or_many_prefer_one`, `test_one_or_many_prefer_many`, `test_borrow_cow_str`, `into_deserializer`, `test_boolfromint`
- **Types:** 0/38 matched (target 3)
- **Missing types:** `SBox`, `SPin`, `SPinBox`, `SRc`, `SPinRc`, `SRcWeak`, `SArc`, `SPinArc`, `SArcWeak`, `SCell`, `SRefCell`, `SMutex`, `SRwLock`, `S`, `Struct`, `S1`, `S2a`, `S2b`, `S2c`, `S6`, `S0`, `S2`, `S32`, `S3`, `S4`, `S5`, `SVec`, `A`, `S1a`, `S1Mut`, `S1aMut`, `Test`, `S1Vec`, `S2Vec`, `BorrowedStr`, `Deserializer`, `SStrict`, `SFlexible`
- **Tests:** 0/28 matched
- **Provenance warning:** port-lint provenance header matched only by basename: `serde_with/src/lib.rs` vs expected `serde_with/tests/serde_as/lib.rs`
- **Proposed provenance header:** `// port-lint: source serde_with/tests/serde_as/lib.rs` (current: `// port-lint: source serde_with/src/lib.rs`)
- **Lint issues:** 1

### 6. serde_with.lib

- **Target:** `serdewith.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 454510.0
- **Functions:** 0/1 matched (target 27)
- **Missing functions:** `inspect_error`
- **Types:** 0/44 matched (target 27)
- **Missing types:** `As`, `Same`, `DisplayFromStr`, `IfIsHumanReadable`, `NoneAsEmptyString`, `DefaultOnError`, `DefaultOnNull`, `BytesOrString`, `DurationSeconds`, `DurationSecondsWithFrac`, `DurationMilliSeconds`, `DurationMilliSecondsWithFrac`, `DurationMicroSeconds`, `DurationMicroSecondsWithFrac`, `DurationNanoSeconds`, `DurationNanoSecondsWithFrac`, `TimestampSeconds`, `TimestampSecondsWithFrac`, `TimestampMilliSeconds`, `TimestampMilliSecondsWithFrac`, `TimestampMicroSeconds`, `TimestampMicroSecondsWithFrac`, `TimestampNanoSeconds`, `TimestampNanoSecondsWithFrac`, `Bytes`, `OneOrMany`, `PickFirst`, `FromInto`, `FromIntoRef`, `TryFromInto`, `TryFromIntoRef`, `BorrowCow`, `InspectError`, `VecSkipError`, `MapSkipError`, `BoolFromInt`, `StringWithSeparator`, `Map`, `Seq`, `MapPreventDuplicates`, `MapFirstKeyWins`, `SetPreventDuplicates`, `SetLastValueWins`, `Schema`

### 7. serde_with.flatten_maybe

- **Target:** `serdewith.FlattenMaybe`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 262709.9
- **Functions:** 1/22 matched (target 3)
- **Missing functions:** `expecting`, `visit_bool`, `visit_i8`, `visit_i16`, `visit_i32`, `visit_i64`, `visit_i128`, `visit_u8`, `visit_u16`, `visit_u32`, `visit_u64`, `visit_u128`, `visit_f32`, `visit_f64`, `visit_char`, `visit_unit`, `visit_str`, `visit_bytes`, `visit_borrowed_str`, `visit_borrowed_bytes`, `visit_map`
- **Types:** 0/5 matched (target 3)
- **Missing types:** `FlattenedMaybe`, `Value`, `Field`, `FieldVisitor`, `FlattenedMaybeVisitor`

### 8. tests.base64

- **Target:** `serdewith.Base64`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 91107.3
- **Functions:** 2/2 matched (target 9)
- **Missing functions:** _none_
- **Types:** 0/9 matched (target 16)
- **Missing types:** `BDefault`, `BPadded`, `BUnpadded`, `B64Standard`, `B64UrlSafe`, `B64Crypt`, `B64Bcrypt`, `B64ImapMutf7`, `B64BinHex`
- **Tests:** 2/2 matched

### 9. de.skip_error

- **Target:** `serdewith.SkipError`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 91009.5
- **Functions:** 1/5 matched (target 4)
- **Missing functions:** `deserialize_as`, `expecting`, `visit_seq`, `visit_map`
- **Types:** 0/5 matched (target 2)
- **Missing types:** `GoodOrError`, `SeqVisitor`, `Value`, `MapSkipErrorVisitor`, `KVPair`

### 10. tests.rust

- **Target:** `serdewith.Rust`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 82108.3
- **Functions:** 13/17 matched (target 20)
- **Missing functions:** `eq`, `hash`, `cmp`, `partial_cmp`
- **Types:** 0/4 matched (target 7)
- **Missing types:** `S`, `W`, `UnwrapOrSkipRef`, `UnwrapOrSkip`
- **Tests:** 13/13 matched
- **Lint issues:** 1

### 11. tests.with_suffix

- **Target:** `serdewith.WithSuffix`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 40707.3
- **Functions:** 3/3 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/4 matched (target 3)
- **Missing types:** `Match`, `Player`, `Foo`, `Data`
- **Tests:** 3/3 matched

### 12. tests.with_prefix

- **Target:** `serdewith.WithPrefix`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 40707.3
- **Functions:** 3/3 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/4 matched (target 3)
- **Missing types:** `Match`, `Player`, `Foo`, `Data`
- **Tests:** 3/3 matched

### 13. tests.hex

- **Target:** `serdewith.Hex`
- **Similarity:** 0.23
- **Dependents:** 0
- **Priority Score:** 10407.7
- **Functions:** 3/3 matched (target 11)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 5)
- **Missing types:** `B`
- **Tests:** 3/3 matched

### 14. serde_with.formats

- **Target:** `serdewith.Formats`
- **Similarity:** 0.95
- **Dependents:** 0
- **Priority Score:** 1200.5
- **Functions:** 1/1 matched (target 9)
- **Missing functions:** _none_
- **Types:** 11/11 matched (target 32)
- **Missing types:** _none_

### 15. tests.json

- **Target:** `serdewith.Json`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 406.8
- **Functions:** 2/2 matched (target 5)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 8)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 16. duplicate_key_impls.mod

- **Target:** `serdewith.DuplicateKey [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_

### 17. derives.lib

- **Target:** `serdewith.OneOrMany [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only by basename: `serde_with/src/lib.rs` vs expected `serde_with/tests/derives/lib.rs`
- **Proposed provenance header:** `// port-lint: source serde_with/tests/derives/lib.rs` (current: `// port-lint: source serde_with/src/lib.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

