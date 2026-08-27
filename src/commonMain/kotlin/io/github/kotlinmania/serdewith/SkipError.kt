// port-lint: source de/skip_error.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject

/**
 * Serializes and deserializes a list, silently ignoring elements that fail deserialization.
 */
class ListSkipErrorSerializer<T>(
    private val elementSerializer: KSerializer<T>,
) : KSerializer<List<T>> {
    private val defaultListSerializer = ListSerializer(elementSerializer)
    override val descriptor: SerialDescriptor = defaultListSerializer.descriptor

    override fun serialize(encoder: Encoder, value: List<T>) {
        defaultListSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): List<T> {
        if (decoder is JsonDecoder) {
            val jsonElement = decoder.decodeJsonElement()
            if (jsonElement is JsonArray) {
                val result = mutableListOf<T>()
                val json = decoder.json
                for (elem in jsonElement) {
                    try {
                        result.add(json.decodeFromJsonElement(elementSerializer, elem))
                    } catch (_: Exception) {
                        // Skip corrupted/invalid element
                    }
                }
                return result
            }
        }
        return defaultListSerializer.deserialize(decoder)
    }
}

/**
 * Serializes and deserializes a map, silently ignoring key-value pairs that fail deserialization.
 */
class MapSkipErrorSerializer<K, V>(
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>,
) : KSerializer<Map<K, V>> {
    private val defaultMapSerializer = MapSerializer(keySerializer, valueSerializer)
    override val descriptor: SerialDescriptor = defaultMapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Map<K, V>) {
        defaultMapSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Map<K, V> {
        if (decoder is JsonDecoder) {
            val jsonElement = decoder.decodeJsonElement()
            if (jsonElement is JsonObject) {
                val result = mutableMapOf<K, V>()
                val json = decoder.json
                for ((kStr, vElem) in jsonElement) {
                    try {
                        val key = json.decodeFromString(keySerializer, "\"$kStr\"")
                        val value = json.decodeFromJsonElement(valueSerializer, vElem)
                        result[key] = value
                    } catch (_: Exception) {
                        // Skip corrupted/invalid entry
                    }
                }
                return result
            }
        }
        return defaultMapSerializer.deserialize(decoder)
    }
}
