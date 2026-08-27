// port-lint: source serde_with/src/flatten_maybe.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

/**
 * Helper for deserializing a field that might be present either in flattened form
 * (top-level properties in the parent JSON object) or nested form (under [fieldName]).
 */
class FlattenedMaybeSerializer<T>(
    private val fieldName: String,
    private val serializer: KSerializer<T>,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        serializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): T {
        if (decoder is JsonDecoder) {
            val json = decoder.json
            val root = decoder.decodeJsonElement()
            if (root is JsonObject) {
                val hasNested = root.containsKey(fieldName)
                val nestedElement = root[fieldName]

                // Also check if any flattened fields can be parsed
                val remainingFields =
                    buildJsonObject {
                        for ((k, v) in root) {
                            if (k != fieldName) {
                                put(k, v)
                            }
                        }
                    }

                if (hasNested && nestedElement != null) {
                    // Nested representation
                    return json.decodeFromJsonElement(serializer, nestedElement)
                } else if (remainingFields.isNotEmpty()) {
                    // Try flattened representation
                    return json.decodeFromJsonElement(serializer, remainingFields)
                } else {
                    throw SerializationException("Missing field `$fieldName` in either flattened or nested form")
                }
            }
        }
        return serializer.deserialize(decoder)
    }
}
