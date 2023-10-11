package tmg.flashback.season.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.contract.repository.models.NotificationReminder
import tmg.flashback.results.contract.repository.models.NotificationSchedule
import tmg.flashback.results.repository.models.prefKey

internal class NotificationRepositoryImplTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)

    private lateinit var underTest: NotificationsRepositoryImpl

    private fun initUnderTest() {
        underTest = NotificationsRepositoryImpl(
            notificationRepository = mockNotificationRepository,
            preferenceManager = mockPreferenceManager
        )
    }


    @Test
    fun `getting notification schedule queries all preferences`() {
        every { mockNotificationRepository.isChannelEnabled(any()) } returns true
        initUnderTest()

        val expected = NotificationSchedule(
            freePractice = true,
            qualifying = true,
            sprintQualifying = true,
            sprint = true,
            race = true,
            other = true,
        )
        assertEquals(expected, underTest.notificationSchedule)
    }



    //region Runtime Notifications - Onboarding

    @Test
    fun `is runtime notifications reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.seenRuntimeNotifications )
        verify {
            mockPreferenceManager.getBoolean(keyRuntimeNotifications, false)
        }
    }

    @Test
    fun `setting runtime notifications enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.seenRuntimeNotifications = true
        verify {
            mockPreferenceManager.save(keyRuntimeNotifications, true)
        }
    }

    @Test
    fun `setting runtime notifications disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.seenRuntimeNotifications = false
        verify {
            mockPreferenceManager.save(keyRuntimeNotifications, false)
        }
    }

    //endregion





    @Test
    fun `is enabled for upcoming notifications sends key to pref manager`() {
        every { mockNotificationRepository.isChannelEnabled(NotificationUpcoming.RACE.channelId) } returns true

        initUnderTest()
        assertEquals(true, underTest.isUpcomingEnabled(NotificationUpcoming.RACE))

        verify {
            mockNotificationRepository.isChannelEnabled(NotificationUpcoming.RACE.channelId)
        }
    }


    @Test
    fun `is enabled for results available notifications sends key to pref manager`() {
        every { mockNotificationRepository.isChannelEnabled(NotificationResultsAvailable.RACE.channelId) } returns true

        initUnderTest()
        assertEquals(true, underTest.isEnabled(NotificationResultsAvailable.RACE))

        verify {
            mockNotificationRepository.isChannelEnabled(NotificationResultsAvailable.RACE.channelId)
        }
    }








    //region Notification preferences - Reminder

    @Test
    fun `is notification reminder reads value from preferences repository`() {
        every { mockPreferenceManager.getInt(any(), any()) } returns 900

        initUnderTest()

        assertEquals(NotificationReminder.MINUTES_15, underTest.notificationReminderPeriod)
        verify {
            mockPreferenceManager.getInt(keyNotificationReminder, 1800)
        }
    }

    @Test
    fun `is notification reminder reads value from preferences repository with invalid defaults to 30 mins`() {
        every { mockPreferenceManager.getInt(any(), any()) } returns 1

        initUnderTest()

        assertEquals(NotificationReminder.MINUTES_30, underTest.notificationReminderPeriod)
        verify {
            mockPreferenceManager.getInt(keyNotificationReminder, 1800)
        }
    }

    @Test
    fun `setting notification reminder enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationReminderPeriod = NotificationReminder.MINUTES_60
        verify {
            mockPreferenceManager.save(keyNotificationReminder, NotificationReminder.MINUTES_60.seconds)
        }
    }

    //endregion

    companion object {
        private const val keyNotificationReminder: String = "UP_NEXT_NOTIFICATION_REMINDER"

        private const val keyRuntimeNotifications: String = "RUNTIME_NOTIFICATION_PROMPT"
        private const val keyNotificationOnboarding: String = "UP_NEXT_NOTIFICATION_ONBOARDING"
    }
}