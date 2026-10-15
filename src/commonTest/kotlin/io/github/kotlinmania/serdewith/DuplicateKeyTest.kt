// port-lint: tests duplicate_key_impls/error_on_duplicate.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.indexmap.IndexMap
import io.github.kotlinmania.indexmap.IndexSet
import io.github.kotlinmania.prettyassertions.assertEq
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdejson.fromStr
import kotlin.test.Test
import kotlin.test.assertTrue

class DuplicateKeyTest {
    private val intDe = IntAsString
    private val intValDe =
        object : DeserializeAs<Int> {
            override fun deserializeAs(deserializer: Deserializer): io.github.kotlinmania.serde.SerdeResult<Int> =
                deserializer.deserializeI64(
                    object : io.github.kotlinmania.serdecore.de.Visitor<Int> {
                        override fun expecting(): String = "an integer"

                        override fun visitI64(v: Long) =
                            io.github.kotlinmania.serde.SerdeResult
                                .success(v.toInt())

                        override fun visitU64(v: ULong) =
                            io.github.kotlinmania.serde.SerdeResult
                                .success(v.toInt())

                        override fun visitStr(v: String) =
                            io.github.kotlinmania.serde
                                .serdeCatching { v.toInt() }
                    },
                )
        }

    @Test
    fun testErrorOnDuplicateSet() {
        val adapter = SetWithDuplicateStrategy(IntAsString, intValDe, DuplicateStrategy.ErrorOnDuplicate)
        val res = fromStr("[1, 2, 2, 3]", adapter.asDeserialize())
        assertTrue(res.isFailure)
    }

    @Test
    fun testFirstValueWinsSet() {
        val adapter = SetWithDuplicateStrategy(IntAsString, intValDe, DuplicateStrategy.FirstValueWins)
        val result = fromStr("[1, 2, 2, 3]", adapter.asDeserialize()).getOrThrow()
        assertEq(setOf(1, 2, 3), result)
    }

    @Test
    fun testLastValueWinsSet() {
        val adapter = SetWithDuplicateStrategy(IntAsString, intValDe, DuplicateStrategy.LastValueWins)
        val result = fromStr("[1, 2, 2, 3]", adapter.asDeserialize()).getOrThrow()
        assertEq(setOf(1, 2, 3), result)
    }

    @Test
    fun testErrorOnDuplicateMap() {
        val adapter = MapWithDuplicateStrategy(intDe, intValDe, DuplicateStrategy.ErrorOnDuplicate)
        val res = fromStr("""{"1": 1, "2": 2, "1": 3}""", adapter.asDeserialize())
        assertTrue(res.isFailure)
    }

    @Test
    fun testFirstValueWinsMap() {
        val adapter = MapWithDuplicateStrategy(intDe, intValDe, DuplicateStrategy.FirstValueWins)
        val result = fromStr("""{"1": 1, "2": 2, "1": 3}""", adapter.asDeserialize()).getOrThrow()
        assertEq(1, result[1])
        assertEq(2, result[2])
    }

    @Test
    fun testLastValueWinsMap() {
        val adapter = MapWithDuplicateStrategy(intDe, intValDe, DuplicateStrategy.LastValueWins)
        val result = fromStr("""{"1": 1, "2": 2, "1": 3}""", adapter.asDeserialize()).getOrThrow()
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
