package tmg.flashback.core.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.core.managers.CrashManager
import tmg.flashback.core.repositories.CoreRepository
import java.lang.RuntimeException

internal class CrashControllerTest() {

    private var mockCoreRepository: CoreRepository = mockk(relaxed = true)
    private var mockCrashManager: CrashManager = mockk(relaxed = true)

    private lateinit var sut: CrashController

    private fun initSUT() {
        sut = CrashController(mockCoreRepository, mockCrashManager)
    }

    //region Crash reporting enabled

    @Test
    fun `CrashController crash reporting enabled`() {
        every { mockCoreRepository.crashReporting } returns true
        initSUT()

        assertTrue(sut.enabled)
        verify { mockCoreRepository.crashReporting }
    }

    @Test
    fun `CrashController crash reporting disabled`() {
        every { mockCoreRepository.crashReporting } returns false
        initSUT()

        assertFalse(sut.enabled)
        verify { mockCoreRepository.crashReporting }
    }

    @Test
    fun `CrashController crash reporting update saves in prefs`() {

        initSUT()

        sut.enabled = true
        verify { mockCoreRepository.crashReporting = true }
    }

    //endregion

    //region Initialisation

    @Test
    fun `CrashController initialise sends all data to firebase`() {
        val expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        every { mockCoreRepository.appFirstBootTime } returns LocalDate.now()
        every { mockCoreRepository.analytics } returns true
        every { mockCoreRepository.appOpenedCount } returns 1
        every { mockCoreRepository.deviceUdid } returns "test-udid"
        every { mockCoreRepository.crashReporting } returns true

        initSUT()
        sut.initialise()

        verify {
            mockCrashManager.initialise(true, true, "test-udid", expectedDate, 1)
        }
    }

    //endregion

    //region Logging

    @Test
    fun `CrashController log msg forwards to firebase if toggle is enabled`() {
        every { mockCoreRepository.crashReporting } returns true

        initSUT()
        sut.log("msg")

        verify { mockCrashManager.logError("msg") }
    }

    @Test
    fun `CrashController log msg forwards to firebase if toggle is disabled`() {
        every { mockCoreRepository.crashReporting } returns false

        initSUT()
        sut.log("msg")

        verify(exactly = 0) { mockCrashManager.logError("msg") }
    }

    @Test
    fun `CrashController log msg with exception forwards to firebase if toggle is enabled`() {
        every { mockCoreRepository.crashReporting } returns true

        val exception = RuntimeException()
        initSUT()
        sut.logError(exception, "msg")

        verify { mockCrashManager.logException(exception, "msg") }
    }

    @Test
    fun `CrashController log msg with exception forwards to firebase if toggle is disabled`() {
        every { mockCoreRepository.crashReporting } returns false

        val exception = RuntimeException()
        initSUT()
        sut.logError(exception, "msg")

        verify(exactly = 0) { mockCrashManager.logException(exception, "msg") }
    }

    //endregion
}