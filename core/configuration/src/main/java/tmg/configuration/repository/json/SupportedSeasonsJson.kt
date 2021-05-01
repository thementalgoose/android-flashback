package tmg.configuration.repository.json

data class AllSeasonsJson(
    val seasons: List<AllSeasonJson>? = null
)

data class AllSeasonJson(
    val s: Int?
)

//region Converters

fun AllSeasonsJson.convert(): Set<Int> {
    return this.seasons
        ?.mapNotNull { it.s }
        ?.toSet() ?: emptySet()
}

//endregion