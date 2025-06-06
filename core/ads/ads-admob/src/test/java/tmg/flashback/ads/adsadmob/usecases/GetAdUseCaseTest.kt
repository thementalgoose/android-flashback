package tmg.flashback.ads.adsadmob.usecases

import android.content.Context
import com.google.android.gms.ads.nativead.NativeAd
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.adsadmob.manager.AdsManager
import tmg.flashback.ads.adsadmob.repository.AdsCacheRepository
import tmg.testutils.BaseTest

internal class GetAdUseCaseTest: BaseTest() {

    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockAdsCacheRepository: AdsCacheRepository = mockk(relaxed = true)
    private val mockAdsManager: AdsManager = mockk(relaxed = true)

    private val mockNativeAd0: NativeAd = mockk(relaxed = true)
    private val mockNativeAd1: NativeAd = mockk(relaxed = true)
    private val mockNativeAds: List<NativeAd> = listOf(mockNativeAd0, mockNativeAd1)
    private val mockContext: Context = mockk(relaxed = true)

    private lateinit var underTest: GetAdUseCase

    private fun initUnderTest() {
        underTest = GetAdUseCase(mockAdsRepository, mockAdsCacheRepository, mockAdsManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockAdsRepository.isEnabled } returns true
        every { mockAdsCacheRepository.listOfAds } returns emptyList()
        coEvery { mockAdsManager.getNativeAd(any()) } returns mockNativeAds
    }

    @Test
    fun `get ad returns null when repository is not enabled`() = coroutineTest {
        every { mockAdsRepository.isEnabled } returns false

        initUnderTest()
        runBlocking {
            assertNull(underTest.getAd(mockContext))
        }
        coVerify(exactly = 0) {
            mockAdsManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns add at index 0 when ads already populated`() = coroutineTest {
        every { mockAdsCacheRepository.listOfAds } returns mockNativeAds

        initUnderTest()
        runBlocking {
            assertEquals(mockNativeAd0, underTest.getAd(mockContext, 0))
        }
        coVerify(exactly = 0) {
            mockAdsManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns add at index 1 when ads already populated`() = coroutineTest {
        every { mockAdsCacheRepository.listOfAds } returns mockNativeAds

        initUnderTest()
        runBlocking {
            assertEquals(mockNativeAd1, underTest.getAd(mockContext, 1))
        }
        coVerify(exactly = 0) {
            mockAdsManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns null if request for ads fails`() = coroutineTest {
        every { mockAdsCacheRepository.listOfAds } returns emptyList()
        coEvery { mockAdsManager.getNativeAd(any()) } throws RuntimeException("Its fucked")

        initUnderTest()
        runBlocking {
            assertNull(underTest.getAd(mockContext, 0))
        }
        coVerify {
            mockAdsManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns add at index 0 when ads request returns`() = coroutineTest {

        initUnderTest()
        runBlocking {
            assertEquals(mockNativeAd0, underTest.getAd(mockContext, 0))
        }
        coVerify {
            mockAdsManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns add at index 1 when ads request returns`() = coroutineTest {
        initUnderTest()
        runBlocking {
            assertEquals(mockNativeAd1, underTest.getAd(mockContext, 1))
        }
        coVerify {
            mockAdsManager.getNativeAd(any())
        }
    }
}