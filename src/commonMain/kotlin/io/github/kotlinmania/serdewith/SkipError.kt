// port-lint: source de/skip_error.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.DeserializeSeed
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.MapAccess
import io.github.kotlinmania.serdecore.de.SeqAccess
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.SerializeMap
import io.github.kotlinmania.serdecore.ser.SerializeSeq
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Serializes and deserializes a list, silently ignoring elements that fail deserialization.
 */
class ListSkipError<T>(
    private val elementAs: SerializeAs<T>? = null,
    private val elementDe: DeserializeAs<T>? = null,
) : SerializeAs<List<T>>,
    DeserializeAs<List<T>> {
    override fun <Ok> serializeAs(source: List<T>, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val seqResult = serializer.serializeSeq(source.size)
        if (seqResult.isFailure) return SerdeResult.failure(seqResult.unwrapErr())
        val seq: SerializeSeq<Ok> = seqResult.getOrThrow()
        for (item in source) {
            val ser =
                if (elementAs != null) {
                    SerializeAsWrap(item, elementAs)
                } else if (item is Serialize) {
                    item
                } else {
                    SerializePrimitive(item)
                }
            val res = seq.serializeElement(ser)
            if (res.isFailure) return SerdeResult.failure(res.unwrapErr())
        }
        return seq.end()
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<List<T>> {
        val de = elementDe ?: return SerdeResult.success(emptyList())
        val seed =
            object : DeserializeSeed<SkipResult<T>> {
                override fun <D> deserialize(deserializer: D): SerdeResult<SkipResult<T>> where D : Deserializer {
                    val res = de.deserializeAs(deserializer)
                    return if (res.isSuccess) {
                        SerdeResult.success(SkipResult.Value(res.getOrThrow()))
                    } else {
                        SerdeResult.success(SkipResult.Skip)
                    }
                }
            }

        return deserializer.deserializeSeq(
            object : Visitor<List<T>> {
                override fun expecting(): String = "a sequence"

                override fun <A> visitSeq(access: A): SerdeResult<List<T>> where A : SeqAccess {
                    val list = mutableListOf<T>()
                    while (true) {
                        val elemResult = access.nextElementSeed(seed)
                        if (elemResult.isFailure) break
                        val elem = elemResult.getOrThrow() ?: break
                        when (elem) {
                            is SkipResult.Value -> {
                                list.add(elem.v)
                            }

                            is SkipResult.Skip -> {}
                        }
                    }
                    return SerdeResult.success(list)
                }
            },
        )
    }
}

/**
 * Serializes and deserializes a map, silently ignoring entries that fail deserialization.
 */
class MapSkipError<K, V>(
    private val keyDe: DeserializeAs<K>? = null,
    private val valDe: DeserializeAs<V>? = null,
) : SerializeAs<Map<K, V>>,
    DeserializeAs<Map<K, V>> {
    override fun <Ok> serializeAs(source: Map<K, V>, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val mapResult = serializer.serializeMap(source.size)
        if (mapResult.isFailure) return SerdeResult.failure(mapResult.unwrapErr())
        val map: SerializeMap<Ok> = mapResult.getOrThrow()
        for ((k, v) in source) {
            val kSer = if (k is Serialize) k else SerializePrimitive(k)
            val kRes = map.serializeKey(kSer)
            if (kRes.isFailure) return SerdeResult.failure(kRes.unwrapErr())
            val vSer = if (v is Serialize) v else SerializePrimitive(v)
            val vRes = map.serializeValue(vSer)
            if (vRes.isFailure) return SerdeResult.failure(vRes.unwrapErr())
        }
        return map.end()
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Map<K, V>> {
        val kde = keyDe ?: return SerdeResult.success(emptyMap())
        val vde = valDe ?: return SerdeResult.success(emptyMap())

        val kSeed =
            object : DeserializeSeed<SkipResult<K>> {
                override fun <D> deserialize(deserializer: D): SerdeResult<SkipResult<K>> where D : Deserializer {
                    val res = kde.deserializeAs(deserializer)
                    return if (res.isSuccess) {
                        SerdeResult.success(SkipResult.Value(res.getOrThrow()))
                    } else {
                        SerdeResult.success(SkipResult.Skip)
                    }
                }
            }
        val vSeed =
            object : DeserializeSeed<SkipResult<V>> {
                override fun <D> deserialize(deserializer: D): SerdeResult<SkipResult<V>> where D : Deserializer {
                    val res = vde.deserializeAs(deserializer)
                    return if (res.isSuccess) {
                        SerdeResult.success(SkipResult.Value(res.getOrThrow()))
                    } else {
                        SerdeResult.success(SkipResult.Skip)
                    }
                }
            }

        return deserializer.deserializeMap(
            object : Visitor<Map<K, V>> {
                override fun expecting(): String = "a map"

                override fun <A> visitMap(access: A): SerdeResult<Map<K, V>> where A : MapAccess {
                    val result = mutableMapOf<K, V>()
                    while (true) {
                        val keyRes = access.nextKeySeed(kSeed)
                        if (keyRes.isFailure) break
                        val keyItem = keyRes.getOrThrow() ?: break
                        val valRes = access.nextValueSeed(vSeed)
                        if (valRes.isFailure) break
                        val valItem = valRes.getOrThrow()
                        if (keyItem is SkipResult.Value && valItem is SkipResult.Value) {
                            result[keyItem.v] = valItem.v
                        }
                    }
                    return SerdeResult.success(result)
                }
            },
        )
    }
}

private sealed class SkipResult<out T> {
    data class Value<T>(
        val v: T,
    ) : SkipResult<T>()

    object Skip : SkipResult<Nothing>()
}
