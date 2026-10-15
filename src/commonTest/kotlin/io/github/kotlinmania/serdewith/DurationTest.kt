// port-lint: tests utils.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class DurationTest {
    @Test
    fun testDurationSecondsSerializer() {
        val duration = 42.seconds
        val wrap = SerializeAsWrap(duration, DurationSeconds)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("42", encoded)

        val decoded = fromStr(encoded, DurationSeconds.asDeserialize()).getOrThrow()
        assertEquals(duration, decoded)
    }

    @Test
    fun testDurationMillisSerializer() {
        val duration = 1500.milliseconds
        val wrap = SerializeAsWrap(duration, DurationMillis)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("1500", encoded)

        val decoded = fromStr(encoded, DurationMillis.asDeserialize()).getOrThrow()
        assertEquals(duration, decoded)
    }

    @Test
    fun testTimestampSecondsSerializer() {
        val instant = Instant.fromEpochSeconds(1609459200)
        val wrap = SerializeAsWrap(instant, TimestampSeconds)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("1609459200", encoded)

        val decoded = fromStr(encoded, TimestampSeconds.asDeserialize()).getOrThrow()
        assertEquals(instant, decoded)
    }

    @Test
    fun testTimestampMillisSerializer() {
        val instant = Instant.fromEpochMilliseconds(1609459200123L)
        val wrap = SerializeAsWrap(instant, TimestampMillis)
        val encoded = toStr(wrap).getOrThrow()
        assertEquals("1609459200123", encoded)

        val decoded = fromStr(encoded, TimestampMillis.asDeserialize()).getOrThrow()
        assertEquals(instant, decoded)
    }

    @Test
    fun testParseFloatIntoTimeParts() {
        // Test normal behavior
        assertEquals(
            TimeParts(Sign.Positive, 123u, 456_000_000u),
            parseFloatIntoTimeParts("+123.456").getOrThrow(),
        )
        assertEquals(
            TimeParts(Sign.Negative, 123u, 987_000u),
            parseFloatIntoTimeParts("-123.000987").getOrThrow(),
        )
        assertEquals(
            TimeParts(Sign.Positive, 18446744073709551615uL, 123_456_789u),
            parseFloatIntoTimeParts("18446744073709551615.123456789").getOrThrow(),
        )

        // Test behavior around 0
        assertEquals(
            TimeParts(Sign.Positive, 0u, 456_000_000u),
            parseFloatIntoTimeParts("+0.456").getOrThrow(),
        )
        assertEquals(
            TimeParts(Sign.Negative, 0u, 987_000u),
            parseFloatIntoTimeParts("-0.000987").getOrThrow(),
        )
        assertEquals(
            TimeParts(Sign.Positive, 0u, 123_456_789u),
            parseFloatIntoTimeParts("0.123456789").getOrThrow(),
        )
    }
}
