package tmg.flashback.weekend.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.SprintResult
import tmg.flashback.formula1.model.model
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.model.ScreenWeekendNav
import tmg.testutils.BaseTest

internal class WeekendViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)

    private lateinit var underTest: WeekendViewModel

    private fun underTest(screenWeekendData: ScreenWeekendData = fakeScreenWeekendData, tab: ScreenWeekendNav? = null) {
        underTest = WeekendViewModel(
            raceRepository = mockRaceRepository,
            savedStateHandle = SavedStateHandle(mapOf(
                "data" to screenWeekendData,
                "tab" to (tab?.name ?: "")
            )),
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockRaceRepository.getRace(season = 2020, round = 1) } returns flow { emit(Race.model()) }
    }

    @Test
    fun `loading season and round outputs weekend info`() = runTest(testDispatcher) {
        underTest()

        underTest.outputs.weekendInfo.test {
            assertEquals(RaceInfo.model().toWeekendInfo(), awaitItem())
        }
    }

    @Test
    fun `loading season and round outputs default schedule tab`() = runTest(testDispatcher) {
        every { mockRaceRepository.getRace(season = currentSeasonYear, round = 1) } returns flow { emit(Race.model()) }
        underTest(fakeScreenWeekendData.copy(
            season = currentSeasonYear,
            round = 1,
            dateString = "31/12/$currentSeasonYear"
        ))

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.SCHEDULE })
        }
    }

    @Test
    fun `loading season and round outputs default race tab when overridden`() = runTest(testDispatcher) {
        every { mockRaceRepository.getRace(season = currentSeasonYear, round = 1) } returns flow { emit(Race.model()) }
        underTest(fakeScreenWeekendData.copy(
            season = currentSeasonYear,
            round = 1,
            dateString = "31/12/$currentSeasonYear"
        ))

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.SCHEDULE })
        }
    }

    @Test
    fun `loading season and round outputs on not current season year defaults to race tab`() = runTest(testDispatcher) {
        every { mockRaceRepository.getRace(season = currentSeasonYear, round = 1) } returns flow { emit(Race.model()) }
        underTest()

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.SCHEDULE})
        }
    }

    @Test
    fun `loading season and round with no sprint quali hides quali tab`() = runTest(testDispatcher) {
        every { mockRaceRepository.getRace(season = any(), round = any()) } returns flow { emit(Race.model(
            sprint = SprintResult.model(qualifying = emptyList(), race = emptyList())
        )) }

        underTest()

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.tab == WeekendNavItem.SCHEDULE })
            assertTrue(item.any { it.tab == WeekendNavItem.QUALIFYING })
            assertTrue(item.none { it.tab == WeekendNavItem.SPRINT })
            assertTrue(item.any { it.tab == WeekendNavItem.RACE })
        }
    }

    @Test
    fun `loading season and round with sprint quali shows all tabs`() = runTest(testDispatcher) {
        underTest()

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.tab == WeekendNavItem.SCHEDULE })
            assertTrue(item.any { it.tab == WeekendNavItem.QUALIFYING })
            assertTrue(item.any { it.tab == WeekendNavItem.SPRINT })
            assertTrue(item.any { it.tab == WeekendNavItem.RACE })
        }
    }

    @Test
    fun `loading season and round and select tab schedule outputs schedule`() = runTest(testDispatcher) {
        underTest()

        underTest.inputs.clickTab(WeekendNavItem.SCHEDULE)
        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.SCHEDULE })
        }
    }

    @Test
    fun `loading season and round and select tab qualifying outputs qualifying`() = runTest(testDispatcher) {
        underTest()

        underTest.inputs.clickTab(WeekendNavItem.QUALIFYING)
        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.QUALIFYING })
        }
    }

    @Test
    fun `loading season and round and select tab sprint outputs sprint`() = runTest(testDispatcher) {
        underTest()

        underTest.inputs.clickTab(WeekendNavItem.SPRINT)
        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.SPRINT })
        }
    }

    @Test
    fun `loading season and round and select tab race outputs race`() = runTest(testDispatcher) {
        underTest()

        underTest.inputs.clickTab(WeekendNavItem.RACE)
        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.RACE })
        }
    }

    @Test
    fun `refresh calls driver repository`() = runTest(testDispatcher) {
        underTest()

        runBlocking {
            underTest.inputs.refresh()
        }

        coVerify {
            mockRaceRepository.fetchRaces(2020)
        }
        underTest.outputs.isRefreshing.test {
            assertEquals(false, awaitItem())
        }
    }

    private val fakeScreenWeekendData = ScreenWeekendData(
        season = 2020,
        round = 1,
        raceName = "raceName",
        circuitId = "circuitId",
        circuitName = "circuitName",
        country = "country",
        countryISO = "countryISO",
        date = LocalDate.of(2020, 1, 1)
    )
}