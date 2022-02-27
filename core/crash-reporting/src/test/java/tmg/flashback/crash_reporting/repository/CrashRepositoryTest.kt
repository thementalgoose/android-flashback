package tmg.flashback.crash_reporting.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class CrashRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: CrashRepository

    private fun initUnderTest() {
        underTest = CrashRepository(mockPreferenceManager)
    }

    //region Is crash reporting enabled

    @Test
    fun `is crash reporting enabled reads value from preferences repository with default to true`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        assertTrue(underTest.isEnabled)
        verify {
            mockPreferenceManager.getBoolean(CRASH_REPORTING_KEY, true)
        }
    }

    @Test
    fun `setting crash reporting enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.isEnabled = true
        verify {
            mockPreferenceManager.save(CRASH_REPORTING_KEY, true)
        }
    }

    @Test
    fun `setting crash reporting disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.isEnabled = false
        verify {
            mockPreferenceManager.save(CRASH_REPORTING_KEY, false)
        }
    }

    //endregion

    //region Shake to report enabled

    @Test
    fun `is shake to report enabled reads value from preferences repository with default to true`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        assertTrue(underTest.shakeToReport)
        verify {
            mockPreferenceManager.getBoolean(SHAKE_TO_REPORT_KEY, true)
        }
    }

    @Test
    fun `setting shake to report enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.shakeToReport = true
        verify {
            mockPreferenceManager.save(SHAKE_TO_REPORT_KEY, true)
        }
    }

    @Test
    fun `setting shake to report disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.shakeToReport = false
        verify {
            mockPreferenceManager.save(SHAKE_TO_REPORT_KEY, false)
        }
    }

    //endregion

    companion object {
        private const val CRASH_REPORTING_KEY = "CRASH_REPORTING"
        private const val SHAKE_TO_REPORT_KEY = "SHAKE_TO_REPORT"
    }
}