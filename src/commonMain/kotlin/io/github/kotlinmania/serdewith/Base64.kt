// port-lint: source base64.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import io.github.kotlinmania.base64.alphabet.Alphabet as Base64Charset

/**
 * A base64 alphabet definition backed by base64-kotlin's [Base64Charset].
 */
sealed interface Base64Alphabet {
    val charset: Base64Charset

    /** Standard Base64 alphabet. */
    object Standard : Base64Alphabet {
        override val charset: Base64Charset = Base64Charset.STANDARD
    }

    /** URL-safe Base64 alphabet. */
    object UrlSafe : Base64Alphabet {
        override val charset: Base64Charset = Base64Charset.URL_SAFE
    }

    /** Bcrypt Base64 alphabet. */
    object Bcrypt : Base64Alphabet {
        override val charset: Base64Charset = Base64Charset.BCRYPT
    }

    /** BinHex Base64 alphabet. */
    object BinHex : Base64Alphabet {
        override val charset: Base64Charset = Base64Charset.BIN_HEX
    }

    /** Crypt Base64 alphabet. */
    object Crypt : Base64Alphabet {
        override val charset: Base64Charset = Base64Charset.CRYPT
    }

    /** IMAP modified UTF-7 Base64 alphabet. */
    object ImapMutf7 : Base64Alphabet {
        override val charset: Base64Charset = Base64Charset.IMAP_MUTF7
    }
}

/** An alias for [Base64Alphabet] matching upstream Rust `Alphabet` trait. */
typealias Alphabet = Base64Alphabet

/** Standard Base64 alphabet. */
typealias StandardAlphabet = Base64Alphabet.Standard

/** URL-safe Base64 alphabet. */
typealias UrlSafeAlphabet = Base64Alphabet.UrlSafe

/** Bcrypt Base64 alphabet. */
typealias BcryptAlphabet = Base64Alphabet.Bcrypt

/** BinHex Base64 alphabet. */
typealias BinHexAlphabet = Base64Alphabet.BinHex

/** Crypt Base64 alphabet. */
typealias CryptAlphabet = Base64Alphabet.Crypt

/** IMAP modified UTF-7 Base64 alphabet. */
typealias ImapMutf7Alphabet = Base64Alphabet.ImapMutf7

/**
 * Base64 encoding and decoding utilities backed by base64-kotlin alphabets.
 */
object Base64Codec {
    fun encode(
        data: ByteArray,
        alphabet: Base64Alphabet = Base64Alphabet.Standard,
        padded: Boolean = true,
    ): String {
        val table = alphabet.charset.asString()
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
        alphabet: Base64Alphabet = Base64Alphabet.Standard,
    ): ByteArray {
        val table = alphabet.charset.asString()
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
    private val alphabet: Base64Alphabet = Base64Alphabet.Standard,
    private val padded: Boolean = true,
) : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.Base64", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(Base64Codec.encode(value, alphabet, padded))
    }

    override fun deserialize(decoder: Decoder): ByteArray =
        Base64Codec.decode(decoder.decodeString(), alphabet)
}

/** Standard Base64 serializer with padding. */
object Base64StandardPaddedSerializer : Base64Serializer(Base64Alphabet.Standard, true)

/** Standard Base64 serializer without padding. */
object Base64StandardUnpaddedSerializer : Base64Serializer(Base64Alphabet.Standard, false)

/** URL-safe Base64 serializer with padding. */
object Base64UrlSafePaddedSerializer : Base64Serializer(Base64Alphabet.UrlSafe, true)

/** URL-safe Base64 serializer without padding. */
object Base64UrlSafeUnpaddedSerializer : Base64Serializer(Base64Alphabet.UrlSafe, false)

/** Bcrypt Base64 serializer. */
object Base64BcryptSerializer : Base64Serializer(Base64Alphabet.Bcrypt, false)

/** BinHex Base64 serializer. */
object Base64BinHexSerializer : Base64Serializer(Base64Alphabet.BinHex, true)

/** Crypt Base64 serializer. */
object Base64CryptSerializer : Base64Serializer(Base64Alphabet.Crypt, false)

/** IMAP modified UTF-7 Base64 serializer. */
object Base64ImapMutf7Serializer : Base64Serializer(Base64Alphabet.ImapMutf7, false)
