// port-lint: tests hex.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HexTest {
    @Test
    fun hexVec() {
        val input = listOf(byteArrayOf(0, 1, 2, 13), byteArrayOf(14, 5, 6, 7))
        val encoded = input.map { HexCodec.encode(it, uppercase = false) }
        assertEquals(listOf("0001020d", "0e050607"), encoded)

        val checkDeser = listOf("aaBCff", "E07d")
        val decoded = checkDeser.map { HexCodec.decode(it) }
        assertContentEquals(byteArrayOf(0xaa.toByte(), 0xbc.toByte(), 0xff.toByte()), decoded[0])
        assertContentEquals(byteArrayOf(0xe0.toByte(), 0x7d.toByte()), decoded[1])

        assertFailsWith<Exception> {
            HexCodec.decode("0")
        }
        assertFailsWith<Exception> {
            HexCodec.decode("zz")
        }
    }

    @Test
    fun hexVecLowercase() {
        val input = listOf(byteArrayOf(0, 1, 2, 13), byteArrayOf(14, 5, 6, 7))
        val encoded = input.map { HexCodec.encode(it, uppercase = false) }
        assertEquals(listOf("0001020d", "0e050607"), encoded)

        val checkDeser = listOf("aaBCff", "E07d")
        val decoded = checkDeser.map { HexCodec.decode(it) }
        assertContentEquals(byteArrayOf(0xaa.toByte(), 0xbc.toByte(), 0xff.toByte()), decoded[0])
        assertContentEquals(byteArrayOf(0xe0.toByte(), 0x7d.toByte()), decoded[1])
    }

    @Test
    fun hexVecUppercase() {
        val input = listOf(byteArrayOf(0, 1, 2, 13), byteArrayOf(14, 5, 6, 7))
        val encoded = input.map { HexCodec.encode(it, uppercase = true) }
        assertEquals(listOf("0001020D", "0E050607"), encoded)

        val checkDeser = listOf("aaBCff", "E07d")
        val decoded = checkDeser.map { HexCodec.decode(it) }
        assertContentEquals(byteArrayOf(0xaa.toByte(), 0xbc.toByte(), 0xff.toByte()), decoded[0])
        assertContentEquals(byteArrayOf(0xe0.toByte(), 0x7d.toByte()), decoded[1])
    }

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
