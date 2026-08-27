// port-lint: tests key_value_map.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class KeyValueMapTest {
    @Test
    fun testKvmapStructJson() {
        val map = mapOf("a" to 1, "c" to 2)
        val serializer = KeyValueMapSerializer(String.serializer(), Int.serializer())

        val json = Json.encodeToString(serializer, map)
        val decoded = Json.decodeFromString(serializer, json)
        assertEquals(map, decoded)
    }

    @Test
    fun testKeyValueMapSerialization() {
        val map = mapOf("a" to 1, "b" to 2)
        val serializer = KeyValueMapSerializer(String.serializer(), Int.serializer())

        val json = Json.encodeToString(serializer, map)
        val decoded = Json.decodeFromString(serializer, json)
        assertEquals(map, decoded)
    }
}
