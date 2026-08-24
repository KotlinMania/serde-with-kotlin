// port-lint: source lib.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializes and deserializes a [Boolean] as an integer (0 or 1).
 */
class BoolFromIntSerializer(
    private val strictness: Strictness = Strict,
) : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.BoolFromInt", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeInt(if (value) 1 else 0)
    }

    override fun deserialize(decoder: Decoder): Boolean {
        val intVal = decoder.decodeInt()
        return when (strictness) {
            is Strict ->
                when (intVal) {
                    0 -> false
                    1 -> true
                    else -> throw SerializationException("Invalid boolean integer value: $intVal (expected 0 or 1 in strict mode)")
                }
            is Flexible -> intVal != 0
            else -> intVal != 0
        }
    }
}

/** Strict boolean from int serializer (0 = false, 1 = true). */
object StrictBoolFromIntSerializer : KSerializer<Boolean> by BoolFromIntSerializer(Strict)

/** Flexible boolean from int serializer (0 = false, non-zero = true). */
object FlexibleBoolFromIntSerializer : KSerializer<Boolean> by BoolFromIntSerializer(Flexible)
