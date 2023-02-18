package tmg.flashback.ads.core.components

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.ads.contract.repository.AdsRepository

internal class NativeBannerViewModelTest {

    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)

    private lateinit var underTest: NativeBannerViewModel

    private fun initUnderTest() {
        underTest = NativeBannerViewModel(
            adsRepository = mockAdsRepository
        )
    }

    @Test
    fun `adverts enabled reads from repository`() {
        every { mockAdsRepository.areAdvertsEnabled } returns true

        initUnderTest()
        assertTrue(underTest.areAdvertsEnabled)

        verify {
            mockAdsRepository.areAdvertsEnabled
        }
    }
}