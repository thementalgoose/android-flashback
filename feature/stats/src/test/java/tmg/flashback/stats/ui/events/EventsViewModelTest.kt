package tmg.flashback.stats.ui.events

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.EventsRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class EventsViewModelTest: BaseTest() {

    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)

    private lateinit var underTest: EventsViewModel

    private fun initUnderTest() {
        underTest = EventsViewModel(
            eventsRepository = mockEventsRepository,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `setup events returns list from repository`() {
        val event1 = Event.model(date = LocalDate.of(2020, 10, 2))
        val event2 = Event.model(date = LocalDate.of(2020, 10, 3))
        every { mockEventsRepository.getEvents(2020) } returns flow { emit(listOf(event2, event1)) }

        initUnderTest()
        underTest.inputs.setup(2020)

        underTest.outputs.events.test {
            assertValue(listOf(event1, event2))
        }
    }
}