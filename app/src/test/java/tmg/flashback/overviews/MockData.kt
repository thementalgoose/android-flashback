package tmg.flashback.overviews

import android.graphics.Color
import org.threeten.bp.LocalDate
import tmg.flashback.currentYear
import tmg.flashback.repo.models.stats.*

const val mockDriverId: String = "mockDriver"
const val mockDriverFirstName: String = "firstname"
const val mockDriverLastName: String = "lastname"
const val mockDriverCode: String = "FIR"
const val mockDriverNumber: Int = 1
const val mockDriverWikiUrl: String = "https://www.wikipedia.com"
val mockDriverPhotoUrl: String? = "https://image.com"
val mockDriverDateOfBirth: LocalDate = LocalDate.of(1995, 4, 4)
const val mockDriverNationality: String = "British"
const val mockDriverNationalityISO: String = "GBR"

const val mockConstructorId: String = "mockConstructorId"
const val mockConstructorName: String = "constructor name"

const val mockCircuitId: String = "mockCircuitId"

//region Driver Overview

val mockDriverOverviewConstructor = SlimConstructor(
        id = mockConstructorId,
        name = mockConstructorName,
        color = Color.RED
)
val mockDriverOverviewConstructor2 = SlimConstructor(
        id = "${mockConstructorId}2",
        name = "${mockConstructorName}2",
        color = Color.GREEN
)
val mockDriverOverviewConstructor3 = SlimConstructor(
        id = "${mockConstructorId}3",
        name = "${mockConstructorName}3",
        color = Color.BLUE
)

val mockDriverOverviewRaceFirst = DriverOverviewRace(
        status = "Finished",
        finished = 1,
        points = 25,
        qualified = 1,
        round = 1,
        season = 2019,
        raceName = "Test GP",
        date = LocalDate.of(2020, 1, 2),
        constructor = null,
        circuitName = "Test Circuit",
        circuitId = mockCircuitId,
        circuitNationality = "Italy",
        circuitNationalityISO = "ITA"
)
val mockDriverOverviewRaceSecond = DriverOverviewRace(
        status = "Finished",
        finished = 2,
        points = 18,
        qualified = 3,
        round = 2,
        season = 2019,
        raceName = "TSecond GP",
        date = LocalDate.of(2020, 2, 2),
        constructor = null,
        circuitName = "Another circuit",
        circuitId = mockCircuitId,
        circuitNationality = "Italy",
        circuitNationalityISO = "ITA"
)

private val mockDriverOverviewStanding = DriverOverviewStanding(
        bestFinish = 2,
        bestFinishQuantity = 1,
        bestQualifying = 1,
        bestQualifyingQuantity = 1,
        championshipStanding = 2,
        isInProgress = false,
        points = 25,
        podiums = 1,
        races = 2,
        season = 2020,
        wins = 1,
        constructors = listOf(mockDriverOverviewConstructor),
        raceOverview = listOf(
                mockDriverOverviewRaceFirst,
                mockDriverOverviewRaceSecond
        )
)


/**
 * Constants
 */
val mockDriverOverview2019Standing = mockDriverOverviewStanding.copy(
        season = 2019,
        races = 2,
        raceOverview = listOf(
                mockDriverOverviewRaceFirst.copy(season = 2019, round = 1),
                mockDriverOverviewRaceSecond.copy(season = 2019, round = 2)
        )
)
val mockDriverOverview = DriverOverview(
        id = mockDriverId,
        firstName = mockDriverFirstName,
        lastName = mockDriverLastName,
        code = mockDriverCode,
        number = mockDriverNumber,
        wikiUrl = mockDriverWikiUrl,
        photoUrl = mockDriverPhotoUrl,
        dateOfBirth = mockDriverDateOfBirth,
        nationality = mockDriverNationality,
        nationalityISO = mockDriverNationalityISO,
        standings = listOf(
                mockDriverOverviewStanding.copy(
                        season = 2018,
                        championshipStanding = 1,
                        races = 2,
                        raceOverview = listOf(
                                mockDriverOverviewRaceFirst.copy(season = 2018, round = 1),
                                mockDriverOverviewRaceSecond.copy(season = 2018, round = 2)
                        )
                ),
                mockDriverOverview2019Standing
        )
)

// One of the standings items has a championship in progress
val mockDriverOverviewChampionshipInProgress = mockDriverOverview.copy(
        standings = listOf(
                mockDriverOverviewStanding.copy(season = 2018),
                mockDriverOverviewStanding.copy(
                        season = 2020,
                        isInProgress = true
                )
        )
)

// Season has completed and they won 1 world championship
val mockDriverOverviewWonChampionship = mockDriverOverview.copy(
        standings = listOf(
                mockDriverOverviewStanding.copy(
                        isInProgress = false,
                        championshipStanding = 1
                )
        )
)

// Season has completed and they havent won a world championship
val mockDriverOverviewNotWonChampionship = mockDriverOverview.copy(
        standings = listOf(
                mockDriverOverviewStanding.copy(
                        isInProgress = false,
                        championshipStanding = 2
                )
        )
)

// Driver is in the rookie season and doesn't have a championship standing yet
val mockDriverOverviewRookieSeason = mockDriverOverview.copy(
        standings = listOf(
                mockDriverOverviewStanding.copy(
                        isInProgress = true,
                        championshipStanding = 1
                )
        )
)

// Driver overview with two season with a year off inbetween them
val mockDriverOverviewTwoSeasonWithYearBetweenThem = mockDriverOverview.copy(
        standings = listOf(
                mockDriverOverviewStanding.copy(
                        season = 2019,
                        championshipStanding = 1
                ),
                mockDriverOverviewStanding.copy(
                        season = 2017,
                        championshipStanding = 2
                )
        )
)

// Driver overview with two season with a year off inbetween them
val mockDriverOverviewTwoSeasonWithYearBetweenThemEndingInCurrentYear = mockDriverOverview.copy(
        standings = listOf(
                mockDriverOverviewStanding.copy(
                        season = currentYear,
                        championshipStanding = 2
                ),
                mockDriverOverviewStanding.copy(
                        season = 2017,
                        championshipStanding = 1
                )
        )
)

// Driver overview with two season with a year off inbetween them
val mockDriverOverviewConstructorChangeThenYearOffEndingInCurrentSeason = mockDriverOverview.copy(
        standings = listOf(
                mockDriverOverviewStanding.copy(
                        season = 2011,
                        championshipStanding = 1,
                        constructors = listOf(
                                mockDriverOverviewConstructor
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = 2013,
                        constructors = listOf(
                                mockDriverOverviewConstructor,
                                mockDriverOverviewConstructor2
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = 2014,
                        constructors = listOf(
                                mockDriverOverviewConstructor
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = currentYear - 4,
                        constructors = listOf(
                                mockDriverOverviewConstructor,
                                mockDriverOverviewConstructor2,
                                mockDriverOverviewConstructor
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = currentYear - 3,
                        constructors = listOf(
                                mockDriverOverviewConstructor
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = currentYear - 2,
                        constructors = listOf(
                                mockDriverOverviewConstructor,
                                mockDriverOverviewConstructor2
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = currentYear - 1,
                        constructors = listOf(
                                mockDriverOverviewConstructor
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = currentYear,
                        isInProgress = true,
                        constructors = listOf(
                                mockDriverOverviewConstructor2
                        )
                )
        )
)

//endregion

//region Constructors

/**
 * MOCK Constructor data
 *
 * Because these objects are quite complicated, there are scenario test data that is setup and tested
 *   against
 */

val mockConstructorAlpha: Constructor = Constructor(
        id = "alpha",
        name = "alphaName",
        wikiUrl = "https:   //www.wiki.com",
        nationality = "British",
        nationalityISO = "GBR",
        color = Color.GREEN
)
val mockConstructorBeta: Constructor = Constructor(
        id = "beta",
        name = "betaName",
        wikiUrl = "https:   //www.wiki.com",
        nationality = "German",
        nationalityISO = "DEU",
        color = Color.BLUE
)

//endregion

//region Drivers

val mockDriver1: RoundDriver = RoundDriver(
        id = "1",
        firstName = "name1",
        lastName = "last1",
        code = "#123123",
        number = 12,
        wikiUrl = "https://www.wiki.com",
        photoUrl = "https://www.images.com",
        dateOfBirth = LocalDate.of(1995, 1, 1),
        nationality = "British",
        nationalityISO = "GBR",
        constructor = mockConstructorAlpha,
        constructorAtEndOfSeason = mockConstructorAlpha
)
val mockDriver2 = mockDriver1.copy(
        id = "2",
        firstName = "name2",
        lastName = "name2",
        constructor = mockConstructorBeta,
        constructorAtEndOfSeason = mockConstructorBeta
)
val mockDriver3 = mockDriver1.copy(
        id = "3",
        firstName = "name3",
        lastName = "name3",
        constructor = mockConstructorAlpha,
        constructorAtEndOfSeason = mockConstructorAlpha
)
val mockDriver4 = mockDriver1.copy(
        id = "4",
        firstName = "name4",
        lastName = "name4",
        constructor = mockConstructorBeta,
        constructorAtEndOfSeason = mockConstructorBeta
)

//endregion

//region Constructor Overview

/**
 * Setup a mock constructor overview object, with
 * - Constructor doing two seasons
 * - Each season had two drivers
 */
val mockConstructorOverviewStanding1Driver1 = ConstructorOverviewDriverStanding(
        driver = mockDriver1.toConstructorDriver(),
        bestFinish = 2,
        bestQualifying = 3,
        points = 12,
        finishesInP1 = 0,
        finishesInP2 = 1,
        finishesInP3 = 0,
        finishesInPoints = 1,
        qualifyingP1 = 0,
        qualifyingP2 = 0,
        qualifyingP3 = 1,
        qualifyingTop10 = 2,
        races = 2,
        championshipStanding = 1
)
val mockConstructorOverviewStanding1Driver2 = ConstructorOverviewDriverStanding(
        driver = mockDriver2.toConstructorDriver(),
        bestFinish = 3,
        bestQualifying = 1,
        points = 10,
        finishesInP1 = 0,
        finishesInP2 = 0,
        finishesInP3 = 1,
        finishesInPoints = 2,
        qualifyingP1 = 1,
        qualifyingP2 = 0,
        qualifyingP3 = 1,
        qualifyingTop10 = 2,
        races = 2,
        championshipStanding = 2
)
val mockConstructorOverviewStanding1 = ConstructorOverviewStanding(
        drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1,
                mockDriver2.id to mockConstructorOverviewStanding1Driver2
        ),
        isInProgress = false,
        championshipStanding = 2,
        points = 22,
        season = 2019,
        races = 2
)

val mockConstructorOverviewStanding2Driver1 = ConstructorOverviewDriverStanding(
        driver = mockDriver1.toConstructorDriver(),
        bestFinish = 3,
        bestQualifying = 3,
        points = 12,
        finishesInP1 = 0,
        finishesInP2 = 0,
        finishesInP3 = 2,
        finishesInPoints = 1,
        qualifyingP1 = 0,
        qualifyingP2 = 0,
        qualifyingP3 = 1,
        qualifyingTop10 = 2,
        races = 2,
        championshipStanding = 1
)
val mockConstructorOverviewStanding2Driver2 = ConstructorOverviewDriverStanding(
        driver = mockDriver2.toConstructorDriver(),
        bestFinish = 4,
        bestQualifying = 1,
        points = 17,
        finishesInP1 = 0,
        finishesInP2 = 0,
        finishesInP3 = 0,
        finishesInPoints = 3,
        qualifyingP1 = 1,
        qualifyingP2 = 0,
        qualifyingP3 = 1,
        qualifyingTop10 = 2,
        races = 3,
        championshipStanding = 2
)
val mockConstructorOverviewStanding2 = ConstructorOverviewStanding(
        drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding2Driver1,
                mockDriver2.id to mockConstructorOverviewStanding2Driver2
        ),
        isInProgress = false,
        championshipStanding = 1,
        points = 29,
        season = 2018,
        races = 3
)

val mockConstructorOverview = ConstructorOverview(
        id = mockConstructorAlpha.id,
        name = mockConstructorAlpha.name,
        wikiUrl = mockConstructorAlpha.wikiUrl,
        nationality = mockConstructorAlpha.nationality,
        nationalityISO = mockConstructorAlpha.nationalityISO,
        color = mockConstructorAlpha.color,
        standings = listOf(
                mockConstructorOverviewStanding1,
                mockConstructorOverviewStanding2
        )
)

// One of the standings items has a championship in progress
val mockConstructorOverviewChampionshipInProgress = mockConstructorOverview.copy(
        standings = listOf(
                mockConstructorOverviewStanding1.copy(season = 2018),
                mockConstructorOverviewStanding2.copy(
                        season = 2020,
                        isInProgress = true
                )
        )
)

// One of the standings items has a championship in progress
val mockConstructorOverviewChampionshipWon = mockConstructorOverview.copy(
        standings = listOf(
                mockConstructorOverviewStanding1.copy(season = 2018),
                mockConstructorOverviewStanding2.copy(
                        season = 2020,
                        championshipStanding = 1
                )
        )
)

// One of the standings items has a championship in progress
val mockConstructorOverviewChampionshipWonInProgress = mockConstructorOverview.copy(
        standings = listOf(
                mockConstructorOverviewStanding1.copy(season = 2018),
                mockConstructorOverviewStanding2.copy(
                        season = 2020,
                        championshipStanding = 1,
                        isInProgress = true
                )
        )
)

// One of the standings items has a championship in progress
val mockConstructorOverviewChampionshipNotWon = mockConstructorOverview.copy(
        standings = listOf(
                mockConstructorOverviewStanding1.copy(
                        season = 2018,
                        championshipStanding = 2
                ),
                mockConstructorOverviewStanding2.copy(
                        season = 2020,
                        championshipStanding = 4
                )
        )
)

// Constructor history item values
val mockConstructorOverviewStandings = mockConstructorOverview.copy(
        standings = listOf(
                mockConstructorOverviewStanding1.copy(
                        season = 2018
                ),
                mockConstructorOverviewStanding1.copy(
                        season = 2017
                ),
                mockConstructorOverviewStanding1.copy(
                        season = 2015
                ),
                mockConstructorOverviewStanding1.copy(
                        season = 2013
                ),
                mockConstructorOverviewStanding1.copy(
                        season = 2012
                ),
                mockConstructorOverviewStanding1.copy(
                        season = 2011
                ),
                mockConstructorOverviewStanding1.copy(
                        season = 2010
                )
        )
)

//endregion
