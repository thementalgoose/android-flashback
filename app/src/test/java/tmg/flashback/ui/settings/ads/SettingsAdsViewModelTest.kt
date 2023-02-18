package tmg.flashback.ui.settings.ads

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.ads.contract.repository.AdsRepository
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsAdsViewModelTest: BaseTest() {

    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsAdsViewModel

    private fun initUnderTest() {
        underTest = SettingsAdsViewModel(
            adsRepository = mockAdsRepository
        )
    }

    @Test
    fun `ads enabled is true when user pref is enabled`() {
        every { mockAdsRepository.userPrefEnabled } returns true

        initUnderTest()
        underTest.outputs.adsEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `ads enabled is false when user pref is false`() {
        every { mockAdsRepository.userPrefEnabled } returns false

        initUnderTest()
        underTest.outputs.adsEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `clicking ads enabled updates pref and updates values`() {
        every { mockAdsRepository.userPrefEnabled } returns false

        initUnderTest()
        val observer = underTest.outputs.adsEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Ads.enableAds(true))

        verify {
            mockAdsRepository.userPrefEnabled = true
        }
        observer.assertEmittedCount(2)
    }
}