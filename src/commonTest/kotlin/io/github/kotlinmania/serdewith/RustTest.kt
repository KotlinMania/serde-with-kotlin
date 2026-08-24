package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RustTest {
    @Test
    fun testDoubleOptionSerializer() {
        val serializer = DoubleOptionSerializer(Int.serializer())
        val json = Json { explicitNulls = true }

        val valueObj: DoubleOption<Int> = DoubleOption.Value(42)
        val encodedVal = json.encodeToString(serializer, valueObj)
        assertEquals("42", encodedVal)

        val nullObj: DoubleOption<Int> = DoubleOption.Null
        val encodedNull = json.encodeToString(serializer, nullObj)
        assertEquals("null", encodedNull)

        val decodedVal = json.decodeFromString(serializer, "100")
        assertTrue(decodedVal is DoubleOption.Value && decodedVal.value == 100)

        val decodedNull = json.decodeFromString(serializer, "null")
        assertTrue(decodedNull.isNull)
    }

    @Test
    fun testDeserializeIgnoreAnySerializer() {
        val serializer = DeserializeIgnoreAnySerializer { "default" }
        val json = Json { ignoreUnknownKeys = true }

        val decoded = json.decodeFromString(serializer, "{\"any\":\"data\",\"nested\":[1,2,3]}")
        assertEquals("default", decoded)
    }
}
