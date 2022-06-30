package tmg.flashback.statistics.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class UpNextRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: UpNextRepository

    private fun initSUT() {
        sut = UpNextRepository(mockPreferenceManager)
    }

    //region Notification preferences - Race

    @Test
    fun `is notification race reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initSUT()

        Assertions.assertTrue(sut.notificationRace)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationRace, false)
        }
    }

    @Test
    fun `setting notification race enabled saves value from preferences repository`() {
        initSUT()

        sut.notificationRace = true
        verify {
            mockPreferenceManager.save(keyNotificationRace, true)
        }
    }

    @Test
    fun `setting notification race disabled saves value from preferences repository`() {
        initSUT()

        sut.notificationRace = false
        verify {
            mockPreferenceManager.save(keyNotificationRace, false)
        }
    }

    //endregion

    //region Notification preferences - Qualifying

    @Test
    fun `is notification qualifying reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initSUT()

        Assertions.assertTrue(sut.notificationQualifying)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationQualifying, false)
        }
    }

    @Test
    fun `setting notification qualifying enabled saves value from preferences repository`() {
        initSUT()

        sut.notificationQualifying = true
        verify {
            mockPreferenceManager.save(keyNotificationQualifying, true)
        }
    }

    @Test
    fun `setting notification qualifying disabled saves value from preferences repository`() {
        initSUT()

        sut.notificationQualifying = false
        verify {
            mockPreferenceManager.save(keyNotificationQualifying, false)
        }
    }

    //endregion

    //region Notification preferences - Free Practice

    @Test
    fun `is notification free practice reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initSUT()

        Assertions.assertTrue(sut.notificationFreePractice )
        verify {
            mockPreferenceManager.getBoolean(keyNotificationFreePractice, false)
        }
    }

    @Test
    fun `setting notification free practice enabled saves value from preferences repository`() {
        initSUT()

        sut.notificationFreePractice = true
        verify {
            mockPreferenceManager.save(keyNotificationFreePractice, true)
        }
    }

    @Test
    fun `setting notification free practice disabled saves value from preferences repository`() {
        initSUT()

        sut.notificationFreePractice = false
        verify {
            mockPreferenceManager.save(keyNotificationFreePractice, false)
        }
    }

    //endregion

    //region Notification preferences - Other

    @Test
    fun `is notification other reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initSUT()

        Assertions.assertTrue(sut.notificationOther )
        verify {
            mockPreferenceManager.getBoolean(keyNotificationOther, false)
        }
    }

    @Test
    fun `setting notification other enabled saves value from preferences repository`() {
        initSUT()

        sut.notificationOther = true
        verify {
            mockPreferenceManager.save(keyNotificationOther, true)
        }
    }

    @Test
    fun `setting notification other disabled saves value from preferences repository`() {
        initSUT()

        sut.notificationOther = false
        verify {
            mockPreferenceManager.save(keyNotificationOther, false)
        }
    }

    //endregion

    //region Notification preferences - Onboarding

    @Test
    fun `is notification onboarding reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initSUT()

        Assertions.assertTrue(sut.seenNotificationOnboarding )
        verify {
            mockPreferenceManager.getBoolean(keyNotificationOnboarding, false)
        }
    }

    @Test
    fun `setting notification onboarding enabled saves value from preferences repository`() {
        initSUT()

        sut.seenNotificationOnboarding = true
        verify {
            mockPreferenceManager.save(keyNotificationOnboarding, true)
        }
    }

    @Test
    fun `setting notification onboarding disabled saves value from preferences repository`() {
        initSUT()

        sut.seenNotificationOnboarding = false
        verify {
            mockPreferenceManager.save(keyNotificationOnboarding, false)
        }
    }

    //endregion





    //region Notification preferences - Race Notify

    @Test
    fun `is notification race notify reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initSUT()

        Assertions.assertTrue(sut.notificationNotifyRace)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationRaceNotify, false)
        }
    }

    @Test
    fun `setting notification race notify enabled saves value from preferences repository`() {
        initSUT()

        sut.notificationNotifyRace = true
        verify {
            mockPreferenceManager.save(keyNotificationRaceNotify, true)
        }
    }

    @Test
    fun `setting notification race notify disabled saves value from preferences repository`() {
        initSUT()

        sut.notificationNotifyRace = false
        verify {
            mockPreferenceManager.save(keyNotificationRaceNotify, false)
        }
    }

    //endregion

    //region Notification preferences - Sprint Notify

    @Test
    fun `is notification sprint notify reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initSUT()

        Assertions.assertTrue(sut.notificationNotifySprint)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationSprintNotify, false)
        }
    }

    @Test
    fun `setting notification sprint notify enabled saves value from preferences repository`() {
        initSUT()

        sut.notificationNotifySprint = true
        verify {
            mockPreferenceManager.save(keyNotificationSprintNotify, true)
        }
    }

    @Test
    fun `setting notification sprint notify disabled saves value from preferences repository`() {
        initSUT()

        sut.notificationNotifySprint = false
        verify {
            mockPreferenceManager.save(keyNotificationSprintNotify, false)
        }
    }

    //endregion

    //region Notification preferences - Qualifying Notify

    @Test
    fun `is notification qualifying notify reads value from preferences repository with default to false`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initSUT()

        Assertions.assertTrue(sut.notificationNotifyQualifying)
        verify {
            mockPreferenceManager.getBoolean(keyNotificationQualifyingNotify, false)
        }
    }

    @Test
    fun `setting notification qualifying notify enabled saves value from preferences repository`() {
        initSUT()

        sut.notificationNotifyQualifying = true
        verify {
            mockPreferenceManager.save(keyNotificationQualifyingNotify, true)
        }
    }

    @Test
    fun `setting notification qualifying notify disabled saves value from preferences repository`() {
        initSUT()

        sut.notificationNotifyQualifying = false
        verify {
            mockPreferenceManager.save(keyNotificationQualifyingNotify, false)
        }
    }

    //endregion



    //region Notification preferences - Reminder

    @Test
    fun `is notification reminder reads value from preferences repository`() {
        every { mockPreferenceManager.getInt(any(), any()) } returns 900

        initSUT()

        assertEquals(tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_15, sut.notificationReminderPeriod)
        verify {
            mockPreferenceManager.getInt(keyNotificationReminder, 1800)
        }
    }

    @Test
    fun `is notification reminder reads value from preferences repository with invalid defaults to 30 mins`() {
        every { mockPreferenceManager.getInt(any(), any()) } returns 1

        initSUT()

        assertEquals(tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_30, sut.notificationReminderPeriod)
        verify {
            mockPreferenceManager.getInt(keyNotificationReminder, 1800)
        }
    }

    @Test
    fun `setting notification reminder enabled saves value from preferences repository`() {
        initSUT()

        sut.notificationReminderPeriod = tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_60
        verify {
            mockPreferenceManager.save(keyNotificationReminder, tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_60.seconds)
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