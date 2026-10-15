// port-lint: source json.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserialize
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer
import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr

/**
 * Serializes and deserializes values as embedded JSON strings using Serde.
 */
class JsonString<T>(
    private val elementAs: SerializeAs<T>? = null,
    private val deserialize: Deserialize<T>? = null,
) : SerializeAs<T>,
    DeserializeAs<T> {
    override fun <Ok> serializeAs(source: T, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val jsonStrResult =
            if (elementAs != null) {
                val wrap = SerializeAsWrap(source, elementAs)
                toStr(wrap)
            } else if (source is Serialize) {
                toStr(source)
            } else {
                return SerdeResult.failure(SerdeError.custom("Source does not implement Serialize"))
            }

        if (jsonStrResult.isFailure) {
            return SerdeResult.failure(jsonStrResult.unwrapErr())
        }
        return serializer.serializeStr(jsonStrResult.getOrThrow())
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<T> =
        deserializer.deserializeStr(
            object : Visitor<T> {
                override fun expecting(): String = "a string containing json data"

                override fun visitStr(v: String): SerdeResult<T> {
                    val de =
                        deserialize
                            ?: return SerdeResult.failure(SerdeError.custom("Missing Deserialize implementation"))
                    return fromStr(v, de)
                }
            },
        )
}
