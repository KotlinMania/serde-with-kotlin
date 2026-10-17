// port-lint: tests schemars_1.rs
package io.github.kotlinmania.serdewith

import io.github.kotlinmania.prettyassertions.assertEq
import io.github.kotlinmania.schemars.Value
import io.github.kotlinmania.schemars.generate.SchemaGenerator
import io.github.kotlinmania.schemars.jsonSchema
import io.github.kotlinmania.schemars.jsonschemaimpls.I32Schema
import io.github.kotlinmania.schemars.jsonschemaimpls.StringSchema
import kotlin.test.Test

class Schemars1Test {
    @Test
    fun displayFromStrAndHexSchemas() {
        val gen = SchemaGenerator.default()
        val displaySchema = WrapSchema(DisplayFromStrJsonSchemaAs<Int>()).jsonSchema(gen)
        assertEq(Value.Str("string"), displaySchema.get("type"))

        val hexSchema = WrapSchema(HexJsonSchemaAs<ByteArray>()).jsonSchema(gen)
        assertEq(Value.Str("string"), hexSchema.get("type"))
        assertEq(Value.Str("^(?:[0-9A-Fa-f]{2})*$"), hexSchema.get("pattern"))
    }

    @Test
    fun boolFromIntSchemas() {
        val gen = SchemaGenerator.default()
        val strict = WrapSchema(BoolFromIntStrictJsonSchemaAs).jsonSchema(gen)
        assertEq(Value.Str("integer"), strict.get("type"))
        assertEq(Value.Number(0.0), strict.get("minimum"))
        assertEq(Value.Number(1.0), strict.get("maximum"))

        val flexible = WrapSchema(BoolFromIntFlexibleJsonSchemaAs).jsonSchema(gen)
        assertEq(Value.Str("integer"), flexible.get("type"))
        assertEq(null, flexible.get("minimum"))
    }

    @Test
    fun oneOrManyAndPickFirstSchemas() {
        val gen = SchemaGenerator.default()
        val preferOne =
            OneOrManyPreferOneJsonSchemaAs(SameJsonSchemaAs<String>(StringSchema)).jsonSchema(gen)
        val anyOf = (preferOne.get("anyOf") as Value.Array).items
        assertEq(2, anyOf.size)

        val pickFirst =
            PickFirstJsonSchemaAs(
                listOf(
                    SameJsonSchemaAs<Int>(I32Schema),
                    DisplayFromStrJsonSchemaAs(),
                ),
            ).jsonSchema(gen)
        val pickAnyOf = (pickFirst.get("anyOf") as Value.Array).items
        assertEq(2, pickAnyOf.size)
        val second = (pickAnyOf[1] as Value.Object).entries
        assertEq(Value.Bool(true), second["writeOnly"])
    }

    @Test
    fun keyValueMapAndTimespanSchemas() {
        val gen = SchemaGenerator.default()
        val entryAdapter =
            object : JsonSchemaAs<Map<String, String>> {
                override fun schemaName(): String = "Entry"

                override fun jsonSchema(generator: SchemaGenerator) =
                    jsonSchema {
                        this["type"] = "object"
                        this["properties"] =
                            Value.Object(
                                mutableMapOf(
                                    "\$key\$" to Value.Object(mutableMapOf("type" to Value.Str("string"))),
                                    "val" to Value.Object(mutableMapOf("type" to Value.Str("integer"))),
                                ),
                            )
                        this["required"] =
                            Value.Array(
                                mutableListOf(Value.Str("\$key\$"), Value.Str("val")),
                            )
                    }
            }

        val kvSchema = KeyValueMapJsonSchemaAs(entryAdapter).jsonSchema(gen)
        assertEq(Value.Str("object"), kvSchema.get("type"))
        val addProps = (kvSchema.get("additionalProperties") as Value.Object).entries
        val props = (addProps["properties"] as Value.Object).entries
        assertEq(false, props.containsKey("\$key\$"))
        assertEq(true, props.containsKey("val"))

        val flexDuration =
            FlexibleTimespanJsonSchemaAs<Long>(TimespanTargetType.U64, signed = false).jsonSchema(gen)
        val oneOf = (flexDuration.get("oneOf") as Value.Array).items
        assertEq(2, oneOf.size)
    }
}
