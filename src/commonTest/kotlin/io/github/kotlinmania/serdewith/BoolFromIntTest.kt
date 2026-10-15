package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BoolFromIntTest {
    @Test
    fun testBoolFromInt() {
        val falseWrap = SerializeAsWrap(false, StrictBoolFromInt)
        val trueWrap = SerializeAsWrap(true, StrictBoolFromInt)

        assertEquals("0", toStr(falseWrap).getOrThrow())
        assertEquals("1", toStr(trueWrap).getOrThrow())

        val falseRes = fromStr("0", StrictBoolFromInt.asDeserialize())
        assertEquals(false, falseRes.getOrThrow())

        val trueRes = fromStr("1", StrictBoolFromInt.asDeserialize())
        assertEquals(true, trueRes.getOrThrow())

        val failRes2 = fromStr("2", StrictBoolFromInt.asDeserialize())
        assertTrue(failRes2.isFailure)

        val failResNeg = fromStr("-100", StrictBoolFromInt.asDeserialize())
        assertTrue(failResNeg.isFailure)
    }

    @Test
    fun testFlexibleBoolFromInt() {
        val trueRes = fromStr("5", FlexibleBoolFromInt.asDeserialize())
        assertEquals(true, trueRes.getOrThrow())

        val falseRes = fromStr("0", FlexibleBoolFromInt.asDeserialize())
        assertEquals(false, falseRes.getOrThrow())
    }
}
