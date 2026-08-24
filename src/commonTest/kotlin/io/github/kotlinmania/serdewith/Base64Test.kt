package io.github.kotlinmania.serdewith

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class Base64Test {
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
