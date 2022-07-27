package tmg.flashback.ads.usecases

import com.google.android.gms.ads.nativead.NativeAd
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.config.repository.AdsRepository

internal class ClearCachedAdvertsUseCaseTest {

    private val mockRepository: AdsRepository = mockk(relaxed = true)
    private val mockAdvert: NativeAd = mockk(relaxed = true)

    private lateinit var underTest: ClearCachedAdvertsUseCase

    private fun initUnderTest() {
        underTest = ClearCachedAdvertsUseCase(mockRepository)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockRepository.listOfAds } returns listOf(mockAdvert)
    }

    @Test
    fun `clear calls destroy on mock adverts and sets list to empty`() {
        initUnderTest()
        underTest.clear()

        verify {
            mockAdvert.destroy()
            mockRepository.listOfAds = emptyList()
        }
    }
}