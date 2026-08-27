// port-lint: tests with_suffix.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class WithSuffixTest {
    @Test
    fun testFlattenWithSuffix() {
        val player1 = mapOf("name" to "name1", "votes" to "1")
        val player2 = mapOf("name" to "name2", "votes" to "2")
        val tags = mapOf("t" to "T")

        val suffixedPlayer1 = WithSuffix.addSuffix(player1, "_player1")
        val suffixedPlayer2 = WithSuffix.addSuffix(player2, "_player2")
        val suffixedTags = WithSuffix.addSuffix(tags, "_tag")

        val combined = mutableMapOf<String, String>()
        combined.putAll(suffixedPlayer1)
        combined.putAll(suffixedPlayer2)
        combined.putAll(suffixedTags)

        assertEquals("name1", combined["name_player1"])
        assertEquals("1", combined["votes_player1"])
        assertEquals("name2", combined["name_player2"])
        assertEquals("2", combined["votes_player2"])
        assertEquals("T", combined["t_tag"])
    }

    @Test
    fun testPlainWithSuffix() {
        val serializer = WithSuffixMapSerializer("_player1", String.serializer())
        val json = Json { prettyPrint = false }
        val map = mapOf("name" to "name1", "votes" to "1")
        val encoded = json.encodeToString(serializer, map)
        assertEquals("{\"name_player1\":\"name1\",\"votes_player1\":\"1\"}", encoded)

        val decoded = json.decodeFromString(serializer, encoded)
        assertEquals(map, decoded)
    }

    @Test
    fun testEnumUnitVariantWithSuffix() {
        val original = mapOf("One" to 1, "Two" to 2, "Three" to 3)
        val suffixed = WithSuffix.addSuffix(original, "_foo")
        assertEquals(
            mapOf("One_foo" to 1, "Two_foo" to 2, "Three_foo" to 3),
            suffixed,
        )
    }

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
