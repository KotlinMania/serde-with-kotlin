package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals

class OneOrManyTest {
    @Test
    fun testOneOrManyPreferOne() {
        val adapter = OneOrMany(IntAsString, IntAsString, PreferOne)
        val de = adapter.asDeserialize()

        // Single element array decodes as list of 1
        assertEquals(listOf(42), fromStr("\"42\"", de).getOrThrow())
        assertEquals(listOf(42), fromStr("[\"42\"]", de).getOrThrow())
        assertEquals(listOf(1, 2, 3), fromStr("[\"1\",\"2\",\"3\"]", de).getOrThrow())

        // Single element list encodes as single element in PreferOne
        val singleWrap = SerializeAsWrap(listOf(42), adapter)
        assertEquals("\"42\"", toStr(singleWrap).getOrThrow())

        val manyWrap = SerializeAsWrap(listOf(1, 2, 3), adapter)
        assertEquals("[\"1\",\"2\",\"3\"]", toStr(manyWrap).getOrThrow())
    }

    @Test
    fun testOneOrManyPreferMany() {
        val adapter = OneOrMany(IntAsString, IntAsString, PreferMany)
        val wrap = SerializeAsWrap(listOf(42), adapter)
        assertEquals("[\"42\"]", toStr(wrap).getOrThrow())
    }
}
