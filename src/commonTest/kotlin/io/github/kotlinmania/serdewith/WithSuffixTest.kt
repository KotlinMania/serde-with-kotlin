package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class WithSuffixTest {
    @Test
    fun testAddAndStripSuffixMap() {
        val original = mapOf("frames" to "4", "sheet" to "normal")
        val suffixed = WithSuffix.addSuffix(original, "_frozen")
        assertEquals(mapOf("frames_frozen" to "4", "sheet_frozen" to "normal"), suffixed)

        val stripped = WithSuffix.stripSuffix(suffixed, "_frozen")
        assertEquals(original, stripped)
    }

    @Test
    fun testAddAndStripSuffixJsonObject() {
        val original =
            buildJsonObject {
                put("frames", 4)
                put("spritesheet", "normal")
            }
        val suffixed = WithSuffix.addSuffix(original, "_vis")
        assertEquals(2, suffixed.size)
        assertEquals("4", suffixed["frames_vis"]?.toString())

        val stripped = WithSuffix.stripSuffix(suffixed, "_vis")
        assertEquals("4", stripped["frames"]?.toString())
    }

    @Test
    fun testWithSuffixMapSerializer() {
        val serializer = WithSuffixMapSerializer("_bar", String.serializer())
        val json = Json { prettyPrint = false }

        val map = mapOf("x" to "10", "y" to "20")
        val encoded = json.encodeToString(serializer, map)
        assertEquals("{\"x_bar\":\"10\",\"y_bar\":\"20\"}", encoded)

        val decoded = json.decodeFromString(serializer, encoded)
        assertEquals(map, decoded)
    }
}
