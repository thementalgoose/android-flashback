package tmg.flashback.formula1.model

fun SeasonOverview.Companion.model(
    season: Int = 2020,
    roundOverviews: List<RoundOverview> = listOf(RoundOverview.model())
): SeasonOverview = SeasonOverview(
    season = season,
    roundOverviews = roundOverviews
)