package tmg.flashback.weekend.presentation.qualifying

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import tmg.flashback.data.repo.RaceRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.QualifyingResult
import tmg.flashback.formula1.model.QualifyingRound
import tmg.flashback.formula1.model.QualifyingType
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.testutils.BaseTest
import java.time.Year

internal class QualifyingViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: QualifyingViewModel

    private fun initUnderTest() {
        underTest = QualifyingViewModel(
            raceRepository = mockRaceRepository,
            navigator = mockNavigator,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `loading view with no race results in same year shows not available yet`() = runTest(testDispatcher) {
        val currentSeason = Year.now().value
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                QualifyingModel.NotAvailableYet
            ), awaitItem())
        }
    }

    @Test
    fun `loading view with no race results in different year shows not available`() = runTest(testDispatcher) {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                QualifyingModel.NotAvailable
            ), awaitItem())
        }
    }

    @Test
    fun `loading view with list of results for q1q2q3`() = runTest(testDispatcher) {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                QualifyingModel.Q1Q2Q3.model()
            ), awaitItem())
        }
    }


    @Test
    fun `loading view with list of results for q1q2`() = runTest(testDispatcher) {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model(
            qualifying = listOf(
                QualifyingRound.model(label = QualifyingType.Q1, results = listOf(
                    QualifyingResult.model(lapTime = LapTime.model(0,1,2,1))
                )),
                QualifyingRound.model(label = QualifyingType.Q2, results = listOf(
                    QualifyingResult.model(lapTime = LapTime.model(0,1,2,2))
                ))
            )
        )) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                QualifyingModel.Q1Q2.model()
            ), awaitItem())
        }
    }


    @Test
    fun `loading view with list of results for q1`() = runTest(testDispatcher) {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model(
            qualifying = listOf(
                QualifyingRound.model(label = QualifyingType.Q1, results = listOf(
                    QualifyingResult.model(lapTime = LapTime.model(0,1,2,1))
                ))
            )
        )) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                QualifyingModel.Q1.model()
            ), awaitItem())
        }
    }

    @Test
    fun `clicking qualifying result launches stats navigation component`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020, 1)

        val input = QualifyingResult.model()
        underTest.inputs.clickDriver(input.entry.driver)

        underTest.outputs.list.test {
            assertNotNull(awaitItem())
        }

        verify {
            mockNavigator.navigate(
                Screen.Driver.with(
                    driverId = input.entry.driver.id,
                    driverName = input.entry.driver.name,
                )
            )
        }
    }
}