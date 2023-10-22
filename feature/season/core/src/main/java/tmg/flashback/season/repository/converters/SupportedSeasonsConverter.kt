package tmg.flashback.season.repository.converters

import tmg.flashback.season.repository.json.AllSeasonsJson

fun AllSeasonsJson.convert(): Set<Int> {
    return this.seasons
            ?.mapNotNull { it.s }
            ?.toSet() ?: emptySet()
}