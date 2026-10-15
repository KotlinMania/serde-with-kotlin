package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NoneAsEmptyStringTest {
    @Test
    fun testNoneAsEmptyString() {
        val nullWrap = SerializeAsWrap<String?, SerializeAs<String?>>(null, NoneAsEmptyString)
        val helloWrap = SerializeAsWrap<String?, SerializeAs<String?>>("hello", NoneAsEmptyString)

        assertEquals("\"\"", toStr(nullWrap).getOrThrow())
        assertEquals("\"hello\"", toStr(helloWrap).getOrThrow())

        val de = NoneAsEmptyString.asDeserialize()

        assertNull(fromStr("\"\"", de).getOrThrow())
        assertEquals("hello", fromStr("\"hello\"", de).getOrThrow())
    }
}
