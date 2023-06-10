package tmg.flashback.weekend.ui

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
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.SprintResult
import tmg.flashback.formula1.model.model
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListDoesNotMatchItem
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class WeekendViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)

    private lateinit var underTest: WeekendViewModel

    private fun underTest() {
        underTest = WeekendViewModel(
            raceRepository = mockRaceRepository,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockRaceRepository.getRace(season = 2020, round = 1) } returns flow { emit(Race.model()) }
    }

    @Test
    fun `loading season and round outputs weekend info`() = runTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

        underTest.outputs.weekendInfo.test {
            assertEquals(RaceInfo.model().toWeekendInfo(), awaitItem())
        }
    }

    @Test
    fun `loading season and round outputs default schedule tab`() = runTest {
        every { mockRaceRepository.getRace(season = currentSeasonYear, round = 1) } returns flow { emit(Race.model()) }
        underTest()
        underTest.inputs.load(season = currentSeasonYear, round = 1, date = LocalDate.of(
            currentSeasonYear, 12, 31))

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.SCHEDULE })
        }
    }

    @Test
    fun `loading season and round outputs on not current season year defaults to race tab`() = runTest {
        every { mockRaceRepository.getRace(season = currentSeasonYear, round = 1) } returns flow { emit(Race.model()) }
        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.RACE })
        }
    }

    @Test
    fun `loading season and round with no sprint quali hides quali tab`() = runTest {
        every { mockRaceRepository.getRace(season = any(), round = any()) } returns flow { emit(Race.model(
            sprint = SprintResult.model(qualifying = emptyList(), race = emptyList())
        )) }

        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.tab == WeekendNavItem.SCHEDULE })
            assertTrue(item.any { it.tab == WeekendNavItem.QUALIFYING })
            assertTrue(item.none { it.tab == WeekendNavItem.SPRINT })
            assertTrue(item.any { it.tab == WeekendNavItem.RACE })
        }
    }

    @Test
    fun `loading season and round with sprint quali shows all tabs`() = runTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.tab == WeekendNavItem.SCHEDULE })
            assertTrue(item.any { it.tab == WeekendNavItem.QUALIFYING })
            assertTrue(item.any { it.tab == WeekendNavItem.SPRINT })
            assertTrue(item.any { it.tab == WeekendNavItem.RACE })
        }
    }

    @Test
    fun `loading season and round and select tab schedule outputs schedule`() = runTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

        underTest.inputs.clickTab(WeekendNavItem.SCHEDULE)
        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.SCHEDULE })
        }
    }

    @Test
    fun `loading season and round and select tab qualifying outputs qualifying`() = runTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

        underTest.inputs.clickTab(WeekendNavItem.QUALIFYING)
        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.QUALIFYING })
        }
    }

    @Test
    fun `loading season and round and select tab sprint outputs sprint`() = runTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

        underTest.inputs.clickTab(WeekendNavItem.SPRINT)
        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.SPRINT })
        }
    }

    @Test
    fun `loading season and round and select tab race outputs race`() = runTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

        underTest.inputs.clickTab(WeekendNavItem.RACE)
        underTest.outputs.tabs.test {
            val item = awaitItem()
            assertTrue(item.any { it.isSelected && it.tab == WeekendNavItem.RACE })
        }
    }

    @Test
    fun `refresh calls driver repository`() = runTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1, date = LocalDate.of(2020, 1, 1))

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
}