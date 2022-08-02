package tmg.flashback.stats.ui.weekend.race

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve
import java.time.Year


internal class RaceViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: RaceViewModel

    private fun initUnderTest() {
        underTest = RaceViewModel(
            raceRepository = mockRaceRepository,
            statsNavigationComponent = mockStatsNavigationComponent,
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
    fun `loading view with list of results`() {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                RaceModel.Result.model()
            ))
        }
    }

    @Test
    fun `clicking race result launches stats navigation component`() {
        initUnderTest()
        underTest.load(2020, 1)

        val input = RaceRaceResult.model()
        underTest.inputs.clickDriver(input)

        underTest.outputs.list.testObserve()

        verify {
            mockStatsNavigationComponent.driverSeason(
                id = input.driver.driver.id,
                name = input.driver.driver.name,
                season = 2020
            )
        }
    }
}