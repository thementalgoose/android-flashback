package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.Season

internal fun Season.Companion.model(
    season: Int = 2020,
    races: List<Race> = listOf(
        Race.model()
    ),
    event: List<Event> = listOf(
        Event.model()
    )
): Season = Season(
    season = season,
    races = races,
    event = event
)