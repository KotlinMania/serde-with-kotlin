// port-lint: tests serde_as/serde_conv.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.prettyassertions.assertEq
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SerdeConvTest {
    private val boolAsStringSerializer =
        serdeConv(
            String.serializer(),
            { it.toString() },
            { it.toBooleanStrict() },
        )

    data class Rgb(
        val red: Int,
        val green: Int,
        val blue: Int,
    )

    private val rgbAsArraySerializer =
        serdeConv(
            ListSerializer(Int.serializer()),
            { listOf(it.red, it.green, it.blue) },
            { list ->
                require(list.size == 3) { "Expected 3 elements, got ${list.size}" }
                Rgb(list[0], list[1], list[2])
            },
        )

    @Test
    fun testBoolAsString() {
        assertEq("\"false\"", Json.encodeToString(boolAsStringSerializer, false))
        assertEq("\"true\"", Json.encodeToString(boolAsStringSerializer, true))

        assertEq(false, Json.decodeFromString(boolAsStringSerializer, "\"false\""))
        assertEq(true, Json.decodeFromString(boolAsStringSerializer, "\"true\""))

        assertFailsWith<SerializationException> {
            Json.decodeFromString(boolAsStringSerializer, "123")
        }
        assertFailsWith<SerializationException> {
            Json.decodeFromString(boolAsStringSerializer, "\"not_a_bool\"")
        }
    }

    @Test
    fun testBoolAsStringVec() {
        val listSerializer = ListSerializer(boolAsStringSerializer)

        assertEq("[\"false\"]", Json.encodeToString(listSerializer, listOf(false)))
        assertEq("[\"true\"]", Json.encodeToString(listSerializer, listOf(true)))
        assertEq("[\"false\",\"true\"]", Json.encodeToString(listSerializer, listOf(false, true)))

        assertEq(listOf(false), Json.decodeFromString(listSerializer, "[\"false\"]"))
        assertEq(listOf(true), Json.decodeFromString(listSerializer, "[\"true\"]"))
        assertEq(listOf(false, true), Json.decodeFromString(listSerializer, "[\"false\",\"true\"]"))

        assertFailsWith<SerializationException> {
            Json.decodeFromString(listSerializer, "123")
        }
    }

    @Test
    fun testRgbAsArray() {
        val orange = Rgb(255, 128, 0)
        val encoded = Json.encodeToString(rgbAsArraySerializer, orange)
        assertEq("[255,128,0]", encoded)

        val decoded = Json.decodeFromString(rgbAsArraySerializer, "[255,128,0]")
        assertEq(orange, decoded)

        assertFailsWith<SerializationException> {
            Json.decodeFromString(rgbAsArraySerializer, "[255,128]")
        }
    }
}
