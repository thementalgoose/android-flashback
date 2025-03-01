package tmg.flashback.season.presentation.events

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.data.repo.EventsRepository
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.model
import tmg.testutils.BaseTest

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
    fun `setup events returns list from repository`() = runTest(testDispatcher) {
        val event1 = Event.model(date = LocalDate.of(2020, 10, 2))
        val event2 = Event.model(date = LocalDate.of(2020, 10, 3))
        every { mockEventsRepository.getEvents(2020) } returns flow { emit(listOf(event2, event1)) }

        initUnderTest()
        underTest.inputs.setup(2020)

        underTest.outputs.events.test {
            assertEquals(listOf(event1, event2), awaitItem())
        }
    }
}