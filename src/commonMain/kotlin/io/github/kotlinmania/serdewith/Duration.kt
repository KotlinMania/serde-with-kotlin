package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serializer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.Instant

/**
 * Serializes [Duration] as integer seconds.
 */
object DurationSeconds : SerializeAs<Duration>, DeserializeAs<Duration> {
    override fun <Ok> serializeAs(source: Duration, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeI64(source.inWholeSeconds)

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Duration> =
        deserializer.deserializeI64(
            object : Visitor<Duration> {
                override fun expecting(): String = "integer seconds"

                override fun visitI64(v: Long): SerdeResult<Duration> = SerdeResult.success(v.seconds)

                override fun visitU64(v: ULong): SerdeResult<Duration> = SerdeResult.success(v.toLong().seconds)
            },
        )
}

/**
 * Serializes [Duration] as fractional seconds [Double].
 */
object DurationSecondsWithFrac : SerializeAs<Duration>, DeserializeAs<Duration> {
    override fun <Ok> serializeAs(source: Duration, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeF64(source.toDouble(DurationUnit.SECONDS))

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Duration> =
        deserializer.deserializeF64(
            object : Visitor<Duration> {
                override fun expecting(): String = "floating point or integer seconds"

                override fun visitF64(v: Double): SerdeResult<Duration> =
                    SerdeResult.success((v * 1_000_000_000.0).toLong().nanoseconds)

                override fun visitI64(v: Long): SerdeResult<Duration> = SerdeResult.success(v.seconds)

                override fun visitU64(v: ULong): SerdeResult<Duration> = SerdeResult.success(v.toLong().seconds)
            },
        )
}

/**
 * Serializes [Duration] as milliseconds [Long].
 */
object DurationMillis : SerializeAs<Duration>, DeserializeAs<Duration> {
    override fun <Ok> serializeAs(source: Duration, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeI64(source.inWholeMilliseconds)

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Duration> =
        deserializer.deserializeI64(
            object : Visitor<Duration> {
                override fun expecting(): String = "integer milliseconds"

                override fun visitI64(v: Long): SerdeResult<Duration> = SerdeResult.success(v.milliseconds)

                override fun visitU64(v: ULong): SerdeResult<Duration> = SerdeResult.success(v.toLong().milliseconds)
            },
        )
}

/**
 * Serializes [Duration] as microseconds [Long].
 */
object DurationMicros : SerializeAs<Duration>, DeserializeAs<Duration> {
    override fun <Ok> serializeAs(source: Duration, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeI64(source.inWholeMicroseconds)

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Duration> =
        deserializer.deserializeI64(
            object : Visitor<Duration> {
                override fun expecting(): String = "integer microseconds"

                override fun visitI64(v: Long): SerdeResult<Duration> = SerdeResult.success(v.microseconds)

                override fun visitU64(v: ULong): SerdeResult<Duration> = SerdeResult.success(v.toLong().microseconds)
            },
        )
}

/**
 * Serializes [Duration] as nanoseconds [Long].
 */
object DurationNanos : SerializeAs<Duration>, DeserializeAs<Duration> {
    override fun <Ok> serializeAs(source: Duration, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeI64(source.inWholeNanoseconds)

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Duration> =
        deserializer.deserializeI64(
            object : Visitor<Duration> {
                override fun expecting(): String = "integer nanoseconds"

                override fun visitI64(v: Long): SerdeResult<Duration> = SerdeResult.success(v.nanoseconds)

                override fun visitU64(v: ULong): SerdeResult<Duration> = SerdeResult.success(v.toLong().nanoseconds)
            },
        )
}

/**
 * Serializes [Instant] as integer seconds since Unix epoch.
 */
object TimestampSeconds : SerializeAs<Instant>, DeserializeAs<Instant> {
    override fun <Ok> serializeAs(source: Instant, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeI64(source.epochSeconds)

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Instant> =
        deserializer.deserializeI64(
            object : Visitor<Instant> {
                override fun expecting(): String = "integer seconds since epoch"

                override fun visitI64(v: Long): SerdeResult<Instant> = SerdeResult.success(Instant.fromEpochSeconds(v))

                override fun visitU64(v: ULong): SerdeResult<Instant> =
                    SerdeResult.success(Instant.fromEpochSeconds(v.toLong()))
            },
        )
}

/**
 * Serializes [Instant] as fractional seconds [Double] since Unix epoch.
 */
object TimestampSecondsWithFrac : SerializeAs<Instant>, DeserializeAs<Instant> {
    override fun <Ok> serializeAs(source: Instant, serializer: Serializer<Ok>): SerdeResult<Ok> {
        val secs = source.epochSeconds.toDouble() + (source.nanosecondsOfSecond.toDouble() / 1_000_000_000.0)
        return serializer.serializeF64(secs)
    }

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Instant> =
        deserializer.deserializeF64(
            object : Visitor<Instant> {
                override fun expecting(): String = "floating point or integer seconds since epoch"

                override fun visitF64(v: Double): SerdeResult<Instant> {
                    val wholeSecs = v.toLong()
                    val nano = ((v - wholeSecs.toDouble()) * 1_000_000_000.0).toLong()
                    return SerdeResult.success(Instant.fromEpochSeconds(wholeSecs, nano))
                }

                override fun visitI64(v: Long): SerdeResult<Instant> = SerdeResult.success(Instant.fromEpochSeconds(v))

                override fun visitU64(v: ULong): SerdeResult<Instant> =
                    SerdeResult.success(Instant.fromEpochSeconds(v.toLong()))
            },
        )
}

/**
 * Serializes [Instant] as milliseconds since Unix epoch.
 */
object TimestampMillis : SerializeAs<Instant>, DeserializeAs<Instant> {
    override fun <Ok> serializeAs(source: Instant, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeI64(source.toEpochMilliseconds())

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Instant> =
        deserializer.deserializeI64(
            object : Visitor<Instant> {
                override fun expecting(): String = "integer milliseconds since epoch"

                override fun visitI64(v: Long): SerdeResult<Instant> =
                    SerdeResult.success(Instant.fromEpochMilliseconds(v))

                override fun visitU64(v: ULong): SerdeResult<Instant> =
                    SerdeResult.success(Instant.fromEpochMilliseconds(v.toLong()))
            },
        )
}

/**
 * Sign of a duration or timestamp.
 */
enum class Sign {
    Positive,
    Negative,
}

/**
 * Error kinds for float parsing errors.
 */
enum class ParseFloatErrorKind {
    INVALID_VALUE,
    CUSTOM,
}

/**
 * Error during parsing floating-point seconds, strongly typed for Swift Export.
 */
class ParseFloatError(
    val kind: ParseFloatErrorKind,
    message: String,
) : Exception(message) {
    companion object {
        fun invalidValue(): ParseFloatError =
            ParseFloatError(ParseFloatErrorKind.INVALID_VALUE, "invalid value")

        fun custom(message: String): ParseFloatError =
            ParseFloatError(ParseFloatErrorKind.CUSTOM, message)
    }
}

/**
 * Flat result container for float parsing, satisfying SWIFT.md.
 */
class ParseFloatResult<out T> private constructor(
    val value: T?,
    val error: ParseFloatError?,
) {
    init {
        require((value == null) != (error == null)) {
            "ParseFloatResult must have exactly one of value or error"
        }
    }

    fun isSuccess(): Boolean = value != null

    fun isFailure(): Boolean = error != null

    fun getOrNull(): T? = value

    fun unwrapErr(): ParseFloatError = error ?: throw IllegalStateException("Expected error, got value: $value")

    fun getOrThrow(): T = value ?: throw error!!

    companion object {
        fun <T> success(value: T): ParseFloatResult<T> = ParseFloatResult(value, null)

        fun <T> failure(error: ParseFloatError): ParseFloatResult<T> = ParseFloatResult(null, error)
    }
}

/**
 * Deconstructed time parts.
 */
data class TimeParts(
    val sign: Sign,
    val seconds: ULong,
    val subseconds: UInt,
)

/**
 * Parses a float string into sign, seconds, and subsecond nanoseconds.
 */
fun parseFloatIntoTimeParts(valueStr: String): ParseFloatResult<TimeParts> {
    var value = valueStr
    val sign =
        when {
            value.startsWith('+') -> {
                value = value.substring(1)
                Sign.Positive
            }

            value.startsWith('-') -> {
                value = value.substring(1)
                Sign.Negative
            }

            else -> {
                Sign.Positive
            }
        }

    val parts = value.split('.')
    return when (parts.size) {
        1 -> {
            val seconds = parts[0].toULongOrNull() ?: return ParseFloatResult.failure(ParseFloatError.invalidValue())
            ParseFloatResult.success(TimeParts(sign, seconds, 0u))
        }

        2 -> {
            val seconds = parts[0].toULongOrNull() ?: return ParseFloatResult.failure(ParseFloatError.invalidValue())
            val subsecondsStr = parts[1]
            if (subsecondsStr.length > 9) {
                return ParseFloatResult.failure(
                    ParseFloatError.custom(
                        "Duration and Timestamps with no more than 9 digits precision, but '$valueStr' has more",
                    ),
                )
            }
            var subseconds =
                subsecondsStr.toUIntOrNull()
                    ?: return ParseFloatResult.failure(ParseFloatError.invalidValue())
            var multiplier = 1u
            repeat(9 - subsecondsStr.length) {
                multiplier *= 10u
            }
            subseconds *= multiplier
            ParseFloatResult.success(TimeParts(sign, seconds, subseconds))
        }

        else -> {
            ParseFloatResult.failure(ParseFloatError.invalidValue())
        }
    }
}
