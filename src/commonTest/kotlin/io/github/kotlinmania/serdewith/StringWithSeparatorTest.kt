package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals

class StringWithSeparatorTest {
    @Test
    fun testCommaSeparated() {
        val list = listOf("alpha", "beta", "gamma")
        val wrap = SerializeAsWrap(list, CommaSeparatedStringList)
        val json = toStr(wrap).getOrThrow()
        assertEquals("\"alpha,beta,gamma\"", json)

        val de = CommaSeparatedStringList.asDeserialize()
        val decoded = fromStr(json, de).getOrThrow()
        assertEquals(list, decoded)
    }

    @Test
    fun testSpaceSeparated() {
        val list = listOf("hello", "world")
        val wrap = SerializeAsWrap(list, SpaceSeparatedStringList)
        val json = toStr(wrap).getOrThrow()
        assertEquals("\"hello world\"", json)

        val de = SpaceSeparatedStringList.asDeserialize()
        val decoded = fromStr(json, de).getOrThrow()
        assertEquals(list, decoded)
    }
}
