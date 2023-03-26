package tmg.flashback.results.repository.converters

import tmg.flashback.results.repository.json.AllSeasonsJson

fun AllSeasonsJson.convert(): Set<Int> {
    return this.seasons
            ?.mapNotNull { it.s }
            ?.toSet() ?: emptySet()
}