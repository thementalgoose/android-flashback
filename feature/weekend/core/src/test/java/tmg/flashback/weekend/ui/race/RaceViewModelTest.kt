package tmg.flashback.weekend.ui.race

import app.cash.turbine.Event
import app.cash.turbine.test
import app.cash.turbine.testIn
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


internal class RaceViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: RaceViewModel

    private fun initUnderTest() {
        underTest = RaceViewModel(
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
                RaceModel.NotAvailableYet
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
                RaceModel.NotAvailable
            ), awaitItem())
        }
    }

    @Test
    fun `loading view with list of results for driver`() = runTest(testDispatcher) {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                RaceModel.DriverResult.model()
            ), awaitItem())
        }
    }

    @Test
    fun `loading view with list of results for constructor`() = runTest(testDispatcher) {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.show(RaceResultType.CONSTRUCTORS)
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                RaceModel.ConstructorResult.model()
            ), awaitItem())
        }
    }

    @Test
    fun `race result type updates when item is changed`() = runTest(testDispatcher) {
        initUnderTest()

        val observer = underTest.raceResultType.testIn(this)

        underTest.show(RaceResultType.CONSTRUCTORS)

        underTest.show(RaceResultType.DRIVERS)

        val list = observer.cancelAndConsumeRemainingEvents()
        assertEquals(RaceResultType.DRIVERS, (list[0] as Event.Item<RaceResultType>).value)
        assertEquals(RaceResultType.CONSTRUCTORS, (list[1] as Event.Item<RaceResultType>).value)
        assertEquals(RaceResultType.DRIVERS, (list[2] as Event.Item<RaceResultType>).value)
        assertEquals(3, list.size)
    }

    @Test
    fun `clicking race result launches stats navigation component`() = runTest(testDispatcher) {
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
                    driverName = input.driver.name
                ))
        }
    }

    @Test
    fun `clicking driver race result launches stats navigation component`() = runTest(testDispatcher) {
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
                    constructorName = input.name
                ))
        }
    }
}