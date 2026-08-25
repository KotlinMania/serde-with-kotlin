// port-lint: tests serde_as/enum_map.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
data class NamedItem(
    val name: String,
    val count: Int,
)

class EnumMapTest {
    @Test
    fun jsonRoundTrip() {
        val serializer =
            TaggedListAsMapSerializer(
                NamedItem.serializer(),
                tagExtractor = { it.name },
                valueFactory = { tag, item -> item.copy(name = tag) },
            )
        val json = Json { prettyPrint = false }

        val items =
            listOf(
                NamedItem("apple", 5),
                NamedItem("banana", 10),
            )

        val encoded = json.encodeToString(serializer, items)
        assertEquals("{\"apple\":{\"name\":\"apple\",\"count\":5},\"banana\":{\"name\":\"banana\",\"count\":10}}", encoded)

        val decoded = json.decodeFromString(serializer, encoded)
        assertEquals(items, decoded)
    }

    @Test
    fun testTaggedListAsMapSerializer() {
        val serializer =
            TaggedListAsMapSerializer(
                NamedItem.serializer(),
                tagExtractor = { it.name },
                valueFactory = { tag, item -> item.copy(name = tag) },
            )
        val json = Json { prettyPrint = false }

        val items =
            listOf(
                NamedItem("apple", 5),
                NamedItem("banana", 10),
            )

        val encoded = json.encodeToString(serializer, items)
        assertEquals("{\"apple\":{\"name\":\"apple\",\"count\":5},\"banana\":{\"name\":\"banana\",\"count\":10}}", encoded)

        val decoded = json.decodeFromString(serializer, encoded)
        assertEquals(items, decoded)
    }
}
