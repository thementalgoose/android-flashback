package tmg.flashback.driver_old

import android.graphics.Color
import org.threeten.bp.LocalDate
import tmg.flashback.currentYear
import tmg.flashback.repo.models.stats.DriverOverview
import tmg.flashback.repo.models.stats.DriverOverviewRace
import tmg.flashback.repo.models.stats.DriverOverviewStanding
import tmg.flashback.repo.models.stats.SlimConstructor

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
                        season = 2016,
                        constructors = listOf(
                                mockDriverOverviewConstructor,
                                mockDriverOverviewConstructor2,
                                mockDriverOverviewConstructor
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = 2017,
                        constructors = listOf(
                                mockDriverOverviewConstructor
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = 2018,
                        constructors = listOf(
                                mockDriverOverviewConstructor,
                                mockDriverOverviewConstructor2
                        )
                ),
                mockDriverOverviewStanding.copy(
                        season = 2019,
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