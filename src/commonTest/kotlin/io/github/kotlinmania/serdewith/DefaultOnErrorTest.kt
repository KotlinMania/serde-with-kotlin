// port-lint: tests tests/serde_as/default_on.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultOnErrorTest {
    @Test
    fun testDefaultOnError() {
        val serializer = DefaultOnErrorSerializer(Int.serializer()) { 0 }

        assertEquals(123, Json.decodeFromString(serializer, "123"))
        assertEquals(0, Json.decodeFromString(serializer, "0"))
        assertEquals(0, Json.decodeFromString(serializer, "\"\""))
        assertEquals(0, Json.decodeFromString(serializer, "\"12+3\""))
        assertEquals(0, Json.decodeFromString(serializer, "\"abc\""))
    }

    @Test
    fun testDefaultOnNull() {
        val serializer = DefaultOnNullSerializer(Int.serializer()) { 0 }

        assertEquals(123, Json.decodeFromString(serializer, "123"))
        assertEquals(0, Json.decodeFromString(serializer, "0"))
        assertEquals(0, Json.decodeFromString(serializer, "null"))
    }
}
