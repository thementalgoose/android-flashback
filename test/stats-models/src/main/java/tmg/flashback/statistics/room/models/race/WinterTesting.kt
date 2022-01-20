package tmg.flashback.statistics.room.models.race

fun WinterTesting.Companion.model(
    season: Int = 2020,
    label: String = "label",
    date: String = "2020-10-12"
): WinterTesting = WinterTesting(
    season = season,
    label = label,
    date = date
)