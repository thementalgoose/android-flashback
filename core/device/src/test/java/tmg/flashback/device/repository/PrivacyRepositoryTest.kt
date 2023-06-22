package tmg.flashback.device.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class PrivacyRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: PrivacyRepository

    private fun initUnderTest() {
        underTest = PrivacyRepository(mockPreferenceManager)
    }

    //region Is analytics enabled

    @Test
    fun `is analytics enabled reads value from preferences repository with default to true`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        assertTrue(underTest.analytics)
        verify {
            mockPreferenceManager.getBoolean(ANALYTICS_KEY, true)
        }
    }

    @Test
    fun `setting analytics enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.analytics = true
        verify {
            mockPreferenceManager.save(ANALYTICS_KEY, true)
        }
    }

    @Test
    fun `setting analytics disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.analytics = false
        verify {
            mockPreferenceManager.save(ANALYTICS_KEY, false)
        }
    }

    //endregion

    //region Is crash reporting enabled

    @Test
    fun `is crash reporting enabled reads value from preferences repository with default to true`() {
        every { mockPreferenceManager.getBoolean(any(), any()) } returns true

        initUnderTest()

        assertTrue(underTest.crashReporting)
        verify {
            mockPreferenceManager.getBoolean(CRASH_REPORTING_KEY, true)
        }
    }

    @Test
    fun `setting crash reporting enabled saves value from preferences repository`() {
        initUnderTest()

        underTest.crashReporting = true
        verify {
            mockPreferenceManager.save(CRASH_REPORTING_KEY, true)
        }
    }

    @Test
    fun `setting crash reporting disabled saves value from preferences repository`() {
        initUnderTest()

        underTest.crashReporting = false
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
        private const val ANALYTICS_KEY = "ANALYTICS_OPT_IN"
        private const val CRASH_REPORTING_KEY = "CRASH_REPORTING"
        private const val SHAKE_TO_REPORT_KEY = "SHAKE_TO_REPORT"
    }
}