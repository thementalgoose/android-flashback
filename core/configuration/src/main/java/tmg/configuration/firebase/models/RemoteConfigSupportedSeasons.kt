package tmg.configuration.firebase.models

data class RemoteConfigAllSeasons(
    val seasons: List<RemoteConfigAllSeason>? = null
)

data class RemoteConfigAllSeason(
    val s: Int?
)

//region Converters

fun RemoteConfigAllSeasons.convert(): Set<Int> {
    return this.seasons
        ?.mapNotNull { it.s }
        ?.toSet() ?: emptySet()
}

//endregion