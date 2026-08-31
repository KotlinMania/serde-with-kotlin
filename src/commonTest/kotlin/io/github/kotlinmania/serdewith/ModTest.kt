// port-lint: tests serde_with/src/lib.rs
package io.github.kotlinmania.serdewith

import kotlin.test.Test
import kotlin.test.assertEquals

class ModTest {
    @Test
    fun testSerdeWithLibVersion() {
        assertEquals("3.17.0", SerdeWithLib.VERSION)
    }
}
