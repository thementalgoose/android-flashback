package tmg.flashback.widgets.upnext.presentation.preview

import java.time.LocalDate
import java.time.LocalTime
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule

val fakeOverviewRace = OverviewRace(
    date = LocalDate.of(2020, 1, 3),
    time = LocalTime.of(15, 0, 0),
    season = 2020,
    round = 1,
    raceName = "Emilia Romagna Grand Prix",
    circuitId = "imola",
    circuitName = "Imola Circuit",
    laps = "66",
    country = "Italy",
    countryISO = "ITA",
    hasQualifying = false,
    hasSprint = false,
    hasResults = false,
    schedule = listOf(
        Schedule("FP1", LocalDate.of(2020, 1, 1), LocalTime.of(12, 0), null),
        Schedule("FP2", LocalDate.of(2020, 1, 1), LocalTime.of(15, 0), null),
        Schedule("FP3", LocalDate.of(2020, 1, 2), LocalTime.of(11, 0), null),
        Schedule("Qualifying", LocalDate.of(2020, 1, 2), LocalTime.of(14, 0), null),
        Schedule("Race", LocalDate.of(2020, 1, 3), LocalTime.of(15, 0), null)
    )
)