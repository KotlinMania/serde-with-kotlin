// port-lint: source with_suffix.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

/**
 * Transforms JSON object field names by adding a suffix on serialization and stripping it on deserialization.
 */
class WithSuffixSerializer<T>(
    private val suffix: String,
    private val delegate: KSerializer<T>,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        if (encoder is JsonEncoder) {
            val jsonTree = encoder.json.encodeToJsonElement(delegate, value)
            if (jsonTree is JsonObject) {
                val suffixedMap = mutableMapOf<String, JsonElement>()
                for ((k, v) in jsonTree) {
                    suffixedMap["$k$suffix"] = v
                }
                encoder.encodeJsonElement(JsonObject(suffixedMap))
                return
            }
        }
        delegate.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): T {
        if (decoder is JsonDecoder) {
            val jsonTree = decoder.decodeJsonElement()
            if (jsonTree is JsonObject) {
                val strippedMap = mutableMapOf<String, JsonElement>()
                for ((k, v) in jsonTree) {
                    if (k.endsWith(suffix)) {
                        strippedMap[k.removeSuffix(suffix)] = v
                    }
                }
                return decoder.json.decodeFromJsonElement(delegate, JsonObject(strippedMap))
            }
        }
        return delegate.deserialize(decoder)
    }
}

/**
 * Adapter that adds a suffix onto field/map names during serialization
 * and trims away the suffix during deserialization.
 */
class WithSuffixMapSerializer<V>(
    val suffix: String,
    valueSerializer: KSerializer<V>,
) : KSerializer<Map<String, V>> {
    private val delegate = MapSerializer(String.serializer(), valueSerializer)
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: Map<String, V>) {
        val suffixedMap = value.mapKeys { (k, _) -> "$k$suffix" }
        delegate.serialize(encoder, suffixedMap)
    }

    override fun deserialize(decoder: Decoder): Map<String, V> {
        val map = delegate.deserialize(decoder)
        val result = mutableMapOf<String, V>()
        for ((k, v) in map) {
            if (k.endsWith(suffix)) {
                result[k.removeSuffix(suffix)] = v
            }
        }
        return result
    }
}

/**
 * Utility functions for manipulating suffixed key-value pairs and JSON objects.
 */
object WithSuffix {
    /**
     * Adds [suffix] to all keys in [map].
     */
    fun <V> addSuffix(map: Map<String, V>, suffix: String): Map<String, V> = map.mapKeys { (k, _) -> "$k$suffix" }

    /**
     * Filters and strips [suffix] from matching keys in [map].
     */
    fun <V> stripSuffix(map: Map<String, V>, suffix: String): Map<String, V> {
        val result = mutableMapOf<String, V>()
        for ((k, v) in map) {
            if (k.endsWith(suffix)) {
                result[k.removeSuffix(suffix)] = v
            }
        }
        return result
    }

    /**
     * Adds [suffix] to all keys in [jsonObject].
     */
    fun addSuffix(jsonObject: JsonObject, suffix: String): JsonObject =
        buildJsonObject {
            for ((k, v) in jsonObject) {
                put("$k$suffix", v)
            }
        }

    /**
     * Filters and strips [suffix] from matching keys in [jsonObject].
     */
    fun stripSuffix(jsonObject: JsonObject, suffix: String): JsonObject =
        buildJsonObject {
            for ((k, v) in jsonObject) {
                if (k.endsWith(suffix)) {
                    put(k.removeSuffix(suffix), v)
                }
            }
        }
}
