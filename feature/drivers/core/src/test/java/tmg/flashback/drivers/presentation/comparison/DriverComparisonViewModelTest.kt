package tmg.flashback.drivers.presentation.comparison

import app.cash.turbine.test
import app.cash.turbine.testIn
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.formula1.model.Season
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.model.model
import tmg.testutils.BaseTest

internal class DriverComparisonViewModelTest: BaseTest() {

    private lateinit var underTest: DriverComparisonViewModel

    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockSeasonRepository: SeasonRepository = mockk(relaxed = true)

    private fun initUnderTest() {
        underTest = DriverComparisonViewModel(
            seasonRepository = mockSeasonRepository,
            networkConnectivityManager = mockNetworkConnectivityManager,
            ioDispatcher = testDispatcher
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockSeasonRepository.getSeason(2024) } returns flow { emit(season) }
        every { mockSeasonRepository.getDriverStandings(2024) } returns flow { emit(
            SeasonDriverStandings.model(standings = listOf(
                SeasonDriverStandingSeason.model(season = 2024, driver = driverLeft, points = 10.0),
                SeasonDriverStandingSeason.model(season = 2024, driver = driverRight, points = 20.0)
            ))
        )}
        every { mockNetworkConnectivityManager.isConnected } returns true
    }

    @Test
    fun `setup pulls race list from repo`() = runTest {
        initUnderTest()
        underTest.inputs.setup(2024)

        underTest.outputs.uiState.test {
            val value = awaitItem()
            assertEquals(listOf(Driver.model(), driverLeft, driverRight), value.driverList)
            assertEquals(false, value.isLoading)
            assertEquals(true, value.networkAvailable)
        }
        verify {
            mockSeasonRepository.getSeason(2024)
        }
        coVerify {
            mockSeasonRepository.fetchRaces(2024)
        }
    }

    @Test
    fun `select left driver refreshes state`() = runTest {
        initUnderTest()
        underTest.inputs.setup(2024)
        underTest.inputs.selectDriverLeft(driverLeft.id)

        underTest.outputs.uiState.test {
            val value = awaitItem()
            assertEquals(driverLeft, value.driverLeft)
        }
    }

    @Test
    fun `select right driver refreshes state`() = runTest {
        initUnderTest()
        underTest.inputs.setup(2024)
        underTest.inputs.selectDriverRight(driverRight.id)

        underTest.outputs.uiState.test {
            val value = awaitItem()
            assertEquals(driverRight, value.driverRight)
        }
    }

    @Test
    fun `swapping drivers refreshes state with mirrored data`() = runTest {
        initUnderTest()

        underTest.inputs.setup(2024)
        underTest.inputs.selectDriverRight(driverRight.id)
        underTest.inputs.selectDriverLeft(driverLeft.id)

        advanceUntilIdle()

        underTest.outputs.uiState.test {
            awaitItem().let {
                assertEquals(driverRight, it.driverRight)
                assertEquals(driverLeft, it.driverLeft)
            }

            underTest.inputs.swapDrivers()

            awaitItem().let {
                assertEquals(driverRight, it.driverLeft)
                assertEquals(driverLeft, it.driverRight)
            }

            underTest.inputs.selectDriverLeft(null)

            awaitItem().let {
                assertEquals(null, it.driverLeft)
                assertEquals(driverLeft, it.driverRight)
            }

            underTest.inputs.swapDrivers()

            awaitItem().let {
                assertEquals(driverLeft, it.driverLeft)
                assertEquals(null, it.driverRight)
            }

            underTest.inputs.selectDriverLeft(null)

            awaitItem().let {
                assertEquals(null, it.driverLeft)
                assertEquals(null, it.driverRight)
            }
        }
    }

    @Test
    fun `setup and selecting drivers populates stats`() = runTest {
        initUnderTest()

        underTest.inputs.setup(2024)
        underTest.inputs.selectDriverRight(driverRight.id)
        underTest.inputs.selectDriverLeft(driverLeft.id)

        advanceUntilIdle()

        underTest.outputs.uiState.test {

            val result = awaitItem()

            assertEquals(expectedLeft, result.comparison?.left)
            assertEquals(expectedRight, result.comparison?.right)
            assertEquals(listOf(Constructor.model()), result.comparison?.leftConstructors)
            assertEquals(listOf(Constructor.model()), result.comparison?.rightConstructors)

            underTest.swapDrivers()

            val swappedResult = awaitItem()

            assertEquals(expectedLeft, swappedResult.comparison?.right)
            assertEquals(expectedRight, swappedResult.comparison?.left)
        }
    }

    companion object {

        private val driverLeft: Driver = Driver.model(id = "1")
        private val driverRight: Driver = Driver.model(id = "2")
        private val races = listOf(
            Race.model(
                raceInfo = RaceInfo.model(round = 1),
                race = listOf(
                    RaceResult.model(
                        driver = DriverEntry.model(driver = driverLeft),
                        grid = 3,
                        qualified = 3,
                        finish = 1,
                        status = RaceStatus.FINISHED
                    ),
                    RaceResult.model(
                        driver = DriverEntry.model(driver = driverRight),
                        grid = 4,
                        qualified = 7,
                        finish = 3,
                        status = RaceStatus.FINISHED
                    )
                )
            ),
            Race.model(
                raceInfo = RaceInfo.model(round = 2),
                race = listOf(
                    RaceResult.model(
                        driver = DriverEntry.model(driver = driverLeft),
                        grid = 7,
                        qualified = 2,
                        finish = 4,
                        status = RaceStatus.FINISHED
                    ),
                    RaceResult.model(
                        driver = DriverEntry.model(driver = driverRight),
                        grid = 3,
                        qualified = 4,
                        finish = 2,
                        status = RaceStatus.FINISHED
                    )
                )
            ),
            Race.model(
                raceInfo = RaceInfo.model(round = 3),
                race = listOf(
                    RaceResult.model(
                        driver = DriverEntry.model(driver = driverLeft),
                        grid = 9,
                        qualified = 8,
                        finish = 1,
                        status = RaceStatus.FINISHED
                    ),
                    RaceResult.model(
                        driver = DriverEntry.model(driver = driverRight),
                        grid = 7,
                        qualified = 1,
                        finish = 11,
                        status = RaceStatus.WITHDREW
                    )
                )
            )
        )
        private val season: Season = Season.model(
            season = 2024,
            races = races
        )


        val expectedLeft = ComparisonValue(
            racesHeadToHead = 2,
            qualifyingHeadToHead = 2,
            points = 10.0,
            pointsFinishes = 3,
            podiums = 2,
            wins = 2,
            dnfs = 0,
        )

        val expectedRight = ComparisonValue(
            racesHeadToHead = 1,
            qualifyingHeadToHead = 1,
            points = 20.0,
            pointsFinishes = 3,
            podiums = 2,
            wins = 0,
            dnfs = 1,
        )
    }
}