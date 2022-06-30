package tmg.flashback.notifications.usecases

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.repository.NotificationRepository

internal class RemoteNotificationSubscribeUseCaseTest {

    private val mockRemoteNotificationManager: RemoteNotificationManager = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)

    private lateinit var underTest: RemoteNotificationSubscribeUseCase

    private fun initUnderTest() {
        underTest = RemoteNotificationSubscribeUseCase(
            mockRemoteNotificationManager,
            mockNotificationRepository
        )
    }

    @BeforeEach
    internal fun setUp() {
        coEvery { mockRemoteNotificationManager.subscribeToTopic(any()) } returns true
    }

    @Test
    fun `subscribe from topic calls remote notification manager`() {
        initUnderTest()
        runBlocking {
            assertTrue(underTest.subscribe("topic"))
        }

        coVerify {
            mockRemoteNotificationManager.subscribeToTopic("topic")
        }
    }

    @Test
    fun `subscribe from topic doesnt update remote topics if subscription fail`() {
        coEvery { mockRemoteNotificationManager.subscribeToTopic(any()) } returns false
        every { mockNotificationRepository.remoteNotificationTopics } returns setOf( "topic2")

        initUnderTest()
        runBlocking {
            assertFalse(underTest.subscribe("topic"))
        }

        verify(exactly = 0) {
            mockNotificationRepository.remoteNotificationTopics = any()
        }
        coVerify {
            mockRemoteNotificationManager.subscribeToTopic("topic")
        }
    }

    @Test
    fun `subscribe from topic updates remote topics if subscription succeeds`() {
        coEvery { mockRemoteNotificationManager.subscribeToTopic(any()) } returns true
        every { mockNotificationRepository.remoteNotificationTopics } returns setOf("topic", "topic2")

        initUnderTest()
        runBlocking {
            assertTrue(underTest.subscribe("topic"))
        }

        verify {
            mockNotificationRepository.remoteNotificationTopics = setOf("topic2", "topic")
        }
        coVerify {
            mockRemoteNotificationManager.subscribeToTopic("topic")
        }
    }
}