// port-lint: source formats.rs
package io.github.kotlinmania.serdewith

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatsTest {
    @Test
    fun formatMarkersCoverPrimitiveAndNamedFormats() {
        val formats =
            listOf<Format>(
                ByteFormat,
                UByteFormat,
                ShortFormat,
                UShortFormat,
                IntFormat,
                UIntFormat,
                LongFormat,
                ULongFormat,
                I128Format,
                U128Format,
                FloatFormat,
                DoubleFormat,
                BooleanFormat,
                StringFormat,
                Uppercase,
                Lowercase,
                PreferOne,
                PreferMany,
                Padded,
                Unpadded,
            )

        assertEquals(20, formats.distinct().size)
    }

    @Test
    fun strictnessMarkersAreConcreteChoices() {
        val strictness = listOf<Strictness>(Strict, Flexible)

        assertEquals(2, strictness.distinct().size)
    }

    @Test
    fun predefinedSeparatorsReturnUpstreamDelimiters() {
        assertEquals(" ", SpaceSeparator.separator())
        assertEquals(",", CommaSeparator.separator())
        assertEquals(";", SemicolonSeparator.separator())
        assertEquals(":", ColonSeparator.separator())
        assertEquals("\n", UnixLineSeparator.separator())
        assertEquals("\r\n", DosLineSeparator.separator())
    }
}
