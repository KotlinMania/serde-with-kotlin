package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SkipErrorTest {
    @Test
    fun testListSkipError() {
        val serializer = ListSkipErrorSerializer(Int.serializer())
        // "invalid" string cannot be decoded as Int, so it should be skipped
        val json = "[1, \"not-a-number\", 2, null, 3]"
        val result = Json.decodeFromString(serializer, json)
        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun testMapSkipError() {
        val serializer = MapSkipErrorSerializer(String.serializer(), Int.serializer())
        val json = "{\"a\": 1, \"b\": \"invalid\", \"c\": 3}"
        val result = Json.decodeFromString(serializer, json)
        assertEquals(mapOf("a" to 1, "c" to 3), result)
    }
}
