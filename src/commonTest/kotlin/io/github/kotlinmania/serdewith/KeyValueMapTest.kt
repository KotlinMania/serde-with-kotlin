// port-lint: tests key_value_map.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals

class KeyValueMapTest {
    @Test
    fun testKeyValueMapSerialization() {
        val map = mapOf("a" to 1, "c" to 2)
        val adapter =
            KeyValueMap<String, Int>(
                keyDe =
                    object : DeserializeAs<String> {
                        override fun deserializeAs(deserializer: Deserializer) =
                            deserializer.deserializeStr(
                                object : io.github.kotlinmania.serdecore.de.Visitor<String> {
                                    override fun expecting() = "a string"

                                    override fun visitStr(v: String) =
                                        io.github.kotlinmania.serde.SerdeResult
                                            .success(v)
                                },
                            )
                    },
                valDe =
                    object : DeserializeAs<Int> {
                        override fun deserializeAs(deserializer: Deserializer) =
                            deserializer.deserializeI64(
                                object : io.github.kotlinmania.serdecore.de.Visitor<Int> {
                                    override fun expecting() = "an integer"

                                    override fun visitI64(v: Long) =
                                        io.github.kotlinmania.serde.SerdeResult
                                            .success(v.toInt())

                                    override fun visitU64(v: ULong) =
                                        io.github.kotlinmania.serde.SerdeResult
                                            .success(v.toInt())
                                },
                            )
                    },
            )

        val wrap = SerializeAsWrap(map, adapter)
        val json = toStr(wrap).getOrThrow()
        assertEquals("[{\"key\":\"a\",\"value\":1},{\"key\":\"c\",\"value\":2}]", json)

        val decoded = fromStr(json, adapter.asDeserialize()).getOrThrow()
        assertEquals(map, decoded)
    }
}
