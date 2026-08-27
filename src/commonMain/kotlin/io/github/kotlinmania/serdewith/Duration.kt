// port-lint: source utils/duration.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
object DurationSecondsSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.DurationSeconds", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeSeconds)
    }

    override fun deserialize(decoder: Decoder): Duration = decoder.decodeLong().seconds
}

/**
 * Serializes [Duration] as fractional seconds [Double].
 */
object DurationSecondsWithFracSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.DurationSecondsWithFrac", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeDouble(value.toDouble(DurationUnit.SECONDS))
    }

    override fun deserialize(decoder: Decoder): Duration {
        val secs = decoder.decodeDouble()
        return (secs * 1_000_000_000.0).toLong().nanoseconds
    }
}

/**
 * Serializes [Duration] as milliseconds [Long].
 */
object DurationMillisSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.DurationMillis", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeMilliseconds)
    }

    override fun deserialize(decoder: Decoder): Duration = decoder.decodeLong().milliseconds
}

/**
 * Serializes [Duration] as microseconds [Long].
 */
object DurationMicrosSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.DurationMicros", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeMicroseconds)
    }

    override fun deserialize(decoder: Decoder): Duration = decoder.decodeLong().microseconds
}

/**
 * Serializes [Duration] as nanoseconds [Long].
 */
object DurationNanosSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.DurationNanos", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeNanoseconds)
    }

    override fun deserialize(decoder: Decoder): Duration = decoder.decodeLong().nanoseconds
}

/**
 * Serializes [Instant] as integer seconds since Unix epoch.
 */
object TimestampSecondsSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.TimestampSeconds", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.epochSeconds)
    }

    override fun deserialize(decoder: Decoder): Instant = Instant.fromEpochSeconds(decoder.decodeLong())
}

/**
 * Serializes [Instant] as fractional seconds [Double] since Unix epoch.
 */
object TimestampSecondsWithFracSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.TimestampSecondsWithFrac", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: Instant) {
        val secs = value.epochSeconds.toDouble() + (value.nanosecondsOfSecond.toDouble() / 1_000_000_000.0)
        encoder.encodeDouble(secs)
    }

    override fun deserialize(decoder: Decoder): Instant {
        val secs = decoder.decodeDouble()
        val wholeSecs = secs.toLong()
        val nano = ((secs - wholeSecs.toDouble()) * 1_000_000_000.0).toLong()
        return Instant.fromEpochSeconds(wholeSecs, nano)
    }
}

/**
 * Serializes [Instant] as milliseconds since Unix epoch.
 */
object TimestampMillisSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.serdewith.TimestampMillis", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.toEpochMilliseconds())
    }

    override fun deserialize(decoder: Decoder): Instant = Instant.fromEpochMilliseconds(decoder.decodeLong())
}

/**
 * Sign of a duration or timestamp.
 */
enum class Sign {
    Positive,
    Negative,
}

/**
 * Error during parsing floating-point seconds.
 */
sealed class ParseFloatError : Exception() {
    data object InvalidValue : ParseFloatError()

    data class Custom(
        override val message: String,
    ) : ParseFloatError()
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
fun parseFloatIntoTimeParts(valueStr: String): Result<TimeParts> {
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
            else -> Sign.Positive
        }

    val parts = value.split('.')
    return when (parts.size) {
        1 -> {
            val seconds = parts[0].toULongOrNull() ?: return Result.failure(ParseFloatError.InvalidValue)
            Result.success(TimeParts(sign, seconds, 0u))
        }
        2 -> {
            val seconds = parts[0].toULongOrNull() ?: return Result.failure(ParseFloatError.InvalidValue)
            val subsecondsStr = parts[1]
            if (subsecondsStr.length > 9) {
                return Result.failure(
                    ParseFloatError.Custom(
                        "Duration and Timestamps with no more than 9 digits precision, but '$valueStr' has more",
                    ),
                )
            }
            var subseconds = subsecondsStr.toUIntOrNull() ?: return Result.failure(ParseFloatError.InvalidValue)
            var multiplier = 1u
            for (i in 0 until (9 - subsecondsStr.length)) {
                multiplier *= 10u
            }
            subseconds *= multiplier
            Result.success(TimeParts(sign, seconds, subseconds))
        }
        else -> Result.failure(ParseFloatError.InvalidValue)
    }
}
