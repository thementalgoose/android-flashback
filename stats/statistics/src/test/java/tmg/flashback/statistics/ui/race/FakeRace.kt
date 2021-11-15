package tmg.flashback.statistics.ui.race_old

import tmg.flashback.formula1.model.*

/**
 * Fake race model to check ordering
 *
 *              Q1      Q2      Q3      Qualifying    Race
 * driverId1    1       1       2       2             1
 * driverId2    3       2       1       1             2
 * driverId3    2       3               3             4
 * driverId4    4                       4             3
 */
internal val driver1 = DriverConstructor.model(driver = Driver.model("driverId1"))
internal val driver2 = DriverConstructor.model(driver = Driver.model("driverId2"))
internal val driver3 = DriverConstructor.model(driver = Driver.model("driverId3"))
internal val driver4 = DriverConstructor.model(driver = Driver.model("driverId4"))

internal val raceModel: Race = Race.model(
    qualifying = listOf(
        RaceQualifyingRound.model(
            label = RaceQualifyingType.Q1,
            order = 1,
            results = listOf(
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver1,
                    lapTime = LapTime.model(0, 1, 1, 0),
                    position = 2
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver2,
                    lapTime = LapTime.model(0, 1, 3, 0),
                    position = 1
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver3,
                    lapTime = LapTime.model(0, 1, 2, 0),
                    position = 3
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver4,
                    lapTime = LapTime.model(0, 1, 4, 0),
                    position = 4
                )
            )
        ),
        RaceQualifyingRound.model(
            label = RaceQualifyingType.Q2,
            order = 2,
            results = listOf(
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver1,
                    lapTime = LapTime.model(0, 1, 1, 0),
                    position = 2
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver2,
                    lapTime = LapTime.model(0, 1, 2, 0),
                    position = 1
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver3,
                    lapTime = LapTime.model(0, 1, 3, 0),
                    position = 3
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver4,
                    lapTime = null,
                    position = 4
                )
            )
        ),
        RaceQualifyingRound.model(
            label = RaceQualifyingType.Q3,
            order = 3,
            results = listOf(
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver1,
                    lapTime = LapTime.model(0, 1, 1, 0),
                    position = 2
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver2,
                    lapTime = LapTime.model(0, 1, 1, 0),
                    position = 1
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver3,
                    lapTime = null,
                    position = 3
                ),
                RaceQualifyingRoundDriver.Qualifying.model(
                    driver = driver4,
                    lapTime = null,
                    position = 4
                )
            )
        )
    ),
    race = listOf(
        RaceRaceResult.model(
            driver = driver1,
            time = LapTime.model(0, 1, 1, 0),
            qualified = 2,
            grid = 2,
            finish = 1,
            points = 25.0
        ),
        RaceRaceResult.model(
            driver = driver2,
            time = LapTime.model(0, 1, 2, 0),
            qualified = 1,
            grid = 1,
            finish = 2,
            points = 18.0
        ),
        RaceRaceResult.model(
            driver = driver3,
            time = LapTime.model(0, 1, 4, 0),
            qualified = 3,
            grid = 3,
            finish = 4,
            points = 12.0
        ),
        RaceRaceResult.model(
            driver = driver4,
            time = LapTime.model(0, 1, 3, 0),
            qualified = 4,
            grid = 4,
            finish = 3,
            points = 15.0
        )
    )
)