// port-lint: source with_prefix.rs
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
 * Transforms JSON object field names by adding a prefix on serialization and stripping it on deserialization.
 */
class WithPrefixSerializer<T>(
    private val prefix: String,
    private val delegate: KSerializer<T>,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        if (encoder is JsonEncoder) {
            val jsonTree = encoder.json.encodeToJsonElement(delegate, value)
            if (jsonTree is JsonObject) {
                val prefixedMap = mutableMapOf<String, JsonElement>()
                for ((k, v) in jsonTree) {
                    prefixedMap["$prefix$k"] = v
                }
                encoder.encodeJsonElement(JsonObject(prefixedMap))
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
                    if (k.startsWith(prefix)) {
                        strippedMap[k.removePrefix(prefix)] = v
                    }
                }
                return decoder.json.decodeFromJsonElement(delegate, JsonObject(strippedMap))
            }
        }
        return delegate.deserialize(decoder)
    }
}

/**
 * Adapter that adds a prefix onto field/map names during serialization
 * and trims away the prefix during deserialization.
 */
class WithPrefixMapSerializer<V>(
    val prefix: String,
    valueSerializer: KSerializer<V>,
) : KSerializer<Map<String, V>> {
    private val delegate = MapSerializer(String.serializer(), valueSerializer)
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: Map<String, V>) {
        val prefixedMap = value.mapKeys { (k, _) -> "$prefix$k" }
        delegate.serialize(encoder, prefixedMap)
    }

    override fun deserialize(decoder: Decoder): Map<String, V> {
        val map = delegate.deserialize(decoder)
        val result = mutableMapOf<String, V>()
        for ((k, v) in map) {
            if (k.startsWith(prefix)) {
                result[k.removePrefix(prefix)] = v
            }
        }
        return result
    }
}

/**
 * Utility functions for manipulating prefixed key-value pairs and JSON objects.
 */
object WithPrefix {
    /**
     * Adds [prefix] to all keys in [map].
     */
    fun <V> addPrefix(map: Map<String, V>, prefix: String): Map<String, V> = map.mapKeys { (k, _) -> "$prefix$k" }

    /**
     * Filters and strips [prefix] from matching keys in [map].
     */
    fun <V> stripPrefix(map: Map<String, V>, prefix: String): Map<String, V> {
        val result = mutableMapOf<String, V>()
        for ((k, v) in map) {
            if (k.startsWith(prefix)) {
                result[k.removePrefix(prefix)] = v
            }
        }
        return result
    }

    /**
     * Adds [prefix] to all keys in [jsonObject].
     */
    fun addPrefix(jsonObject: JsonObject, prefix: String): JsonObject =
        buildJsonObject {
            for ((k, v) in jsonObject) {
                put("$prefix$k", v)
            }
        }

    /**
     * Filters and strips [prefix] from matching keys in [jsonObject].
     */
    fun stripPrefix(jsonObject: JsonObject, prefix: String): JsonObject =
        buildJsonObject {
            for ((k, v) in jsonObject) {
                if (k.startsWith(prefix)) {
                    put(k.removePrefix(prefix), v)
                }
            }
        }
}
