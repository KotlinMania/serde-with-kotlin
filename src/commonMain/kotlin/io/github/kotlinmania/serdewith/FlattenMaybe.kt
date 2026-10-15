// port-lint: source flatten_maybe.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.ser.Serializer
import io.github.kotlinmania.serdejson.JsonDeserializer
import io.github.kotlinmania.serdejson.JsonMap
import io.github.kotlinmania.serdejson.JsonParser
import io.github.kotlinmania.serdejson.Value

/**
 * Helper for deserializing a field that might be present either in flattened form
 * or nested form under [fieldName] using Serde.
 */
class FlattenedMaybe<T>(
    private val fieldName: String,
    private val elementAs: SerializeAs<T>? = null,
    private val elementDe: DeserializeAs<T>? = null,
) : SerializeAs<T>,
    DeserializeAs<T> {
    override fun <Ok> serializeAs(source: T, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val element = elementAs ?: return SerdeResult.failure(SerdeError("Missing element serializer"))
        return element.serializeAs(source, serializer)
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<T> {
        val de = elementDe ?: return SerdeResult.failure(SerdeError("Missing element deserializer"))

        val mapResult = JsonMap.deserialize(deserializer)
        if (mapResult.isFailure) return SerdeResult.failure(mapResult.unwrapErr())
        val map = mapResult.getOrThrow()

        val nested = map.get(fieldName)
        if (nested != null) {
            val jsonStr = nested.toString()
            val parser = JsonParser(jsonStr)
            return de.deserializeAs(JsonDeserializer(parser))
        } else {
            val remaining = JsonMap.new()
            for ((k, v) in map) {
                if (k != fieldName) {
                    remaining.insert(k, v)
                }
            }
            if (remaining.isNotEmpty()) {
                val jsonStr = Value.Object(remaining).toString()
                val parser = JsonParser(jsonStr)
                return de.deserializeAs(JsonDeserializer(parser))
            }
        }
        return SerdeResult.failure(SerdeError("missing field: $fieldName"))
    }
}
