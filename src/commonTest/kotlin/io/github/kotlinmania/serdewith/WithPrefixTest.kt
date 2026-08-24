package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class WithPrefixTest {
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
