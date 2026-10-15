// port-lint: tests rust.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RustTest {
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
    fun prohibitDuplicateValueHashset() {
        val adapter = SetWithDuplicateStrategy(IntAsString, intValDe, DuplicateStrategy.ErrorOnDuplicate)
        val res = fromStr("[1, 2, 3, 4, 1]", adapter.asDeserialize())
        assertTrue(res.isFailure)
    }

    @Test
    fun duplicateKeyFirstWinsHashmap() {
        val adapter = MapWithDuplicateStrategy(intDe, intValDe, DuplicateStrategy.FirstValueWins)
        val result = fromStr("{\"1\": 1, \"2\": 2, \"1\": 3}", adapter.asDeserialize()).getOrThrow()
        assertEquals(1, result[1])
    }

    @Test
    fun duplicateValueFirstWinsHashset() {
        val adapter = SetWithDuplicateStrategy(IntAsString, intValDe, DuplicateStrategy.FirstValueWins)
        val result = fromStr("[1, 2, 2, 3]", adapter.asDeserialize()).getOrThrow()
        assertEquals(setOf(1, 2, 3), result)
    }

    @Test
    fun duplicateValueLastWinsHashset() {
        val adapter = SetWithDuplicateStrategy(IntAsString, intValDe, DuplicateStrategy.LastValueWins)
        val result = fromStr("[1, 2, 2, 3]", adapter.asDeserialize()).getOrThrow()
        assertEquals(setOf(1, 2, 3), result)
    }

    @Test
    fun unwrapOrSkipRefSome() {
        val adapter = DoubleOptionAdapter(IntAsString, intDe)
        val valueObj: DoubleOption<Int> = DoubleOption.Value(42)
        val wrap = SerializeAsWrap(valueObj, adapter)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("\"42\"", encoded)
    }

    @Test
    fun unwrapOrSkipRefNone() {
        val adapter = DoubleOptionAdapter(IntAsString, intDe)
        val nullObj: DoubleOption<Int> = DoubleOption.Null
        val wrap = SerializeAsWrap(nullObj, adapter)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("null", encoded)
    }

    @Test
    fun unwrapOrSkipSome() {
        val adapter = DoubleOptionAdapter(IntAsString, intDe)
        val decoded = fromStr("\"42\"", adapter.asDeserialize()).getOrThrow()
        assertTrue(decoded is DoubleOption.Value && decoded.value == 42)
    }

    @Test
    fun unwrapOrSkipNone() {
        val adapter = DoubleOptionAdapter(IntAsString, intDe)
        val decoded = fromStr("null", adapter.asDeserialize()).getOrThrow()
        assertTrue(decoded.isNull)
    }

    @Test
    fun testDeserializeIgnoreAny() {
        val adapter = DeserializeIgnoreAny { "default" }
        val decoded = fromStr("{\"any\":\"data\"}", adapter.asDeserialize()).getOrThrow()
        assertEquals("default", decoded)
    }
}
