package tmg.flashback.firebase.models

internal fun FHistorySeason.Companion.model(
    all: Map<String, Map<String, FHistorySeasonRound?>?> = mapOf(
        "s2020" to mapOf(
            "1" to FHistorySeasonRound.model()
        )
    ),
    win: Map<String, FHistorySeasonWin>? = mapOf(
        "s2020" to FHistorySeasonWin.model()
    )
): FHistorySeason = FHistorySeason(
    all = all,
    win = win
)

internal fun FHistorySeasonRound.Companion.model(
    date: String = "2020-01-01",
    r: Int = 1, // Round
    s: Int = 2020, // Season
    circuitId: String = "circuitId",
    country: String = "country",
    countryISO: String = "countryISO",
    circuit: String = "circuit",
    name: String = "name",
    data: Boolean? = true,
    hasQ: Boolean? = true,
    hasR: Boolean? = true
): FHistorySeasonRound = FHistorySeasonRound(
    date = date,
    r = r,
    s = s,
    circuitId = circuitId,
    country = country,
    countryISO = countryISO,
    circuit = circuit,
    name = name,
    data = data,
    hasQ = hasQ,
    hasR = hasR
)

internal fun FHistorySeasonWin.Companion.model(
    s: Int = 2020, // Season
    constr: List<FHistorySeasonWinConstructor>? = listOf(FHistorySeasonWinConstructor.model()),
    driver: List<FHistorySeasonWinDriver>? = listOf(FHistorySeasonWinDriver.model())
): FHistorySeasonWin = FHistorySeasonWin(
    s = s,
    constr = constr,
    driver = driver
)

internal fun FHistorySeasonWinConstructor.Companion.model(
    id: String = "constructorId",
    name: String = "constructorName",
    color: String = "constructorColor",
    p: Int = 1 // Points
): FHistorySeasonWinConstructor = FHistorySeasonWinConstructor(
    id = id,
    name = name,
    color = color,
    p = p
)

internal fun FHistorySeasonWinDriver.Companion.model(
    id: String = "driverId",
    name: String = "driverName",
    img: String = "driverImg",
    p: Int = 2 // Points
): FHistorySeasonWinDriver = FHistorySeasonWinDriver(
    id = id,
    name = name,
    img = img,
    p = p
)