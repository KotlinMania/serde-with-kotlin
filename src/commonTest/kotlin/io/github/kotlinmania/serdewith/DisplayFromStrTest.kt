// port-lint: tests serde_as/lib.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class DisplayFromStrTest {
    @Serializable
    data class Sample(
        @Serializable(with = IntAsStringSerializer::class)
        val intVal: Int,
        @Serializable(with = LongAsStringSerializer::class)
        val longVal: Long,
        @Serializable(with = DoubleAsStringSerializer::class)
        val doubleVal: Double,
        @Serializable(with = BooleanAsStringSerializer::class)
        val boolVal: Boolean,
        @Serializable(with = ShortAsStringSerializer::class)
        val shortVal: Short,
        @Serializable(with = ByteAsStringSerializer::class)
        val byteVal: Byte,
        @Serializable(with = UIntAsStringSerializer::class)
        val uintVal: UInt,
        @Serializable(with = ULongAsStringSerializer::class)
        val ulongVal: ULong,
    )

    @Test
    fun testDisplayFromstr() {
        val sample =
            Sample(
                intVal = 123,
                longVal = 456789012345L,
                doubleVal = 3.1415,
                boolVal = true,
                shortVal = 42.toShort(),
                byteVal = 7.toByte(),
                uintVal = 99u,
                ulongVal = 123456789uL,
            )

        val json = Json.encodeToString(Sample.serializer(), sample)
        val decoded = Json.decodeFromString(Sample.serializer(), json)
        assertEquals(sample, decoded)
    }

    @Test
    fun testSerializationAndDeserialization() {
        val sample =
            Sample(
                intVal = 123,
                longVal = 456789012345L,
                doubleVal = 3.1415,
                boolVal = true,
                shortVal = 42.toShort(),
                byteVal = 7.toByte(),
                uintVal = 99u,
                ulongVal = 123456789uL,
            )

        val json = Json.encodeToString(Sample.serializer(), sample)
        val decoded = Json.decodeFromString(Sample.serializer(), json)
        assertEquals(sample, decoded)
    }
}
