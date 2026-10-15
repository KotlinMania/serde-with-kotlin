package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Returns a default value if deserialization fails.
 */
class DefaultOnError<T>(
    private val elementAs: SerializeAs<T>,
    private val elementDe: DeserializeAs<T>,
    private val defaultValue: () -> T,
) : SerializeAs<T>,
    DeserializeAs<T> {
    override fun <Ok> serializeAs(source: T, serializer: Serializer<Ok>): SerdeResult<Ok> =
        elementAs.serializeAs(source, serializer)

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<T> {
        val result = elementDe.deserializeAs(deserializer)
        return if (result.isSuccess) {
            result
        } else {
            SerdeResult.success(defaultValue())
        }
    }
}

/**
 * Returns a default value if the deserialized value is null or missing.
 */
class DefaultOnNull<T>(
    private val elementAs: SerializeAs<T?>,
    private val elementDe: DeserializeAs<T?>,
    private val defaultValue: () -> T,
) : SerializeAs<T?>,
    DeserializeAs<T> {
    override fun <Ok> serializeAs(source: T?, serializer: Serializer<Ok>): SerdeResult<Ok> =
        elementAs.serializeAs(source, serializer)

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<T> {
        val result = elementDe.deserializeAs(deserializer)
        return if (result.isSuccess) {
            val v = result.getOrThrow()
            if (v != null) {
                SerdeResult.success(v)
            } else {
                SerdeResult.success(defaultValue())
            }
        } else {
            SerdeResult.success(defaultValue())
        }
    }
}
