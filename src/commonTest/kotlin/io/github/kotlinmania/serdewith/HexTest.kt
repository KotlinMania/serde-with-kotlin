package io.github.kotlinmania.serdewith

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class HexTest {
    @Test
    fun testHexLowercase() {
        val input = "Hello World!".encodeToByteArray()
        val encoded = HexCodec.encode(input, uppercase = false)
        assertEquals("48656c6c6f20576f726c6421", encoded)

        val decoded = HexCodec.decode(encoded)
        assertContentEquals(input, decoded)
    }

    @Test
    fun testHexUppercase() {
        val input = "Hello World!".encodeToByteArray()
        val encoded = HexCodec.encode(input, uppercase = true)
        assertEquals("48656C6C6F20576F726C6421", encoded)

        val decoded = HexCodec.decode(encoded)
        assertContentEquals(input, decoded)
    }

    @Test
    fun testHexMixedCaseDecoding() {
        val decoded = HexCodec.decode("00aAbc99FF")
        assertContentEquals(byteArrayOf(0x00, 0xaa.toByte(), 0xbc.toByte(), 0x99.toByte(), 0xff.toByte()), decoded)
    }
}
