package tmg.flashback.formula1.model

fun Season.Companion.model(
    season: Int = 2020,
    races: List<Race> = listOf(
        Race.model()
    ),
    winterTesting: List<WinterTesting> = emptyList()
): Season = Season(
    season = season,
    races = races,
    winterTesting = winterTesting
)