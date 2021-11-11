package tmg.flashback.common.ui.settings.notifications

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.common.testutils.assertExpectedOrder
import tmg.flashback.common.testutils.findPref
import tmg.flashback.notifications.R
import tmg.flashback.notifications.controllers.NotificationController
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

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
        val expected = listOf(
                Pair(R.string.settings_notifications_title, null),
                Pair(R.string.settings_notifications_channel_qualifying_title, R.string.settings_notifications_channel_qualifying_description),
                Pair(R.string.settings_notifications_channel_race_title, R.string.settings_notifications_channel_race_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `init without notification channels supported shows notification items`() {
        every { mockNotificationController.isNotificationChannelsSupported } returns false

        initSUT()
        val expected = listOf(
                Pair(R.string.settings_notifications_title, null),
                Pair(R.string.settings_notifications_nonchannel_title, R.string.settings_notifications_nonchannel_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `click pref for notification qualifying fires open notification with channel`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_notifications_channel_qualifying_title))
        sut.outputs.openNotificationsChannel.test {
            assertDataEventValue("qualifying")
        }
    }

    @Test
    fun `click pref for notification race fires open notification with channel`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_notifications_channel_race_title))
        sut.outputs.openNotificationsChannel.test {
            assertDataEventValue("race")
        }
    }

    @Test
    fun `click pref for notification fires open notification with channel`() {
        every { mockNotificationController.isNotificationChannelsSupported } returns false
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_notifications_nonchannel_title))
        sut.outputs.openNotifications.test {
            assertEventFired()
        }
    }
}