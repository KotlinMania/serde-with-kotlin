// port-lint: tests duplicate_key_impls/mod.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.indexmap.IndexMap
import io.github.kotlinmania.indexmap.IndexSet
import io.github.kotlinmania.prettyassertions.assertEq
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DuplicateKeyTest {
    @Test
    fun testErrorOnDuplicateSet() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.ErrorOnDuplicate)
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        }
    }

    @Test
    fun testFirstValueWinsSet() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.FirstValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        assertEq(setOf(1, 2, 3), result)
    }

    @Test
    fun testLastValueWinsSet() {
        val serializer = SetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.LastValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        assertEq(setOf(1, 2, 3), result)
    }

    @Test
    fun testErrorOnDuplicateMap() {
        val serializer =
            MapWithDuplicateStrategySerializer(
                Int.serializer(),
                Int.serializer(),
                DuplicateStrategy.ErrorOnDuplicate,
            )
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, """{"1": 1, "2": 2, "1": 3}""")
        }
    }

    @Test
    fun testFirstValueWinsMap() {
        val serializer =
            MapWithDuplicateStrategySerializer(
                Int.serializer(),
                Int.serializer(),
                DuplicateStrategy.FirstValueWins,
            )
        val result = Json.decodeFromString(serializer, """{"1": 1, "2": 2, "1": 3}""")
        assertEq(1, result[1])
        assertEq(2, result[2])
    }

    @Test
    fun testLastValueWinsMap() {
        val serializer =
            MapWithDuplicateStrategySerializer(
                Int.serializer(),
                Int.serializer(),
                DuplicateStrategy.LastValueWins,
            )
        val result = Json.decodeFromString(serializer, """{"1": 1, "2": 2, "1": 3}""")
        assertEq(3, result[1])
        assertEq(2, result[2])
    }

    @Test
    fun testErrorOnDuplicateIndexSet() {
        val serializer = IndexSetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.ErrorOnDuplicate)
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        }
    }

    @Test
    fun testFirstValueWinsIndexSet() {
        val serializer = IndexSetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.FirstValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        val expected = IndexSet.from(listOf(1, 2, 3))
        assertEq(expected, result)
    }

    @Test
    fun testLastValueWinsIndexSet() {
        val serializer = IndexSetWithDuplicateStrategySerializer(Int.serializer(), DuplicateStrategy.LastValueWins)
        val result = Json.decodeFromString(serializer, "[1, 2, 2, 3]")
        val expected = IndexSet.from(listOf(1, 2, 3))
        assertEq(expected, result)
    }

    @Test
    fun testErrorOnDuplicateIndexMap() {
        val serializer =
            IndexMapWithDuplicateStrategySerializer(
                Int.serializer(),
                Int.serializer(),
                DuplicateStrategy.ErrorOnDuplicate,
            )
        assertFailsWith<SerializationException> {
            Json.decodeFromString(serializer, """{"1": 1, "2": 2, "1": 3}""")
        }
    }

    @Test
    fun testFirstValueWinsIndexMap() {
        val serializer =
            IndexMapWithDuplicateStrategySerializer(
                Int.serializer(),
                Int.serializer(),
                DuplicateStrategy.FirstValueWins,
            )
        val result = Json.decodeFromString(serializer, """{"1": 1, "2": 2, "1": 3}""")
        assertEq(1, result[1])
        assertEq(2, result[2])
    }

    @Test
    fun testLastValueWinsIndexMap() {
        val serializer =
            IndexMapWithDuplicateStrategySerializer(
                Int.serializer(),
                Int.serializer(),
                DuplicateStrategy.LastValueWins,
            )
        val result = Json.decodeFromString(serializer, """{"1": 1, "2": 2, "1": 3}""")
        assertEq(3, result[1])
        assertEq(2, result[2])
    }

    @Test
    fun testPreventDuplicateInsertsAdapters() {
        val set = IndexSet.new<Int>()
        val setAdapter = PreventDuplicateInsertsIndexSet(set)
        assertEq(true, setAdapter.insert(1))
        assertEq(false, setAdapter.insert(1))
        assertEq(1, setAdapter.set.len())

        val lastWinsSet = IndexSet.new<Int>()
        val lastWinsAdapter = DuplicateInsertsLastWinsIndexSet(lastWinsSet)
        lastWinsAdapter.replace(1)
        lastWinsAdapter.replace(1)
        assertEq(1, lastWinsAdapter.set.len())

        val map = IndexMap.new<Int, String>()
        val mapAdapter = PreventDuplicateInsertsIndexMap(map)
        assertEq(true, mapAdapter.insert(1, "a"))
        assertEq(false, mapAdapter.insert(1, "b"))
        assertEq("a", mapAdapter.map[1])

        val firstWinsMap = IndexMap.new<Int, String>()
        val firstWinsAdapter = DuplicateInsertsFirstWinsIndexMap(firstWinsMap)
        firstWinsAdapter.insert(1, "first")
        firstWinsAdapter.insert(1, "second")
        assertEq("first", firstWinsAdapter.map[1])
    }
}
