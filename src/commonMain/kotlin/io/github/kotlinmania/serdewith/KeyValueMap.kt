// port-lint: source key_value_map.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.DeserializeSeed
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.MapAccess
import io.github.kotlinmania.serdecore.de.SeqAccess
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.SerializeSeq
import io.github.kotlinmania.serdecore.ser.SerializeStruct
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Key-value entry representation for collections.
 */
data class KeyValueEntry<K, V>(
    val key: K,
    val value: V,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val structResult = serializer.serializeStruct("KeyValueEntry", 2)
        if (structResult.isFailure) return SerdeResult.failure(structResult.unwrapErr())
        val struct: SerializeStruct<Ok> = structResult.getOrThrow()
        val kSer = if (key is Serialize) key else SerializePrimitive(key)
        val kRes = struct.serializeField("key", kSer)
        if (kRes.isFailure) return SerdeResult.failure(kRes.unwrapErr())
        val vSer = if (value is Serialize) value else SerializePrimitive(value)
        val vRes = struct.serializeField("value", vSer)
        if (vRes.isFailure) return SerdeResult.failure(vRes.unwrapErr())
        return struct.end()
    }
}

/**
 * Helper to serialize primitive values as Serialize.
 */
class SerializePrimitive(
    private val value: Any?,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
        when (value) {
            is String -> serializer.serializeStr(value)
            is Boolean -> serializer.serializeBool(value)
            is Int -> serializer.serializeI32(value)
            is Long -> serializer.serializeI64(value)
            is Double -> serializer.serializeF64(value)
            is Float -> serializer.serializeF32(value)
            is Short -> serializer.serializeI16(value)
            is Byte -> serializer.serializeI8(value)
            is UInt -> serializer.serializeU32(value)
            is ULong -> serializer.serializeU64(value)
            is UShort -> serializer.serializeU16(value)
            is UByte -> serializer.serializeU8(value)
            is Char -> serializer.serializeChar(value)
            null -> serializer.serializeNone()
            is Serialize -> value.serialize(serializer)
            else -> serializer.serializeStr(value.toString())
        }
}

private object FieldStringSeed : DeserializeSeed<String> {
    override fun <D> deserialize(deserializer: D): SerdeResult<String> where D : Deserializer =
        deserializer.deserializeStr(
            object : Visitor<String> {
                override fun expecting(): String = "a field name"

                override fun visitStr(v: String): SerdeResult<String> = SerdeResult.success(v)
            },
        )
}

/**
 * Serializes a [Map] as a list of [KeyValueEntry] objects and vice versa using Serde.
 */
class KeyValueMap<K, V>(
    private val keyDe: DeserializeAs<K>? = null,
    private val valDe: DeserializeAs<V>? = null,
) : SerializeAs<Map<K, V>>,
    DeserializeAs<Map<K, V>> {
    override fun <Ok> serializeAs(source: Map<K, V>, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val seqResult = serializer.serializeSeq(source.size)
        if (seqResult.isFailure) return SerdeResult.failure(seqResult.unwrapErr())
        val seq: SerializeSeq<Ok> = seqResult.getOrThrow()
        for ((k, v) in source) {
            val entry = KeyValueEntry(k, v)
            val elemResult = seq.serializeElement(entry)
            if (elemResult.isFailure) return SerdeResult.failure(elemResult.unwrapErr())
        }
        return seq.end()
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Map<K, V>> =
        deserializer.deserializeSeq(
            object : Visitor<Map<K, V>> {
                override fun expecting(): String = "a sequence of key-value entries"

                override fun <A> visitSeq(access: A): SerdeResult<Map<K, V>> where A : SeqAccess {
                    val result = mutableMapOf<K, V>()
                    val kde = keyDe ?: return SerdeResult.failure(SerdeError("Missing key deserializer"))
                    val vde = valDe ?: return SerdeResult.failure(SerdeError("Missing value deserializer"))

                    val kSeed =
                        object : DeserializeSeed<K> {
                            override fun <D> deserialize(deserializer: D): SerdeResult<K> where D : Deserializer =
                                kde.deserializeAs(deserializer)
                        }

                    val vSeed =
                        object : DeserializeSeed<V> {
                            override fun <D> deserialize(deserializer: D): SerdeResult<V> where D : Deserializer =
                                vde.deserializeAs(deserializer)
                        }

                    val entrySeed = EntrySeed(kSeed, vSeed)

                    while (true) {
                        val elemResult = access.nextElementSeed(entrySeed)
                        if (elemResult.isFailure) return SerdeResult.failure(elemResult.unwrapErr())
                        val pair = elemResult.getOrThrow() ?: break
                        result[pair.first] = pair.second
                    }
                    return SerdeResult.success(result)
                }
            },
        )
}

private class EntrySeed<K, V>(
    private val kSeed: DeserializeSeed<K>,
    private val vSeed: DeserializeSeed<V>,
) : DeserializeSeed<Pair<K, V>> {
    override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<Pair<K, V>> =
        deserializer.deserializeMap(
            object : Visitor<Pair<K, V>> {
                override fun expecting(): String = "a map with key and value fields"

                override fun <M : MapAccess> visitMap(access: M): SerdeResult<Pair<K, V>> {
                    var k: K? = null
                    var v: V? = null
                    while (true) {
                        val keyStrResult = access.nextKeySeed(FieldStringSeed)
                        if (keyStrResult.isFailure) return SerdeResult.failure(keyStrResult.unwrapErr())
                        val fieldName = keyStrResult.getOrThrow() ?: break
                        when (fieldName) {
                            "key" -> {
                                val valResult = access.nextValueSeed(kSeed)
                                if (valResult.isFailure) return SerdeResult.failure(valResult.unwrapErr())
                                k = valResult.getOrThrow()
                            }

                            "value" -> {
                                val valResult = access.nextValueSeed(vSeed)
                                if (valResult.isFailure) return SerdeResult.failure(valResult.unwrapErr())
                                v = valResult.getOrThrow()
                            }

                            else -> {
                                access.nextValueSeed(IgnoreAnySeed)
                            }
                        }
                    }
                    if (k == null || v == null) {
                        return SerdeResult.failure(SerdeError("missing field: key or value"))
                    }
                    return SerdeResult.success(k to v)
                }
            },
        )
}
