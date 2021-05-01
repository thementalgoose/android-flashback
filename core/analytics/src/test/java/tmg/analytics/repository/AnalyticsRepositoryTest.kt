package tmg.analytics.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.device.repository.SharedPreferenceRepository

internal class AnalyticsRepositoryTest {

    private val mockSharedPreferenceRepository: SharedPreferenceRepository = mockk(relaxed = true)

    private lateinit var sut: AnalyticsRepository

    private fun initSUT() {
        sut = AnalyticsRepository(mockSharedPreferenceRepository)
    }

    //region Is analytics enabled

    @Test
    fun `is analytics enabled reads value from preferences repository with default to true`() {
        every { mockSharedPreferenceRepository.getBoolean(any(), any()) } returns true

        initSUT()

        assertTrue(sut.isEnabled)
        verify {
            mockSharedPreferenceRepository.getBoolean(ANALYTICS_KEY, true)
        }
    }

    @Test
    fun `setting analytics enabled saves value from preferences repository`() {
        initSUT()

        sut.isEnabled = true
        verify {
            mockSharedPreferenceRepository.save(ANALYTICS_KEY, true)
        }
    }

    @Test
    fun `setting analytics disabled saves value from preferences repository`() {
        initSUT()

        sut.isEnabled = false
        verify {
            mockSharedPreferenceRepository.save(ANALYTICS_KEY, false)
        }
    }

    //endregion

    companion object {
        private const val ANALYTICS_KEY = "ANALYTICS_OPT_IN"
    }
}