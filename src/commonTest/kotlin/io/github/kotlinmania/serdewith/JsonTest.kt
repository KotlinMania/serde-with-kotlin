// port-lint: tests json.rs
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
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals

data class Nested(
    val value: String,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val structRes = serializer.serializeStruct("Nested", 1)
        if (structRes.isFailure) return SerdeResult.failure(structRes.unwrapErr())
        val struct = structRes.getOrThrow()
        val fRes = struct.serializeField("value", SerializePrimitive(value))
        if (fRes.isFailure) return SerdeResult.failure(fRes.unwrapErr())
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

    companion object : Deserialize<Nested> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<Nested> =
            deserializer.deserializeMap(
                object : Visitor<Nested> {
                    override fun expecting(): String = "a Nested map"

                    override fun <A> visitMap(access: A): SerdeResult<Nested> where A : MapAccess {
                        var v = ""
                        while (true) {
                            val key = access.nextKeySeed(StringSeed).getOrNull() ?: break
                            if (key == "value") {
                                v = access.nextValueSeed(StringSeed).getOrNull().orEmpty()
                            } else {
                                access.nextValueSeed(IgnoreAnySeed)
                            }
                        }
                        return SerdeResult.success(Nested(v))
                    }
                },
            )
    }
}

class JsonTest {
    @Test
    fun testJsonString() {
        val jsonStringAdapter = JsonString<Nested>(deserialize = Nested)
        val wrap = SerializeAsWrap(Nested("444"), jsonStringAdapter)
        val json = toStr(wrap).getOrThrow()
        assertEquals("\"{\\\"value\\\":\\\"444\\\"}\"", json)

        val decoded = fromStr(json, jsonStringAdapter.asDeserialize()).getOrThrow()
        assertEquals(Nested("444"), decoded)
    }
}
