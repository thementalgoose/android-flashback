package tmg.flashback.providers.previewdata

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.CircuitHistoryRaceResult

internal fun CircuitHistoryRace.Companion.model(
    name: String = "name",
    season: Int = 2020,
    round: Int = 1,
    wikiUrl: String? = "wikiUrl",
    date: LocalDate = LocalDate.of(1995, 10, 12),
    time: LocalTime? = LocalTime.of(12, 34),
    preview: List<CircuitHistoryRaceResult> = listOf(CircuitHistoryRaceResult.model())
): CircuitHistoryRace = CircuitHistoryRace(
    name = name,
    season = season,
    round = round,
    wikiUrl = wikiUrl,
    date = date,
    time = time,
    preview = preview
)