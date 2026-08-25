// port-lint: tests with_prefix.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class WithPrefixTest {
    @Test
    fun testFlattenWithPrefix() {
        val player1 = mapOf("name" to "name1", "votes" to "1")
        val player2 = mapOf("name" to "name2", "votes" to "2")
        val tags = mapOf("t" to "T")

        val prefixedPlayer1 = WithPrefix.addPrefix(player1, "player1_")
        val prefixedPlayer2 = WithPrefix.addPrefix(player2, "player2_")
        val prefixedTags = WithPrefix.addPrefix(tags, "tag_")

        val combined = mutableMapOf<String, String>()
        combined.putAll(prefixedPlayer1)
        combined.putAll(prefixedPlayer2)
        combined.putAll(prefixedTags)

        assertEquals("name1", combined["player1_name"])
        assertEquals("1", combined["player1_votes"])
        assertEquals("name2", combined["player2_name"])
        assertEquals("2", combined["player2_votes"])
        assertEquals("T", combined["tag_t"])
    }

    @Test
    fun testPlainWithPrefix() {
        val serializer = WithPrefixMapSerializer("player1_", String.serializer())
        val json = Json { prettyPrint = false }
        val map = mapOf("name" to "name1", "votes" to "1")
        val encoded = json.encodeToString(serializer, map)
        assertEquals("{\"player1_name\":\"name1\",\"player1_votes\":\"1\"}", encoded)

        val decoded = json.decodeFromString(serializer, encoded)
        assertEquals(map, decoded)
    }

    @Test
    fun testEnumUnitVariantWithPrefix() {
        val original = mapOf("One" to 1, "Two" to 2, "Three" to 3)
        val prefixed = WithPrefix.addPrefix(original, "foo_")
        assertEquals(
            mapOf("foo_One" to 1, "foo_Two" to 2, "foo_Three" to 3),
            prefixed,
        )
    }

    @Test
    fun testAddAndStripPrefixMap() {
        val original = mapOf("name" to "Alice", "role" to "Admin")
        val prefixed = WithPrefix.addPrefix(original, "user_")
        assertEquals(mapOf("user_name" to "Alice", "user_role" to "Admin"), prefixed)

        val stripped = WithPrefix.stripPrefix(prefixed, "user_")
        assertEquals(original, stripped)
    }

    @Test
    fun testAddAndStripPrefixJsonObject() {
        val original =
            buildJsonObject {
                put("name", "Bob")
                put("votes", 42)
            }
        val prefixed = WithPrefix.addPrefix(original, "player1_")
        assertEquals(2, prefixed.size)
        assertEquals("Bob", prefixed["player1_name"]?.toString()?.replace("\"", ""))

        val stripped = WithPrefix.stripPrefix(prefixed, "player1_")
        assertEquals("Bob", stripped["name"]?.toString()?.replace("\"", ""))
    }

    @Test
    fun testWithPrefixMapSerializer() {
        val serializer = WithPrefixMapSerializer("foo_", String.serializer())
        val json = Json { prettyPrint = false }

        val map = mapOf("a" to "1", "b" to "2")
        val encoded = json.encodeToString(serializer, map)
        assertEquals("{\"foo_a\":\"1\",\"foo_b\":\"2\"}", encoded)

        val decoded = json.decodeFromString(serializer, encoded)
        assertEquals(map, decoded)
    }
}
