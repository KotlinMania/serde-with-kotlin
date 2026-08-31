// port-lint: tests serde_with/src/utils/duration.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class DurationTest {
    @Test
    fun testDurationSecondsSerializer() {
        val json = Json { prettyPrint = false }
        val duration = 42.seconds
        val encoded = json.encodeToString(DurationSecondsSerializer, duration)
        assertEquals("42", encoded)

        val decoded = json.decodeFromString(DurationSecondsSerializer, encoded)
        assertEquals(duration, decoded)
    }

    @Test
    fun testDurationMillisSerializer() {
        val json = Json { prettyPrint = false }
        val duration = 1500.milliseconds
        val encoded = json.encodeToString(DurationMillisSerializer, duration)
        assertEquals("1500", encoded)

        val decoded = json.decodeFromString(DurationMillisSerializer, encoded)
        assertEquals(duration, decoded)
    }

    @Test
    fun testTimestampSecondsSerializer() {
        val json = Json { prettyPrint = false }
        val instant = Instant.fromEpochSeconds(1609459200)
        val encoded = json.encodeToString(TimestampSecondsSerializer, instant)
        assertEquals("1609459200", encoded)

        val decoded = json.decodeFromString(TimestampSecondsSerializer, encoded)
        assertEquals(instant, decoded)
    }

    @Test
    fun testTimestampMillisSerializer() {
        val json = Json { prettyPrint = false }
        val instant = Instant.fromEpochMilliseconds(1609459200123L)
        val encoded = json.encodeToString(TimestampMillisSerializer, instant)
        assertEquals("1609459200123", encoded)

        val decoded = json.decodeFromString(TimestampMillisSerializer, encoded)
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
