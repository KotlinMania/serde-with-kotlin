// port-lint: source lib.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializes and deserializes values via their string representations.
 */
open class DisplayFromStrSerializer<T>(
    serialName: String,
    private val toStr: (T) -> String,
    private val fromStr: (String) -> T,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(toStr(value))
    }

    override fun deserialize(decoder: Decoder): T = fromStr(decoder.decodeString())
}

/**
 * Serializes an [Int] as a string.
 */
object IntAsStringSerializer : DisplayFromStrSerializer<Int>(
    "io.github.kotlinmania.serdewith.IntAsString",
    { it.toString() },
    { it.toInt() },
)

/**
 * Serializes a [Long] as a string.
 */
object LongAsStringSerializer : DisplayFromStrSerializer<Long>(
    "io.github.kotlinmania.serdewith.LongAsString",
    { it.toString() },
    { it.toLong() },
)

/**
 * Serializes a [Short] as a string.
 */
object ShortAsStringSerializer : DisplayFromStrSerializer<Short>(
    "io.github.kotlinmania.serdewith.ShortAsString",
    { it.toString() },
    { it.toShort() },
)

/**
 * Serializes a [Byte] as a string.
 */
object ByteAsStringSerializer : DisplayFromStrSerializer<Byte>(
    "io.github.kotlinmania.serdewith.ByteAsString",
    { it.toString() },
    { it.toByte() },
)

/**
 * Serializes a [Double] as a string.
 */
object DoubleAsStringSerializer : DisplayFromStrSerializer<Double>(
    "io.github.kotlinmania.serdewith.DoubleAsString",
    { it.toString() },
    { it.toDouble() },
)

/**
 * Serializes a [Float] as a string.
 */
object FloatAsStringSerializer : DisplayFromStrSerializer<Float>(
    "io.github.kotlinmania.serdewith.FloatAsString",
    { it.toString() },
    { it.toFloat() },
)

/**
 * Serializes a [Boolean] as a string.
 */
object BooleanAsStringSerializer : DisplayFromStrSerializer<Boolean>(
    "io.github.kotlinmania.serdewith.BooleanAsString",
    { it.toString() },
    { it.toBooleanStrict() },
)

/**
 * Serializes a [UInt] as a string.
 */
object UIntAsStringSerializer : DisplayFromStrSerializer<UInt>(
    "io.github.kotlinmania.serdewith.UIntAsString",
    { it.toString() },
    { it.toUInt() },
)

/**
 * Serializes a [ULong] as a string.
 */
object ULongAsStringSerializer : DisplayFromStrSerializer<ULong>(
    "io.github.kotlinmania.serdewith.ULongAsString",
    { it.toString() },
    { it.toULong() },
)

/**
 * Serializes a [UShort] as a string.
 */
object UShortAsStringSerializer : DisplayFromStrSerializer<UShort>(
    "io.github.kotlinmania.serdewith.UShortAsString",
    { it.toString() },
    { it.toUShort() },
)

/**
 * Serializes a [UByte] as a string.
 */
object UByteAsStringSerializer : DisplayFromStrSerializer<UByte>(
    "io.github.kotlinmania.serdewith.UByteAsString",
    { it.toString() },
    { it.toUByte() },
)
