package tmg.flashback.formula1.extensions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.raceStatusFinish
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.model.DriverConstructors
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.RaceQualifyingResult
import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.formula1.model.Season
import tmg.flashback.formula1.model.SeasonDriverComparison
import tmg.flashback.formula1.model.SeasonDriverComparisonDriver
import tmg.flashback.formula1.model.model

internal class SeasonExtensionsTest {

    @Test
    fun `compare drivers with driver id not in seaosn returns null`() {
        assertNull(Season.model().compareDrivers(
            driverId1 = "invalid-driver-id",
            driverId2 = "another-invalid"
        ))
    }

    @Test
    fun `compare drivers returns season driver comparison object`() {

        val driver1 = Driver.model(id = "1")
        val driver2 = Driver.model(id = "2")
        val input = Season.model(
            races = listOf(
                Race.model(
                    raceInfo = RaceInfo.model(round = 1),
                    qualifying = emptyList(),
                    sprint = emptyList(),
                    race = listOf(
                        RaceRaceResult.model(
                            driver = DriverConstructor.model(driver = driver1),
                            finish = 1,
                            qualified = 2,
                            status = raceStatusFinish
                        ),
                        RaceRaceResult.model(
                            driver = DriverConstructor.model(driver = driver2),
                            finish = 4,
                            qualified = 3,
                            status = raceStatusFinish
                        ),
                    )
                ),
                Race.model(
                    raceInfo = RaceInfo.model(round = 2),
                    qualifying = emptyList(),
                    sprint = emptyList(),
                    race = listOf(
                        RaceRaceResult.model(
                            driver = DriverConstructor.model(driver = driver1),
                            finish = 4,
                            qualified = 5,
                            status = raceStatusFinish
                        ),
                        RaceRaceResult.model(
                            driver = DriverConstructor.model(driver = driver2),
                            finish = 3,
                            qualified = 3,
                            status = raceStatusFinish
                        ),
                    )
                ),
                Race.model(
                    raceInfo = RaceInfo.model(round = 3),
                    race = listOf(
                        RaceRaceResult.model(
                            driver = DriverConstructor.model(driver = driver1),
                            finish = 3,
                            qualified = 1,
                            status = raceStatusFinish
                        ),
                        RaceRaceResult.model(
                            driver = DriverConstructor.model(driver = driver2),
                            finish = 4,
                            qualified = 4,
                            status = "DNF"
                        ),
                    )
                ),
            )
        )

        val expected = SeasonDriverComparison(
            season = 2020,
            driver1 = DriverConstructors.model(
                driver = driver1
            ),
            driver1Stats = SeasonDriverComparisonDriver.model(
                finishes = 2,
                qualifying = 2,
                podiumCount = 2,
                winCount = 1,
                poleCount = 0,
                bestFinishPosition = 1,
                bestQualifyingPosition = 1,
                dnfCount = 0,
            ),
            driver2 = DriverConstructors.model(
                driver = driver2
            ),
            driver2Stats = SeasonDriverComparisonDriver.model(
                finishes = 1,
                qualifying = 1,
                podiumCount = 1,
                winCount = 0,
                poleCount = 0,
                bestFinishPosition = 3,
                bestQualifyingPosition = 3,
                dnfCount = 1,
            )
        )

        assertEquals(expected, input.compareDrivers(driver1.id, driver2.id))
    }
}