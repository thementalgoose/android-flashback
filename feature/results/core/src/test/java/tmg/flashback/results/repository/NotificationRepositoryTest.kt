package tmg.flashback.results.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.repository.models.NotificationReminder
import tmg.flashback.results.repository.models.NotificationSchedule
import tmg.flashback.results.repository.models.prefKey

internal class NotificationRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: NotificationsRepositoryImpl

    private fun initUnderTest() {
        underTest = NotificationsRepositoryImpl(mockPreferenceManager)
    }


    @Test
    fun `getting notification schedule queries all preferences`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true
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



    //region Notification preferences - Onboarding

    @Test
    fun `is notification onboarding reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.seenNotificationOnboarding )
        verify {
            mockPreferenceManager.getBoolean(keyNotificationOnboarding, false)
        }
    }

    @Test
    fun `setting notification onboarding enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.seenNotificationOnboarding = true
        verify {
            mockPreferenceManager.save(keyNotificationOnboarding, true)
        }
    }

    @Test
    fun `setting notification onboarding disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.seenNotificationOnboarding = false
        verify {
            mockPreferenceManager.save(keyNotificationOnboarding, false)
        }
    }

    //endregion



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
        every { mockPreferenceManager.getBoolean(NotificationUpcoming.RACE.prefKey, false) } returns true

        initUnderTest()
        assertEquals(true, underTest.isUpcomingEnabled(NotificationUpcoming.RACE))

        verify {
            mockPreferenceManager.getBoolean(NotificationUpcoming.RACE.prefKey, false)
        }
    }


    @Test
    fun `set enabled for upcoming notifications sends key to pref manager`() {
        initUnderTest()
        underTest.setUpcomingEnabled(NotificationUpcoming.RACE, true)

        verify {
            mockPreferenceManager.save(NotificationUpcoming.RACE.prefKey, true)
        }
    }


    @Test
    fun `is enabled for results available notifications sends key to pref manager`() {
        every { mockPreferenceManager.getBoolean(NotificationResultsAvailable.RACE.prefKey, false) } returns true

        initUnderTest()
        assertEquals(true, underTest.isEnabled(NotificationResultsAvailable.RACE))

        verify {
            mockPreferenceManager.getBoolean(NotificationResultsAvailable.RACE.prefKey, false)
        }
    }


    @Test
    fun `set enabled for results available notifications sends key to pref manager`() {
        initUnderTest()
        underTest.setEnabled(NotificationResultsAvailable.RACE, true)

        verify {
            mockPreferenceManager.save(NotificationResultsAvailable.RACE.prefKey, true)
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