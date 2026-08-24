package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultOnErrorTest {
    @Test
    fun testDefaultOnError() {
        val serializer = DefaultOnErrorSerializer(Int.serializer()) { -1 }

        assertEquals(42, Json.decodeFromString(serializer, "42"))
        assertEquals(-1, Json.decodeFromString(serializer, "\"not-a-number\""))
    }

    @Test
    fun testDefaultOnNull() {
        val serializer = DefaultOnNullSerializer(Int.serializer()) { 99 }

        assertEquals(42, Json.decodeFromString(serializer, "42"))
        assertEquals(99, Json.decodeFromString(serializer, "null"))
    }
}
