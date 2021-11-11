package tmg.flashback.statistics.ui.dashboard.season

import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

fun SeasonItem.Companion.calendarMonthModel(
    month: Month = Month.OCTOBER,
    year: Int = 2020
): SeasonItem.CalendarMonth = SeasonItem.CalendarMonth(
    month = month,
    year = year
)

fun SeasonItem.Companion.calendarWeekModel(
    forMonth: Month = Month.OCTOBER,
    startingDay: LocalDate = LocalDate.of(2020, 10, 12),
    race: OverviewRace? = OverviewRace.model()
): SeasonItem.CalendarWeek = SeasonItem.CalendarWeek(
    forMonth = forMonth,
    startingDay = startingDay,
    race = race
)

fun SeasonItem.Companion.trackModel(
    season: Int = 2020,
    raceName: String = "name",
    circuitName: String = "circuitName",
    circuitId: String = "circuitId",
    raceCountry: String = "country",
    raceCountryISO: String = "countryISO",
    date: LocalDate = LocalDate.of(2020, 10, 12),
    round: Int = 1,
    hasQualifying: Boolean = true,
    hasResults: Boolean = false
): SeasonItem.Track = SeasonItem.Track(
    season = season,
    raceName = raceName,
    circuitName = circuitName,
    circuitId = circuitId,
    raceCountry = raceCountry,
    raceCountryISO = raceCountryISO,
    date = date,
    round = round,
    hasQualifying = hasQualifying,
    hasResults = hasResults
)

fun SeasonItem.Companion.driverModel(
    season: Int = 2020,
    points: Double = 1.0,
    driver: Driver = Driver.model(),
    constructors: List<Constructor> = listOf(
        Constructor.model()
    ),
    driverId: String = driver.id,
    position: Int = 1,
    maxPointsInSeason: Double = 1.0,
    animationSpeed: AnimationSpeed = AnimationSpeed.QUICK
): SeasonItem.Driver = SeasonItem.Driver(
    season = season,
    points = points,
    driver = driver,
    constructors = constructors,
    driverId = driverId,
    position = position,
    maxPointsInSeason = maxPointsInSeason,
    animationSpeed = animationSpeed
)

fun SeasonItem.Companion.constructorModel(
    season: Int = 2020,
    position: Int = 1,
    constructor: Constructor = Constructor.model(),
    constructorId: String = constructor.id,
    driver: List<Pair<Driver, Double>> = listOf(
        Driver.model() to 1.0
    ),
    points: Double = 1.0,
    maxPointsInSeason: Double = 1.0,
    barAnimation: AnimationSpeed = AnimationSpeed.QUICK
): SeasonItem.Constructor = SeasonItem.Constructor(
    season = season,
    position = position,
    constructor = constructor,
    constructorId = constructorId,
    driver = driver,
    points = points,
    maxPointsInSeason = maxPointsInSeason,
    barAnimation = barAnimation
)

fun SeasonItem.Companion.errorModel(
    item: SyncDataItem = SyncDataItem.InternalError
): SeasonItem.ErrorItem = SeasonItem.ErrorItem(
    item = item
)