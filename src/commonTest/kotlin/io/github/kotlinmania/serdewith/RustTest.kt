// port-lint: tests rust.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RustTest {
    @Test
    fun prohibitDuplicateValueHashset() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.ErrorOnDuplicate)
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, "[1, 2, 3, 4, 1]")
        }
    }

    @Test
    fun prohibitDuplicateValueBtreeset() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.ErrorOnDuplicate)
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, "[1, 2, 3, 4, 1]")
        }
    }

    @Test
    fun prohibitDuplicateKeyHashmap() {
        val serializer = MapWithDuplicateStrategySerializer(String.serializer(), Int.serializer(), DuplicateStrategy.ErrorOnDuplicate)
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, "{\"1\": 1, \"2\": 2, \"1\": 3}")
        }
    }

    @Test
    fun prohibitDuplicateKeyBtreemap() {
        val serializer = MapWithDuplicateStrategySerializer(String.serializer(), Int.serializer(), DuplicateStrategy.ErrorOnDuplicate)
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, "{\"1\": 1, \"2\": 2, \"1\": 3}")
        }
    }

    @Test
    fun duplicateKeyFirstWinsHashmap() {
        val serializer = MapWithDuplicateStrategySerializer(String.serializer(), Int.serializer(), DuplicateStrategy.FirstValueWins)
        val result = Json.decodeFromString(serializer, "{\"1\": 1, \"2\": 2, \"1\": 3}")
        assertEquals(1, result["1"])
    }

    @Test
    fun duplicateKeyFirstWinsBtreemap() {
        val serializer = MapWithDuplicateStrategySerializer(String.serializer(), Int.serializer(), DuplicateStrategy.FirstValueWins)
        val result = Json.decodeFromString(serializer, "{\"1\": 1, \"2\": 2, \"1\": 3}")
        assertEquals(1, result["1"])
    }

    @Test
    fun duplicateValueFirstWinsHashset() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.FirstValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        assertEquals(setOf(1, 2, 3), result)
    }

    @Test
    fun duplicateValueLastWinsHashset() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.LastValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        assertEquals(setOf(1, 2, 3), result)
    }

    @Test
    fun duplicateValueLastWinsBtreeset() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.LastValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        assertEquals(setOf(1, 2, 3), result)
    }

    @Test
    fun unwrapOrSkipRefSome() {
        val serializer = DoubleOptionSerializer(Int.serializer())
        val valueObj: DoubleOption<Int> = DoubleOption.Value(42)
        val encoded = Json.encodeToString(serializer, valueObj)
        assertEquals("42", encoded)
    }

    @Test
    fun unwrapOrSkipRefNone() {
        val serializer = DoubleOptionSerializer(Int.serializer())
        val nullObj: DoubleOption<Int> = DoubleOption.Null
        val encoded = Json.encodeToString(serializer, nullObj)
        assertEquals("null", encoded)
    }

    @Test
    fun unwrapOrSkipSome() {
        val serializer = DoubleOptionSerializer(Int.serializer())
        val decoded = Json.decodeFromString(serializer, "42")
        assertTrue(decoded is DoubleOption.Value && decoded.value == 42)
    }

    @Test
    fun unwrapOrSkipNone() {
        val serializer = DoubleOptionSerializer(Int.serializer())
        val decoded = Json.decodeFromString(serializer, "null")
        assertTrue(decoded.isNull)
    }

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
