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
