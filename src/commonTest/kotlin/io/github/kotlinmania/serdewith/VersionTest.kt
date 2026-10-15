package io.github.kotlinmania.serdewith

import kotlin.test.Test
import kotlin.test.assertEquals

class VersionTest {
    @Test
    fun testSerdeWithLibVersion() {
        assertEquals("3.17.0", SerdeWithLib.VERSION)
    }
}
