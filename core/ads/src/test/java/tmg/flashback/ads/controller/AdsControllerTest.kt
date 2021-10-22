package tmg.flashback.ads.controller

import com.google.android.gms.ads.nativead.NativeAd
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.repository.AdsRepository
import tmg.testutils.BaseTest

internal class AdsControllerTest: BaseTest() {

    private val mockRepository: AdsRepository = mockk(relaxed = true)
    private val mockManager: AdsManager = mockk(relaxed = true)

    private val mockNativeAds: List<NativeAd> = listOf(mockk(relaxed = true))

    private lateinit var sut: AdsController

    private fun initSUT() {
        sut = AdsController(mockRepository, mockManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockRepository.isEnabled } returns true
        coEvery { mockManager.getNativeAd(any()) } returns mockNativeAds
    }

    @Test
    fun `are adverts enabled reads value from repository`() {
        every { mockRepository.isEnabled } returns true
        initSUT()
        assertTrue(sut.areAdvertsEnabled)
        verify {
            mockRepository.isEnabled
        }
    }

    @Test
    fun `initialize the manager`() {
        initSUT()
        sut.initialise(mockk())
        verify {
            mockManager.initialize(any())
        }
    }

    @Test
    fun `get ad returns null if not enabled in repository`() = coroutineTest {
        every { mockRepository.isEnabled } returns false
        initSUT()
        runBlockingTest {
            assertNull(sut.getAd(mockk()))
        }
        coVerify(exactly = 0) {
            mockManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns null if manager returns empty ad`() {
        every { mockRepository.isEnabled } returns true
        coEvery { mockManager.getNativeAd(any()) } returns emptyList()
        initSUT()
        runBlockingTest {
            assertNull(sut.getAd(mockk()))
        }
        coVerify {
            mockManager.getNativeAd(any())
        }
    }

    @Test
    fun `get ad returns native ad from manager if available`() {
        every { mockRepository.isEnabled } returns true
        initSUT()
        runBlockingTest {
            assertEquals(mockNativeAds, sut.getAd(mockk()))
        }
        coVerify {
            mockManager.getNativeAd(any())
        }
    }
}
