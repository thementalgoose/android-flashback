package tmg.flashback.stats.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.stats.repository.models.NotificationReminder
import tmg.flashback.stats.repository.models.NotificationResults
import tmg.flashback.stats.repository.models.NotificationSchedule

internal class NotificationRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: NotificationRepository

    private fun initUnderTest() {
        underTest = NotificationRepository(mockPreferenceManager)
    }

    //region Notification preferences - Race

    @Test
    fun `is notification race reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.notificationRace)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationRace, false)
        }
    }

    @Test
    fun `setting notification race enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationRace = true
        verify {
            mockPreferenceManager.save(keyNotificationRace, true)
        }
    }

    @Test
    fun `setting notification race disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationRace = false
        verify {
            mockPreferenceManager.save(keyNotificationRace, false)
        }
    }

    //endregion

    //region Notification preferences - Qualifying

    @Test
    fun `is notification qualifying reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.notificationQualifying)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationQualifying, false)
        }
    }

    @Test
    fun `setting notification qualifying enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationQualifying = true
        verify {
            mockPreferenceManager.save(keyNotificationQualifying, true)
        }
    }

    @Test
    fun `setting notification qualifying disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationQualifying = false
        verify {
            mockPreferenceManager.save(keyNotificationQualifying, false)
        }
    }

    //endregion

    //region Notification preferences - Free Practice

    @Test
    fun `is notification free practice reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.notificationFreePractice )
        verify {
            mockPreferenceManager.getBoolean(keyNotificationFreePractice, false)
        }
    }

    @Test
    fun `setting notification free practice enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationFreePractice = true
        verify {
            mockPreferenceManager.save(keyNotificationFreePractice, true)
        }
    }

    @Test
    fun `setting notification free practice disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationFreePractice = false
        verify {
            mockPreferenceManager.save(keyNotificationFreePractice, false)
        }
    }

    //endregion

    //region Notification preferences - Other

    @Test
    fun `is notification other reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.notificationOther )
        verify {
            mockPreferenceManager.getBoolean(keyNotificationOther, false)
        }
    }

    @Test
    fun `setting notification other enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationOther = true
        verify {
            mockPreferenceManager.save(keyNotificationOther, true)
        }
    }

    @Test
    fun `setting notification other disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationOther = false
        verify {
            mockPreferenceManager.save(keyNotificationOther, false)
        }
    }

    //endregion


    @Test
    fun `getting notification schedule queries all preferences`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true
        initUnderTest()

        val expected = NotificationSchedule(
            freePractice = true,
            qualifying = true,
            race = true,
            other = true,
        )
        assertEquals(expected, underTest.notificationSchedule)

        verify {
            mockPreferenceManager.getBoolean(keyNotificationFreePractice, any())
            mockPreferenceManager.getBoolean(keyNotificationQualifying, any())
            mockPreferenceManager.getBoolean(keyNotificationRace, any())
            mockPreferenceManager.getBoolean(keyNotificationOther, any())
        }
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





    //region Notification preferences - Race Notify

    @Test
    fun `is notification race notify reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.notificationNotifyRace)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationRaceNotify, false)
        }
    }

    @Test
    fun `setting notification race notify enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationNotifyRace = true
        verify {
            mockPreferenceManager.save(keyNotificationRaceNotify, true)
        }
    }

    @Test
    fun `setting notification race notify disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationNotifyRace = false
        verify {
            mockPreferenceManager.save(keyNotificationRaceNotify, false)
        }
    }

    //endregion

    //region Notification preferences - Sprint Notify

    @Test
    fun `is notification sprint notify reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.notificationNotifySprint)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationSprintNotify, false)
        }
    }

    @Test
    fun `setting notification sprint notify enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationNotifySprint = true
        verify {
            mockPreferenceManager.save(keyNotificationSprintNotify, true)
        }
    }

    @Test
    fun `setting notification sprint notify disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationNotifySprint = false
        verify {
            mockPreferenceManager.save(keyNotificationSprintNotify, false)
        }
    }

    //endregion

    //region Notification preferences - Qualifying Notify

    @Test
    fun `is notification qualifying notify reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        Assertions.assertTrue(underTest.notificationNotifyQualifying)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationQualifyingNotify, false)
        }
    }

    @Test
    fun `setting notification qualifying notify enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationNotifyQualifying = true
        verify {
            mockPreferenceManager.save(keyNotificationQualifyingNotify, true)
        }
    }

    @Test
    fun `setting notification qualifying notify disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.notificationNotifyQualifying = false
        verify {
            mockPreferenceManager.save(keyNotificationQualifyingNotify, false)
        }
    }

    //endregion






    @Test
    fun `getting notification results queries all preferences`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true
        initUnderTest()

        val expected = NotificationResults(
            sprint = true,
            qualifying = true,
            race = true,
        )
        assertEquals(expected, underTest.notificationResults)

        verify {
            mockPreferenceManager.getBoolean(keyNotificationQualifyingNotify, any())
            mockPreferenceManager.getBoolean(keyNotificationSprintNotify, any())
            mockPreferenceManager.getBoolean(keyNotificationRaceNotify, any())
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
        private const val keyNotificationRace: String = "UP_NEXT_NOTIFICATION_RACE"
        private const val keyNotificationQualifying: String = "UP_NEXT_NOTIFICATION_QUALIFYING"
        private const val keyNotificationFreePractice: String = "UP_NEXT_NOTIFICATION_FREE_PRACTICE"
        private const val keyNotificationOther: String = "UP_NEXT_NOTIFICATION_OTHER"
        private const val keyNotificationReminder: String = "UP_NEXT_NOTIFICATION_REMINDER"

        private const val keyNotificationRaceNotify: String = "UP_NEXT_NOTIFICATION_RACE_NOTIFY"
        private const val keyNotificationSprintNotify: String = "UP_NEXT_NOTIFICATION_SPRINT_NOTIFY"
        private const val keyNotificationQualifyingNotify: String = "UP_NEXT_NOTIFICATION_QUALIFYING_NOTIFY"

        private const val keyNotificationOnboarding: String = "UP_NEXT_NOTIFICATION_ONBOARDING"
    }
}