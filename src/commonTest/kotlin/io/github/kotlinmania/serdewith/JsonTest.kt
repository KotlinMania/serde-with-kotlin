package io.github.kotlinmania.serdewith

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonTest {
    @Serializable
    data class Inner(
        val value: Int,
    )

    @Serializable
    data class Outer(
        @Serializable(with = InnerJsonSerializer::class)
        val otherStruct: Inner,
    )

    object InnerJsonSerializer : kotlinx.serialization.KSerializer<Inner> by JsonStringSerializer(Inner.serializer())

    @Test
    fun testJsonStringSerialization() {
        val outer = Outer(Inner(10))
        val json = Json.encodeToString(Outer.serializer(), outer)
        assertEquals("{\"otherStruct\":\"{\\\"value\\\":10}\"}", json)

        val decoded = Json.decodeFromString(Outer.serializer(), json)
        assertEquals(outer, decoded)
    }
}
