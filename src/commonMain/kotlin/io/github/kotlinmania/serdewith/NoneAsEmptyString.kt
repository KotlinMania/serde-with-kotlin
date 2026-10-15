package io.github.kotlinmania.serdewith

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serializer

/**
 * Serializes `null` as empty string and deserializes empty string as `null`.
 */
object NoneAsEmptyString : SerializeAs<String?>, DeserializeAs<String?> {
    override fun <Ok> serializeAs(source: String?, serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeStr(source.orEmpty())

    override fun deserializeAs(deserializer: Deserializer): SerdeResult<String?> =
        deserializer.deserializeStr(NoneAsEmptyStringVisitor)
}

private object NoneAsEmptyStringVisitor : Visitor<String?> {
    override fun expecting(): String = "a string"

    override fun visitStr(v: String): SerdeResult<String?> =
        SerdeResult.success(if (v.isEmpty()) null else v)

    override fun visitNone(): SerdeResult<String?> =
        SerdeResult.success(null)

    override fun <D> visitSome(deserializer: D): SerdeResult<String?> where D : Deserializer =
        deserializer.deserializeStr(this)
}
