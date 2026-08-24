# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 14/35 (40.0%)
- **Function parity:** 11/901 matched (target 83) — 1.2%
- **Class/type parity:** 15/249 matched (target 102) — 6.0%
- **Combined symbol parity:** 26/1150 matched (target 185) — 2.3%
- **Average inline-code cosine:** 0.09 (function body across 13 matched files)
- **Average documentation cosine:** 0.27 (doc text across 13 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 13 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. key_value_map

- **Target:** `serdewith.KeyValueMap`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1808210.0
- **Functions:** 1/52 matched (target 2)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_map`, `is_human_readable`, `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_i128`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_u128`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_element`, `end`, `serialize_field`, `serialize_key`, `serialize_value`, `deserialize_seq`, `deserialize_any`, `next_element_seed`, `size_hint`, `deserialize_tuple`, `deserialize_tuple_struct`, `deserialize_map`, `deserialize_struct`, `visit_seq`, `next_key_seed`, `next_value_seed`
- **Types:** 1/30 matched (target 2)
- **Missing types:** `KeyValueMap`, `KeyValueMapVisitor`, `Value`, `SeqAsMapSerializer`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `SerializeSeqElement`, `ElementAsKeyValueSerializer`, `KeyValueSeqSerializer`, `KeyValueTupleSerializer`, `KeyValueTupleStructSerializer`, `KeyValueStructSerializer`, `SeqDeserializer`, `MapKeyDeserializer`, `KeyValueSeqDeserialize`, `KeyValueTupleDeserialize`, `KeyValueTupleStructDeserialize`, `KeyValueMapDeserialize`, `KeyValueStructDeserialize`, `VisitorWrapper`, `MapAccessWrapper`, `SeqAccessWrapper`

### 2. enum_map

- **Target:** `serdewith.EnumMap`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1697010.0
- **Functions:** 1/49 matched (target 2)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_map`, `is_human_readable`, `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_i128`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_u128`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_element`, `end`, `serialize_field`, `deserialize_seq`, `deserialize_any`, `next_element_seed`, `size_hint`, `deserialize_enum`, `variant_seed`, `unit_variant`, `newtype_variant_seed`, `tuple_variant`, `struct_variant`
- **Types:** 0/21 matched (target 1)
- **Missing types:** `EnumMap`, `EnumMapVisitor`, `Value`, `SeqAsMapSerializer`, `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `SerializeSeqElement`, `EnumAsMapElementSerializer`, `SerializeVariant`, `SeqDeserializer`, `EnumDeserializer`, `Variant`, `SeedTupleVariant`, `SeedStructVariant`

### 3. with_prefix

- **Target:** `serdewith.WithPrefix`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 525509.8
- **Functions:** 2/43 matched (target 10)
- **Missing functions:** `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_key`, `serialize_value`, `serialize_entry`, `end`, `serialize_field`, `deserialize_any`, `deserialize_option`, `deserialize_identifier`, `expecting`, `visit_map`, `next_key_seed`, `next_value_seed`, `visit_unit`
- **Types:** 1/12 matched (target 4)
- **Missing types:** `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `Value`, `WithPrefixOption`

### 4. with_suffix

- **Target:** `serdewith.WithSuffix`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 525509.8
- **Functions:** 2/43 matched (target 6)
- **Missing functions:** `serialize_bool`, `serialize_i8`, `serialize_i16`, `serialize_i32`, `serialize_i64`, `serialize_u8`, `serialize_u16`, `serialize_u32`, `serialize_u64`, `serialize_f32`, `serialize_f64`, `serialize_char`, `serialize_str`, `serialize_bytes`, `serialize_none`, `serialize_some`, `serialize_unit`, `serialize_unit_struct`, `serialize_unit_variant`, `serialize_newtype_struct`, `serialize_newtype_variant`, `serialize_seq`, `serialize_tuple`, `serialize_tuple_struct`, `serialize_tuple_variant`, `serialize_map`, `serialize_struct`, `serialize_struct_variant`, `serialize_key`, `serialize_value`, `serialize_entry`, `end`, `serialize_field`, `deserialize_any`, `deserialize_option`, `deserialize_identifier`, `expecting`, `visit_map`, `next_key_seed`, `next_value_seed`, `visit_unit`
- **Types:** 1/12 matched (target 2)
- **Missing types:** `Ok`, `Error`, `SerializeSeq`, `SerializeTuple`, `SerializeTupleStruct`, `SerializeTupleVariant`, `SerializeMap`, `SerializeStruct`, `SerializeStructVariant`, `Value`, `WithSuffixOption`

### 5. lib

- **Target:** `serdewith.Mod`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 454510.0
- **Functions:** 0/1 matched (target 14)
- **Missing functions:** `inspect_error`
- **Types:** 0/44 matched (target 23)
- **Missing types:** `As`, `Same`, `DisplayFromStr`, `IfIsHumanReadable`, `NoneAsEmptyString`, `DefaultOnError`, `DefaultOnNull`, `BytesOrString`, `DurationSeconds`, `DurationSecondsWithFrac`, `DurationMilliSeconds`, `DurationMilliSecondsWithFrac`, `DurationMicroSeconds`, `DurationMicroSecondsWithFrac`, `DurationNanoSeconds`, `DurationNanoSecondsWithFrac`, `TimestampSeconds`, `TimestampSecondsWithFrac`, `TimestampMilliSeconds`, `TimestampMilliSecondsWithFrac`, `TimestampMicroSeconds`, `TimestampMicroSecondsWithFrac`, `TimestampNanoSeconds`, `TimestampNanoSecondsWithFrac`, `Bytes`, `OneOrMany`, `PickFirst`, `FromInto`, `FromIntoRef`, `TryFromInto`, `TryFromIntoRef`, `BorrowCow`, `InspectError`, `VecSkipError`, `MapSkipError`, `BoolFromInt`, `StringWithSeparator`, `Map`, `Seq`, `MapPreventDuplicates`, `MapFirstKeyWins`, `SetPreventDuplicates`, `SetLastValueWins`, `Schema`

### 6. flatten_maybe

- **Target:** `serdewith.FlattenMaybe`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 262709.9
- **Functions:** 1/22 matched (target 2)
- **Missing functions:** `expecting`, `visit_bool`, `visit_i8`, `visit_i16`, `visit_i32`, `visit_i64`, `visit_i128`, `visit_u8`, `visit_u16`, `visit_u32`, `visit_u64`, `visit_u128`, `visit_f32`, `visit_f64`, `visit_char`, `visit_unit`, `visit_str`, `visit_bytes`, `visit_borrowed_str`, `visit_borrowed_bytes`, `visit_map`
- **Types:** 0/5 matched (target 1)
- **Missing types:** `FlattenedMaybe`, `Value`, `Field`, `FieldVisitor`, `FlattenedMaybeVisitor`

### 7. utils.duration

- **Target:** `serdewith.Duration`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 262610.0
- **Functions:** 0/20 matched (target 16)
- **Missing functions:** `is_positive`, `is_negative`, `apply_f64`, `apply_i64`, `new`, `checked_mul`, `checked_div`, `with_duration`, `to_system_time`, `to_std_duration`, `from`, `serialize_as`, `expecting`, `visit_i64`, `visit_u64`, `visit_f64`, `visit_str`, `deserialize_as`, `parse_float_into_time_parts`, `test_parse_float_into_time_parts`
- **Types:** 0/6 matched (target 8)
- **Missing types:** `Sign`, `DurationSigned`, `DurationVisitorFlexible`, `Value`, `DurationDeserializationVisitor`, `ParseFloatError`
- **Tests:** 0/1 matched

### 8. base64

- **Target:** `serdewith.Base64`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 161610.0
- **Functions:** 0/5 matched (target 4)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_str`, `charset`
- **Types:** 0/11 matched (target 14)
- **Missing types:** `Base64`, `Helper`, `Value`, `Sealed`, `Alphabet`, `Standard`, `UrlSafe`, `Crypt`, `Bcrypt`, `ImapMutf7`, `BinHex`

### 9. de.skip_error

- **Target:** `serdewith.SkipError`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 91009.5
- **Functions:** 1/5 matched (target 4)
- **Missing functions:** `deserialize_as`, `expecting`, `visit_seq`, `visit_map`
- **Types:** 0/5 matched (target 2)
- **Missing types:** `GoodOrError`, `SeqVisitor`, `Value`, `MapSkipErrorVisitor`, `KVPair`

### 10. json

- **Target:** `serdewith.Json`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 70710.0
- **Functions:** 0/4 matched (target 2)
- **Missing functions:** `serialize_as`, `deserialize_as`, `expecting`, `visit_str`
- **Types:** 0/3 matched (target 1)
- **Missing types:** `JsonString`, `Helper`, `Value`

### 11. rust

- **Target:** `serdewith.Rust`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 60909.3
- **Functions:** 2/6 matched (target 5)
- **Missing functions:** `expecting`, `visit_seq`, `visit_map`, `deserialize_ignore_any`
- **Types:** 1/3 matched (target 6)
- **Missing types:** `SeqVisitor`, `MapVisitor`
- **Lint issues:** 1

### 12. hex

- **Target:** `serdewith.Hex`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/2 matched (target 5)
- **Missing functions:** `serialize_as`, `deserialize_as`
- **Types:** 0/1 matched (target 4)
- **Missing types:** `Hex`

### 13. formats

- **Target:** `serdewith.Formats`
- **Similarity:** 0.95
- **Dependents:** 0
- **Priority Score:** 1200.5
- **Functions:** 1/1 matched (target 9)
- **Missing functions:** _none_
- **Types:** 11/11 matched (target 32)
- **Missing types:** _none_

### 14. duplicate_key_impls.mod

- **Target:** `serdewith.DuplicateKey [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

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
| `content.mod` | `content.Mod` | 0 | `content/mod.rs` | `content/Mod.kt` |
| `de.mod` | `de.Mod` | 0 | `de/mod.rs` | `de/Mod.kt` |
| `ser.mod` | `ser.Mod` | 0 | `ser/mod.rs` | `ser/Mod.kt` |

