package tmg.flashback.notifications.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.prefs.manager.PreferenceManager

internal class NotificationRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockSystemNotificationManager: SystemNotificationManager = mockk(relaxed = true)

    private lateinit var underTest: NotificationIdsRepository

    private fun initUnderTest() {
        underTest = NotificationIdsRepository(
            preferenceManager = mockPreferenceManager,
            systemNotificationManager = mockSystemNotificationManager
        )
    }

    //region Notification Remote Topics

    @Test
    fun `settings notification remote topics saves to preference manager`() {
        initUnderTest()

        underTest.remoteNotificationTopics = setOf("test", "test2")
        verify {
            mockPreferenceManager.save(keyNotificationRemoteTopics, setOf("test", "test2"))
        }
    }

    @Test
    fun `settings notification remote topics retreives to preference manager ignoring invalid`() {
        val expected = setOf("test", "test2")
        every { mockPreferenceManager.getSet(keyNotificationRemoteTopics, any()) } returns mutableSetOf("test", "test", "test2")
        initUnderTest()

        assertEquals(expected, underTest.remoteNotificationTopics)
        verify {
            mockPreferenceManager.getSet(keyNotificationRemoteTopics, emptySet())
        }
    }

    //endregion

    //region Notification Remote token

    @Test
    fun `settings notification remote token saves to preference manager`() {
        initUnderTest()

        underTest.remoteNotificationToken = "token"
        verify {
            mockPreferenceManager.save(keyNotificationRemoteToken, "token")
        }
    }

    @Test
    fun `settings notification remote token retreives to preference manager ignoring invalid`() {
        every { mockPreferenceManager.getString(keyNotificationRemoteToken, any()) } returns "token"
        initUnderTest()

        assertEquals("token", underTest.remoteNotificationToken)
        verify {
            mockPreferenceManager.getString(keyNotificationRemoteToken, null)
        }
    }

    //endregion

    //region Notification Ids

    @Test
    fun `settings notification ids saves to preference manager`() {
        initUnderTest()

        underTest.notificationIds = setOf(123, 124, 125)
        verify {
            mockPreferenceManager.save(keyNotificationIds, setOf("123", "124", "125"))
        }
    }

    @Test
    fun `settings notification ids retreives to preference manager ignoring invalid`() {
        val expected = setOf(123, 124)
        every { mockPreferenceManager.getSet(keyNotificationIds, any()) } returns mutableSetOf("123", "null", "123", "124")
        initUnderTest()

        assertEquals(expected, underTest.notificationIds)
        verify {
            mockPreferenceManager.getSet(keyNotificationIds, emptySet())
        }
    }

    //endregion

    @Test
    fun `is channel enabled forwards call onto system notification manager`() {
        initUnderTest()
        underTest.isChannelEnabled("channelId")
        verify {
            mockSystemNotificationManager.isChannelEnabled("channelId")
        }
    }

    companion object {

        private const val keyNotificationRemoteTopics: String = "NOTIFICATION_REMOTE_TOPICS"
        private const val keyNotificationIds = "NOTIFICATION_IDS"
        private const val keyNotificationRemoteToken: String = "NOTIFICATION_REMOTE_TOKEN"
    }
}