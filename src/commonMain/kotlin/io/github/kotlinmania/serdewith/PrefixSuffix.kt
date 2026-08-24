// port-lint: source with_prefix.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

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
