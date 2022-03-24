package tmg.flashback.statistics.usecases

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.testutils.BaseTest

internal class WeekendOverviewUseCaseTest: BaseTest() {

    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)

    private lateinit var underTest: WeekendOverviewUseCase

    class TestFlowCollector<T> : FlowCollector<T> {
        var values: MutableList<T> = mutableListOf()
        override suspend fun emit(value: T) {
            values.add(value)
        }
    }

    private fun initUnderTest() {
        underTest = WeekendOverviewUseCase(
            mockOverviewRepository,
            mockFetchSeasonUseCase
        )
    }

    @Test
    fun `fetch season use case is called when calling get season`() = coroutineTest {
        every { mockFetchSeasonUseCase.fetch(2020) } returns flow { emit(true) }

        initUnderTest()
        val flowCollector = TestFlowCollector<List<OverviewRace>?>()
        runBlocking {
            underTest.get(2020).collect(flowCollector)
        }

        coVerify { mockFetchSeasonUseCase.fetch(2020) }
    }

    @Test
    fun `null is returned when race hasnt been synced`() = coroutineTest {
        every { mockFetchSeasonUseCase.fetch(2020) } returns flow { emit(false) }
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(Overview.model()) }

        initUnderTest()
        val flowCollector = TestFlowCollector<List<OverviewRace>?>()
        runBlocking {
            underTest.get(2020).collect(flowCollector)
        }

        coVerify { mockFetchSeasonUseCase.fetch(2020) }
        assertEquals(null, flowCollector.values[0])
    }

    @Test
    fun `weekend overview is mapped successfully from overview`() = coroutineTest {
        every { mockFetchSeasonUseCase.fetch(2020) } returns flow { emit(true) }
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(Overview.model()) }

        initUnderTest()
        val flowCollector = TestFlowCollector<List<OverviewRace>?>()
        runBlocking {
            underTest.get(2020).collect(flowCollector)
        }

        coVerify { mockFetchSeasonUseCase.fetch(2020) }
        assertEquals(listOf(Overview.model()), flowCollector.values[0])
    }
}