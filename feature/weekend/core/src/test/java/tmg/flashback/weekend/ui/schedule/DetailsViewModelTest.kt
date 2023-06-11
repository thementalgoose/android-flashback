package tmg.flashback.weekend.ui.schedule

import android.net.Uri
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.with
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.flashback.weekend.R
import tmg.flashback.weekend.repository.WeatherRepository
import tmg.flashback.weekend.ui.details.DetailsModel
import tmg.flashback.weekend.ui.details.DetailsModel.Links
import tmg.flashback.weekend.ui.details.DetailsViewModel
import tmg.testutils.BaseTest

internal class DetailsViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationsRepository = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockWeatherRepository: WeatherRepository = mockk(relaxed = true)

    private lateinit var underTest: DetailsViewModel

    private fun initUnderTest() {
        underTest = DetailsViewModel(
            raceRepository = mockRaceRepository,
            notificationRepository = mockNotificationRepository,
            openWebpageUseCase = mockOpenWebpageUseCase,
            navigator = mockNavigator,
            weatherRepository = mockWeatherRepository
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns true
        every { mockNotificationRepository.notificationUpcomingQualifying } returns true
        every { mockNotificationRepository.notificationUpcomingRace } returns true
        every { mockNotificationRepository.notificationUpcomingOther } returns true
        every { mockWeatherRepository.weatherTemperatureMetric } returns true
        every { mockWeatherRepository.weatherWindspeedMetric } returns false
    }

    @Test
    fun `initial loads details model for race`() = runTest {
        val schedule = Schedule.model()
        every { mockRaceRepository.getRace(2020, 1) } returns flow {
            emit(Race.model(schedule = listOf(schedule)))
        }

        initUnderTest()
        underTest.inputs.load(2020, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                links,
                DetailsModel.ScheduleWeekend.model(),
                DetailsModel.Track.model(),
            ), awaitItem())
        }
    }

    @Test
    fun `initial loads details model for race weekend with notifications enabled`() = runTest {
        `initial loads details model for race weekend`(true)
    }

    @Test
    fun `initial loads details model for race weekend with notifications disabled`() = runTest {
        `initial loads details model for race weekend`(false)
    }

    @Test
    fun `click link opens webpage`() = runTest {
        val link = DetailsModel.Link(0, 0, "https://url.com")
        initUnderTest()

        underTest.inputs.linkClicked(link)

        verify {
            mockOpenWebpageUseCase.open("https://url.com", title = "")
        }
    }

    @Test
    fun `click link with circuit history url navigates to circuit`() = runTest {
        val link = DetailsModel.Link(0, 0, "flashback://circuit-history/circuitId/circuitName")
        initUnderTest()

        underTest.inputs.linkClicked(link)

        verify {
            mockNavigator.navigate(Screen.Circuit.with("circuitId", "circuitName"))
        }
    }

    private suspend fun TestScope.`initial loads details model for race weekend`(enabled: Boolean) {
        val fp1 = Schedule.model(label = "FP1", date = LocalDate.of(2020, 1, 1))
        val fp2 = Schedule.model(label = "FP2", date = LocalDate.of(2020, 1, 2))
        val qualifying = Schedule.model(label = "Qualifying", date = LocalDate.of(2020, 1, 2), time = LocalTime.of(14, 30))
        val race = Schedule.model(label = "Race", date = LocalDate.of(2020, 1, 3))

        every { mockNotificationRepository.notificationUpcomingFreePractice } returns enabled
        every { mockNotificationRepository.notificationUpcomingQualifying } returns enabled
        every { mockNotificationRepository.notificationUpcomingRace } returns enabled
        every { mockNotificationRepository.notificationUpcomingOther } returns enabled

        every { mockRaceRepository.getRace(2020, 1) } returns flow {
            emit(Race.model(schedule = listOf(fp1, qualifying, race, fp2)))
        }

        initUnderTest()
        underTest.inputs.load(2020, 1)

        underTest.outputs.list.test {
            assertEquals(listOf(
                links,
                DetailsModel.ScheduleWeekend.model(
                    days = listOf(
                        fp1.date to listOf(fp1 to enabled),
                        qualifying.date to listOf(fp2 to enabled, qualifying to enabled),
                        race.date to listOf(race to enabled)
                    )
                ),
                DetailsModel.Track.model(),
            ), awaitItem())
        }
    }

    private val links: Links get() = Links(
        listOf(
            DetailsModel.Link(
                label = R.string.details_link_circuit,
                icon = R.drawable.ic_details_track,
                url = "flashback://circuit-history/circuitId/circuitName"
            ),
            DetailsModel.Link(
                label = R.string.details_link_map,
                icon = R.drawable.ic_details_maps,
                url = "geo:51.101,-1.101?q=${Uri.encode("circuitName")}"
            )
        )
    )
}