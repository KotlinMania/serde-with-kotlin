// port-lint: source json.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

/**
 * Serializes and deserializes values as embedded JSON strings.
 */
class JsonStringSerializer<T>(
    private val elementSerializer: KSerializer<T>,
    private val json: Json = Json.Default,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.JsonString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        val jsonString = json.encodeToString(elementSerializer, value)
        encoder.encodeString(jsonString)
    }

    override fun deserialize(decoder: Decoder): T {
        val jsonString = decoder.decodeString()
        return json.decodeFromString(elementSerializer, jsonString)
    }
}
