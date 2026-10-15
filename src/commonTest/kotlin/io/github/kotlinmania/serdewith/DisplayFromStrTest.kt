package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals

class DisplayFromStrTest {
    @Test
    fun testDisplayFromStr() {
        val intWrap = SerializeAsWrap(123, IntAsString)
        assertEquals("\"123\"", toStr(intWrap).getOrThrow())
        val intVal = fromStr("\"123\"", IntAsString.asDeserialize()).getOrThrow()
        assertEquals(123, intVal)

        val longWrap = SerializeAsWrap(456789012345L, LongAsString)
        assertEquals("\"456789012345\"", toStr(longWrap).getOrThrow())
        val longVal = fromStr("\"456789012345\"", LongAsString.asDeserialize()).getOrThrow()
        assertEquals(456789012345L, longVal)

        val boolWrap = SerializeAsWrap(true, BooleanAsString)
        assertEquals("\"true\"", toStr(boolWrap).getOrThrow())
        val boolVal = fromStr("\"true\"", BooleanAsString.asDeserialize()).getOrThrow()
        assertEquals(true, boolVal)

        val uintWrap = SerializeAsWrap(99u, UIntAsString)
        assertEquals("\"99\"", toStr(uintWrap).getOrThrow())
        val uintVal = fromStr("\"99\"", UIntAsString.asDeserialize()).getOrThrow()
        assertEquals(99u, uintVal)
    }
}
