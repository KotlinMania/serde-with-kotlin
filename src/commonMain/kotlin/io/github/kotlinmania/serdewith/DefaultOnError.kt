// port-lint: source lib.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull

/**
 * Returns a default value if deserialization throws an exception.
 */
class DefaultOnErrorSerializer<T>(
    private val elementSerializer: KSerializer<T>,
    private val defaultValue: () -> T,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = elementSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        elementSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): T =
        if (decoder is JsonDecoder) {
            try {
                val elem = decoder.decodeJsonElement()
                decoder.json.decodeFromJsonElement(elementSerializer, elem)
            } catch (_: Exception) {
                defaultValue()
            }
        } else {
            try {
                elementSerializer.deserialize(decoder)
            } catch (_: Exception) {
                defaultValue()
            }
        }
}

/**
 * Returns a default value if the serialized value is null or missing.
 */
class DefaultOnNullSerializer<T>(
    private val elementSerializer: KSerializer<T>,
    private val defaultValue: () -> T,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = elementSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        elementSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): T {
        if (decoder is JsonDecoder) {
            val elem = decoder.decodeJsonElement()
            if (elem is JsonNull) {
                return defaultValue()
            }
            return decoder.json.decodeFromJsonElement(elementSerializer, elem)
        }
        return try {
            elementSerializer.deserialize(decoder)
        } catch (_: Exception) {
            defaultValue()
        }
    }
}
