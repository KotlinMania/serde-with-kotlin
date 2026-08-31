// port-lint: source rust.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull

/**
 * Tri-state optional representation distinguishing between:
 * - [Missing]: Field was completely absent from payload
 * - [Null]: Field was explicitly set to null
 * - [Value]: Field contained a valid value
 */
sealed class DoubleOption<out T> {
    object Missing : DoubleOption<Nothing>()

    object Null : DoubleOption<Nothing>()

    data class Value<out T>(
        val value: T,
    ) : DoubleOption<T>()

    val isMissing: Boolean get() = this is Missing
    val isNull: Boolean get() = this is Null
    val isValue: Boolean get() = this is Value

    fun valueOrNull(): T? =
        when (this) {
            is Value -> value
            else -> null
        }
}

/**
 * Serializer for [DoubleOption].
 */
@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
class DoubleOptionSerializer<T>(
    private val elementSerializer: KSerializer<T>,
) : KSerializer<DoubleOption<T>> {
    override val descriptor: SerialDescriptor = elementSerializer.descriptor

    override fun serialize(encoder: Encoder, value: DoubleOption<T>) {
        when (value) {
            is DoubleOption.Missing -> {
                // Typically omitted or encoded as null depending on format
            }
            is DoubleOption.Null -> {
                encoder.encodeNull()
            }
            is DoubleOption.Value -> {
                elementSerializer.serialize(encoder, value.value)
            }
        }
    }

    override fun deserialize(decoder: Decoder): DoubleOption<T> {
        if (decoder is JsonDecoder) {
            val elem = decoder.decodeJsonElement()
            return if (elem is JsonNull) {
                DoubleOption.Null
            } else {
                val value = decoder.json.decodeFromJsonElement(elementSerializer, elem)
                DoubleOption.Value(value)
            }
        }
        return if (!decoder.decodeNotNullMark()) {
            decoder.decodeNull()
            DoubleOption.Null
        } else {
            DoubleOption.Value(elementSerializer.deserialize(decoder))
        }
    }
}

/**
 * Serializer that deserializes any value, ignores it, and returns the given default value.
 */
@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
class DeserializeIgnoreAnySerializer<T>(
    private val defaultValue: () -> T,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor =
        DisplayFromStrSerializer("io.github.kotlinmania.serdewith.IgnoreAny", { "" }, { "" }).descriptor

    override fun serialize(encoder: Encoder, value: T) {
        // No-op or encode empty string
        value.hashCode()
        encoder.encodeString("")
    }

    override fun deserialize(decoder: Decoder): T {
        if (decoder is JsonDecoder) {
            decoder.decodeJsonElement() // consume and ignore
        } else {
            try {
                decoder.decodeString()
            } catch (_: Exception) {
                // Ignore decoding error
            }
        }
        return defaultValue()
    }
}
