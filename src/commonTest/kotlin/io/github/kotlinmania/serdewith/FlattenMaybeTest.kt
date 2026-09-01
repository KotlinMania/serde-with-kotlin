// port-lint: tests flatten_maybe.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
data class InnerPayload(
    val i: Int,
    val text: String = "",
)

class FlattenMaybeTest {
    @Test
    fun testFlattenedMaybeDeserialization() {
        val serializer = FlattenedMaybeSerializer("t", InnerPayload.serializer())
        val json = Json { ignoreUnknownKeys = true }

        // Nested form: {"t":{"i":1,"text":"hello"}}
        val nestedJson = "{\"t\":{\"i\":1,\"text\":\"hello\"}}"
        val parsedNested = json.decodeFromString(serializer, nestedJson)
        assertEquals(1, parsedNested.i)
        assertEquals("hello", parsedNested.text)

        // Flattened form: {"i":2,"text":"world"}
        val flatJson = "{\"i\":2,\"text\":\"world\"}"
        val parsedFlat = json.decodeFromString(serializer, flatJson)
        assertEquals(2, parsedFlat.i)
        assertEquals("world", parsedFlat.text)
    }
}
