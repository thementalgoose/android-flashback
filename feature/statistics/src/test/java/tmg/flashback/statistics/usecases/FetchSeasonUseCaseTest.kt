package tmg.flashback.statistics.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.repo.EventsRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.testutils.BaseTest

internal class FetchSeasonUseCaseTest: BaseTest() {

    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)
    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)

    private lateinit var underTest: FetchSeasonUseCase

    class TestFlowCollector : FlowCollector<Boolean> {
        var values: MutableList<Boolean> = mutableListOf()
        override suspend fun emit(value: Boolean) {
            values.add(value)
        }
    }

    private fun initUnderTest() {
        underTest = FetchSeasonUseCase(
            mockOverviewRepository,
            mockEventsRepository,
            mockRaceRepository
        )
    }

    @Test
    fun `fetch emits true when race hasnt previously been synced`() = coroutineTest {
        coEvery { mockRaceRepository.hasntPreviouslySynced(any()) } returns false

        val flowCollector = TestFlowCollector()
        initUnderTest()
        runBlockingTest {
            underTest.fetch(2020).collect(flowCollector)
        }

        coVerify {
            mockRaceRepository.hasntPreviouslySynced(2020)
        }
        coVerify(exactly = 0) {
            mockRaceRepository.fetchRaces(2020)
            mockOverviewRepository.fetchOverview(2020)
            mockEventsRepository.fetchEvents(2020)
        }
        assertEquals(listOf(true), flowCollector.values)
    }

    @Test
    fun `fetch emits false then true and calls fetch season if we should sync`() = coroutineTest {
        coEvery { mockRaceRepository.hasntPreviouslySynced(any()) } returns true

        val flowCollector = TestFlowCollector()
        initUnderTest()
        runBlockingTest {
            underTest.fetch(2020).collect(flowCollector)
        }

        coVerify {
            mockRaceRepository.hasntPreviouslySynced(2020)
            mockRaceRepository.fetchRaces(2020)
            mockOverviewRepository.fetchOverview(2020)
            mockEventsRepository.fetchEvents(2020)
        }
        assertEquals(listOf(false, true), flowCollector.values)
    }

    @Test
    fun `fetch season calls repositories to fetch race and events data`() = coroutineTest {
        initUnderTest()
        runBlockingTest {
            assertTrue(underTest.fetchSeason(2020))
        }

        coVerify {
            mockRaceRepository.fetchRaces(2020)
            mockOverviewRepository.fetchOverview(2020)
            mockEventsRepository.fetchEvents(2020)
        }
    }
}