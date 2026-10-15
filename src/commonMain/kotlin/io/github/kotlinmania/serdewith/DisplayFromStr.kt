package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serde.serdeCatching
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Serializes and deserializes values via their string representations using Serde.
 */
open class DisplayFromStr<T>(
    private val fromStr: (String) -> T,
    private val toStr: (T) -> String = { it.toString() },
) : SerializeAs<T>,
    DeserializeAs<T> {
    override fun <Ok> serializeAs(source: T, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeStr(toStr(source))

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<T> =
        deserializer.deserializeStr(
            object : Visitor<T> {
                override fun expecting(): String = "a string"

                override fun visitStr(v: String): SerdeResult<T> =
                    serdeCatching {
                        fromStr(v)
                    }
            },
        )
}

/** Predefined DisplayFromStr adapters for standard types. */
object IntAsString : DisplayFromStr<Int>({ it.toInt() })

object LongAsString : DisplayFromStr<Long>({ it.toLong() })

object ShortAsString : DisplayFromStr<Short>({ it.toShort() })

object ByteAsString : DisplayFromStr<Byte>({ it.toByte() })

object DoubleAsString : DisplayFromStr<Double>({ it.toDouble() })

object FloatAsString : DisplayFromStr<Float>({ it.toFloat() })

object BooleanAsString : DisplayFromStr<Boolean>({ it.toBooleanStrict() })

object UIntAsString : DisplayFromStr<UInt>({ it.toUInt() })

object ULongAsString : DisplayFromStr<ULong>({ it.toULong() })

object UShortAsString : DisplayFromStr<UShort>({ it.toUShort() })

object UByteAsString : DisplayFromStr<UByte>({ it.toUByte() })
