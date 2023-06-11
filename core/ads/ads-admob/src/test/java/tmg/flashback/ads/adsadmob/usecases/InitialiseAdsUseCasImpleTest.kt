package tmg.flashback.ads.adsadmob.usecases

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.adsadmob.manager.AdsManager

internal class InitialiseAdsUseCasImpleTest {

    private val mockAdsManager: AdsManager = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockApplicationContext: Context = mockk(relaxed = true)

    private lateinit var underTest: InitialiseAdsUseCaseImpl

    private fun initUnderTest() {
        underTest = InitialiseAdsUseCaseImpl(
            mockApplicationContext,
            mockAdsManager,
            mockAdsRepository
        )
    }

    @Test
    fun `initialising with ads disabled doesnt call initialize`() {
        every { mockAdsRepository.isEnabled } returns false

        initUnderTest()
        underTest.initialise()

        verify(exactly = 0) {
            mockAdsManager.initialize(any())
        }
    }

    @Test
    fun `initialising with ads enabled calls initialize`() {
        every { mockAdsRepository.isEnabled } returns true

        initUnderTest()
        underTest.initialise()

        verify {
            mockAdsManager.initialize(mockApplicationContext)
        }
    }
}