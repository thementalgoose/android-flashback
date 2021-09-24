package tmg.notifications.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.core.prefs.manager.PreferenceManager
import tmg.notifications.NotificationRegistration

internal class NotificationRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: NotificationRepository

    private fun initSUT() {
        sut = NotificationRepository(mockPreferenceManager)
    }

    //region Season info enabled

    @Test
    fun `season info notifications update saves value in shared prefs repository`() {

        initSUT()

        sut.enabledSeasonInfo = NotificationRegistration.OPT_IN
        verify {
            mockPreferenceManager.save(SEASON_INFO_KEY, NotificationRegistration.OPT_IN.key)
        }
    }

    @ParameterizedTest
    @CsvSource(
        ",DEFAULT",
        "OPT_IN,OPT_IN",
        "OPT_OUT,OPT_OUT"
    )
    fun `season info notifications read unrecognised resolves to default`(key: String?, expected: NotificationRegistration) {
        every { mockPreferenceManager.getString(any(), any()) } returns key

        initSUT()

        assertEquals(expected, sut.enabledSeasonInfo)
        verify {
            mockPreferenceManager.getString(SEASON_INFO_KEY, "")
        }
    }

    //endregion

    //region Notification Ids

    @Test
    fun `settings notification ids saves to preference manager`() {
        initSUT()

        sut.notificationIds = setOf(123, 124, 125)
        verify {
            mockPreferenceManager.save(keyNotificationIds, setOf("123", "124", "125"))
        }
    }

    @Test
    fun `settings notification ids retreives to preference manager ignoring invalid`() {
        val expected = setOf(123, 124)
        every { mockPreferenceManager.getSet(keyNotificationIds, any()) } returns mutableSetOf("123", "null", "123", "124")
        initSUT()

        assertEquals(expected, sut.notificationIds)
        verify {
            mockPreferenceManager.getSet(keyNotificationIds, emptySet())
        }
    }

    //endregion

    companion object {
        private const val RACE_KEY = "NOTIFICATION_RACE"
        private const val QUALIFYING_KEY = "NOTIFICATION_QUALIFYING"
        private const val SEASON_INFO_KEY = "NOTIFICATION_SEASON_INFO"

        private const val keyNotificationIds = "NOTIFICATION_IDS"
    }
}