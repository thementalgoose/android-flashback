package tmg.flashback.stats.usecases

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.stats.repository.NotificationRepository
import tmg.testutils.BaseTest

internal class ResubscribeNotificationsUseCaseTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
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

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.notificationNotifyRace } returns false
        every { mockNotificationRepository.notificationNotifySprint } returns false
        every { mockNotificationRepository.notificationNotifyQualifying } returns false
    }

    @Test
    fun `resubscribe subscribes notify race if preference is enabled`() = coroutineTest {
        every { mockNotificationRepository.notificationNotifyRace } returns true

        initUnderTest()
        runBlocking { underTest.resubscribe() }

        coVerify {
            mockRemoteNotificationSubscribeUseCase.subscribe(NOTIFY_RACE)
        }
    }

    @Test
    fun `resubscribe unsubscribes notify race if preference is disabled`() = coroutineTest {
        every { mockNotificationRepository.notificationNotifyRace } returns false

        initUnderTest()
        runBlocking { underTest.resubscribe() }

        coVerify {
            mockRemoteNotificationUnsubscribeUseCase.unsubscribe(NOTIFY_RACE)
        }
    }

    @Test
    fun `resubscribe subscribes notify sprint if preference is enabled`() = coroutineTest {
        every { mockNotificationRepository.notificationNotifySprint } returns true

        initUnderTest()
        runBlocking { underTest.resubscribe() }

        coVerify {
            mockRemoteNotificationSubscribeUseCase.subscribe(NOTIFY_SPRINT)
        }
    }

    @Test
    fun `resubscribe unsubscribes notify sprint if preference is disabled`() = coroutineTest {
        every { mockNotificationRepository.notificationNotifySprint } returns false

        initUnderTest()
        runBlocking { underTest.resubscribe() }

        coVerify {
            mockRemoteNotificationUnsubscribeUseCase.unsubscribe(NOTIFY_SPRINT)
        }
    }

    @Test
    fun `resubscribe subscribes notify qualifying if preference is enabled`() = coroutineTest {
        every { mockNotificationRepository.notificationNotifyQualifying } returns true

        initUnderTest()
        runBlocking { underTest.resubscribe() }

        coVerify {
            mockRemoteNotificationSubscribeUseCase.subscribe(NOTIFY_QUALIFYING)
        }
    }

    @Test
    fun `resubscribe unsubscribes notify qualifying if preference is disabled`() = coroutineTest {
        every { mockNotificationRepository.notificationNotifyQualifying } returns false

        initUnderTest()
        runBlocking { underTest.resubscribe() }

        coVerify {
            mockRemoteNotificationUnsubscribeUseCase.unsubscribe(NOTIFY_QUALIFYING)
        }
    }

    companion object {
        private const val NOTIFY_RACE = "notify_race"
        private const val NOTIFY_SPRINT = "notify_sprint"
        private const val NOTIFY_QUALIFYING = "notify_qualifying"
    }
}