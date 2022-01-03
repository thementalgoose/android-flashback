package tmg.flashback.notifications.controllers

import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.flashback.notifications.NotificationRegistration
import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.testutils.BaseTest

internal class NotificationControllerTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockSystemNotificationManager: SystemNotificationManager = mockk(relaxed = true)
    private val mockRemoteNotificationManager: RemoteNotificationManager = mockk(relaxed = true)
    private val mockAlarmManager: SystemAlarmManager = mockk(relaxed = true)

    private lateinit var sut: NotificationController

    private fun initSUT() {
        sut = NotificationController(mockNotificationRepository, mockSystemNotificationManager, mockRemoteNotificationManager, mockAlarmManager)
    }

    @Test
    fun `subscribe method subscribes to topics if all are set to default`() = coroutineTest {
        coEvery { mockRemoteNotificationManager.subscribeToTopic(any()) } returns true

        initSUT()
        runBlockingTest {
            sut.subscribeToRemoteNotifications()
        }

        coVerify {
            mockRemoteNotificationManager.subscribeToTopic(keyTopicSeasonInfo)
            // Legacy
            mockRemoteNotificationManager.unsubscribeToTopic("race")
            mockRemoteNotificationManager.unsubscribeToTopic("qualifying")
        }
    }

    @Test
    fun `subscribe method doesnt update repository if subscription fails`() {
        coEvery { mockRemoteNotificationManager.subscribeToTopic(any()) } returns false

        initSUT()
        runBlockingTest {
            sut.subscribeToRemoteNotifications()
        }

        coVerify {
            mockRemoteNotificationManager.subscribeToTopic(keyTopicSeasonInfo)
        }
    }

    @Test
    fun `subscribe method does nothing if topics if already subscribed`() {
        initSUT()
        runBlockingTest {
            sut.subscribeToRemoteNotifications()
        }

        coVerify(exactly = 0) {
            mockRemoteNotificationManager.subscribeToTopic(any())
        }
    }

    //region Scheduling notifications

    @Test
    fun `scheduling notification schedules with manager and updates ids`() {

        val expectedRequestCode: Int = 9
        val expectedChannelId: String = "channelId"
        val expectedTitle: String = "title"
        val expectedText: String = "text"
        val expectedTimestamp: LocalDateTime = LocalDateTime.now()

        every { mockNotificationRepository.notificationIds } returns setOf(1,4)
        initSUT()
        sut.scheduleLocalNotification(expectedRequestCode, expectedChannelId, expectedTitle, expectedText, expectedTimestamp)
        verify {
            mockAlarmManager.schedule(expectedRequestCode, expectedChannelId, expectedTitle, expectedText, expectedTimestamp)
            mockNotificationRepository.notificationIds = setOf(1,4,9)
        }
    }

    //endregion

    //region Cancel notifications

    @Test
    fun `cancel specific notification cancels and removes from repository`() {
        every { mockNotificationRepository.notificationIds } returns setOf(1,4)
        initSUT()
        sut.cancelLocalNotification(1)
        verify {
            mockAlarmManager.cancel(1)
            mockNotificationRepository.notificationIds = setOf(4)
        }
    }

    @Test
    fun `cancel all notifications cancels request for all in repository and clears repository`() {
        every { mockNotificationRepository.notificationIds } returns setOf(1,4)
        initSUT()
        sut.cancelAllNotifications()
        verify {
            mockAlarmManager.cancel(1)
            mockAlarmManager.cancel(4)
            mockNotificationRepository.notificationIds = emptySet()
        }
    }

    //endregion

    //region Notification channels

    @Test
    fun `create notification channel tells local manager to create channel`() {
        val expectedChannelId = "channelId"
        val expectedLabel = 3
        initSUT()
        sut.createNotificationChannel(expectedChannelId, expectedLabel)
        verify {
            mockSystemNotificationManager.createChannel(expectedChannelId, expectedLabel)
        }
    }

    @Test
    fun `delete notification channel tells local manager to delete channel`() {
        val expectedChannelId = "channelId"
        initSUT()
        sut.deleteNotificationChannel(expectedChannelId)
        verify {
            mockSystemNotificationManager.cancelChannel(expectedChannelId)
        }
    }

    //endregion

    //region Get currently scheduled ids

    @Test
    fun `currently scheduled notification ids returns from repository`() {
        val expected = setOf(198, 12039493)
        every { mockNotificationRepository.notificationIds } returns expected
        initSUT()
        assertEquals(expected, sut.notificationsCurrentlyScheduled)
        verify {
            mockNotificationRepository.notificationIds
        }
    }

    //endregion

    companion object {
        private const val keyTopicSeasonInfo: String = "seasonInfo"
    }
}