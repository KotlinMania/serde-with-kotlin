// port-lint: tests enum_map.rs
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

data class NamedItem(
    val name: String,
    val count: Int,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val structRes = serializer.serializeStruct("NamedItem", 2)
        if (structRes.isFailure) return SerdeResult.failure(structRes.unwrapErr())
        val struct = structRes.getOrThrow()
        val nameRes = struct.serializeField("name", SerializePrimitive(name))
        if (nameRes.isFailure) return SerdeResult.failure(nameRes.unwrapErr())
        val countRes = struct.serializeField("count", SerializePrimitive(count))
        if (countRes.isFailure) return SerdeResult.failure(countRes.unwrapErr())
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

    companion object : Deserialize<NamedItem>, DeserializeAs<NamedItem> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<NamedItem> =
            deserializeAs(deserializer)

        override fun deserializeAs(deserializer: Deserializer): SerdeResult<NamedItem> =
            deserializer.deserializeMap(
                object : Visitor<NamedItem> {
                    override fun expecting(): String = "a NamedItem map"

                    override fun <A> visitMap(access: A): SerdeResult<NamedItem> where A : MapAccess {
                        var name: String? = null
                        var count: Int? = null
                        while (true) {
                            val key = access.nextKeySeed(StringSeed).getOrNull() ?: break
                            when (key) {
                                "name" -> {
                                    name = access.nextValueSeed(StringSeed).getOrNull()
                                }

                                "count" -> {
                                    count = access.nextValueSeed(IntSeed).getOrNull()
                                }

                                else -> {
                                    access.nextValueSeed(IgnoreAnySeed)
                                }
                            }
                        }
                        return SerdeResult.success(
                            NamedItem(name.orEmpty(), count ?: 0),
                        )
                    }
                },
            )
    }
}

class EnumMapTest {
    @Test
    fun jsonRoundTrip() {
        val adapter =
            TaggedListAsMap(
                tagExtractor = { it.name },
                elementDe = NamedItem,
                valueFactory = { tag, item -> item.copy(name = tag) },
            )

        val items =
            listOf(
                NamedItem("apple", 5),
                NamedItem("banana", 10),
            )

        val wrap = SerializeAsWrap(items, adapter)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("{\"apple\":{\"name\":\"apple\",\"count\":5},\"banana\":{\"name\":\"banana\",\"count\":10}}", encoded)

        val decoded = fromStr(encoded, adapter.asDeserialize()).getOrThrow()
        assertEquals(items, decoded)
    }
}
