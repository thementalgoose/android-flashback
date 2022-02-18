package tmg.flashback.regulations.domain

import tmg.flashback.regulations.R

fun hasFormatInfoFor(season: Int): Boolean {
    return when (season) {
        2022 -> true
        else -> false
    }
}

internal enum class Season(
    val season: Int,
    val sections: List<Section>
) {
    SEASON_2022(
        season = 2022,
        sections = listOf(
            Section(
                label = R.string.format_qualifying,
                items = listOf(

                )
            )
        )
    )
}