package tmg.flashback.stats.ui.weekend.schedule

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.repository.NotificationRepository
import tmg.testutils.BaseTest

internal class ScheduleViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)

    private lateinit var underTest: ScheduleViewModel

    private fun initUnderTest() {
        underTest = ScheduleViewModel(
            raceRepository = mockRaceRepository,
            notificationRepository = mockNotificationRepository
        )
    }

    @Test
    fun `
}