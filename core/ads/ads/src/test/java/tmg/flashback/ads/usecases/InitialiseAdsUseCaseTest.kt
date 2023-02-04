package tmg.flashback.ads.usecases

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.config.repository.AdsRepository

internal class InitialiseAdsUseCaseTest {

    private val mockAdsManager: AdsManager = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockApplicationContext: Context = mockk(relaxed = true)

    private lateinit var underTest: InitialiseAdsUseCase

    private fun initUnderTest() {
        underTest = InitialiseAdsUseCase(
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