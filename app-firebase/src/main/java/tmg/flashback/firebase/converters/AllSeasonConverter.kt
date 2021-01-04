package tmg.flashback.firebase.converters

import tmg.flashback.firebase.models.FAllSeasons

fun FAllSeasons.convert(): Set<Int> {
    return this.seasons
        ?.map { it.s }
        ?.toSet() ?: emptySet()
}