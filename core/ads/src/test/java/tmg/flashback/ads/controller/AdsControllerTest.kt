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

    private val mockNativeAd: NativeAd = mockk(relaxed = true)
    private val mockNativeAds: List<NativeAd> = listOf(mockNativeAd)

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
    fun `user config adverts enabled reads value from repo`() {
        every { mockRepository.userPrefEnabled } returns true
        initSUT()
        assertTrue(sut.userConfigAdvertsEnabled)
        verify {
            mockRepository.userPrefEnabled
        }
    }

    @Test
    fun `user config adverts enabled writes value to repo`() {
        initSUT()
        sut.userConfigAdvertsEnabled = true
        verify {
            mockRepository.userPrefEnabled = true
        }
    }

    @Test
    fun `allow user config reads value from repo`() {
        every { mockRepository.allowUserConfig } returns true
        initSUT()
        assertTrue(sut.allowUserConfig)
        verify {
            mockRepository.allowUserConfig
        }
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
    fun `are adverts enabled doesnt change if repository value does`() {
        every { mockRepository.isEnabled } returns true
        initSUT()
        assertTrue(sut.areAdvertsEnabled)

        every { mockRepository.isEnabled } returns false
        assertTrue(sut.areAdvertsEnabled)
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
            assertEquals(mockNativeAd, sut.getAd(mockk()))
        }
        coVerify {
            mockManager.getNativeAd(any())
        }
    }
}