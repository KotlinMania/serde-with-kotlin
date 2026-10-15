// port-lint: tests de/skip_error.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdejson.fromStr
import kotlin.test.Test
import kotlin.test.assertEquals

class SkipErrorTest {
    private val stringDe =
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
        }

    private val intDe = IntAsString

    @Test
    fun testVecSkipError() {
        val adapter = ListSkipError(elementDe = intDe)
        val de = adapter.asDeserialize()
        val json = "[\"1\", \"not-a-number\", \"2\", \"3\"]"
        val result = fromStr(json, de).getOrThrow()
        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun testMapSkipError() {
        val adapter = MapSkipError(keyDe = stringDe, valDe = intDe)
        val de = adapter.asDeserialize()
        val json = "{\"a\": \"1\", \"b\": \"invalid\", \"c\": \"3\"}"
        val result = fromStr(json, de).getOrThrow()
        assertEquals(mapOf("a" to 1, "c" to 3), result)
    }
}
