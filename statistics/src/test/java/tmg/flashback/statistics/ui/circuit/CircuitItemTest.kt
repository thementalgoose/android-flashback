package tmg.flashback.statistics.ui.circuit

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

fun CircuitItem.Companion.circuitInfoModel(
    circuit: CircuitHistory = CircuitHistory.model()
): CircuitItem.CircuitInfo = CircuitItem.CircuitInfo(
    circuit = circuit
)

fun CircuitItem.Companion.raceModel(
    name: String = "name",
    date: LocalDate = LocalDate.of(1995, 10, 12),
    time: LocalTime? = LocalTime.of(12, 34),
    season: Int = 2020,
    round: Int = 1,
    circuitName: String = "circuitName",
    country: String = "country",
    countryISO: String = "countryISO"
): CircuitItem.Race = CircuitItem.Race(
    name = name,
    date = date,
    time = time,
    season = season,
    round = round,
    circuitName = circuitName,
    country = country,
    countryISO = countryISO
)

fun CircuitItem.Companion.trackImageModel(
    trackLayout: TrackLayout = TrackLayout.ALBERT_PARK
): CircuitItem.TrackImage = CircuitItem.TrackImage(
    trackLayout = trackLayout
)

fun CircuitItem.Companion.errorItemModel(
    item: SyncDataItem = SyncDataItem.InternalError
): CircuitItem.ErrorItem = CircuitItem.ErrorItem(
    item = item
)