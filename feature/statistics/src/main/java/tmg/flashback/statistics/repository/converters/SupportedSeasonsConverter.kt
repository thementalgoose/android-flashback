package tmg.flashback.statistics.repository.converters

import tmg.flashback.statistics.repository.json.AllSeasonsJson

fun AllSeasonsJson.convert(): Set<Int> {
    return this.seasons
            ?.mapNotNull { it.s }
            ?.toSet() ?: emptySet()
}