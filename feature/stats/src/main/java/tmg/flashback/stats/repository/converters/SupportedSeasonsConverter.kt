package tmg.flashback.stats.repository.converters

import tmg.flashback.stats.repository.json.AllSeasonsJson

fun AllSeasonsJson.convert(): Set<Int> {
    return this.seasons
            ?.mapNotNull { it.s }
            ?.toSet() ?: emptySet()
}