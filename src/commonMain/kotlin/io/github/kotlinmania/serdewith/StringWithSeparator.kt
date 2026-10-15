package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serde.serdeCatching
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Serializes and deserializes a [List] as a single delimited string using Serde.
 */
class StringWithSeparator<T>(
    private val separator: Separator,
    private val itemToString: (T) -> String = { it.toString() },
    private val stringToItem: (String) -> T,
) : SerializeAs<List<T>>,
    DeserializeAs<List<T>> {
    override fun <Ok> serializeAs(source: List<T>, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val str = source.joinToString(separator = separator.separator()) { itemToString(it) }
        return serializer.serializeStr(str)
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<List<T>> =
        deserializer.deserializeStr(
            object : Visitor<List<T>> {
                override fun expecting(): String = "a delimited string"

                override fun visitStr(v: String): SerdeResult<List<T>> =
                    serdeCatching {
                        if (v.isEmpty()) {
                            emptyList()
                        } else {
                            val sep = separator.separator()
                            v.split(sep).map { stringToItem(it) }
                        }
                    }
            },
        )
}

/** Predefined separator adapters for lists of strings. */
object CommaSeparatedStringList :
    SerializeAs<List<String>> by StringWithSeparator(
        separator = CommaSeparator,
        stringToItem = { it },
    ),
    DeserializeAs<List<String>> by StringWithSeparator(
        separator = CommaSeparator,
        stringToItem = { it },
    )

object SpaceSeparatedStringList :
    SerializeAs<List<String>> by StringWithSeparator(
        separator = SpaceSeparator,
        stringToItem = { it },
    ),
    DeserializeAs<List<String>> by StringWithSeparator(
        separator = SpaceSeparator,
        stringToItem = { it },
    )
