package tmg.flashback.season.usecases

import android.app.Notification
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable.QUALIFYING
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable.RACE
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.testutils.BaseTest

internal class ResubscribeNotificationsUseCaseTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepository = mockk(relaxed = true)
    private val mockRemoteNotificationSubscribeUseCase: RemoteNotificationSubscribeUseCase = mockk(relaxed = true)
    private val mockRemoteNotificationUnsubscribeUseCase: RemoteNotificationUnsubscribeUseCase = mockk(relaxed = true)

    private lateinit var underTest: ResubscribeNotificationsUseCase

    private fun initUnderTest() {
        underTest = ResubscribeNotificationsUseCase(
            mockNotificationRepository,
            mockRemoteNotificationSubscribeUseCase,
            mockRemoteNotificationUnsubscribeUseCase
        )
    }

    @Test
    fun `resubscribe subscribes subscribes if preference is enabled`() = coroutineTest {
        every { mockNotificationRepository.isEnabled(RACE) } returns true
        every { mockNotificationRepository.isEnabled(QUALIFYING) } returns false

        initUnderTest()
        runBlocking { underTest.resubscribe() }

        coVerify {
            mockRemoteNotificationSubscribeUseCase.subscribe(RACE.remoteSubscriptionTopic)
            mockRemoteNotificationUnsubscribeUseCase.unsubscribe(QUALIFYING.remoteSubscriptionTopic)
        }
    }
}