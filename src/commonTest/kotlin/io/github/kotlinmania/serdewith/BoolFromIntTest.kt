// port-lint: tests serde_with/src/lib.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BoolFromIntTest {
    @Test
    fun testBoolfromint() {
        assertEquals("0", Json.encodeToString(StrictBoolFromIntSerializer, false))
        assertEquals("1", Json.encodeToString(StrictBoolFromIntSerializer, true))

        assertEquals(false, Json.decodeFromString(StrictBoolFromIntSerializer, "0"))
        assertEquals(true, Json.decodeFromString(StrictBoolFromIntSerializer, "1"))

        assertFailsWith<SerializationException> {
            Json.decodeFromString(StrictBoolFromIntSerializer, "2")
        }
        assertFailsWith<SerializationException> {
            Json.decodeFromString(StrictBoolFromIntSerializer, "-100")
        }
    }

    @Test
    fun testStrictBoolFromInt() {
        assertEquals("1", Json.encodeToString(StrictBoolFromIntSerializer, true))
        assertEquals("0", Json.encodeToString(StrictBoolFromIntSerializer, false))

        assertEquals(true, Json.decodeFromString(StrictBoolFromIntSerializer, "1"))
        assertEquals(false, Json.decodeFromString(StrictBoolFromIntSerializer, "0"))

        assertFailsWith<SerializationException> {
            Json.decodeFromString(StrictBoolFromIntSerializer, "2")
        }
    }

    @Test
    fun testFlexibleBoolFromInt() {
        assertEquals(true, Json.decodeFromString(FlexibleBoolFromIntSerializer, "5"))
        assertEquals(false, Json.decodeFromString(FlexibleBoolFromIntSerializer, "0"))
    }
}
