package tmg.flashback.race

import android.graphics.Color
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.repo.enums.RaceStatus
import tmg.flashback.repo.models.stats.*

val mockRoundName: String = "Round name"
val mockLocalDateOfRound1: LocalDate = LocalDate.of(2020, 5, 10)
val mockLocalTimeOfRound1: LocalTime = LocalTime.of(14, 10)

val mockConstructorAlpha: Constructor = Constructor(
        id = "alpha",
        name = "alphaName",
        wikiUrl = "https://www.wiki.com",
        nationality = "British",
        nationalityISO = "GBR",
        color = Color.GREEN
)
val mockConstructorBeta: Constructor = Constructor(
        id = "beta",
        name = "betaName",
        wikiUrl = "https://www.wiki.com",
        nationality = "German",
        nationalityISO = "DEU",
        color = Color.BLUE
)

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
        lastName = "name3"
)
val mockDriver4 = mockDriver1.copy(
        id = "4",
        firstName = "name4",
        lastName = "name4",
        constructor = mockConstructorBeta,
        constructorAtEndOfSeason = mockConstructorBeta
)

val mockCircuitCharlie = CircuitSummary(
        id = "charlie",
        name = "circuit",
        wikiUrl = "https://www.wiki.com",
        locality = "Italian",
        country = "Italy",
        countryISO = "ITA",
        locationLat = 51.1,
        locationLng = 1.03
)






val raceResultDriver4 = RoundRaceResult(
        driver = mockDriver4,
        time = LapTime(1,0,0,0),
        points = 20,
        grid = 3,
        qualified = 4,
        finish = 1,
        status = "Finished",
        fastestLap = null
)
val raceResultDriver3 = RoundRaceResult(
        driver = mockDriver3,
        time = LapTime(1,1,0,0),
        points = 15,
        grid = 4,
        qualified = 3,
        finish = 2,
        status = "Finished",
        fastestLap = null
)
val raceResultDriver2 = RoundRaceResult(
        driver = mockDriver2,
        time = LapTime(1,2,0,0),
        points = 10,
        grid = 2,
        qualified = 2,
        finish = 3,
        status = "Finished",
        fastestLap = null
)
val raceResultDriver1 = RoundRaceResult(
        driver = mockDriver1,
        time = LapTime(1,0,0,0),
        points = 5,
        grid = 1,
        qualified = 1,
        finish = 4,
        status = "Engine",
        fastestLap = null
)




/**
 * Q1: 1,3,2,4
 * Q2: 2,1,3
 * Q3: 1,2
 *
 * Grid penalty for driver3.
 * Grid: 1,2,4,3
 *
 * Finish: 4,3,2,1(DNF)
 */
val mockRound: Round = Round(
        season = 2019,
        round = 1,
        date = mockLocalDateOfRound1,
        time = mockLocalTimeOfRound1,
        name = "Round 1",
        drivers = listOf(mockDriver1, mockDriver2, mockDriver3),
        constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
        circuit = mockCircuitCharlie,
        q1 = mapOf(
                mockDriver1.id to RoundQualifyingResult(
                        driver = mockDriver1,
                        time = LapTime(0,1, 1, 0),
                        position = 1
                ),
                mockDriver2.id to RoundQualifyingResult(
                        driver = mockDriver2,
                        time = LapTime(0,1, 3, 0),
                        position = 3
                ),
                mockDriver3.id to RoundQualifyingResult(
                        driver = mockDriver3,
                        time = LapTime(0,1, 2, 0),
                        position = 2
                ),
                mockDriver4.id to RoundQualifyingResult(
                        driver = mockDriver4,
                        time = LapTime(0,1, 4, 0),
                        position = 4
                )
        ),
        q2 = mapOf(
                mockDriver1.id to RoundQualifyingResult(
                        driver = mockDriver1,
                        time = LapTime(0,1, 2, 0),
                        position = 2
                ),
                mockDriver2.id to RoundQualifyingResult(
                        driver = mockDriver2,
                        time = LapTime(0,1, 1, 0),
                        position = 1
                ),
                mockDriver3.id to RoundQualifyingResult(
                        driver = mockDriver3,
                        time = LapTime(0,1, 3, 0),
                        position = 3
                )
        ),
        q3 = mapOf(
                mockDriver1.id to RoundQualifyingResult(
                        driver = mockDriver1,
                        time = LapTime(0,1, 1, 0),
                        position = 1
                ),
                mockDriver2.id to RoundQualifyingResult(
                        driver = mockDriver2,
                        time = LapTime(0,1, 2, 0),
                        position = 2
                )
        ),
        race = mapOf(
                mockDriver1.id to raceResultDriver1,
                mockDriver2.id to raceResultDriver2,
                mockDriver3.id to raceResultDriver3,
                mockDriver4.id to raceResultDriver4
        )
)