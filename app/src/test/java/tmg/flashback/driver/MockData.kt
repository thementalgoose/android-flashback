package tmg.flashback.driver

import android.graphics.Color
import org.threeten.bp.LocalDate
import tmg.flashback.repo.models.stats.DriverOverview
import tmg.flashback.repo.models.stats.DriverOverviewRace
import tmg.flashback.repo.models.stats.DriverOverviewStanding
import tmg.flashback.repo.models.stats.SlimConstructor

const val mockDriverId: String = "mockDriver"
const val mockFirstName: String = "firstname"
const val mockLastName: String = "lastname"
const val mockCode: String = "FIR"
const val mockNumber: Int = 1
const val mockWikiUrl: String = "https://www.wikipedia.com"
val mockPhotoUrl: String? = "https://image.com"
val mockDateOfBirth: LocalDate = LocalDate.of(1995, 4, 4)
const val mockNationality: String = "British"
const val mockNationalityISO: String = "GBR"

const val mockConstructorId: String = "mockConstructorId"
const val mockConstructorName: String = "constructor name"

const val mockCircuitId: String = "mockCircuitId"

val mockDriverOverviewConstructor = SlimConstructor(
        id = mockConstructorId,
        name = mockConstructorName,
        color = Color.RED
)

val mockDriverOverviewRaceOverview = DriverOverviewRace(
        status = "Finished",
        finished = 1,
        points = 25,
        qualified = 1,
        round = 1,
        season = 2020,
        raceName = "Test GP",
        date = LocalDate.of(2020, 0,2),
        circuitName = "Test Circuit",
        circuitId = mockCircuitId,
        circuitNationality = "Italy",
        circuitNationalityISO = "ITA"
)

val mockDriverOverviewStanding = DriverOverviewStanding(
        bestFinish = 1,
        bestFinishQuantity = 1,
        bestQualifying = 1,
        bestQualifyingQuantity = 1,
        championshipStanding = 1,
        isInProgress = true,
        points = 25,
        podiums = 1,
        races = 1,
        season = 2020,
        wins = 1,
        constructors = listOf(mockDriverOverviewConstructor),
        raceOverview = listOf(mockDriverOverviewRaceOverview)
)

val mockDriverOverview = DriverOverview(
        id = mockDriverId,
        firstName = mockFirstName,
        lastName = mockLastName,
        code = mockCode,
        number = mockNumber,
        wikiUrl = mockWikiUrl,
        photoUrl = mockPhotoUrl,
        dateOfBirth = mockDateOfBirth,
        nationality = mockWikiUrl,
        nationalityISO = mockWikiUrl,
        standings = listOf(
                mockDriverOverviewStanding.copy(season = 2018),
                mockDriverOverviewStanding.copy(season = 2019)
        )
)