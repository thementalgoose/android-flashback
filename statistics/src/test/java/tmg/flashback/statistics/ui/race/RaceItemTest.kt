package tmg.flashback.statistics.ui.race

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.ui.model.AnimationSpeed

fun RaceItem.Companion.overviewModel(
    raceName: String = "name",
    country: String = "country",
    countryISO: String = "countryISO",
    circuitId: String = "circuitId",
    circuitName: String = "circuitName",
    round: Int = 1,
    season: Int = 2020,
    laps: String = "12",
    raceDate: LocalDate? = LocalDate.of(2020, 10, 12),
    wikipedia: String = "wikiUrl",
    youtube: String = "youtube",
    schedule: List<Schedule> = listOf(Schedule.model())
): RaceItem.Overview = RaceItem.Overview(
    raceName = raceName,
    country = country,
    countryISO = countryISO,
    circuitId = circuitId,
    circuitName = circuitName,
    round = round,
    season = season,
    raceDate = raceDate,
    laps = laps,
    wikipedia = wikipedia,
    youtube = youtube,
    schedule = schedule
)

fun RaceItem.Companion.scheduleMaxModel(
    schedule: List<Schedule> = listOf(Schedule.model())
): RaceItem.ScheduleMax = RaceItem.ScheduleMax(
    schedule = schedule
)

fun RaceItem.Companion.podiumModel(
    driverFirst: RaceRaceResult = RaceRaceResult.model(),
    driverSecond: RaceRaceResult = RaceRaceResult.model(),
    driverThird: RaceRaceResult = RaceRaceResult.model()
): RaceItem.Podium = RaceItem.Podium(
    driverFirst = driverFirst,
    driverSecond = driverSecond,
    driverThird = driverThird
)

fun RaceItem.Companion.raceResultModel(
    race: RaceRaceResult = RaceRaceResult.model()
): RaceItem.RaceResult = RaceItem.RaceResult(
    race = race
)

fun RaceItem.Companion.sprintQualifyingResultModel(
    qSprint: RaceQualifyingResult.SprintQualifying = RaceQualifyingResult.SprintQualifying.model()
): RaceItem.SprintQualifyingResult = RaceItem.SprintQualifyingResult(
    qSprint = qSprint
)

fun RaceItem.Companion.qualifyingQ1Q2Q3ResultModel(
    driver: DriverConstructor = DriverConstructor.model(),
    finalQualifyingPosition: Int? = 1,
    q1: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    q2: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    q3: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    q1Delta: String? = null,
    q2Delta: String? = null,
    q3Delta: String? = null,
): RaceItem.QualifyingResultQ1Q2Q3 = RaceItem.QualifyingResultQ1Q2Q3(
    driver = driver,
    finalQualifyingPosition = finalQualifyingPosition,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    q1Delta = q1Delta,
    q2Delta = q2Delta,
    q3Delta = q3Delta
)

fun RaceItem.Companion.qualifyingQ1Q2ResultModel(
    driver: DriverConstructor = DriverConstructor.model(),
    finalQualifyingPosition: Int? = 1,
    q1: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    q2: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    q1Delta: String? = null,
    q2Delta: String? = null,
): RaceItem.QualifyingResultQ1Q2 = RaceItem.QualifyingResultQ1Q2(
    driver = driver,
    finalQualifyingPosition = finalQualifyingPosition,
    q1 = q1,
    q2 = q2,
    q1Delta = q1Delta,
    q2Delta = q2Delta,
)

fun RaceItem.Companion.qualifyingQ1ResultModel(
    driver: DriverConstructor = DriverConstructor.model(),
    finalQualifyingPosition: Int? = 1,
    q1: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    q1Delta: String? = null,
): RaceItem.QualifyingResultQ1 = RaceItem.QualifyingResultQ1(
    driver = driver,
    finalQualifyingPosition = finalQualifyingPosition,
    q1 = q1,
    q1Delta = q1Delta,
)

fun RaceItem.Companion.qualifyingHeaderModel(
    showQ1: Boolean = true,
    showQ2: Boolean = true,
    showQ3: Boolean = true
): RaceItem.QualifyingHeader = RaceItem.QualifyingHeader(
    showQ1 = showQ1,
    showQ2 = showQ2,
    showQ3 = showQ3
)

fun RaceItem.Companion.constructorModel(
    constructor: tmg.flashback.formula1.model.Constructor = Constructor.model(),
    points: Double = 1.0,
    driver: List<Pair<Driver, Double>> = listOf(
        Driver.model() to 1.0
    ),
    animationSpeed: AnimationSpeed = AnimationSpeed.QUICK,
    maxTeamPoints: Double = 70.0
): RaceItem.Constructor = RaceItem.Constructor(
    constructor = constructor,
    points = points,
    driver = driver,
    animationSpeed = animationSpeed,
    maxTeamPoints = maxTeamPoints
)

fun RaceItem.Companion.errorItemModel(
    item: SyncDataItem = SyncDataItem.InternalError
): RaceItem.ErrorItem = RaceItem.ErrorItem(
    item = item
)