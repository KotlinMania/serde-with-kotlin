// port-lint: source enum_map.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializes a list of values as a map keyed by a string tag.
 */
class TaggedListAsMapSerializer<T>(
    private val valueSerializer: KSerializer<T>,
    private val tagExtractor: (T) -> String,
    private val valueFactory: (String, T) -> T = { _, v -> v },
) : KSerializer<List<T>> {
    private val mapSerializer = MapSerializer(String.serializer(), valueSerializer)

    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: List<T>) {
        val map = value.associateBy { tagExtractor(it) }
        mapSerializer.serialize(encoder, map)
    }

    override fun deserialize(decoder: Decoder): List<T> {
        val map = mapSerializer.deserialize(decoder)
        return map.map { (k, v) -> valueFactory(k, v) }
    }
}
