package tmg.flashback.notifications.usecases

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.repository.NotificationRepository

internal class RemoteNotificationUnsubscribeUseCaseTest {

    private val mockRemoteNotificationManager: RemoteNotificationManager = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)

    private lateinit var underTest: RemoteNotificationUnsubscribeUseCase

    private fun initUnderTest() {
        underTest = RemoteNotificationUnsubscribeUseCase(
            mockRemoteNotificationManager,
            mockNotificationRepository
        )
    }

    @BeforeEach
    internal fun setUp() {
        coEvery { mockRemoteNotificationManager.unsubscribeToTopic(any()) } returns true
    }

    @Test
    fun `unsubscribe from topic calls remote notification manager`() {
        initUnderTest()
        runBlocking {
            assertTrue(underTest.unsubscribe("topic"))
        }

        coVerify {
            mockRemoteNotificationManager.unsubscribeToTopic("topic")
        }
    }

    @Test
    fun `unsubscribe from topic doesnt update remote topics if unsubscription fail`() {
        coEvery { mockRemoteNotificationManager.unsubscribeToTopic(any()) } returns false
        every { mockNotificationRepository.remoteNotificationTopics } returns setOf("topic", "topic2")

        initUnderTest()
        runBlocking {
            assertFalse(underTest.unsubscribe("topic"))
        }

        verify(exactly = 0) {
            mockNotificationRepository.remoteNotificationTopics = any()
        }
        coVerify {
            mockRemoteNotificationManager.unsubscribeToTopic("topic")
        }
    }

    @Test
    fun `unsubscribe from topic updates remote topics if unsubscription succeeds`() {
        coEvery { mockRemoteNotificationManager.unsubscribeToTopic(any()) } returns true
        every { mockNotificationRepository.remoteNotificationTopics } returns setOf("topic", "topic2")

        initUnderTest()
        runBlocking {
            assertTrue(underTest.unsubscribe("topic"))
        }

        verify {
            mockNotificationRepository.remoteNotificationTopics = setOf("topic2")
        }
        coVerify {
            mockRemoteNotificationManager.unsubscribeToTopic("topic")
        }
    }
}