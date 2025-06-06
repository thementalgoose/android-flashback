package tmg.flashback.presentation.settings.ads

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.presentation.settings.Settings
import tmg.testutils.BaseTest

internal class SettingsAdsViewModelTest: BaseTest() {

    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsAdsViewModel

    private fun initUnderTest() {
        underTest = SettingsAdsViewModel(
            adsRepository = mockAdsRepository
        )
    }

    @Test
    fun `ads enabled is true when user pref is enabled`() = runTest(testDispatcher) {
        every { mockAdsRepository.userPrefEnabled } returns true

        initUnderTest()
        underTest.outputs.adsEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `ads enabled is false when user pref is false`() = runTest(testDispatcher) {
        every { mockAdsRepository.userPrefEnabled } returns false

        initUnderTest()
        underTest.outputs.adsEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `clicking ads enabled updates pref and updates values`() = runTest(testDispatcher) {
        every { mockAdsRepository.userPrefEnabled } returns false

        initUnderTest()
        underTest.outputs.adsEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Ads.enableAds(true))

        verify {
            mockAdsRepository.userPrefEnabled = true
        }
        underTest.outputs.adsEnabled.test { awaitItem() }
    }
}