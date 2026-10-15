// port-lint: tests flatten_maybe.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserialize
import io.github.kotlinmania.serdecore.de.DeserializeSeed
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.MapAccess
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer
import io.github.kotlinmania.serdejson.fromStr
import kotlin.test.Test
import kotlin.test.assertEquals

data class InnerPayload(
    val i: Int,
    val text: String = "",
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val structRes = serializer.serializeStruct("InnerPayload", 2)
        if (structRes.isFailure) return SerdeResult.failure(structRes.unwrapErr())
        val struct = structRes.getOrThrow()
        val iRes = struct.serializeField("i", SerializePrimitive(i))
        if (iRes.isFailure) return SerdeResult.failure(iRes.unwrapErr())
        val tRes = struct.serializeField("text", SerializePrimitive(text))
        if (tRes.isFailure) return SerdeResult.failure(tRes.unwrapErr())
        return struct.end()
    }

    private object StringSeed : DeserializeSeed<String> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<String> =
            deserializer.deserializeStr(
                object : Visitor<String> {
                    override fun expecting(): String = "a string"

                    override fun visitStr(v: String) = SerdeResult.success(v)
                },
            )
    }

    private object IntSeed : DeserializeSeed<Int> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<Int> =
            deserializer.deserializeI64(
                object : Visitor<Int> {
                    override fun expecting(): String = "an integer"

                    override fun visitI64(v: Long) = SerdeResult.success(v.toInt())

                    override fun visitU64(v: ULong) = SerdeResult.success(v.toInt())
                },
            )
    }

    companion object : Deserialize<InnerPayload>, DeserializeAs<InnerPayload> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<InnerPayload> =
            deserializeAs(deserializer)

        override fun deserializeAs(deserializer: Deserializer): SerdeResult<InnerPayload> =
            deserializer.deserializeMap(
                object : Visitor<InnerPayload> {
                    override fun expecting(): String = "an InnerPayload map"

                    override fun <A> visitMap(access: A): SerdeResult<InnerPayload> where A : MapAccess {
                        var iVal: Int? = null
                        var textVal = ""
                        while (true) {
                            val key = access.nextKeySeed(StringSeed).getOrNull() ?: break
                            when (key) {
                                "i" -> {
                                    iVal = access.nextValueSeed(IntSeed).getOrNull()
                                }

                                "text" -> {
                                    textVal = access.nextValueSeed(StringSeed).getOrNull().orEmpty()
                                }

                                else -> {
                                    access.nextValueSeed(IgnoreAnySeed)
                                }
                            }
                        }
                        return SerdeResult.success(InnerPayload(iVal ?: 0, textVal))
                    }
                },
            )
    }
}

class FlattenMaybeTest {
    @Test
    fun testFlattenedMaybeDeserialization() {
        val adapter = FlattenedMaybe("t", elementDe = InnerPayload)
        val de = adapter.asDeserialize()

        val nestedJson = "{\"t\":{\"i\":1,\"text\":\"hello\"}}"
        val parsedNested = fromStr(nestedJson, de).getOrThrow()
        assertEquals(1, parsedNested.i)
        assertEquals("hello", parsedNested.text)

        val flatJson = "{\"i\":2,\"text\":\"world\"}"
        val parsedFlat = fromStr(flatJson, de).getOrThrow()
        assertEquals(2, parsedFlat.i)
        assertEquals("world", parsedFlat.text)
    }
}
