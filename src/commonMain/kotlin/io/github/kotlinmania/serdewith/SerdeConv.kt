// port-lint: source serde_conv.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Creates custom conversion adapters from functions.
 *
 * Maps a target type [T] to and from an intermediate serializable type [Intermediate].
 *
 * @param intermediateSerializer The serializer for the intermediate representation.
 * @param toIntermediate Function converting [T] to [Intermediate].
 * @param fromIntermediate Function converting [Intermediate] to [T].
 */
open class SerdeConvSerializer<T, Intermediate>(
    private val intermediateSerializer: KSerializer<Intermediate>,
    private val toIntermediate: (T) -> Intermediate,
    private val fromIntermediate: (Intermediate) -> T,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = intermediateSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        val intermediate = toIntermediate(value)
        intermediateSerializer.serialize(encoder, intermediate)
    }

    override fun deserialize(decoder: Decoder): T {
        val intermediate = intermediateSerializer.deserialize(decoder)
        return try {
            fromIntermediate(intermediate)
        } catch (e: SerializationException) {
            throw e
        } catch (e: Exception) {
            throw SerializationException("Conversion failed: ${e.message}", e)
        }
    }
}

/**
 * Factory function creating a [SerdeConvSerializer] adapter.
 */
fun <T, Intermediate> serdeConv(
    intermediateSerializer: KSerializer<Intermediate>,
    toIntermediate: (T) -> Intermediate,
    fromIntermediate: (Intermediate) -> T,
): KSerializer<T> = SerdeConvSerializer(intermediateSerializer, toIntermediate, fromIntermediate)
