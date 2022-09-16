package tmg.flashback.stats.ui.drivers.season

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.raceStatusFinish
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.model
import tmg.flashback.stats.ui.drivers.overview.PipeType

fun DriverSeasonModel.Companion.headerModel(
    driver: Driver = Driver.model()
): DriverSeasonModel.Header = DriverSeasonModel.Header(
    driver = driver
)

fun DriverSeasonModel.Companion.statModel(
    isWinning: Boolean = false,
    @DrawableRes
    icon: Int = 0,
    @StringRes
    label: Int = 0,
    value: String = "stat"
): DriverSeasonModel = DriverSeasonModel.Stat(
    isWinning = isWinning,
    icon = icon,
    label = label,
    value = value
)

fun DriverSeasonModel.Companion.messageModel(
    label: Int = 0,
    args: List<Any> = emptyList()
): DriverSeasonModel.Message = DriverSeasonModel.Message(
    label = label,
    args = args
)

fun DriverSeasonModel.Companion.racedForModel(
    season: Int? = null,
    constructors: Constructor = Constructor.model(),
    type: PipeType = PipeType.SINGLE,
    isChampionship: Boolean = false
): DriverSeasonModel.RacedFor = DriverSeasonModel.RacedFor(
    season = season,
    constructors = constructors,
    type = type,
    isChampionship = isChampionship
)

fun DriverSeasonModel.Companion.resultModel(
    season: Int = 2020,
    round: Int = 1,
    raceName: String = "name",
    circuitName: String = "circuitName",
    circuitId: String = "circuitId",
    raceCountry: String = "country",
    raceCountryISO: String = "countryISO",
    date: LocalDate = LocalDate.of(2020, 10, 12),
    showConstructorLabel: Boolean = false,
    constructor: Constructor = Constructor.model(),
    qualified: Int? = 1,
    finished: Int? = 1,
    raceStatus: RaceStatus = raceStatusFinish,
    points: Double = 0.0,
    maxPoints: Int = 25
): DriverSeasonModel.Result = DriverSeasonModel.Result(
    season = season,
    round = round,
    raceName = raceName,
    circuitName = circuitName,
    circuitId = circuitId,
    raceCountry = raceCountry,
    raceCountryISO = raceCountryISO,
    date = date,
    showConstructorLabel = showConstructorLabel,
    constructor = constructor,
    qualified = qualified,
    finished = finished,
    raceStatus = raceStatus,
    points = points,
    maxPoints = maxPoints
)