// port-lint: source with_prefix.rs
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
import io.github.kotlinmania.serdejson.JsonMap
import io.github.kotlinmania.serdejson.Value

/**
 * Adapter that adds a prefix onto field/map names during serialization
 * and trims away the prefix during deserialization using Serde.
 */
class WithPrefixMap<V>(
    val prefix: String,
    private val valueAs: SerializeAs<V>? = null,
    private val valueDe: DeserializeAs<V>? = null,
) : SerializeAs<Map<String, V>>,
    DeserializeAs<Map<String, V>> {
    override fun <Ok> serializeAs(source: Map<String, V>, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val mapResult = serializer.serializeMap(source.size)
        if (mapResult.isFailure) return SerdeResult.failure(mapResult.unwrapErr())
        val map: SerializeMap<Ok> = mapResult.getOrThrow()
        for ((k, v) in source) {
            val prefixedKey = "$prefix$k"
            val kRes = map.serializeKey(SerializePrimitive(prefixedKey))
            if (kRes.isFailure) return SerdeResult.failure(kRes.unwrapErr())
            val vSer =
                if (valueAs != null) {
                    SerializeAsWrap(v, valueAs)
                } else if (v is Serialize) {
                    v
                } else {
                    SerializePrimitive(v)
                }
            val vRes = map.serializeValue(vSer)
            if (vRes.isFailure) return SerdeResult.failure(vRes.unwrapErr())
        }
        return map.end()
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Map<String, V>> {
        val vde = valueDe ?: return SerdeResult.failure(SerdeError.custom("Missing value deserializer"))
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
            object : DeserializeSeed<V> {
                override fun <D> deserialize(deserializer: D): SerdeResult<V> where D : Deserializer =
                    vde.deserializeAs(deserializer)
            }

        return deserializer.deserializeMap(
            object : Visitor<Map<String, V>> {
                override fun expecting(): String = "a map"

                override fun <A> visitMap(access: A): SerdeResult<Map<String, V>> where A : MapAccess {
                    val result = mutableMapOf<String, V>()
                    while (true) {
                        val keyResult = access.nextKeySeed(stringSeed)
                        if (keyResult.isFailure) return SerdeResult.failure(keyResult.unwrapErr())
                        val key = keyResult.getOrThrow() ?: break
                        if (key.startsWith(prefix)) {
                            val valResult = access.nextValueSeed(vSeed)
                            if (valResult.isFailure) return SerdeResult.failure(valResult.unwrapErr())
                            result[key.removePrefix(prefix)] = valResult.getOrThrow()
                        } else {
                            access.nextValueSeed(IgnoreAnySeed)
                        }
                    }
                    return SerdeResult.success(result)
                }
            },
        )
    }
}

/**
 * Utility functions for manipulating prefixed key-value pairs and JSON objects.
 */
object WithPrefix {
    /**
     * Adds [prefix] to all keys in [map].
     */
    fun <V> addPrefix(map: Map<String, V>, prefix: String): Map<String, V> =
        map.mapKeys { (k, _) -> "$prefix$k" }

    /**
     * Filters and strips [prefix] from matching keys in [map].
     */
    fun <V> stripPrefix(map: Map<String, V>, prefix: String): Map<String, V> {
        val result = mutableMapOf<String, V>()
        for ((k, v) in map) {
            if (k.startsWith(prefix)) {
                result[k.removePrefix(prefix)] = v
            }
        }
        return result
    }

    /**
     * Adds [prefix] to all keys in [jsonObject].
     */
    fun addPrefix(jsonObject: Value.Object, prefix: String): Value.Object {
        val newMap = JsonMap.new()
        for ((k, v) in jsonObject.value) {
            newMap.insert("$prefix$k", v)
        }
        return Value.Object(newMap)
    }

    /**
     * Filters and strips [prefix] from matching keys in [jsonObject].
     */
    fun stripPrefix(jsonObject: Value.Object, prefix: String): Value.Object {
        val newMap = JsonMap.new()
        for ((k, v) in jsonObject.value) {
            if (k.startsWith(prefix)) {
                newMap.insert(k.removePrefix(prefix), v)
            }
        }
        return Value.Object(newMap)
    }
}
