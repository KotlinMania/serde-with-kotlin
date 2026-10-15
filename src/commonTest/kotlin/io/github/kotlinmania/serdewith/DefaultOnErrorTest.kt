package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdejson.fromStr
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultOnErrorTest {
    private val intDe =
        object : DeserializeAs<Int> {
            override fun deserializeAs(deserializer: Deserializer) =
                deserializer.deserializeI64(
                    object : Visitor<Int> {
                        override fun expecting(): String = "an integer"

                        override fun visitI64(v: Long) =
                            io.github.kotlinmania.serde.SerdeResult
                                .success(v.toInt())

                        override fun visitU64(v: ULong) =
                            io.github.kotlinmania.serde.SerdeResult
                                .success(v.toInt())
                    },
                )
        }

    private val intOptVisitor =
        object : Visitor<Int?> {
            override fun expecting(): String = "an optional integer"

            override fun visitNone() =
                io.github.kotlinmania.serde.SerdeResult
                    .success(null)

            override fun <D> visitSome(deserializer: D): io.github.kotlinmania.serde.SerdeResult<Int?> where D : Deserializer =
                intDe.deserializeAs(deserializer)
        }

    private val intOptDe =
        object : DeserializeAs<Int?> {
            override fun deserializeAs(deserializer: Deserializer) =
                deserializer.deserializeOption(intOptVisitor)
        }

    @Test
    fun testDefaultOnError() {
        val adapter = DefaultOnError(IntAsString, intDe) { 0 }
        val de = adapter.asDeserialize()

        assertEquals(123, fromStr("123", de).getOrThrow())
        assertEquals(0, fromStr("0", de).getOrThrow())
        assertEquals(0, fromStr("\"\"", de).getOrThrow())
        assertEquals(0, fromStr("\"12+3\"", de).getOrThrow())
        assertEquals(0, fromStr("\"abc\"", de).getOrThrow())
    }

    @Test
    fun testDefaultOnNull() {
        val adapter =
            DefaultOnNull(
                object : SerializeAs<Int?> {
                    override fun <Ok> serializeAs(source: Int?, serializer: io.github.kotlinmania.serdecore.ser.Serializer<Ok>) =
                        serializer.serializeNone()
                },
                intOptDe,
            ) { 0 }

        val de = adapter.asDeserialize()

        assertEquals(123, fromStr("123", de).getOrThrow())
        assertEquals(0, fromStr("0", de).getOrThrow())
        assertEquals(0, fromStr("null", de).getOrThrow())
    }
}
