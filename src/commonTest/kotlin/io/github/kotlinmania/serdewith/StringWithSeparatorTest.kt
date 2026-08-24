package io.github.kotlinmania.serdewith

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class StringWithSeparatorTest {
    @Test
    fun testCommaSeparated() {
        val list = listOf("alpha", "beta", "gamma")
        val json = Json.encodeToString(CommaSeparatedStringListSerializer, list)
        assertEquals("\"alpha,beta,gamma\"", json)

        val decoded = Json.decodeFromString(CommaSeparatedStringListSerializer, json)
        assertEquals(list, decoded)
    }

    @Test
    fun testSpaceSeparated() {
        val list = listOf("hello", "world")
        val json = Json.encodeToString(SpaceSeparatedStringListSerializer, list)
        assertEquals("\"hello world\"", json)

        val decoded = Json.decodeFromString(SpaceSeparatedStringListSerializer, json)
        assertEquals(list, decoded)
    }
}
