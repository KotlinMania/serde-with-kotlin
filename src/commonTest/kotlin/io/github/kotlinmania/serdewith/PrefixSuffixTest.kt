package io.github.kotlinmania.serdewith

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PrefixSuffixTest {
    @Serializable
    data class Player(
        val name: String,
        val score: Int,
    )

    @Test
    fun testWithPrefix() {
        val serializer = WithPrefixSerializer("player1_", Player.serializer())
        val player = Player(name = "Alice", score = 100)

        val json = Json.encodeToString(serializer, player)
        assertEquals("{\"player1_name\":\"Alice\",\"player1_score\":100}", json)

        val decoded = Json.decodeFromString(serializer, json)
        assertEquals(player, decoded)
    }

    @Test
    fun testWithSuffix() {
        val serializer = WithSuffixSerializer("_v1", Player.serializer())
        val player = Player(name = "Bob", score = 200)

        val json = Json.encodeToString(serializer, player)
        assertEquals("{\"name_v1\":\"Bob\",\"score_v1\":200}", json)

        val decoded = Json.decodeFromString(serializer, json)
        assertEquals(player, decoded)
    }
}
