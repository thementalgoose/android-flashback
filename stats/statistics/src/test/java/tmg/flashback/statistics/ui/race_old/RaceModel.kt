package tmg.flashback.statistics.ui.race_old

import org.threeten.bp.LocalDate
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

fun RaceModel.Companion.overviewModel(
    raceName: String = "name",
    country: String = "country",
    countryISO: String = "countryISO",
    circuitId: String = "circuitId",
    circuitName: String = "circuitName",
    round: Int = 1,
    season: Int = 2020,
    raceDate: LocalDate? = LocalDate.of(2020, 10, 12),
    wikipedia: String? = "wikiUrl"
): RaceModel.Overview = RaceModel.Overview(
    raceName = raceName,
    country = country,
    countryISO = countryISO,
    circuitId = circuitId,
    circuitName = circuitName,
    round = round,
    season = season,
    raceDate = raceDate,
    wikipedia = wikipedia
)

fun RaceModel.Companion.podiumModel(
    driverFirst: RaceModel.Single = singleModel(),
    driverSecond: RaceModel.Single = singleModel(),
    driverThird: RaceModel.Single = singleModel(),
): RaceModel.Podium = RaceModel.Podium(
    driverFirst = driverFirst,
    driverSecond = driverSecond,
    driverThird = driverThird
)

fun RaceModel.Companion.singleModel(
    season: Int = 2020,
    round: Int = 1,
    driver: DriverConstructor = DriverConstructor.model(),
    q1: RaceQualifyingResult_Legacy? = RaceQualifyingResult_Legacy.model(),
    q2: RaceQualifyingResult_Legacy? = RaceQualifyingResult_Legacy.model(),
    q3: RaceQualifyingResult_Legacy? = RaceQualifyingResult_Legacy.model(),
    qSprint: RaceSprintQualifyingResult_Legacy? = RaceSprintQualifyingResult_Legacy.model(),
    race: SingleRace? = SingleRace(
        points = 1.0,
        result = LapTime.model(),
        pos = 1,
        gridPos = 1,
        status = "Finished",
        fastestLap = true
    ),
    qualified: Int? = 1,
    q1Delta: String? = "0.001",
    q2Delta: String? = "0.001",
    q3Delta: String? = "0.001",
    displayPrefs: DisplayPrefs = DisplayPrefs(
        q1 = true,
        q2 = true,
        q3 = true,
        deltas = false,
        penalties = true,
        fadeDNF = true
    )
): RaceModel.Single = RaceModel.Single(
    season = season,
    round = round,
    driver = driver,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    qSprint = qSprint,
    race = race,
    qualified = qualified,
    q1Delta = q1Delta,
    q2Delta = q2Delta,
    q3Delta = q3Delta,
    displayPrefs = displayPrefs,
)

fun RaceModel.Companion.raceHeaderModel(
    season: Int = 2020,
    round: Int = 1
): RaceModel.RaceHeader = RaceModel.RaceHeader(
    season = season,
    round = round
)

fun RaceModel.Companion.qualifyingHeaderModel(
    displayPrefs: DisplayPrefs = DisplayPrefs(
        q1 = true,
        q2 = true,
        q3 = true,
        deltas = false,
        penalties = true,
        fadeDNF = true
    )
): RaceModel.QualifyingHeader = RaceModel.QualifyingHeader(
    displayPrefs = displayPrefs
)

fun RaceModel.Companion.constructorStandingsModel(
    constructor: Constructor = Constructor.model(),
    points: Double = 1.0,
    driver: List<Pair<Driver, Double>> = listOf(
        Driver.model() to 1.0
    ),
    animationSpeed: AnimationSpeed = AnimationSpeed.QUICK
): RaceModel.ConstructorStandings = RaceModel.ConstructorStandings(
    constructor = constructor,
    points = points,
    driver = driver,
    animationSpeed = animationSpeed
)

fun RaceModel.Companion.errorItemModel(
    item: SyncDataItem = SyncDataItem.InternalError
): RaceModel.ErrorItem = RaceModel.ErrorItem(
    item = item
)