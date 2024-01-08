package tmg.flashback.drivers.presentation.stathistory

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.formula1.model.DriverHistorySeasonRace
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.model
import tmg.testutils.BaseTest

internal class DriverStatHistoryViewModelTest: BaseTest() {

    private val mockDriverRepository: DriverRepository = mockk(relaxed = true)

    private lateinit var underTest: DriverStatHistoryViewModel

    private fun initUnderTest() = runTest(testDispatcher) {
        underTest = DriverStatHistoryViewModel(
            driverRepository = mockDriverRepository,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `initialise with championship type returns championship`() = runTest(testDispatcher) {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow {
            emit(DriverHistory.model(
                standings = listOf(
                    DriverHistorySeason.model(championshipStanding = 1, season = 2021, isInProgress = false),
                    DriverHistorySeason.model(championshipStanding = 1, season = 2019, isInProgress = false),
                    DriverHistorySeason.model(championshipStanding = 2, season = 2020, isInProgress = false),
                    DriverHistorySeason.model(championshipStanding = 1, season = 2020, isInProgress = true)
                )
            ))
        }

        initUnderTest()
        underTest.inputs.load("driverId", DriverStatHistoryType.CHAMPIONSHIPS)

        underTest.outputs.results.test {
            assertEquals(listOf(
                DriverStatHistoryModel.modelYear(season = 2021),
                DriverStatHistoryModel.modelYear(season = 2019),
            ), awaitItem())
        }
    }

    @Test
    fun `initialise with wins type returns wins`() = runTest(testDispatcher) {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow {
            emit(DriverHistory.model(
                standings = listOf(
                    DriverHistorySeason.model(
                        championshipStanding = 1,
                        season = 2021,
                        isInProgress = false,
                        raceOverview = listOf(
                            DriverHistorySeasonRace.model(
                                finished = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 3)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 2,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 1)
                            )
                        )
                    ),
                    DriverHistorySeason.model(
                        championshipStanding = 2,
                        season = 2020,
                        isInProgress = false,
                        raceOverview = listOf(
                            DriverHistorySeasonRace.model(
                                finished = 3,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 3)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 3,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 1)
                            ),
                        )
                    )
                )
            ))
        }

        initUnderTest()
        underTest.inputs.load("driverId", DriverStatHistoryType.WINS)

        underTest.outputs.results.test {
            assertEquals(listOf(
                DriverStatHistoryModel.modelLabel("2021"),
                DriverStatHistoryModel.modelRace(
                    raceInfo = RaceInfo.model(season = 2021, round = 3),
                ),
                DriverStatHistoryModel.modelRace(
                    raceInfo = RaceInfo.model(season = 2021, round = 1),
                ),
                DriverStatHistoryModel.modelLabel("2020"),
                DriverStatHistoryModel.modelRace(
                    raceInfo = RaceInfo.model(season = 2020, round = 2),
                ),
            ), awaitItem())
        }
    }



    @Test
    fun `initialise with podiums type returns podiums`() = runTest(testDispatcher) {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow {
            emit(DriverHistory.model(
                standings = listOf(
                    DriverHistorySeason.model(
                        championshipStanding = 1,
                        season = 2021,
                        isInProgress = false,
                        raceOverview = listOf(
                            DriverHistorySeasonRace.model(
                                finished = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 1)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 2,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 3)
                            )
                        )
                    ),
                    DriverHistorySeason.model(
                        championshipStanding = 2,
                        season = 2020,
                        isInProgress = false,
                        raceOverview = listOf(
                            DriverHistorySeasonRace.model(
                                finished = 3,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 1)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 3,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 3)
                            )
                        )
                    )
                )
            ))
        }

        initUnderTest()
        underTest.inputs.load("driverId", DriverStatHistoryType.PODIUMS)

        underTest.outputs.results.test {
            assertEquals(listOf(
                DriverStatHistoryModel.modelLabel("2021"),
                DriverStatHistoryModel.modelRacePosition(
                    position = 1,
                    raceInfo = RaceInfo.model(season = 2021, round = 3),
                ),
                DriverStatHistoryModel.modelRacePosition(
                    position = 2,
                    raceInfo = RaceInfo.model(season = 2021, round = 2),
                ),
                DriverStatHistoryModel.modelRacePosition(
                    position = 1,
                    raceInfo = RaceInfo.model(season = 2021, round = 1),
                ),
                DriverStatHistoryModel.modelLabel("2020"),
                DriverStatHistoryModel.modelRacePosition(
                    position = 3,
                    raceInfo = RaceInfo.model(season = 2020, round = 3),
                ),
                DriverStatHistoryModel.modelRacePosition(
                    position = 1,
                    raceInfo = RaceInfo.model(season = 2020, round = 2),
                ),
                DriverStatHistoryModel.modelRacePosition(
                    position = 3,
                    raceInfo = RaceInfo.model(season = 2020, round = 1),
                ),
            ), awaitItem())
        }
    }

    @Test
    fun `initialise with pole positions type returns poles`() = runTest(testDispatcher) {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow {
            emit(DriverHistory.model(
                standings = listOf(
                    DriverHistorySeason.model(
                        championshipStanding = 1,
                        season = 2021,
                        isInProgress = false,
                        raceOverview = listOf(
                            DriverHistorySeasonRace.model(
                                qualified = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 1)
                            ),
                            DriverHistorySeasonRace.model(
                                qualified = 2,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                qualified = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2021, round = 3)
                            )
                        )
                    ),
                    DriverHistorySeason.model(
                        championshipStanding = 2,
                        season = 2020,
                        isInProgress = false,
                        raceOverview = listOf(
                            DriverHistorySeasonRace.model(
                                qualified = 3,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 1)
                            ),
                            DriverHistorySeasonRace.model(
                                qualified = 1,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                qualified = 3,
                                isSprint = false,
                                raceInfo = RaceInfo.model(season = 2020, round = 3)
                            )
                        )
                    )
                )
            ))
        }

        initUnderTest()
        underTest.inputs.load("driverId", DriverStatHistoryType.POLES)

        underTest.outputs.results.test {
            assertEquals(listOf(
                DriverStatHistoryModel.modelLabel("2021"),
                DriverStatHistoryModel.modelRace(
                    raceInfo = RaceInfo.model(season = 2021, round = 3),
                ),
                DriverStatHistoryModel.modelRace(
                    raceInfo = RaceInfo.model(season = 2021, round = 1),
                ),
                DriverStatHistoryModel.modelLabel("2020"),
                DriverStatHistoryModel.modelRace(
                    raceInfo = RaceInfo.model(season = 2020, round = 2),
                ),
            ), awaitItem())
        }
    }
}