package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Unexpected
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Serializes and deserializes a [Boolean] as an integer (0 or 1).
 */
class BoolFromInt(
    private val strictness: Strictness = Strict,
) : SerializeAs<Boolean>,
    DeserializeAs<Boolean> {
    override fun <Ok> serializeAs(source: Boolean, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeI64(if (source) 1L else 0L)

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<Boolean> =
        deserializer.deserializeI64(
            object : Visitor<Boolean> {
                override fun expecting(): String =
                    if (strictness is Strict) "an integer 0 or 1" else "an integer"

                override fun visitI64(v: Long): SerdeResult<Boolean> =
                    when (strictness) {
                        is Strict -> {
                            when (v) {
                                0L -> {
                                    SerdeResult.success(false)
                                }

                                1L -> {
                                    SerdeResult.success(true)
                                }

                                else -> {
                                    SerdeResult.failure(
                                        SerdeError.invalidValue(
                                            Unexpected.Signed(v),
                                            this,
                                        ),
                                    )
                                }
                            }
                        }

                        else -> {
                            SerdeResult.success(v != 0L)
                        }
                    }

                override fun visitU64(v: ULong): SerdeResult<Boolean> =
                    when (strictness) {
                        is Strict -> {
                            when (v) {
                                0uL -> {
                                    SerdeResult.success(false)
                                }

                                1uL -> {
                                    SerdeResult.success(true)
                                }

                                else -> {
                                    SerdeResult.failure(
                                        SerdeError.invalidValue(
                                            Unexpected.Unsigned(v),
                                            this,
                                        ),
                                    )
                                }
                            }
                        }

                        else -> {
                            SerdeResult.success(v != 0uL)
                        }
                    }
            },
        )
}

/** Strict boolean from int (0 = false, 1 = true). */
object StrictBoolFromInt :
    SerializeAs<Boolean> by BoolFromInt(Strict),
    DeserializeAs<Boolean> by BoolFromInt(Strict)

/** Flexible boolean from int (0 = false, non-zero = true). */
object FlexibleBoolFromInt :
    SerializeAs<Boolean> by BoolFromInt(Flexible),
    DeserializeAs<Boolean> by BoolFromInt(Flexible)
