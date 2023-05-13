package tmg.flashback.weekend.ui

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `loading season and round outputs weekend info`() = coroutineTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        underTest.outputs.weekendInfo.test {
            assertValue(RaceInfo.model().toWeekendInfo())
        }
    }

    @Test
    fun `loading season and round outputs default schedule tab`() = coroutineTest {
        every { mockRaceRepository.getRace(season = currentSeasonYear, round = 1) } returns flow { emit(Race.model()) }
        underTest()
        underTest.inputs.load(season = currentSeasonYear, round = 1)

        underTest.outputs.tabs.test {
            assertListMatchesItem {
                it.isSelected && it.tab == WeekendNavItem.SCHEDULE
            }
        }
    }

    @Test
    fun `loading season and round outputs on not current season year defaults to race tab`() = coroutineTest {
        every { mockRaceRepository.getRace(season = currentSeasonYear, round = 1) } returns flow { emit(Race.model()) }
        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        underTest.outputs.tabs.test {
            assertListMatchesItem {
                it.isSelected && it.tab == WeekendNavItem.RACE
            }
        }
    }

    @Test
    fun `loading season and round with no sprint quali hides quali tab`() = coroutineTest {
        every { mockRaceRepository.getRace(season = any(), round = any()) } returns flow { emit(Race.model(
            sprint = SprintResult.model(qualifying = emptyList(), race = emptyList())
        )) }

        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        underTest.outputs.tabs.test {
            assertListMatchesItem { it.tab == WeekendNavItem.SCHEDULE }
            assertListMatchesItem { it.tab == WeekendNavItem.QUALIFYING }
            assertListDoesNotMatchItem { it.tab == WeekendNavItem.SPRINT }
            assertListMatchesItem { it.tab == WeekendNavItem.RACE }
        }
    }

    @Test
    fun `loading season and round with sprint quali shows all tabs`() = coroutineTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        underTest.outputs.tabs.test {
            assertListMatchesItem { it.tab == WeekendNavItem.SCHEDULE }
            assertListMatchesItem { it.tab == WeekendNavItem.QUALIFYING }
            assertListMatchesItem { it.tab == WeekendNavItem.SPRINT }
            assertListMatchesItem { it.tab == WeekendNavItem.RACE }
        }
    }

    @Test
    fun `loading season and round and select tab schedule outputs schedule`() = coroutineTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        underTest.inputs.clickTab(WeekendNavItem.SCHEDULE)
        underTest.outputs.tabs.test {
            assertListMatchesItem { it.isSelected && it.tab == WeekendNavItem.SCHEDULE }
        }
    }

    @Test
    fun `loading season and round and select tab qualifying outputs qualifying`() = coroutineTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        underTest.inputs.clickTab(WeekendNavItem.QUALIFYING)
        underTest.outputs.tabs.test {
            assertListMatchesItem { it.isSelected && it.tab == WeekendNavItem.QUALIFYING }
        }
    }

    @Test
    fun `loading season and round and select tab sprint outputs sprint`() = coroutineTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        underTest.inputs.clickTab(WeekendNavItem.SPRINT)
        underTest.outputs.tabs.test {
            assertListMatchesItem { it.isSelected && it.tab == WeekendNavItem.SPRINT }
        }
    }

    @Test
    fun `loading season and round and select tab race outputs race`() = coroutineTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        underTest.inputs.clickTab(WeekendNavItem.RACE)
        underTest.outputs.tabs.test {
            assertListMatchesItem { it.isSelected && it.tab == WeekendNavItem.RACE }
        }
    }

    @Test
    fun `refresh calls driver repository`() = coroutineTest {
        underTest()
        underTest.inputs.load(season = 2020, round = 1)

        runBlocking {
            underTest.inputs.refresh()
        }

        coVerify {
            mockRaceRepository.fetchRaces(2020)
        }
        underTest.outputs.isRefreshing.test {
            assertValue(false)
        }
    }
}