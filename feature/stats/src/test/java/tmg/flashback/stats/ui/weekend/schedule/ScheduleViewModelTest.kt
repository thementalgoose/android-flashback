package tmg.flashback.stats.ui.weekend.schedule

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.repository.NotificationRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

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

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.notificationFreePractice } returns true
        every { mockNotificationRepository.notificationQualifying } returns true
        every { mockNotificationRepository.notificationRace } returns true
        every { mockNotificationRepository.notificationOther } returns true
    }

    @Test
    fun `initial loads schedule model for race`() {
        val schedule = Schedule.model()
        every { mockRaceRepository.getRace(2020, 1) } returns flow {
            emit(Race.model(schedule = listOf(schedule)))
        }

        initUnderTest()
        underTest.inputs.load(2020, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                ScheduleModel(
                    date = schedule.date,
                    schedules = listOf(
                        schedule to true
                    )
                )
            ))
        }
    }

    @Test
    fun `initial loads schedule model for race weekend with notifications enabled`() {
        `initial loads schedule model for race weekend`(true)
    }

    @Test
    fun `initial loads schedule model for race weekend with notifications disabled`() {
        `initial loads schedule model for race weekend`(false)
    }

    private fun `initial loads schedule model for race weekend`(enabled: Boolean) {
        val fp1 = Schedule.model(label = "FP1", date = LocalDate.of(2020, 1, 1))
        val fp2 = Schedule.model(label = "FP2", date = LocalDate.of(2020, 1, 2))
        val qualifying = Schedule.model(label = "Qualifying", date = LocalDate.of(2020, 1, 2), time = LocalTime.of(14, 30))
        val race = Schedule.model(label = "Race", date = LocalDate.of(2020, 1, 3))

        every { mockNotificationRepository.notificationFreePractice } returns enabled
        every { mockNotificationRepository.notificationQualifying } returns enabled
        every { mockNotificationRepository.notificationRace } returns enabled
        every { mockNotificationRepository.notificationOther } returns enabled

        every { mockRaceRepository.getRace(2020, 1) } returns flow {
            emit(Race.model(schedule = listOf(fp1, qualifying, race, fp2)))
        }

        initUnderTest()
        underTest.inputs.load(2020, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                ScheduleModel(
                    date = fp1.date,
                    schedules = listOf(
                        fp1 to enabled
                    )
                ),
                ScheduleModel(
                    date = qualifying.date,
                    schedules = listOf(
                        fp2 to enabled,
                        qualifying to enabled
                    )
                ),
                ScheduleModel(
                    date = race.date,
                    schedules = listOf(
                        race to enabled
                    )
                )
            ))
        }
    }
}