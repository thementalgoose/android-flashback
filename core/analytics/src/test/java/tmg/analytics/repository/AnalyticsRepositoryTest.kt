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

        assertTrue(sut.isAnalyticsEnabled)
        verify {
            mockSharedPreferenceRepository.getBoolean("ANALYTICS_OPT_IN", true)
        }
    }

    @Test
    fun `setting analytics enabled saves value from preferences repository`() {
        initSUT()

        sut.isAnalyticsEnabled = true
        verify {
            mockSharedPreferenceRepository.save("ANALYTICS_OPT_IN", true)
        }
    }

    @Test
    fun `setting analytics disabled saves value from preferences repository`() {
        initSUT()

        sut.isAnalyticsEnabled = false
        verify {
            mockSharedPreferenceRepository.save("ANALYTICS_OPT_IN", false)
        }
    }

    //endregion
}