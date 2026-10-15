package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * A trait for custom serialization of a type [T].
 */
interface SerializeAs<in T> {
    fun <Ok> serializeAs(source: T, serializer: Serializer<Ok>): SerdeResult<Ok>
}

/**
 * A trait for custom deserialization into a type [T].
 */
interface DeserializeAs<out T> {
    fun deserializeAs(deserializer: Deserializer): SerdeResult<T>
}

/**
 * Helper to adapt a [value] with a custom [asSerializer] to [Serialize].
 */
class SerializeAsWrap<T, U : SerializeAs<T>>(
    val value: T,
    val asSerializer: U,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
        asSerializer.serializeAs(value, serializer)
}

/**
 * Helper container for deserialized values.
 */
class DeserializeAsWrap<out T>(
    val value: T,
)

/**
 * Adapter from [DeserializeAs] to Serde's [io.github.kotlinmania.serdecore.de.Deserialize].
 */
class AsDeserialize<T>(
    private val de: DeserializeAs<T>,
) : io.github.kotlinmania.serdecore.de.Deserialize<T> {
    override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<T> =
        de.deserializeAs(deserializer)
}

fun <T> DeserializeAs<T>.asDeserialize(): io.github.kotlinmania.serdecore.de.Deserialize<T> =
    AsDeserialize(this)

/**
 * Identity adapter that uses the type's standard Serialize/Deserialize implementation.
 */
object Same : SerializeAs<Serialize> {
    override fun <Ok> serializeAs(source: Serialize, serializer: Serializer<Ok>): SerdeResult<Ok> =
        source.serialize(serializer)
}

/**
 * Utility functions for invoking [SerializeAs] and [DeserializeAs].
 */
object As {
    fun <Ok, I> serialize(value: I, serializer: Serializer<Ok>, asSerializer: SerializeAs<I>): SerdeResult<Ok> =
        asSerializer.serializeAs(value, serializer)

    fun <I> deserialize(deserializer: Deserializer, asDeserializer: DeserializeAs<I>): SerdeResult<I> =
        asDeserializer.deserializeAs(deserializer)
}

internal fun <T> SerdeResult<T>.unwrapErr(): io.github.kotlinmania.serde.SerdeError =
    (this as? SerdeResult.Failure)?.error ?: io.github.kotlinmania.serde
        .SerdeError("Called unwrapErr on Success: $this")

internal object IgnoreAnySeed : io.github.kotlinmania.serdecore.de.DeserializeSeed<Unit> {
    override fun <D> deserialize(deserializer: D): SerdeResult<Unit> where D : Deserializer =
        deserializer.deserializeAny(
            object : io.github.kotlinmania.serdecore.de.Visitor<Unit> {
                override fun expecting(): String = "any value"

                override fun visitBool(v: Boolean): SerdeResult<Unit> = SerdeResult.success(Unit)

                override fun visitI64(v: Long): SerdeResult<Unit> = SerdeResult.success(Unit)

                override fun visitU64(v: ULong): SerdeResult<Unit> = SerdeResult.success(Unit)

                override fun visitF64(v: Double): SerdeResult<Unit> = SerdeResult.success(Unit)

                override fun visitStr(v: String): SerdeResult<Unit> = SerdeResult.success(Unit)

                override fun visitNone(): SerdeResult<Unit> = SerdeResult.success(Unit)

                override fun visitUnit(): SerdeResult<Unit> = SerdeResult.success(Unit)

                override fun <A> visitSeq(access: A): SerdeResult<Unit> where A : io.github.kotlinmania.serdecore.de.SeqAccess {
                    while (true) {
                        access.nextElementSeed(IgnoreAnySeed).getOrNull() ?: break
                    }
                    return SerdeResult.success(Unit)
                }

                override fun <A> visitMap(access: A): SerdeResult<Unit> where A : io.github.kotlinmania.serdecore.de.MapAccess {
                    while (true) {
                        access.nextKeySeed(IgnoreAnySeed).getOrNull() ?: break
                        access.nextValueSeed(IgnoreAnySeed).getOrNull() ?: break
                    }
                    return SerdeResult.success(Unit)
                }
            },
        )
}
