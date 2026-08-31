// port-lint: source serde_with/src/formats.rs
package io.github.kotlinmania.serdewith

/**
 * Specifies the format and how lenient deserialization is.
 */

/**
 * Specifies how to serialize or deserialize a type.
 *
 * The format specifier allows callers to configure how a value is serialized
 * or deserialized. For example, a timestamp can be represented as an integer
 * using the UNIX epoch, as a string containing an integer, or as a string using
 * ISO 8601. The [Format] interface allows more flexibility in configuring the
 * format without creating a new type for each case.
 */
interface Format

/** Serialize into a signed 8-bit integer value. */
object ByteFormat : Format

/** Serialize into an unsigned 8-bit integer value. */
object UByteFormat : Format

/** Serialize into a signed 16-bit integer value. */
object ShortFormat : Format

/** Serialize into an unsigned 16-bit integer value. */
object UShortFormat : Format

/** Serialize into a signed 32-bit integer value. */
object IntFormat : Format

/** Serialize into an unsigned 32-bit integer value. */
object UIntFormat : Format

/** Serialize into a signed 64-bit integer value. */
object LongFormat : Format

/** Serialize into an unsigned 64-bit integer value. */
object ULongFormat : Format

/** Serialize into a signed 128-bit integer value. */
object I128Format : Format

/** Serialize into an unsigned 128-bit integer value. */
object U128Format : Format

/** Serialize into a 32-bit floating point value. */
object FloatFormat : Format

/** Serialize into a 64-bit floating point value. */
object DoubleFormat : Format

/** Serialize into a Boolean value. */
object BooleanFormat : Format

/** Serialize into a String value. */
object StringFormat : Format

/** Use uppercase characters. */
object Uppercase : Format

/** Use lowercase characters. */
object Lowercase : Format

/** Use in combination with [OneOrMany]. Emit a single element for lists of size one. */
object PreferOne : Format

/** Use in combination with [OneOrMany]. Always emit the list form. */
object PreferMany : Format

/** Emit padding during serialization. */
object Padded : Format

/** Do not emit padding during serialization. */
object Unpadded : Format

/**
 * Specifies how lenient the deserialization process should be.
 *
 * Formats that use this interface should specify how it affects deserialization
 * behavior.
 */
interface Strictness

/** Use strict deserialization behavior, see [Strictness]. */
object Strict : Strictness

/** Use flexible deserialization behavior, see [Strictness]. */
object Flexible : Strictness

/** Separator for string-based collection serialization and deserialization. */
interface Separator {
    /** Returns the string delimiting two elements in the string-based collection. */
    fun separator(): String
}

/** Predefined separator using a single space. */
object SpaceSeparator : Separator {
    override fun separator(): String = " "
}

/** Predefined separator using a single comma. */
object CommaSeparator : Separator {
    override fun separator(): String = ","
}

/** Predefined separator using a single semicolon. */
object SemicolonSeparator : Separator {
    override fun separator(): String = ";"
}

/** Predefined separator using a single colon. */
object ColonSeparator : Separator {
    override fun separator(): String = ":"
}

/** Predefined separator using a single linefeed. */
object UnixLineSeparator : Separator {
    override fun separator(): String = "\n"
}

/** Predefined separator using a DOS or Windows line ending. */
object DosLineSeparator : Separator {
    override fun separator(): String = "\r\n"
}
