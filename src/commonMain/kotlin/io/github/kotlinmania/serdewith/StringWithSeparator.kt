// port-lint: source lib.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializes and deserializes a [List] as a single delimited string.
 */
class StringListWithSeparatorSerializer<T>(
    private val separator: Separator,
    private val itemToString: (T) -> String = { it.toString() },
    private val stringToItem: (String) -> T,
) : KSerializer<List<T>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.StringListWithSeparator", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: List<T>) {
        val str = value.joinToString(separator = separator.separator()) { itemToString(it) }
        encoder.encodeString(str)
    }

    override fun deserialize(decoder: Decoder): List<T> {
        val text = decoder.decodeString()
        if (text.isEmpty()) return emptyList()
        val sep = separator.separator()
        val parts = text.split(sep)
        return parts.map { stringToItem(it) }
    }
}

/**
 * Serializes a list of strings separated by comma.
 */
object CommaSeparatedStringListSerializer : KSerializer<List<String>> by StringListWithSeparatorSerializer(
    separator = CommaSeparator,
    stringToItem = { it },
)

/**
 * Serializes a list of strings separated by space.
 */
object SpaceSeparatedStringListSerializer : KSerializer<List<String>> by StringListWithSeparatorSerializer(
    separator = SpaceSeparator,
    stringToItem = { it },
)
