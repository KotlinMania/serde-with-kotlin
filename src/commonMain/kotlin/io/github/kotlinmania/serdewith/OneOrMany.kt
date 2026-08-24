// port-lint: source lib.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

/**
 * Deserializes either a single value or a list into a [List], and serializes according to [Format].
 */
class OneOrManySerializer<T>(
    private val elementSerializer: KSerializer<T>,
    private val format: Format = PreferOne,
) : KSerializer<List<T>> {
    private val listSerializer = ListSerializer(elementSerializer)
    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: List<T>) {
        if (format is PreferOne && value.size == 1 && encoder is JsonEncoder) {
            encoder.encodeSerializableValue(elementSerializer, value[0])
        } else {
            listSerializer.serialize(encoder, value)
        }
    }

    override fun deserialize(decoder: Decoder): List<T> {
        if (decoder is JsonDecoder) {
            val jsonElement = decoder.decodeJsonElement()
            val json = decoder.json
            return if (jsonElement is JsonArray) {
                jsonElement.map { json.decodeFromJsonElement(elementSerializer, it) }
            } else {
                listOf(json.decodeFromJsonElement(elementSerializer, jsonElement))
            }
        }
        return listSerializer.deserialize(decoder)
    }
}
