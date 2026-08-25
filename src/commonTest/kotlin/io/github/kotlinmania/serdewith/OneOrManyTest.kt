// port-lint: tests serde_as/lib.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class OneOrManyTest {
    @Test
    fun testOneOrManyPreferOne() {
        val serializer = OneOrManySerializer(Int.serializer(), PreferOne)

        // Single element array decodes as list of 1
        assertEquals(listOf(42), Json.decodeFromString(serializer, "42"))
        assertEquals(listOf(42), Json.decodeFromString(serializer, "[42]"))
        assertEquals(listOf(1, 2, 3), Json.decodeFromString(serializer, "[1, 2, 3]"))

        // Single element list encodes as single element in PreferOne
        assertEquals("42", Json.encodeToString(serializer, listOf(42)))
        assertEquals("[1,2,3]", Json.encodeToString(serializer, listOf(1, 2, 3)))
    }

    @Test
    fun testOneOrManyPreferMany() {
        val serializer = OneOrManySerializer(Int.serializer(), PreferMany)
        assertEquals("[42]", Json.encodeToString(serializer, listOf(42)))
    }
}
