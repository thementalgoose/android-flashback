package tmg.crash_reporting.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.device.repository.SharedPreferenceRepository

internal class CrashRepositoryTest {

    private val mockSharedPreferenceRepository: SharedPreferenceRepository = mockk(relaxed = true)

    private lateinit var sut: CrashRepository

    private fun initSUT() {
        sut = CrashRepository(mockSharedPreferenceRepository)
    }

    //region Is crash reporting enabled

    @Test
    fun `is crash reporting enabled reads value from preferences repository with default to true`() {
        every { mockSharedPreferenceRepository.getBoolean(any(), any()) } returns true

        initSUT()

        assertTrue(sut.isEnabled)
        verify {
            mockSharedPreferenceRepository.getBoolean(CRASH_REPORTING_KEY, true)
        }
    }

    @Test
    fun `setting crash reporting enabled saves value from preferences repository`() {
        initSUT()

        sut.isEnabled = true
        verify {
            mockSharedPreferenceRepository.save(CRASH_REPORTING_KEY, true)
        }
    }

    @Test
    fun `setting crash reporting disabled saves value from preferences repository`() {
        initSUT()

        sut.isEnabled = false
        verify {
            mockSharedPreferenceRepository.save(CRASH_REPORTING_KEY, false)
        }
    }

    //endregion

    //region Shake to report enabled

    @Test
    fun `is shake to report enabled reads value from preferences repository with default to true`() {
        every { mockSharedPreferenceRepository.getBoolean(any(), any()) } returns true

        initSUT()

        assertTrue(sut.shakeToReport)
        verify {
            mockSharedPreferenceRepository.getBoolean(SHAKE_TO_REPORT_KEY, true)
        }
    }

    @Test
    fun `setting shake to report enabled saves value from preferences repository`() {
        initSUT()

        sut.shakeToReport = true
        verify {
            mockSharedPreferenceRepository.save(SHAKE_TO_REPORT_KEY, true)
        }
    }

    @Test
    fun `setting shake to report disabled saves value from preferences repository`() {
        initSUT()

        sut.shakeToReport = false
        verify {
            mockSharedPreferenceRepository.save(SHAKE_TO_REPORT_KEY, false)
        }
    }

    //endregion

    companion object {
        private const val CRASH_REPORTING_KEY = "CRASH_REPORTING"
        private const val SHAKE_TO_REPORT_KEY = "SHAKE_TO_REPORT"
    }
}