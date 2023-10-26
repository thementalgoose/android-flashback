package tmg.flashback.weekend.presentation.sprint

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.with
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.testutils.BaseTest
import java.time.Year

internal class SprintViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: SprintViewModel

    private fun initUnderTest() {
        underTest = SprintViewModel(
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
                SprintModel.NotAvailableYet
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
                SprintModel.NotAvailable
            ), awaitItem())
        }
    }

    @Test
    fun `loading view with list of driver results`() = runTest(testDispatcher) {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                SprintModel.DriverResult.model()
            ), awaitItem())
        }
    }

    @Test
    fun `loading view with list of constructor results`() = runTest(testDispatcher) {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.show(SprintResultType.CONSTRUCTORS)
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                SprintModel.ConstructorResult.model()
            ), awaitItem())
        }
    }

    @Test
    fun `clicking sprint driver result launches stats navigation component`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020, 1)

        val input = DriverEntry.model()
        underTest.inputs.clickDriver(input)

        underTest.outputs.list.test {
            assertNotNull(awaitItem())
        }

        verify {
            mockNavigator.navigate(
                Screen.Driver.with(
                    driverId = input.driver.id,
                    driverName = input.driver.name,
                )
            )
        }
    }

    @Test
    fun `clicking sprint constructor result launches stats navigation component`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020, 1)

        val input = Constructor.model()
        underTest.inputs.clickConstructor(input)

        underTest.outputs.list.test {
            assertNotNull(awaitItem())
        }

        verify {
            mockNavigator.navigate(
                Screen.Constructor.with(
                    constructorId = input.id,
                    constructorName = input.name,
                )
            )
        }
    }
}