// port-lint: source base64.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Base64 alphabet definition.
 */
interface Base64Alphabet {
    val chars: String
}

/** Standard Base64 alphabet. */
object StandardAlphabet : Base64Alphabet {
    override val chars: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
}

/** URL-safe Base64 alphabet. */
object UrlSafeAlphabet : Base64Alphabet {
    override val chars: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
}

/** Bcrypt Base64 alphabet. */
object BcryptAlphabet : Base64Alphabet {
    override val chars: String = "./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
}

/** BinHex Base64 alphabet. */
object BinHexAlphabet : Base64Alphabet {
    override val chars: String = "!\"#$%&'()*+,-012345689@ABCDEFGHIJKLMNPQRSTUVXYZ[`abcdefhijklmpqr"
}

/** Crypt Base64 alphabet. */
object CryptAlphabet : Base64Alphabet {
    override val chars: String = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
}

/** IMAP modified UTF-7 Base64 alphabet. */
object ImapMutf7Alphabet : Base64Alphabet {
    override val chars: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+,"
}

/**
 * Base64 encoding and decoding utilities.
 */
object Base64Codec {
    fun encode(
        data: ByteArray,
        alphabet: Base64Alphabet = StandardAlphabet,
        padded: Boolean = true,
    ): String {
        val table = alphabet.chars
        val sb = StringBuilder((data.size * 4 + 2) / 3)
        var i = 0
        while (i + 2 < data.size) {
            val b0 = data[i].toInt() and 0xFF
            val b1 = data[i + 1].toInt() and 0xFF
            val b2 = data[i + 2].toInt() and 0xFF
            sb.append(table[b0 ushr 2])
            sb.append(table[((b0 and 0x03) shl 4) or (b1 ushr 4)])
            sb.append(table[((b1 and 0x0F) shl 2) or (b2 ushr 6)])
            sb.append(table[b2 and 0x3F])
            i += 3
        }
        if (i < data.size) {
            val b0 = data[i].toInt() and 0xFF
            sb.append(table[b0 ushr 2])
            if (i + 1 < data.size) {
                val b1 = data[i + 1].toInt() and 0xFF
                sb.append(table[((b0 and 0x03) shl 4) or (b1 ushr 4)])
                sb.append(table[(b1 and 0x0F) shl 2])
                if (padded) sb.append('=')
            } else {
                sb.append(table[(b0 and 0x03) shl 4])
                if (padded) {
                    sb.append('=')
                    sb.append('=')
                }
            }
        }
        return sb.toString()
    }

    fun decode(
        text: String,
        alphabet: Base64Alphabet = StandardAlphabet,
    ): ByteArray {
        val table = alphabet.chars
        val decodeTable = IntArray(256) { -1 }
        for (idx in table.indices) {
            decodeTable[table[idx].code] = idx
        }

        val filtered = StringBuilder(text.length)
        for (ch in text) {
            if (ch == '=') break
            if (ch.isWhitespace()) continue
            if (ch.code < 256 && decodeTable[ch.code] != -1) {
                filtered.append(ch)
            } else {
                throw IllegalArgumentException("Invalid Base64 character: $ch")
            }
        }

        val len = filtered.length
        if (len % 4 == 1) {
            throw IllegalArgumentException("Invalid Base64 length: $len")
        }
        val remainder = len % 4
        if (remainder == 2) {
            val c1 = decodeTable[filtered[len - 1].code]
            if ((c1 and 0x0F) != 0) {
                throw IllegalArgumentException("Invalid last symbol")
            }
        } else if (remainder == 3) {
            val c2 = decodeTable[filtered[len - 1].code]
            if ((c2 and 0x03) != 0) {
                throw IllegalArgumentException("Invalid last symbol")
            }
        }

        val out = mutableListOf<Byte>()
        var i = 0
        while (i < len) {
            val c0 = decodeTable[filtered[i].code]
            val c1 = if (i + 1 < len) decodeTable[filtered[i + 1].code] else 0
            val c2 = if (i + 2 < len) decodeTable[filtered[i + 2].code] else 0
            val c3 = if (i + 3 < len) decodeTable[filtered[i + 3].code] else 0

            val b0 = ((c0 shl 2) or (c1 ushr 4)) and 0xFF
            out.add(b0.toByte())

            if (i + 2 < len) {
                val b1 = (((c1 and 0x0F) shl 4) or (c2 ushr 2)) and 0xFF
                out.add(b1.toByte())
            }
            if (i + 3 < len) {
                val b2 = (((c2 and 0x03) shl 6) or c3) and 0xFF
                out.add(b2.toByte())
            }
            i += 4
        }
        return out.toByteArray()
    }
}

/**
 * Base64 serializer for [ByteArray].
 */
open class Base64Serializer(
    private val alphabet: Base64Alphabet = StandardAlphabet,
    private val padded: Boolean = true,
) : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.Base64", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(Base64Codec.encode(value, alphabet, padded))
    }

    override fun deserialize(decoder: Decoder): ByteArray = Base64Codec.decode(decoder.decodeString(), alphabet)
}

/** Standard Base64 serializer with padding. */
object Base64StandardPaddedSerializer : Base64Serializer(StandardAlphabet, true)

/** Standard Base64 serializer without padding. */
object Base64StandardUnpaddedSerializer : Base64Serializer(StandardAlphabet, false)

/** URL-safe Base64 serializer with padding. */
object Base64UrlSafePaddedSerializer : Base64Serializer(UrlSafeAlphabet, true)

/** URL-safe Base64 serializer without padding. */
object Base64UrlSafeUnpaddedSerializer : Base64Serializer(UrlSafeAlphabet, false)

/** Bcrypt Base64 serializer. */
object Base64BcryptSerializer : Base64Serializer(BcryptAlphabet, false)

/** BinHex Base64 serializer. */
object Base64BinHexSerializer : Base64Serializer(BinHexAlphabet, true)
