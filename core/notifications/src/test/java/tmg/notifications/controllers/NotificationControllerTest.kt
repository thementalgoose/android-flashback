package tmg.notifications.controllers

import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import tmg.notifications.NotificationRegistration
import tmg.notifications.managers.PushNotificationManager
import tmg.notifications.repository.NotificationRepository
import tmg.test.BaseTest

internal class NotificationControllerTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockNotificationManager: PushNotificationManager = mockk(relaxed = true)

    private lateinit var sut: NotificationController

    private fun initSUT() {
        sut = NotificationController(mockNotificationRepository, mockNotificationManager)
    }

    @Test
    fun `subscribe method subscribes to topics if all are set to default`() = coroutineTest {
        coEvery { mockNotificationManager.subscribeToTopic(any()) } returns true
        every { mockNotificationRepository.enabledRace } returns NotificationRegistration.DEFAULT
        every { mockNotificationRepository.enabledQualifying } returns NotificationRegistration.DEFAULT
        every { mockNotificationRepository.enabledSeasonInfo } returns NotificationRegistration.DEFAULT

        initSUT()
        runBlockingTest {
            sut.subscribe()
        }

        coVerify {
            mockNotificationManager.subscribeToTopic(keyTopicRace)
            mockNotificationManager.subscribeToTopic(keyTopicQualifying)
            mockNotificationManager.subscribeToTopic(keyTopicSeasonInfo)
        }
        verify {
            mockNotificationRepository.enabledRace
            mockNotificationRepository.enabledRace = NotificationRegistration.OPT_IN
            mockNotificationRepository.enabledQualifying
            mockNotificationRepository.enabledQualifying = NotificationRegistration.OPT_IN
            mockNotificationRepository.enabledSeasonInfo
            mockNotificationRepository.enabledSeasonInfo = NotificationRegistration.OPT_IN
        }
    }

    @Test
    fun `subscribe method doesnt update repository if subscription fails`() {
        coEvery { mockNotificationManager.subscribeToTopic(any()) } returns false
        every { mockNotificationRepository.enabledRace } returns NotificationRegistration.DEFAULT
        every { mockNotificationRepository.enabledQualifying } returns NotificationRegistration.DEFAULT
        every { mockNotificationRepository.enabledSeasonInfo } returns NotificationRegistration.DEFAULT

        initSUT()
        runBlockingTest {
            sut.subscribe()
        }

        coVerify {
            mockNotificationManager.subscribeToTopic(keyTopicRace)
            mockNotificationManager.subscribeToTopic(keyTopicQualifying)
            mockNotificationManager.subscribeToTopic(keyTopicSeasonInfo)
        }
        verify {
            mockNotificationRepository.enabledRace
            mockNotificationRepository.enabledQualifying
            mockNotificationRepository.enabledSeasonInfo
        }
        verify(exactly = 0) {
            mockNotificationRepository.enabledRace = NotificationRegistration.OPT_IN
            mockNotificationRepository.enabledQualifying = NotificationRegistration.OPT_IN
            mockNotificationRepository.enabledSeasonInfo = NotificationRegistration.OPT_IN
        }
    }

    @Test
    fun `subscribe method does nothing if topics if already subscribed`() {
        every { mockNotificationRepository.enabledRace } returns NotificationRegistration.OPT_IN
        every { mockNotificationRepository.enabledQualifying } returns NotificationRegistration.OPT_IN
        every { mockNotificationRepository.enabledSeasonInfo } returns NotificationRegistration.OPT_IN

        initSUT()
        runBlockingTest {
            sut.subscribe()
        }

        coVerify(exactly = 0) {
            mockNotificationManager.subscribeToTopic(any())
        }
        verify(exactly = 0) {
            mockNotificationRepository.enabledRace = NotificationRegistration.OPT_IN
            mockNotificationRepository.enabledQualifying = NotificationRegistration.OPT_IN
            mockNotificationRepository.enabledSeasonInfo = NotificationRegistration.OPT_IN
        }
    }

    companion object {
        private const val keyTopicRace: String = "race"
        private const val keyTopicQualifying: String = "qualifying"
        private const val keyTopicSeasonInfo: String = "seasonInfo"
    }
}