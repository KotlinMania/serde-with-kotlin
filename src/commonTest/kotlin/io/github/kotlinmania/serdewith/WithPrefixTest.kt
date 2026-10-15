// port-lint: tests with_prefix.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdejson.JsonMap
import io.github.kotlinmania.serdejson.JsonNumber
import io.github.kotlinmania.serdejson.Value
import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals

class WithPrefixTest {
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

    @Test
    fun testFlattenWithPrefix() {
        val player1 = mapOf("name" to "name1", "votes" to "1")
        val player2 = mapOf("name" to "name2", "votes" to "2")
        val tags = mapOf("t" to "T")

        val prefixedPlayer1 = WithPrefix.addPrefix(player1, "player1_")
        val prefixedPlayer2 = WithPrefix.addPrefix(player2, "player2_")
        val prefixedTags = WithPrefix.addPrefix(tags, "tag_")

        val combined = mutableMapOf<String, String>()
        combined.putAll(prefixedPlayer1)
        combined.putAll(prefixedPlayer2)
        combined.putAll(prefixedTags)

        assertEquals("name1", combined["player1_name"])
        assertEquals("1", combined["player1_votes"])
        assertEquals("name2", combined["player2_name"])
        assertEquals("2", combined["player2_votes"])
        assertEquals("T", combined["tag_t"])
    }

    @Test
    fun testPlainWithPrefix() {
        val adapter = WithPrefixMap<String>("player1_", valueDe = stringDe)
        val map = mapOf("name" to "name1", "votes" to "1")
        val wrap = SerializeAsWrap(map, adapter)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("{\"player1_name\":\"name1\",\"player1_votes\":\"1\"}", encoded)

        val decoded = fromStr(encoded, adapter.asDeserialize()).getOrThrow()
        assertEquals(map, decoded)
    }

    @Test
    fun testEnumUnitVariantWithPrefix() {
        val original = mapOf("One" to 1, "Two" to 2, "Three" to 3)
        val prefixed = WithPrefix.addPrefix(original, "foo_")
        assertEquals(
            mapOf("foo_One" to 1, "foo_Two" to 2, "foo_Three" to 3),
            prefixed,
        )
    }

    @Test
    fun testAddAndStripPrefixMap() {
        val original = mapOf("name" to "Alice", "role" to "Admin")
        val prefixed = WithPrefix.addPrefix(original, "user_")
        assertEquals(mapOf("user_name" to "Alice", "user_role" to "Admin"), prefixed)

        val stripped = WithPrefix.stripPrefix(prefixed, "user_")
        assertEquals(original, stripped)
    }

    @Test
    fun testAddAndStripPrefixJsonObject() {
        val map = JsonMap.new()
        map.insert("name", Value.Str("Bob"))
        map.insert("votes", Value.Number(JsonNumber.fromI64(42L)))
        val original = Value.Object(map)

        val prefixed = WithPrefix.addPrefix(original, "player1_")
        assertEquals(2, prefixed.value.len())
        assertEquals("Bob", (prefixed.value.get("player1_name") as Value.Str).value)

        val stripped = WithPrefix.stripPrefix(prefixed, "player1_")
        assertEquals("Bob", (stripped.value.get("name") as Value.Str).value)
    }

    @Test
    fun testWithPrefixMapSerializer() {
        val adapter = WithPrefixMap<String>("foo_", valueDe = stringDe)
        val map = mapOf("a" to "1", "b" to "2")
        val wrap = SerializeAsWrap(map, adapter)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("{\"foo_a\":\"1\",\"foo_b\":\"2\"}", encoded)

        val decoded = fromStr(encoded, adapter.asDeserialize()).getOrThrow()
        assertEquals(map, decoded)
    }
}
