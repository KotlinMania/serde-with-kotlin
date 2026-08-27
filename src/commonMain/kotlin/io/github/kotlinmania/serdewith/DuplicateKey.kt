// port-lint: source duplicate_key_impls/mod.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
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
    private val mapSerializer = kotlinx.serialization.builtins.MapSerializer(keySerializer, valueSerializer)
    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Map<K, V>) {
        mapSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Map<K, V> {
        val composite = decoder.beginStructure(descriptor)
        val result = mutableMapOf<K, V>()
        while (true) {
            val keyIndex = composite.decodeElementIndex(descriptor)
            if (keyIndex == kotlinx.serialization.encoding.CompositeDecoder.DECODE_DONE) break
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
