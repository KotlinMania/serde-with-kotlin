// port-lint: source duplicate_key_impls/error_on_duplicate.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.indexmap.IndexMap
import io.github.kotlinmania.indexmap.IndexSet
import io.github.kotlinmania.serde.SerdeError
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
 * Strategy for handling duplicate keys or set elements during deserialization.
 */
enum class DuplicateStrategy {
    ErrorOnDuplicate,
    FirstValueWins,
    LastValueWins,
}

/**
 * Contract for collections that detect or prevent duplicate element insertions.
 */
interface PreventDuplicateInsertsSet<T> {
    fun insert(value: T): Boolean
}

/**
 * Contract for sets that update to the latest value on duplicate insertion.
 */
interface DuplicateInsertsLastWinsSet<T> {
    fun replace(value: T)
}

/**
 * Contract for maps that detect or prevent duplicate key insertions.
 */
interface PreventDuplicateInsertsMap<K, V> {
    fun insert(key: K, value: V): Boolean
}

/**
 * Contract for maps that keep the first value on duplicate key insertion.
 */
interface DuplicateInsertsFirstWinsMap<K, V> {
    fun insert(key: K, value: V)
}

/**
 * Adapts [IndexSet] to [PreventDuplicateInsertsSet].
 */
class PreventDuplicateInsertsIndexSet<T>(
    val set: IndexSet<T> = IndexSet.new(),
) : PreventDuplicateInsertsSet<T>,
    Iterable<T> by set {
    override fun insert(value: T): Boolean = set.insert(value)
}

/**
 * Adapts [IndexSet] to [DuplicateInsertsLastWinsSet].
 */
class DuplicateInsertsLastWinsIndexSet<T>(
    val set: IndexSet<T> = IndexSet.new(),
) : DuplicateInsertsLastWinsSet<T>,
    Iterable<T> by set {
    override fun replace(value: T) {
        set.replace(value)
    }
}

/**
 * Adapts [IndexMap] to [PreventDuplicateInsertsMap].
 */
class PreventDuplicateInsertsIndexMap<K, V>(
    val map: IndexMap<K, V> = IndexMap.new(),
) : PreventDuplicateInsertsMap<K, V>,
    Iterable<Pair<K, V>> by map {
    override fun insert(key: K, value: V): Boolean {
        if (map.containsKey(key)) return false
        map.insert(key, value)
        return true
    }
}

/**
 * Adapts [IndexMap] to [DuplicateInsertsFirstWinsMap].
 */
class DuplicateInsertsFirstWinsIndexMap<K, V>(
    val map: IndexMap<K, V> = IndexMap.new(),
) : DuplicateInsertsFirstWinsMap<K, V>,
    Iterable<Pair<K, V>> by map {
    override fun insert(key: K, value: V) {
        if (!map.containsKey(key)) {
            map.insert(key, value)
        }
    }
}

/**
 * Serializer for [Set] that applies duplicate handling strategies using Serde.
 */
class SetWithDuplicateStrategy<T>(
    private val elementAs: SerializeAs<T>? = null,
    private val elementDe: DeserializeAs<T>? = null,
    private val strategy: DuplicateStrategy = DuplicateStrategy.ErrorOnDuplicate,
) : SerializeAs<Set<T>>,
    DeserializeAs<Set<T>> {
    override fun <Ok> serializeAs(source: Set<T>, serializer: Serializer<Ok>): SerdeResult<Ok> {
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

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Set<T>> {
        val de = elementDe ?: return SerdeResult.failure(SerdeError.custom("Missing element deserializer"))
        val seed =
            object : DeserializeSeed<T> {
                override fun <D> deserialize(deserializer: D): SerdeResult<T> where D : Deserializer =
                    de.deserializeAs(deserializer)
            }

        return deserializer.deserializeSeq(
            object : Visitor<Set<T>> {
                override fun expecting(): String = "a sequence for set"

                override fun <A> visitSeq(access: A): SerdeResult<Set<T>> where A : SeqAccess {
                    val result = mutableSetOf<T>()
                    while (true) {
                        val elemResult = access.nextElementSeed(seed)
                        if (elemResult.isFailure) return SerdeResult.failure(elemResult.unwrapErr())
                        val item = elemResult.getOrThrow() ?: break
                        when (strategy) {
                            DuplicateStrategy.ErrorOnDuplicate -> {
                                if (!result.add(item)) {
                                    return SerdeResult.failure(SerdeError.custom("Duplicate element found in set: $item"))
                                }
                            }

                            DuplicateStrategy.FirstValueWins -> {
                                result.add(item)
                            }

                            DuplicateStrategy.LastValueWins -> {
                                result.remove(item)
                                result.add(item)
                            }
                        }
                    }
                    return SerdeResult.success(result)
                }
            },
        )
    }
}

/**
 * Serializer for [Map] that applies duplicate key handling strategies using Serde.
 */
class MapWithDuplicateStrategy<K, V>(
    private val keyDe: DeserializeAs<K>? = null,
    private val valDe: DeserializeAs<V>? = null,
    private val strategy: DuplicateStrategy = DuplicateStrategy.ErrorOnDuplicate,
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
        val kde = keyDe ?: return SerdeResult.failure(SerdeError.custom("Missing key deserializer"))
        val vde = valDe ?: return SerdeResult.failure(SerdeError.custom("Missing value deserializer"))

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

        return deserializer.deserializeMap(
            object : Visitor<Map<K, V>> {
                override fun expecting(): String = "a map"

                override fun <A> visitMap(access: A): SerdeResult<Map<K, V>> where A : MapAccess {
                    val result = mutableMapOf<K, V>()
                    while (true) {
                        val keyResult = access.nextKeySeed(kSeed)
                        if (keyResult.isFailure) return SerdeResult.failure(keyResult.unwrapErr())
                        val key = keyResult.getOrThrow() ?: break
                        val valResult = access.nextValueSeed(vSeed)
                        if (valResult.isFailure) return SerdeResult.failure(valResult.unwrapErr())
                        val value = valResult.getOrThrow()
                        when (strategy) {
                            DuplicateStrategy.ErrorOnDuplicate -> {
                                if (result.containsKey(key)) {
                                    return SerdeResult.failure(SerdeError.custom("Duplicate key found in map: $key"))
                                }
                                result[key] = value
                            }

                            DuplicateStrategy.FirstValueWins -> {
                                if (!result.containsKey(key)) {
                                    result[key] = value
                                }
                            }

                            DuplicateStrategy.LastValueWins -> {
                                result[key] = value
                            }
                        }
                    }
                    return SerdeResult.success(result)
                }
            },
        )
    }
}
