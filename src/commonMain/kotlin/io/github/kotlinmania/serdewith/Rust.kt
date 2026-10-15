// port-lint: source rust.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Tri-state optional representation distinguishing between:
 * - [Missing]: Field was completely absent from payload
 * - [Null]: Field was explicitly set to null
 * - [Value]: Field contained a valid value
 */
sealed class DoubleOption<out T> {
    object Missing : DoubleOption<Nothing>()

    object Null : DoubleOption<Nothing>()

    data class Value<out T>(
        val value: T,
    ) : DoubleOption<T>()

    val isMissing: Boolean get() = this is Missing
    val isNull: Boolean get() = this is Null
    val isValue: Boolean get() = this is Value

    fun valueOrNull(): T? =
        when (this) {
            is Value -> value
            else -> null
        }
}

/**
 * Adapter for [DoubleOption] using Serde.
 */
class DoubleOptionAdapter<T>(
    private val elementAs: SerializeAs<T>? = null,
    private val elementDe: DeserializeAs<T>? = null,
) : SerializeAs<DoubleOption<T>>,
    DeserializeAs<DoubleOption<T>> {
    override fun <Ok> serializeAs(source: DoubleOption<T>, serializer: Serializer<Ok>): SerdeResult<Ok> =
        when (source) {
            is DoubleOption.Missing -> {
                serializer.serializeNone()
            }

            is DoubleOption.Null -> {
                serializer.serializeNone()
            }

            is DoubleOption.Value -> {
                if (elementAs != null) {
                    elementAs.serializeAs(source.value, serializer)
                } else if (source.value is Serialize) {
                    source.value.serialize(serializer)
                } else {
                    SerializePrimitive(source.value).serialize(serializer)
                }
            }
        }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<DoubleOption<T>> =
        deserializer.deserializeOption(
            object : Visitor<DoubleOption<T>> {
                override fun expecting(): String = "an optional value"

                override fun visitNone(): SerdeResult<DoubleOption<T>> =
                    SerdeResult.success(DoubleOption.Null)

                override fun <D> visitSome(deserializer: D): SerdeResult<DoubleOption<T>> where D : Deserializer {
                    val de = elementDe
                    return if (de != null) {
                        de.deserializeAs(deserializer).map { DoubleOption.Value(it) }
                    } else {
                        SerdeResult.success(DoubleOption.Null)
                    }
                }
            },
        )
}

/**
 * Adapter that deserializes any value, ignores it, and returns the given default value.
 */
class DeserializeIgnoreAny<T>(
    private val defaultValue: () -> T,
) : DeserializeAs<T> {
    override fun deserializeAs(deserializer: Deserializer): SerdeResult<T> =
        IgnoreAnySeed.deserialize(deserializer).map { defaultValue() }
}
