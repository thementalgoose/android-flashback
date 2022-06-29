package tmg.flashback.stats.ui.weekend.constructor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.di.StatsNavigator
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import java.time.Year

internal class ConstructorOverviewViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockStatsNavigator: StatsNavigator = mockk(relaxed = true)

    private lateinit var underTest: ConstructorViewModel

    private fun initUnderTest() {
        underTest = ConstructorViewModel(
            raceRepository = mockRaceRepository,
            statsNavigator = mockStatsNavigator,
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
                ConstructorModel.NotAvailableYet
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
                ConstructorModel.NotAvailable
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
                ConstructorModel.Constructor.model()
            ))
        }
    }

    @Test
    fun `clicking item goes to constructor overview`() {
        initUnderTest()
        underTest.inputs.clickItem(ConstructorModel.Constructor.model())

        verify {
            mockStatsNavigator.goToConstructorOverview(
                constructorId = ConstructorModel.Constructor.model().constructor.id,
                constructorName = ConstructorModel.Constructor.model().constructor.name
            )
        }
    }
}