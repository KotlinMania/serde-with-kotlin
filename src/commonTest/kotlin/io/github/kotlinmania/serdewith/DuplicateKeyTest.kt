// port-lint: tests duplicate_key_impls/mod.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DuplicateKeyTest {
    @Test
    fun testErrorOnDuplicate() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.ErrorOnDuplicate)
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        }
    }

    @Test
    fun testFirstValueWins() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.FirstValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        assertEquals(setOf(1, 2, 3), result)
    }

    @Test
    fun testLastValueWins() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.LastValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        assertEquals(setOf(1, 2, 3), result)
    }
}
