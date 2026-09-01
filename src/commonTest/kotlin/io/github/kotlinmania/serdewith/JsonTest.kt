// port-lint: tests json.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonTest {
    @Serializable
    data class Nested(
        val value: String,
    )

    @Serializable
    data class Struct(
        @Serializable(with = NestedJsonSerializer::class)
        val value: Nested,
    )

    object NestedJsonSerializer : kotlinx.serialization.KSerializer<Nested> by JsonStringSerializer(Nested.serializer())

    @Test
    fun testJsonstring() {
        val s = Struct(value = Nested(value = "444"))
        val json = Json.encodeToString(Struct.serializer(), s)
        assertEquals("{\"value\":\"{\\\"value\\\":\\\"444\\\"}\"}", json)

        val decoded = Json.decodeFromString(Struct.serializer(), json)
        assertEquals(s, decoded)
    }

    @Test
    fun testJsonstringNested() {
        val map = mapOf("[1,2]" to 3, "[4,5]" to 6)
        val innerJson = Json.encodeToString(JsonStringSerializer(MapSerializer(String.serializer(), Int.serializer())), map)
        assertEquals("\"{\\\"[1,2]\\\":3,\\\"[4,5]\\\":6}\"", innerJson)
    }

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
