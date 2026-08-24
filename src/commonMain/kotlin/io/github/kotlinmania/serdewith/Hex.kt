// port-lint: source hex.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Hex encoding and decoding utilities.
 */
object HexCodec {
    private val HEX_DIGITS_LOWER = "0123456789abcdef".toCharArray()
    private val HEX_DIGITS_UPPER = "0123456789ABCDEF".toCharArray()

    fun encode(data: ByteArray, uppercase: Boolean = false): String {
        val digits = if (uppercase) HEX_DIGITS_UPPER else HEX_DIGITS_LOWER
        val result = CharArray(data.size * 2)
        for (i in data.indices) {
            val v = data[i].toInt() and 0xFF
            result[i * 2] = digits[v ushr 4]
            result[i * 2 + 1] = digits[v and 0x0F]
        }
        return result.concatToString()
    }

    fun decode(hex: String): ByteArray {
        val clean = hex.filter { !it.isWhitespace() }
        require(clean.length % 2 == 0) { "Hex string has odd length: ${clean.length}" }
        val result = ByteArray(clean.length / 2)
        for (i in result.indices) {
            val h = digitValue(clean[i * 2])
            val l = digitValue(clean[i * 2 + 1])
            result[i] = ((h shl 4) or l).toByte()
        }
        return result
    }

    private fun digitValue(c: Char): Int =
        when (c) {
            in '0'..'9' -> c.code - '0'.code
            in 'a'..'f' -> c.code - 'a'.code + 10
            in 'A'..'F' -> c.code - 'A'.code + 10
            else -> throw IllegalArgumentException("Invalid hex character: $c")
        }
}

/**
 * Hex serializer for [ByteArray].
 */
open class HexSerializer(
    private val uppercase: Boolean = false,
) : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.Hex", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(HexCodec.encode(value, uppercase))
    }

    override fun deserialize(decoder: Decoder): ByteArray = HexCodec.decode(decoder.decodeString())
}

/** Lowercase Hex serializer for [ByteArray]. */
object HexLowercaseSerializer : HexSerializer(uppercase = false)

/** Uppercase Hex serializer for [ByteArray]. */
object HexUppercaseSerializer : HexSerializer(uppercase = true)
