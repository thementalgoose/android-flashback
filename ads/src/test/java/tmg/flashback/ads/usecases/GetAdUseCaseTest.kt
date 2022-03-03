package tmg.flashback.ads.usecases

import com.google.android.gms.ads.nativead.NativeAd
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.repository.AdsRepository
import tmg.testutils.BaseTest

internal class GetAdUseCaseTest: BaseTest() {

    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockAdsManager: AdsManager = mockk(relaxed = true)

    private val mockNativeAd: NativeAd = mockk(relaxed = true)
    private val mockNativeAds: List<NativeAd> = listOf(mockNativeAd)

    private lateinit var underTest: GetAdUseCase

    private fun initUnderTest() {
        underTest = GetAdUseCase(mockAdsRepository, mockAdsManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockAdsRepository.isEnabled } returns true
        coEvery { mockAdsManager.getNativeAd(any()) } returns mockNativeAds
    }

    @Test
    fun `get ad returns null if not enabled in repository`() = coroutineTest {
        every { mockAdsRepository.isEnabled } returns false
        initUnderTest()
        runBlockingTest {
            assertNull(underTest.getAd(mockk()))
        }
        coVerify(exactly = 0) {
            mockAdsManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns null if manager returns empty ad`() = coroutineTest {
        every { mockAdsRepository.isEnabled } returns true
        coEvery { mockAdsManager.getNativeAd(any()) } returns emptyList()
        initUnderTest()
        runBlockingTest {
            assertNull(underTest.getAd(mockk()))
        }
        coVerify {
            mockAdsManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns native ad from manager if available`() = coroutineTest {
        every { mockAdsRepository.isEnabled } returns true
        initUnderTest()
        runBlockingTest {
            assertEquals(mockNativeAd, underTest.getAd(mockk()))
        }
        coVerify {
            mockAdsManager.getNativeAd(any())
        }
    }
}