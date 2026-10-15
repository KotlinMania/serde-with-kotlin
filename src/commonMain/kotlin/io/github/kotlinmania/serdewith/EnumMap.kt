// port-lint: source enum_map.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.DeserializeSeed
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.MapAccess
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.SerializeMap
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Serializes a list of values as a map keyed by a string tag using Serde.
 */
class TaggedListAsMap<T>(
    private val tagExtractor: (T) -> String,
    private val elementAs: SerializeAs<T>? = null,
    private val elementDe: DeserializeAs<T>? = null,
    private val valueFactory: (String, T) -> T = { _, v -> v },
) : SerializeAs<List<T>>,
    DeserializeAs<List<T>> {
    override fun <Ok> serializeAs(source: List<T>, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val mapResult = serializer.serializeMap(source.size)
        if (mapResult.isFailure) return SerdeResult.failure(mapResult.unwrapErr())
        val map: SerializeMap<Ok> = mapResult.getOrThrow()
        for (item in source) {
            val key = tagExtractor(item)
            val kRes = map.serializeKey(SerializePrimitive(key))
            if (kRes.isFailure) return SerdeResult.failure(kRes.unwrapErr())
            val vSer =
                if (elementAs != null) {
                    SerializeAsWrap(item, elementAs)
                } else if (item is Serialize) {
                    item
                } else {
                    SerializePrimitive(item)
                }
            val vRes = map.serializeValue(vSer)
            if (vRes.isFailure) return SerdeResult.failure(vRes.unwrapErr())
        }
        return map.end()
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<List<T>> =
        deserializer.deserializeMap(
            object : Visitor<List<T>> {
                override fun expecting(): String = "a map keyed by string tags"

                override fun <A> visitMap(access: A): SerdeResult<List<T>> where A : MapAccess {
                    val result = mutableListOf<T>()
                    val de = elementDe ?: return SerdeResult.failure(SerdeError("Missing element deserializer"))

                    val stringSeed =
                        object : DeserializeSeed<String> {
                            override fun <D> deserialize(deserializer: D): SerdeResult<String> where D : Deserializer =
                                deserializer.deserializeStr(
                                    object : Visitor<String> {
                                        override fun expecting(): String = "a string key"

                                        override fun visitStr(v: String): SerdeResult<String> = SerdeResult.success(v)
                                    },
                                )
                        }

                    val vSeed =
                        object : DeserializeSeed<T> {
                            override fun <D> deserialize(deserializer: D): SerdeResult<T> where D : Deserializer =
                                de.deserializeAs(deserializer)
                        }

                    while (true) {
                        val keyResult = access.nextKeySeed(stringSeed)
                        if (keyResult.isFailure) return SerdeResult.failure(keyResult.unwrapErr())
                        val key = keyResult.getOrThrow() ?: break
                        val valResult = access.nextValueSeed(vSeed)
                        if (valResult.isFailure) return SerdeResult.failure(valResult.unwrapErr())
                        val value = valResult.getOrThrow()
                        result.add(valueFactory(key, value))
                    }
                    return SerdeResult.success(result)
                }
            },
        )
}
