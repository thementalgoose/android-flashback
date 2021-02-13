package tmg.flashback.ui.settings.notifications

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.controllers.NotificationController
import tmg.flashback.managers.notifications.FirebasePushNotificationManager.Companion.topicQualifying
import tmg.flashback.managers.notifications.FirebasePushNotificationManager.Companion.topicRace
import tmg.flashback.testutils.*

internal class SettingsNotificationViewModelTest: BaseTest() {

    private var mockNotificationController: NotificationController = mockk(relaxed = true)

    private lateinit var sut: SettingsNotificationViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationController.isNotificationChannelsSupported } returns true
    }

    private fun initSUT() {
        sut = SettingsNotificationViewModel(mockNotificationController)
    }

    @Test
    fun `init with notification channels supported shows notification items`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_notifications_title),
                AppPreferencesItem.Preference("NotificationQualifying", R.string.settings_notifications_channel_qualifying_title, R.string.settings_notifications_channel_qualifying_description),
                AppPreferencesItem.Preference("NotificationRace", R.string.settings_notifications_channel_race_title, R.string.settings_notifications_channel_race_description)
            ))
        }
    }

    @Test
    fun `init without notification channels supported shows notification items`() {
        every { mockNotificationController.isNotificationChannelsSupported } returns false
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_notifications_title),
                AppPreferencesItem.Preference("NotificationSettings", R.string.settings_notifications_nonchannel_title, R.string.settings_notifications_nonchannel_description),
            ))
        }
    }

    @Test
    fun `click race launches channel event with correct topic`() {
        initSUT()
        sut.inputs.preferenceClicked("NotificationRace", null)
        sut.outputs.openNotifications.test {
            assertEventNotFired()
        }
        sut.outputs.openNotificationsChannel.test {
            assertDataEventValue(topicRace)
        }
    }

    @Test
    fun `click qualifying launches channel event with correct topic`() {
        initSUT()
        sut.inputs.preferenceClicked("NotificationQualifying", null)
        sut.outputs.openNotifications.test {
            assertEventNotFired()
        }
        sut.outputs.openNotificationsChannel.test {
            assertDataEventValue(topicQualifying)
        }
    }

    @Test
    fun `click notification launches settings`() {
        initSUT()
        sut.inputs.preferenceClicked("NotificationSettings", null)
        sut.outputs.openNotifications.test {
            assertEventFired()
        }
        sut.outputs.openNotificationsChannel.test {
            assertEventNotFired()
        }
    }
}