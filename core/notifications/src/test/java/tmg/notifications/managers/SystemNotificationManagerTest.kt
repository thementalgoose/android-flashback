package tmg.notifications.managers

import android.content.Context
import io.mockk.mockk
import org.junit.jupiter.api.Test
import tmg.crash_reporting.controllers.CrashController
import tmg.notifications.navigation.NotificationNavigationProvider
import tmg.testutils.BaseTest

internal class SystemNotificationManagerTest: BaseTest() {

    private val mockApplicationContext: Context = mockk(relaxed = true)
    private val mockCrashController: CrashController = mockk(relaxed = true)
    private val mockNavigationProvider: NotificationNavigationProvider = mockk(relaxed = true)

    private lateinit var sut: SystemNotificationManager

    private fun initSUT() {
        sut = SystemNotificationManager(
            mockApplicationContext,
            mockCrashController,
            mockNavigationProvider
        )
    }

    @Test
    fun `building notification builds notification properly with intent from navigation provider`() {

        TODO()
    }

    @Test
    fun `notification notify calls navigation manager`() {

        TODO()
    }

    @Test
    fun `notification notify logs to crash controller if notification manager is null`() {

        TODO()
    }

    @Test
    fun `notification cancel calls navigation manager`() {

        TODO()
    }

    @Test
    fun `notification cancel logs to crash controller if notification manager is null`() {

        TODO()
    }

    @Test
    fun `notification cancel all calls navigation manager`() {

        TODO()
    }

    @Test
    fun `notification cancel all logs to crash controller if notification manager is null`() {

        TODO()
    }
}