// port-lint: source duplicate_key_impls/mod.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.indexmap.IndexMap
import io.github.kotlinmania.indexmap.IndexSet
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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
 * Serializer for [Set] that applies duplicate handling strategies.
 */
class SetWithDuplicateStrategySerializer<T>(
    private val elementSerializer: KSerializer<T>,
    private val strategy: DuplicateStrategy = DuplicateStrategy.ErrorOnDuplicate,
) : KSerializer<Set<T>> {
    private val listSerializer = ListSerializer(elementSerializer)
    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Set<T>) {
        listSerializer.serialize(encoder, value.toList())
    }

    override fun deserialize(decoder: Decoder): Set<T> {
        val list = listSerializer.deserialize(decoder)
        val result = mutableSetOf<T>()
        for (item in list) {
            when (strategy) {
                DuplicateStrategy.ErrorOnDuplicate -> {
                    if (!result.add(item)) {
                        throw SerializationException("Duplicate element found in set: $item")
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
        return result
    }
}

/**
 * Serializer for [Map] that applies duplicate key handling strategies.
 */
class MapWithDuplicateStrategySerializer<K, V>(
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>,
    private val strategy: DuplicateStrategy = DuplicateStrategy.ErrorOnDuplicate,
) : KSerializer<Map<K, V>> {
    private val mapSerializer = MapSerializer(keySerializer, valueSerializer)
    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Map<K, V>) {
        mapSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Map<K, V> {
        val composite = decoder.beginStructure(descriptor)
        val result = mutableMapOf<K, V>()
        while (true) {
            val keyIndex = composite.decodeElementIndex(descriptor)
            if (keyIndex == CompositeDecoder.DECODE_DONE) break
            val key = composite.decodeSerializableElement(descriptor, keyIndex, keySerializer)
            val valIndex = composite.decodeElementIndex(descriptor)
            val value = composite.decodeSerializableElement(descriptor, valIndex, valueSerializer)
            when (strategy) {
                DuplicateStrategy.ErrorOnDuplicate -> {
                    if (result.containsKey(key)) {
                        throw SerializationException("Duplicate key found in map: $key")
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
        composite.endStructure(descriptor)
        return result
    }
}

/**
 * Serializer for [IndexSet] that applies duplicate handling strategies.
 */
class IndexSetWithDuplicateStrategySerializer<T>(
    private val elementSerializer: KSerializer<T>,
    private val strategy: DuplicateStrategy = DuplicateStrategy.ErrorOnDuplicate,
) : KSerializer<IndexSet<T>> {
    private val listSerializer = ListSerializer(elementSerializer)
    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: IndexSet<T>) {
        listSerializer.serialize(encoder, value.toList())
    }

    override fun deserialize(decoder: Decoder): IndexSet<T> {
        val list = listSerializer.deserialize(decoder)
        val result = IndexSet.new<T>()
        for (item in list) {
            when (strategy) {
                DuplicateStrategy.ErrorOnDuplicate -> {
                    if (!result.insert(item)) {
                        throw SerializationException("Duplicate element found in set: $item")
                    }
                }

                DuplicateStrategy.FirstValueWins -> {
                    result.insert(item)
                }

                DuplicateStrategy.LastValueWins -> {
                    result.replace(item)
                }
            }
        }
        return result
    }
}

/**
 * Serializer for [IndexMap] that applies duplicate key handling strategies.
 */
class IndexMapWithDuplicateStrategySerializer<K, V>(
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>,
    private val strategy: DuplicateStrategy = DuplicateStrategy.ErrorOnDuplicate,
) : KSerializer<IndexMap<K, V>> {
    private val mapSerializer = MapSerializer(keySerializer, valueSerializer)
    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: IndexMap<K, V>) {
        mapSerializer.serialize(encoder, value.asEntries().toMap())
    }

    override fun deserialize(decoder: Decoder): IndexMap<K, V> {
        val composite = decoder.beginStructure(descriptor)
        val result = IndexMap.new<K, V>()
        while (true) {
            val keyIndex = composite.decodeElementIndex(descriptor)
            if (keyIndex == CompositeDecoder.DECODE_DONE) break
            val key = composite.decodeSerializableElement(descriptor, keyIndex, keySerializer)
            val valIndex = composite.decodeElementIndex(descriptor)
            val value = composite.decodeSerializableElement(descriptor, valIndex, valueSerializer)
            when (strategy) {
                DuplicateStrategy.ErrorOnDuplicate -> {
                    if (result.containsKey(key)) {
                        throw SerializationException("Duplicate key found in map: $key")
                    }
                    result.insert(key, value)
                }

                DuplicateStrategy.FirstValueWins -> {
                    if (!result.containsKey(key)) {
                        result.insert(key, value)
                    }
                }

                DuplicateStrategy.LastValueWins -> {
                    result.insert(key, value)
                }
            }
        }
        composite.endStructure(descriptor)
        return result
    }
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
