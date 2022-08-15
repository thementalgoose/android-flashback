package tmg.flashback.stats.ui.drivers.stathistory

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.repo.DriverRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class DriverStatHistoryViewModelTest: BaseTest() {

    private val mockDriverRepository: DriverRepository = mockk(relaxed = true)

    private lateinit var underTest: DriverStatHistoryViewModel

    private fun initUnderTest() {
        underTest = DriverStatHistoryViewModel(
            driverRepository = mockDriverRepository,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `initialise with championship type returns championship`() {
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
            assertValue(listOf(
                DriverStatHistoryModel.modelYear(season = 2019),
                DriverStatHistoryModel.modelYear(season = 2021)
            ))
        }
    }

    @Test
    fun `initialise with wins type returns wins`() {
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
                                raceInfo = RaceInfo.model(season = 2021, round = 1)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 2,
                                raceInfo = RaceInfo.model(season = 2021, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 1,
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
                                raceInfo = RaceInfo.model(season = 2020, round = 1)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 1,
                                raceInfo = RaceInfo.model(season = 2020, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                finished = 3,
                                raceInfo = RaceInfo.model(season = 2020, round = 3)
                            )
                        )
                    ),
                    DriverHistorySeason.model(
                        season = 2022,
                        isInProgress = true,
                        raceOverview = listOf(
                            DriverHistorySeasonRace.model(
                                finished = 1,
                                raceInfo = RaceInfo.model(season = 2022, round = 1)
                            )
                        )
                    )
                )
            ))
        }

        initUnderTest()
        underTest.inputs.load("driverId", DriverStatHistoryType.WINS)

        underTest.outputs.results.test {
            assertValue(listOf(
                DriverStatHistoryModel.modelLabel("2020"),
                DriverStatHistoryModel.modelRace(
                    season = 2020, round = 2,
                ),
                DriverStatHistoryModel.modelLabel("2021"),
                DriverStatHistoryModel.modelRace(
                    season = 2021, round = 1,
                ),
                DriverStatHistoryModel.modelRace(
                    season = 2021, round = 3,
                )
            ))
        }
    }

    @Test
    fun `initialise with pole positions type returns poles`() {
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
                                raceInfo = RaceInfo.model(season = 2021, round = 1)
                            ),
                            DriverHistorySeasonRace.model(
                                qualified = 2,
                                raceInfo = RaceInfo.model(season = 2021, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                qualified = 1,
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
                                raceInfo = RaceInfo.model(season = 2020, round = 1)
                            ),
                            DriverHistorySeasonRace.model(
                                qualified = 1,
                                raceInfo = RaceInfo.model(season = 2020, round = 2)
                            ),
                            DriverHistorySeasonRace.model(
                                qualified = 3,
                                raceInfo = RaceInfo.model(season = 2020, round = 3)
                            )
                        )
                    ),
                    DriverHistorySeason.model(
                        season = 2022,
                        isInProgress = true,
                        raceOverview = listOf(
                            DriverHistorySeasonRace.model(
                                qualified = 1,
                                raceInfo = RaceInfo.model(season = 2022, round = 1)
                            )
                        )
                    )
                )
            ))
        }

        initUnderTest()
        underTest.inputs.load("driverId", DriverStatHistoryType.POLES)

        underTest.outputs.results.test {
            assertValue(listOf(
                DriverStatHistoryModel.modelLabel("2020"),
                DriverStatHistoryModel.modelRace(
                    season = 2020, round = 2,
                ),
                DriverStatHistoryModel.modelLabel("2021"),
                DriverStatHistoryModel.modelRace(
                    season = 2021, round = 1,
                ),
                DriverStatHistoryModel.modelRace(
                    season = 2021, round = 3,
                )
            ))
        }
    }
}