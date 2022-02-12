package tmg.flashback.statistics.ui.dashboard.events

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import tmg.flashback.formula1.enums.EventType
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.EventsRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListDoesNotMatchItem
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class EventListViewModelTest: BaseTest() {

    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)

    private lateinit var sut: EventListViewModel

    private fun initSUT() {
        sut = EventListViewModel(mockEventsRepository)
    }

    @ParameterizedTest(name = "show with event type {0} shows only {0} types")
    @EnumSource(EventType::class)
    fun `show with event type shows only events`(eventType: EventType) {
        every { mockEventsRepository.getEvents(any()) } returns flow {
            emit(EventType.values().map {
                Event.model(type = it, label = "eventType ${it.key}")
            })
        }
        initSUT()
        sut.inputs.show(2020, eventType)

        sut.outputs.list.test {
            assertListMatchesItem {
                it.type == eventType
            }
            assertListDoesNotMatchItem {
                it.type != eventType
            }
        }
        verify {
            mockEventsRepository.getEvents(2020)
        }
    }
}