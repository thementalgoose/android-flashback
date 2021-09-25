package tmg.notifications.managers

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.lang.NullPointerException
import org.junit.Ignore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.crash_reporting.controllers.CrashController
import tmg.notifications.navigation.NotificationNavigationProvider
import tmg.testutils.BaseTest

internal class SystemNotificationManagerTest: BaseTest() {

    private val mockApplicationContext: Context = mockk(relaxed = true)
    private val mockCrashController: CrashController = mockk(relaxed = true)
    private val mockNavigationProvider: NotificationNavigationProvider = mockk(relaxed = true)

    private val mockNotificationManager: NotificationManager = mockk(relaxed = true)

    private lateinit var sut: SystemNotificationManager

    private fun initSUT() {
        sut = SystemNotificationManager(
            mockApplicationContext,
            mockCrashController,
            mockNavigationProvider
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE) } returns mockNotificationManager
    }

    @Test
    @Ignore("Internal android class throws null pointer")
    fun `building notification builds notification properly with intent from navigation provider`() {

        /* Cannot work in unit test environment because of internal notification builder */
    }

    @Test
    fun `notification notify calls navigation manager`() {

        initSUT()
        val notification: Notification = mockk()

        sut.notify("tag", 1, notification)

        verify {
            mockNotificationManager.notify("tag", 1, notification)
        }
    }

    @Test
    fun `notification notify logs to crash controller if notification manager is null`() {
        every { mockApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE) } returns null

        initSUT()
        val notification: Notification = mockk()

        sut.notify("tag", 1, notification)

        verify {
            mockCrashController.logError(any(), "Notification Manager null when notifying (tag,1)")
        }
    }

    @Test
    fun `notification cancel calls navigation manager`() {

        val expectedTag = "tag"
        val expectedId = 1

        initSUT()
        sut.cancel(expectedTag, expectedId)

        verify {
            mockNotificationManager.cancel(expectedTag, expectedId)
        }
    }

    @Test
    fun `notification cancel logs to crash controller if notification manager is null`() {
        every { mockApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE) } returns null

        val expectedTag = "tag"
        val expectedId = 1

        initSUT()
        sut.cancel(expectedTag, expectedId)

        verify {
            mockCrashController.logError(any(), "Notification Manager null when cancelling (tag,1)")
        }
    }

    @Test
    fun `notification cancel all calls navigation manager`() {

        initSUT()
        sut.cancelAll()

        verify {
            mockNotificationManager.cancelAll()
        }
    }

    @Test
    fun `notification cancel all logs to crash controller if notification manager is null`() {
        every { mockApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE) } returns null

        initSUT()
        sut.cancelAll()

        verify {
            mockCrashController.logError(any(), "Notification Manager null when cancelling all")
        }
    }
}