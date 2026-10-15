package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.DeserializeSeed
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.SeqAccess
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.SerializeSeq
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Deserializes either a single value or a sequence into a [List], and serializes according to [Format].
 */
class OneOrMany<T>(
    private val elementAs: SerializeAs<T>,
    private val elementDe: DeserializeAs<T>,
    private val format: Format = PreferOne,
) : SerializeAs<List<T>>,
    DeserializeAs<List<T>> {
    override fun <Ok> serializeAs(source: List<T>, serializer: Serializer<Ok>): SerdeResult<Ok> {
        if (format is PreferOne && source.size == 1) {
            return elementAs.serializeAs(source[0], serializer)
        }
        val seqResult = serializer.serializeSeq(source.size)
        if (seqResult.isFailure) return SerdeResult.failure(seqResult.unwrapErr())
        val seq: SerializeSeq<Ok> = seqResult.getOrThrow()
        for (item in source) {
            val wrap = SerializeAsWrap(item, elementAs)
            val elemResult = seq.serializeElement(wrap)
            if (elemResult.isFailure) return SerdeResult.failure(elemResult.unwrapErr())
        }
        return seq.end()
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<List<T>> {
        val seed =
            object : DeserializeSeed<T> {
                override fun <D> deserialize(deserializer: D): SerdeResult<T> where D : Deserializer =
                    elementDe.deserializeAs(deserializer)
            }

        return deserializer.deserializeAny(
            object : Visitor<List<T>> {
                override fun expecting(): String = "a sequence or a single value"

                override fun <A> visitSeq(access: A): SerdeResult<List<T>> where A : SeqAccess {
                    val list = mutableListOf<T>()
                    while (true) {
                        val elemResult = access.nextElementSeed(seed)
                        if (elemResult.isFailure) return SerdeResult.failure(elemResult.unwrapErr())
                        val elem = elemResult.getOrThrow() ?: break
                        list.add(elem)
                    }
                    return SerdeResult.success(list)
                }

                override fun visitStr(v: String): SerdeResult<List<T>> = singleFromStr(v)

                override fun visitString(v: String): SerdeResult<List<T>> = singleFromStr(v)

                override fun visitBorrowedStr(v: String): SerdeResult<List<T>> = singleFromStr(v)

                private fun singleFromStr(s: String): SerdeResult<List<T>> {
                    val parser =
                        io.github.kotlinmania.serdejson.JsonParser(
                            if (s.startsWith("\"") && s.endsWith("\"")) s else "\"$s\"",
                        )
                    val d =
                        io.github.kotlinmania.serdejson
                            .JsonDeserializer(parser)
                    return elementDe.deserializeAs(d).map { listOf(it) }
                }

                override fun visitI64(v: Long): SerdeResult<List<T>> = singleFromStr(v.toString())

                override fun visitU64(v: ULong): SerdeResult<List<T>> = singleFromStr(v.toString())

                override fun visitF64(v: Double): SerdeResult<List<T>> = singleFromStr(v.toString())

                override fun visitBool(v: Boolean): SerdeResult<List<T>> = singleFromStr(v.toString())
            },
        )
    }
}
