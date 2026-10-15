// port-lint: source serde_conv.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serde.serdeCatching
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Creates custom conversion adapters from functions using Serde.
 *
 * Maps a target type [T] to and from an intermediate serializable type [Intermediate].
 */
open class SerdeConv<T, Intermediate>(
    private val toIntermediate: (T) -> Intermediate,
    private val fromIntermediate: (Intermediate) -> T,
    private val intermediateAs: SerializeAs<Intermediate>? = null,
    private val intermediateDe: DeserializeAs<Intermediate>? = null,
) : SerializeAs<T>,
    DeserializeAs<T> {
    override fun <Ok> serializeAs(source: T, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val intermediate = toIntermediate(source)
        return if (intermediateAs != null) {
            intermediateAs.serializeAs(intermediate, serializer)
        } else if (intermediate is Serialize) {
            intermediate.serialize(serializer)
        } else {
            SerializePrimitive(intermediate).serialize(serializer)
        }
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<T> {
        val de =
            intermediateDe
                ?: return SerdeResult.failure(SerdeError("Missing intermediate deserializer"))
        val intermediateResult = de.deserializeAs(deserializer)
        if (intermediateResult.isFailure) return SerdeResult.failure(intermediateResult.unwrapErr())
        return serdeCatching {
            fromIntermediate(intermediateResult.getOrThrow())
        }
    }
}

/**
 * Factory function creating a [SerdeConv] adapter.
 */
fun <T, Intermediate> serdeConv(
    toIntermediate: (T) -> Intermediate,
    fromIntermediate: (Intermediate) -> T,
    intermediateAs: SerializeAs<Intermediate>? = null,
    intermediateDe: DeserializeAs<Intermediate>? = null,
): SerdeConv<T, Intermediate> = SerdeConv(toIntermediate, fromIntermediate, intermediateAs, intermediateDe)
