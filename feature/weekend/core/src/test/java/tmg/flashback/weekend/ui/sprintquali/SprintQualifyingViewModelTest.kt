package tmg.flashback.weekend.ui.sprintquali

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.QualifyingResult
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve
import java.time.Year

internal class SprintQualifyingViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: SprintQualifyingViewModel

    private fun initUnderTest() {
        underTest = SprintQualifyingViewModel(
            raceRepository = mockRaceRepository,
            navigator = mockNavigator,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `loading view with no race results in same year shows not available yet`() = runTest {
        val currentSeason = Year.now().value
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                SprintQualifyingModel.NotAvailableYet
            ), awaitItem())
        }
    }

    @Test
    fun `loading view with no race results in different year shows not available`() = runTest {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                SprintQualifyingModel.NotAvailable
            ), awaitItem())
        }
    }

    @Test
    fun `loading view with list of results for q1q2q3`() = runTest {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                SprintQualifyingModel.Result.model()
            ), awaitItem())
        }
    }

    @Test
    fun `clicking qualifying result launches stats navigation component`() = runTest {
        initUnderTest()
        underTest.load(2020, 1)

        val input = QualifyingResult.model()
        underTest.inputs.clickDriver(input.entry.driver)

        underTest.outputs.list.test {
            assertNotNull(awaitItem())
        }

        verify {
            mockNavigator.navigate(
                Screen.DriverSeason.with(
                    driverId = input.entry.driver.id,
                    driverName = input.entry.driver.name,
                    season = 2020
                ))
        }
    }
}