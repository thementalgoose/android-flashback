package tmg.flashback.weekend.ui.race

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import tmg.flashback.constructors.contract.ConstructorSeason
import tmg.flashback.constructors.contract.with
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.formula1.model.model
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve
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
    fun `loading view with no race results in same year shows not available yet`() {
        val currentSeason = Year.now().value
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                RaceModel.NotAvailableYet
            ))
        }
    }

    @Test
    fun `loading view with no race results in different year shows not available`() {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                RaceModel.NotAvailable
            ))
        }
    }

    @Test
    fun `loading view with list of results for driver`() {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                RaceModel.DriverResult.model()
            ))
        }
    }

    @Test
    fun `loading view with list of results for constructor`() {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.show(RaceResultType.CONSTRUCTORS)
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                RaceModel.ConstructorResult.model()
            ))
        }
    }

    @Test
    fun `race result type updates when item is changed`() {
        initUnderTest()

        val observer = underTest.raceResultType.testObserve()
        observer.assertValue(RaceResultType.DRIVERS)

        underTest.show(RaceResultType.CONSTRUCTORS)
        observer.assertValue(RaceResultType.CONSTRUCTORS)

        underTest.show(RaceResultType.DRIVERS)
        observer.assertValue(RaceResultType.DRIVERS)
    }

    @Test
    fun `clicking race result launches stats navigation component`() {
        initUnderTest()
        underTest.load(2020, 1)

        val input = RaceResult.model()
        underTest.inputs.clickDriver(input)

        underTest.outputs.list.testObserve()

        verify {
            mockNavigator.navigate(
                Screen.DriverSeason.with(
                    driverId = input.driver.driver.id,
                    driverName = input.driver.driver.name,
                    season = 2020
                ))
        }
    }

    @Test
    fun `clicking driver race result launches stats navigation component`() {
        initUnderTest()
        underTest.load(2020, 1)

        val input = Constructor.model()
        underTest.inputs.clickConstructor(input)

        underTest.outputs.list.testObserve()

        verify {
            mockNavigator.navigate(
                Screen.ConstructorSeason.with(
                    constructorId = input.id,
                    constructorName = input.name,
                    season = 2020
                ))
        }
    }
}