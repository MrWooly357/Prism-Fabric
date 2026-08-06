package net.mrwooly357.prism.client.level.block.state.predicate

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import net.minecraft.resources.Identifier
import net.mrwooly357.prism.client.Prism

object BlockStatePredicateTypeRegistry {

    private val ENTRIES: ImmutableMap<Identifier, BlockStatePredicate.Type<*>> = persistentMapOf<String, BlockStatePredicate.Type<*>>(
    )
        .mapKeys { (id, type) -> Prism.id(id) }
        .toImmutableMap()
    private val IDS: ImmutableMap<BlockStatePredicate.Type<*>, Identifier> = createIdsMap()


    private fun createIdsMap(): ImmutableMap<BlockStatePredicate.Type<*>, Identifier> {
        val map = mutableMapOf<BlockStatePredicate.Type<*>, Identifier>()

        for ((id, type) in ENTRIES)
            map[type] = id

        return map.toImmutableMap()
    }

    operator fun get(id: Identifier): BlockStatePredicate.Type<*> {
        return ENTRIES[id] ?: throw NoSuchElementException("No block state predicate type with id '$id'!")
    }

    fun getId(type: BlockStatePredicate.Type<*>): Identifier {
        return IDS[type] ?: throw NoSuchElementException("No block state predicate type with id '$type'!")
    }
}
