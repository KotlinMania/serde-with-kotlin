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
