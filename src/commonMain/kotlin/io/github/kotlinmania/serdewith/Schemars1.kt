// port-lint: source schemars_1.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.serdewith

import io.github.kotlinmania.schemars.JsonSchema
import io.github.kotlinmania.schemars.Schema
import io.github.kotlinmania.schemars.Value
import io.github.kotlinmania.schemars.generate.SchemaGenerator
import io.github.kotlinmania.schemars.jsonSchema
import io.github.kotlinmania.schemars.jsonschemaimpls.MapSchema
import io.github.kotlinmania.schemars.jsonschemaimpls.OptionSchema
import io.github.kotlinmania.schemars.jsonschemaimpls.SeqSchema
import io.github.kotlinmania.schemars.jsonschemaimpls.SetSchema
import io.github.kotlinmania.schemars.jsonschemaimpls.StringSchema
import io.github.kotlinmania.schemars.jsonschemaimpls.U8Schema
import kotlin.native.HiddenFromObjC

/**
 * A type which can be described as a JSON schema document on behalf of [T].
 *
 * This trait is as [SerializeAs] is to `Serialize` but for [JsonSchema].
 */
@HiddenFromObjC
interface JsonSchemaAs<T> {
    fun inlineSchema(): Boolean = false

    fun schemaName(): String

    fun schemaId(): String = schemaName()

    fun jsonSchema(generator: SchemaGenerator): Schema
}

/**
 * Wrapper adapting a [JsonSchemaAs] implementation into a [JsonSchema].
 */
@HiddenFromObjC
class WrapSchema<T>(
    private val adapter: JsonSchemaAs<T>,
) : JsonSchema {
    override fun inlineSchema(): Boolean = adapter.inlineSchema()

    override fun schemaName(): String = adapter.schemaName()

    override fun schemaId(): String = adapter.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = adapter.jsonSchema(generator)

    companion object {
        fun kvmapTransformSchema1(
            g: SchemaGenerator,
            schema: Schema,
        ): Schema {
            val parents = mutableListOf<String>()
            val obj = schema.asObject()
            val boolVal = schema.asBool()
            val value: Value =
                when {
                    obj != null -> Value.Object(obj.toMutableMap())
                    boolVal != null -> Value.Bool(boolVal)
                    else -> return schema
                }
            val transformed = kvmapTransformSchemaImpl1(g, value, parents, 0)
            return Schema.tryFrom(transformed).getOrThrow()
        }

        private fun kvmapTransformSchemaImpl1(
            g: SchemaGenerator,
            schema: Value,
            parents: MutableList<String>,
            depth: Int,
        ): Value {
            if (depth > 8) return schema
            val schemaObj = (schema as? Value.Object)?.entries ?: return schema

            var pushedParent: String? = null
            val refValue = schemaObj["\$ref"]
            if (refValue != null) {
                val refStr = (refValue as? Value.Str)?.value ?: return schema
                val resolvedName = resolveReference1(g, refStr) ?: return schema
                if (parents.contains(resolvedName)) {
                    return schema
                }
                val defSchema = g.definitions()[resolvedName] as? Value.Object ?: return schema
                schemaObj.clear()
                for ((k, v) in defSchema.entries) {
                    schemaObj[k] = v.deepCopy()
                }
                parents.add(resolvedName)
                pushedParent = resolvedName
            }

            try {
                var done = false
                val ty = (schemaObj["type"] as? Value.Str)?.value

                if (ty == "object") {
                    val properties = (schemaObj["properties"] as? Value.Object)?.entries
                    if (properties != null) {
                        if (properties.remove("\$key\$") != null) {
                            done = true
                        }
                    }

                    val required = (schemaObj["required"] as? Value.Array)?.items
                    if (required != null) {
                        val iter = required.iterator()
                        while (iter.hasNext()) {
                            val req = iter.next()
                            if (req is Value.Str && req.value == "\$key\$") {
                                iter.remove()
                                done = true
                            }
                        }
                    }

                    val maxProps = (schemaObj["maxProperties"] as? Value.Number)?.asLong()
                    if (maxProps != null) {
                        schemaObj["maxProperties"] = Value.Number((maxProps - 1L).coerceAtLeast(0L))
                    }

                    val minProps = (schemaObj["minProperties"] as? Value.Number)?.asLong()
                    if (minProps != null) {
                        schemaObj["minProperties"] = Value.Number((minProps - 1L).coerceAtLeast(0L))
                    }
                }

                if (ty == "array") {
                    val prefixItems = (schemaObj["prefixItems"] as? Value.Array)?.items
                    if (prefixItems != null && prefixItems.isNotEmpty()) {
                        prefixItems.removeAt(0)
                        done = true
                    }

                    val items = (schemaObj["items"] as? Value.Array)?.items
                    if (items != null && items.isNotEmpty()) {
                        items.removeAt(0)
                        done = true
                    }

                    val maxItems = (schemaObj["maxItems"] as? Value.Number)?.asLong()
                    if (maxItems != null) {
                        schemaObj["maxItems"] = Value.Number((maxItems - 1L).coerceAtLeast(0L))
                    }

                    val minItems = (schemaObj["minItems"] as? Value.Number)?.asLong()
                    if (minItems != null) {
                        schemaObj["minItems"] = Value.Number((minItems - 1L).coerceAtLeast(0L))
                    }
                }

                if (done) {
                    return Value.Object(schemaObj)
                }

                for (combinator in listOf("oneOf", "anyOf", "allOf")) {
                    val subschemas = (schemaObj[combinator] as? Value.Array)?.items
                    if (subschemas != null) {
                        for (i in subschemas.indices) {
                            subschemas[i] = kvmapTransformSchemaImpl1(g, subschemas[i], parents, depth + 1)
                        }
                    }
                }

                return Value.Object(schemaObj)
            } finally {
                if (pushedParent != null) {
                    parents.removeAt(parents.lastIndex)
                }
            }
        }

        fun resolveReference1(
            g: SchemaGenerator,
            reference: String,
        ): String? {
            val withoutHash = if (reference.startsWith("#")) reference.substring(1) else return null
            val rawDefPath = g.settings().definitionsPath
            val defPath = if (rawDefPath.startsWith("#")) rawDefPath.substring(1) else rawDefPath
            if (!withoutHash.startsWith(defPath)) return null
            var rest = withoutHash.substring(defPath.length)
            if (!defPath.endsWith("/") && rest.startsWith("/")) {
                rest = rest.substring(1)
            }
            return rest
        }
    }
}

private fun Value.deepCopy(): Value =
    when (this) {
        is Value.Null -> {
            Value.Null
        }

        is Value.Bool -> {
            Value.Bool(value)
        }

        is Value.Number -> {
            Value.Number(value)
        }

        is Value.Str -> {
            Value.Str(value)
        }

        is Value.Array -> {
            Value.Array(items.map { it.deepCopy() }.toMutableList())
        }

        is Value.Object -> {
            Value.Object(
                entries.entries.associate { (k, v) -> k to v.deepCopy() }.toMutableMap(),
            )
        }
    }

/**
 * Forward a [JsonSchema] directly as a [JsonSchemaAs].
 */
@HiddenFromObjC
class SameJsonSchemaAs<T>(
    private val targetSchema: JsonSchema,
) : JsonSchemaAs<T> {
    override fun inlineSchema(): Boolean = targetSchema.inlineSchema()

    override fun schemaName(): String = targetSchema.schemaName()

    override fun schemaId(): String = targetSchema.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = targetSchema.jsonSchema(generator)
}

/**
 * `JsonSchemaAs` for [DisplayFromStr], forwarding to `String`.
 */
@HiddenFromObjC
class DisplayFromStrJsonSchemaAs<T> : JsonSchemaAs<T> {
    override fun inlineSchema(): Boolean = StringSchema.inlineSchema()

    override fun schemaName(): String = StringSchema.schemaName()

    override fun schemaId(): String = StringSchema.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = StringSchema.jsonSchema(generator)
}

/**
 * `JsonSchemaAs` for [Hex].
 */
@HiddenFromObjC
class HexJsonSchemaAs<T> : JsonSchemaAs<T> {
    override fun inlineSchema(): Boolean = true

    override fun schemaName(): String = "Hex<F>"

    override fun schemaId(): String = "serde_with::hex::Hex<F>"

    override fun jsonSchema(generator: SchemaGenerator): Schema =
        jsonSchema {
            this["type"] = "string"
            this["pattern"] = "^(?:[0-9A-Fa-f]{2})*$"
        }
}

/**
 * `JsonSchemaAs` for strict [BoolFromInt].
 */
@HiddenFromObjC
object BoolFromIntStrictJsonSchemaAs : JsonSchemaAs<Boolean> {
    override fun inlineSchema(): Boolean = true

    override fun schemaName(): String = "BoolFromInt<Strict>"

    override fun schemaId(): String = "serde_with::BoolFromInt<Strict>"

    override fun jsonSchema(generator: SchemaGenerator): Schema =
        jsonSchema {
            this["type"] = "integer"
            this["minimum"] = 0.0
            this["maximum"] = 1.0
        }
}

/**
 * `JsonSchemaAs` for flexible [BoolFromInt].
 */
@HiddenFromObjC
object BoolFromIntFlexibleJsonSchemaAs : JsonSchemaAs<Boolean> {
    override fun inlineSchema(): Boolean = true

    override fun schemaName(): String = "BoolFromInt<Flexible>"

    override fun schemaId(): String = "serde_with::BoolFromInt<Flexible>"

    override fun jsonSchema(generator: SchemaGenerator): Schema =
        jsonSchema {
            this["type"] = "integer"
        }
}

/**
 * `JsonSchemaAs` for `Bytes`, forwarding to `Vec<u8>`.
 */
@HiddenFromObjC
class BytesJsonSchemaAs<T> : JsonSchemaAs<T> {
    private val vecU8 = SeqSchema(U8Schema)

    override fun inlineSchema(): Boolean = vecU8.inlineSchema()

    override fun schemaName(): String = vecU8.schemaName()

    override fun schemaId(): String = vecU8.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = vecU8.jsonSchema(generator)
}

/**
 * `JsonSchemaAs` for `BytesOrString`.
 */
@HiddenFromObjC
object BytesOrStringJsonSchemaAs : JsonSchemaAs<ByteArray> {
    override fun inlineSchema(): Boolean = true

    override fun schemaName(): String = "BytesOrString"

    override fun schemaId(): String = "serde_with::BytesOrString"

    override fun jsonSchema(generator: SchemaGenerator): Schema =
        jsonSchema {
            this["anyOf"] =
                Value.Array(
                    mutableListOf(
                        generator.subschemaFor(SeqSchema(U8Schema)).toValue(),
                        jsonSchema {
                            this["type"] = "string"
                            this["writeOnly"] = true
                        }.toValue(),
                    ),
                )
        }
}

/**
 * `JsonSchemaAs` for [DefaultOnError].
 */
@HiddenFromObjC
class DefaultOnErrorJsonSchemaAs<T>(
    private val inner: JsonSchemaAs<T>,
) : JsonSchemaAs<T> by inner

/**
 * `JsonSchemaAs` for [DefaultOnNull].
 */
@HiddenFromObjC
class DefaultOnNullJsonSchemaAs<T>(
    inner: JsonSchemaAs<T>,
) : JsonSchemaAs<T> {
    private val optionSchema = OptionSchema(WrapSchema(inner))

    override fun inlineSchema(): Boolean = optionSchema.inlineSchema()

    override fun schemaName(): String = optionSchema.schemaName()

    override fun schemaId(): String = optionSchema.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = optionSchema.jsonSchema(generator)
}

/**
 * `JsonSchemaAs` for [EnumMap].
 */
@HiddenFromObjC
class EnumMapJsonSchemaAs<T>(
    private val elementSchema: JsonSchema,
) : JsonSchemaAs<List<T>> {
    override fun inlineSchema(): Boolean = false

    override fun schemaName(): String = "EnumMap(${elementSchema.schemaName()})"

    override fun schemaId(): String = "serde_with::EnumMap(${elementSchema.schemaId()})"

    override fun jsonSchema(generator: SchemaGenerator): Schema {
        val innerSchema = elementSchema.jsonSchema(generator)
        val inner = innerSchema.ensureObject()
        val oneOf = (inner["oneOf"] as? Value.Array)?.items ?: return innerSchema

        val properties = mutableMapOf<String, Value>()
        for (variant in oneOf) {
            val variantObj = (variant as? Value.Object)?.entries ?: continue
            val props = (variantObj["properties"] as? Value.Object)?.entries ?: continue
            properties.putAll(props)
        }

        return jsonSchema {
            this["type"] = "object"
            this["properties"] = Value.Object(properties)
            this["additionalProperties"] = false
        }
    }
}

/**
 * `JsonSchemaAs` for [KeyValueMap].
 */
@HiddenFromObjC
class KeyValueMapJsonSchemaAs<T>(
    inner: JsonSchemaAs<T>,
) : JsonSchemaAs<List<T>> {
    private val wrapped = WrapSchema(inner)

    override fun inlineSchema(): Boolean = false

    override fun schemaName(): String = "KeyValueMap(${wrapped.schemaName()})"

    override fun schemaId(): String = "serde_with::KeyValueMap(${wrapped.schemaId()})"

    override fun jsonSchema(generator: SchemaGenerator): Schema {
        val baseSchema = wrapped.jsonSchema(generator)
        val transformed = WrapSchema.kvmapTransformSchema1(generator, baseSchema)
        return jsonSchema {
            this["type"] = "object"
            this["additionalProperties"] = transformed
        }
    }
}

/**
 * `JsonSchemaAs` for `OneOrMany<TA, PreferOne>`.
 */
@HiddenFromObjC
class OneOrManyPreferOneJsonSchemaAs<T>(
    inner: JsonSchemaAs<T>,
) : JsonSchemaAs<List<T>> {
    private val wrapped = WrapSchema(inner)

    override fun inlineSchema(): Boolean = false

    override fun schemaName(): String = "OneOrMany(${wrapped.schemaName()},PreferOne)"

    override fun schemaId(): String = "serde_with::OneOrMany(${wrapped.schemaId()},PreferOne)"

    override fun jsonSchema(generator: SchemaGenerator): Schema {
        val single = generator.subschemaFor(wrapped)
        return jsonSchema {
            this["anyOf"] =
                Value.Array(
                    mutableListOf(
                        single.toValue(),
                        jsonSchema {
                            this["type"] = "array"
                            this["items"] = single
                        }.toValue(),
                    ),
                )
        }
    }
}

/**
 * `JsonSchemaAs` for `OneOrMany<TA, PreferMany>`.
 */
@HiddenFromObjC
class OneOrManyPreferManyJsonSchemaAs<T>(
    inner: JsonSchemaAs<T>,
) : JsonSchemaAs<List<T>> {
    private val wrapped = WrapSchema(inner)

    override fun inlineSchema(): Boolean = false

    override fun schemaName(): String = "OneOrMany<${wrapped.schemaName()}, PreferMany>"

    override fun schemaId(): String = "serde_with::OneOrMany<${wrapped.schemaId()}, PreferMany>"

    override fun jsonSchema(generator: SchemaGenerator): Schema {
        val innerSchema = generator.subschemaFor(wrapped)
        return jsonSchema {
            this["anyOf"] =
                Value.Array(
                    mutableListOf(
                        jsonSchema {
                            this["writeOnly"] = true
                            this["allOf"] = Value.Array(mutableListOf(innerSchema.toValue()))
                        }.toValue(),
                        jsonSchema {
                            this["type"] = "array"
                            this["items"] = innerSchema
                        }.toValue(),
                    ),
                )
        }
    }
}

/**
 * `JsonSchemaAs` for `PickFirst`.
 */
@HiddenFromObjC
class PickFirstJsonSchemaAs<T>(
    private val adapters: List<JsonSchemaAs<T>>,
) : JsonSchemaAs<T> {
    init {
        require(adapters.isNotEmpty()) { "PickFirstJsonSchemaAs requires at least one adapter" }
    }

    override fun inlineSchema(): Boolean = false

    override fun schemaName(): String =
        "PickFirst(${adapters.joinToString("") { WrapSchema(it).schemaName() }})"

    override fun schemaId(): String =
        "serde_with::PickFirst(${adapters.joinToString("") { WrapSchema(it).schemaId() }})"

    override fun jsonSchema(generator: SchemaGenerator): Schema {
        var first = true
        val subschemas =
            adapters
                .map { adapter ->
                    val isFirst = first
                    first = false
                    val schema = generator.subschemaFor(WrapSchema(adapter))
                    if (!isFirst) {
                        jsonSchema {
                            this["writeOnly"] = true
                            this["allOf"] = Value.Array(mutableListOf(schema.toValue()))
                        }.toValue()
                    } else {
                        schema.toValue()
                    }
                }.toMutableList()

        return jsonSchema {
            this["anyOf"] = Value.Array(subschemas)
        }
    }
}

/**
 * `JsonSchemaAs` for `SetLastValueWins`.
 */
@HiddenFromObjC
class SetLastValueWinsJsonSchemaAs<V>(
    inner: JsonSchemaAs<V>,
) : JsonSchemaAs<Set<V>> {
    private val wrapped = WrapSchema(inner)
    private val setSchema = SetSchema(wrapped)

    override fun inlineSchema(): Boolean = wrapped.inlineSchema()

    override fun schemaName(): String = "SetLastValueWins<${wrapped.schemaName()}>"

    override fun schemaId(): String = "serde_with::SetLastValueWins<${wrapped.schemaId()}>"

    override fun jsonSchema(generator: SchemaGenerator): Schema {
        val schema = setSchema.jsonSchema(generator)
        schema.ensureObject().remove("uniqueItems")
        return schema
    }
}

/**
 * `JsonSchemaAs` for `SetPreventDuplicates`.
 */
@HiddenFromObjC
class SetPreventDuplicatesJsonSchemaAs<V>(
    inner: JsonSchemaAs<V>,
) : JsonSchemaAs<Set<V>> {
    private val setSchema = SetSchema(WrapSchema(inner))

    override fun inlineSchema(): Boolean = setSchema.inlineSchema()

    override fun schemaName(): String = setSchema.schemaName()

    override fun schemaId(): String = setSchema.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = setSchema.jsonSchema(generator)
}

/**
 * `JsonSchemaAs` for `MapFirstKeyWins` and `MapPreventDuplicates`.
 */
@HiddenFromObjC
class MapDuplicatePolicyJsonSchemaAs<K, V>(
    key: JsonSchemaAs<K>,
    value: JsonSchemaAs<V>,
) : JsonSchemaAs<Map<K, V>> {
    private val mapSchema = MapSchema(WrapSchema(key), WrapSchema(value))

    override fun inlineSchema(): Boolean = mapSchema.inlineSchema()

    override fun schemaName(): String = mapSchema.schemaName()

    override fun schemaId(): String = mapSchema.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = mapSchema.jsonSchema(generator)
}

/**
 * `JsonSchemaAs` for [StringWithSeparator].
 */
@HiddenFromObjC
class StringWithSeparatorJsonSchemaAs<T> : JsonSchemaAs<T> {
    override fun inlineSchema(): Boolean = StringSchema.inlineSchema()

    override fun schemaName(): String = StringSchema.schemaName()

    override fun schemaId(): String = StringSchema.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = StringSchema.jsonSchema(generator)
}

/**
 * `JsonSchemaAs` for [VecSkipError].
 */
@HiddenFromObjC
class VecSkipErrorJsonSchemaAs<T>(
    inner: JsonSchemaAs<T>,
) : JsonSchemaAs<List<T>> {
    private val vecSchema = SeqSchema(WrapSchema(inner))

    override fun inlineSchema(): Boolean = vecSchema.inlineSchema()

    override fun schemaName(): String = vecSchema.schemaName()

    override fun schemaId(): String = vecSchema.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = vecSchema.jsonSchema(generator)
}

/**
 * Target format for `DurationXXX` and `TimestampYYY` schemas.
 */
@HiddenFromObjC
enum class TimespanTargetType {
    String,
    F64,
    U64,
    I64,
    ;

    fun intoFlexibleSchema(signed: Boolean): Schema {
        val number =
            jsonSchema {
                this["type"] = "number"
            }
        if (!signed) {
            number.ensureObject()["minimum"] = Value.Number(0.0)
        }

        val regex = "[0-9]+(\\.[0-9]+)?([eE][+-]?[0-9]+)?"
        val pattern = if (signed) "^-?$regex$" else "^$regex$"
        val string =
            jsonSchema {
                this["type"] = "string"
                this["pattern"] = pattern
            }

        if (this == String) {
            number.ensureObject()["writeOnly"] = Value.Bool(true)
        } else {
            string.ensureObject()["writeOnly"] = Value.Bool(true)
        }

        return jsonSchema {
            this["oneOf"] = Value.Array(mutableListOf(number.toValue(), string.toValue()))
        }
    }

    fun schemaId(): kotlin.String =
        when (this) {
            String -> "serde_with::FlexibleStringTimespan"
            F64 -> "serde_with::FlexibleF64Timespan"
            U64 -> "serde_with::FlexibleU64Timespan"
            I64 -> "serde_with::FlexibleI64Timespan"
        }

    fun schemaName(): kotlin.String = schemaId().removePrefix("serde_with::")
}

/**
 * `JsonSchemaAs` for flexible duration/timestamp adapters.
 */
@HiddenFromObjC
class FlexibleTimespanJsonSchemaAs<T>(
    private val targetType: TimespanTargetType,
    private val signed: Boolean,
) : JsonSchemaAs<T> {
    override fun inlineSchema(): Boolean = true

    override fun schemaName(): String = targetType.schemaName()

    override fun schemaId(): String = targetType.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = targetType.intoFlexibleSchema(signed)
}

/**
 * `JsonSchemaAs` for [JsonString].
 */
@HiddenFromObjC
class JsonStringJsonSchemaAs<T> : JsonSchemaAs<T> {
    override fun inlineSchema(): Boolean = StringSchema.inlineSchema()

    override fun schemaName(): String = StringSchema.schemaName()

    override fun schemaId(): String = StringSchema.schemaId()

    override fun jsonSchema(generator: SchemaGenerator): Schema = StringSchema.jsonSchema(generator)
}
