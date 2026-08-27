// port-lint: tests serde_with/tests/base64.rs
package io.github.kotlinmania.serdewith

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Base64Test {
    @Test
    fun base64Vec() {
        val checkEqual = listOf(
            byteArrayOf(0, 1, 2, 13),
            byteArrayOf(14, 5, 6, 7),
        )
        val encodedPadded = checkEqual.map { Base64Codec.encode(it, StandardAlphabet, padded = true) }
        assertEquals(listOf("AAECDQ==", "DgUGBw=="), encodedPadded)

        val encodedUnpadded = checkEqual.map { Base64Codec.encode(it, StandardAlphabet, padded = false) }
        assertEquals(listOf("AAECDQ", "DgUGBw"), encodedUnpadded)

        val checkDeserFrom = listOf("qrz/", "4H0=", "4H0")
        val decoded = checkDeserFrom.map { Base64Codec.decode(it, StandardAlphabet) }
        assertContentEquals(byteArrayOf(0xaa.toByte(), 0xbc.toByte(), 0xff.toByte()), decoded[0])
        assertContentEquals(byteArrayOf(0xe0.toByte(), 0x7d.toByte()), decoded[1])
        assertContentEquals(byteArrayOf(0xe0.toByte(), 0x7d.toByte()), decoded[2])

        assertFailsWith<Exception> {
            Base64Codec.decode("0", StandardAlphabet)
        }
        assertFailsWith<Exception> {
            Base64Codec.decode("zz", StandardAlphabet)
        }
    }

    @Test
    fun base64DifferentCharsets() {
        val bytes = byteArrayOf(
            0x69.toByte(), 0xb7.toByte(), 0x1d.toByte(), 0x79.toByte(), 0xf8.toByte(), 0x21.toByte(), 0x8a.toByte(), 0x39.toByte(), 0x25.toByte(), 0x9a.toByte(), 0x7a.toByte(), 0x29.toByte(), 0xaa.toByte(), 0xbb.toByte(),
            0x2d.toByte(), 0xba.toByte(), 0xfc.toByte(), 0x31.toByte(), 0xcb.toByte(), 0x30.toByte(), 0x01.toByte(), 0x08.toByte(), 0x31.toByte(), 0x05.toByte(), 0x18.toByte(), 0x72.toByte(), 0x09.toByte(), 0x28.toByte(), 0xb3.toByte(),
            0x0d.toByte(), 0x38.toByte(), 0xf4.toByte(), 0x11.toByte(), 0x49.toByte(), 0x35.toByte(), 0x15.toByte(), 0x59.toByte(), 0x76.toByte(), 0x19.toByte(), 0xd3.toByte(), 0x5d.toByte(), 0xb7.toByte(), 0xe3.toByte(), 0x9e.toByte(),
            0xbb.toByte(), 0xf3.toByte(), 0xdf.toByte(), 0xbf.toByte(), 0x00.toByte(),
        )

        val standard = Base64Codec.encode(bytes, StandardAlphabet, padded = true)
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/AA==", standard)

        val urlSafe = Base64Codec.encode(bytes, UrlSafeAlphabet, padded = true)
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_AA==", urlSafe)

        val crypt = Base64Codec.encode(bytes, CryptAlphabet, padded = true)
        assertEquals("OPQRSTUVWXYZabcdefghijklmn./0123456789ABCDEFGHIJKLMNopqrstuvwxyz..==", crypt)

        val bcrypt = Base64Codec.encode(bytes, BcryptAlphabet, padded = true)
        assertEquals("YZabcdefghijklmnopqrstuvwx./ABCDEFGHIJKLMNOPQRSTUVWXyz0123456789..==", bcrypt)

        val imap = Base64Codec.encode(bytes, ImapMutf7Alphabet, padded = true)
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+,AA==", imap)

        val binHex = Base64Codec.encode(bytes, BinHexAlphabet, padded = true)
        assertEquals("DEFGHIJKLMNPQRSTUVXYZ[`abc!\"#$%&'()*+,-012345689@ABCdefhijklmpqr!!==", binHex)
    }

    @Test
    fun testBase64Standard() {
        val input = "Hello World".encodeToByteArray()
        val encoded = Base64Codec.encode(input, StandardAlphabet, padded = true)
        assertEquals("SGVsbG8gV29ybGQ=", encoded)

        val decoded = Base64Codec.decode(encoded, StandardAlphabet)
        assertContentEquals(input, decoded)
    }

    @Test
    fun testBase64Unpadded() {
        val input = "Hello World".encodeToByteArray()
        val encoded = Base64Codec.encode(input, StandardAlphabet, padded = false)
        assertEquals("SGVsbG8gV29ybGQ", encoded)

        val decoded = Base64Codec.decode(encoded, StandardAlphabet)
        assertContentEquals(input, decoded)
    }

    @Test
    fun testBase64UrlSafe() {
        val input = byteArrayOf(251.toByte(), 240.toByte(), 239.toByte())
        val encoded = Base64Codec.encode(input, UrlSafeAlphabet, padded = false)
        val decoded = Base64Codec.decode(encoded, UrlSafeAlphabet)
        assertContentEquals(input, decoded)
    }
}
