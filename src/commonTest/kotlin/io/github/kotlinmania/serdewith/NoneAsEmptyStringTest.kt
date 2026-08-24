package io.github.kotlinmania.serdewith

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NoneAsEmptyStringTest {
    @Test
    fun testNoneAsEmptyString() {
        assertEquals("\"\"", Json.encodeToString(NoneAsEmptyStringSerializer, null))
        assertEquals("\"hello\"", Json.encodeToString(NoneAsEmptyStringSerializer, "hello"))

        assertNull(Json.decodeFromString(NoneAsEmptyStringSerializer, "\"\""))
        assertEquals("hello", Json.decodeFromString(NoneAsEmptyStringSerializer, "\"hello\""))
    }
}
