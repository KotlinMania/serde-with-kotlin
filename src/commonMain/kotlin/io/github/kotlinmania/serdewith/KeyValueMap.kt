// port-lint: source serde_with/src/key_value_map.rs
package io.github.kotlinmania.serdewith

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Key-value entry representation for collections.
 */
@Serializable
data class KeyValueEntry<K, V>(
    val key: K,
    val value: V,
)

/**
 * Serializes a [Map] as a list of [KeyValueEntry] objects and vice versa.
 */
class KeyValueMapSerializer<K, V>(
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
) : KSerializer<Map<K, V>> {
    private val entrySerializer = KeyValueEntry.serializer(keySerializer, valueSerializer)
    private val listSerializer = ListSerializer(entrySerializer)

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Map<K, V>) {
        val entries = value.map { (k, v) -> KeyValueEntry(k, v) }
        listSerializer.serialize(encoder, entries)
    }

    override fun deserialize(decoder: Decoder): Map<K, V> {
        val entries = listSerializer.deserialize(decoder)
        return entries.associate { it.key to it.value }
    }
}
