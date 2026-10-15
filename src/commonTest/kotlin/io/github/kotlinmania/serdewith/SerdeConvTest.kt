// port-lint: tests serde_conv.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.prettyassertions.assertEq
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdejson.fromStr
import io.github.kotlinmania.serdejson.toStr
import kotlin.test.Test
import kotlin.test.assertTrue

class SerdeConvTest {
    private val stringDe =
        object : DeserializeAs<String> {
            override fun deserializeAs(deserializer: Deserializer) =
                deserializer.deserializeStr(
                    object : io.github.kotlinmania.serdecore.de.Visitor<String> {
                        override fun expecting() = "a string"

                        override fun visitStr(v: String) =
                            io.github.kotlinmania.serde.SerdeResult
                                .success(v)
                    },
                )
        }

    private val boolAsString =
        serdeConv<Boolean, String>(
            toIntermediate = { it.toString() },
            fromIntermediate = { it.toBooleanStrict() },
            intermediateDe = stringDe,
        )

    @Test
    fun testBoolAsString() {
        val falseWrap = SerializeAsWrap(false, boolAsString)
        val trueWrap = SerializeAsWrap(true, boolAsString)

        assertEq("\"false\"", toStr(falseWrap).getOrThrow())
        assertEq("\"true\"", toStr(trueWrap).getOrThrow())

        val de = boolAsString.asDeserialize()

        assertEq(false, fromStr("\"false\"", de).getOrThrow())
        assertEq(true, fromStr("\"true\"", de).getOrThrow())

        assertTrue(fromStr("123", de).isFailure)
        assertTrue(fromStr("\"not_a_bool\"", de).isFailure)
    }
}
