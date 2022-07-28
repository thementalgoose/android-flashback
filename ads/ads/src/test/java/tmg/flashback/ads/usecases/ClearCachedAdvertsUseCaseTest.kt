package tmg.flashback.ads.usecases

import com.google.android.gms.ads.nativead.NativeAd
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.repository.AdsCacheRepository

internal class ClearCachedAdvertsUseCaseTest {

    private val mockAdsCacheRepository: AdsCacheRepository = mockk(relaxed = true)
    private val mockAdvert: NativeAd = mockk(relaxed = true)

    private lateinit var underTest: ClearCachedAdvertsUseCase

    private fun initUnderTest() {
        underTest = ClearCachedAdvertsUseCase(mockAdsCacheRepository)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockAdsCacheRepository.listOfAds } returns listOf(mockAdvert)
    }

    @Test
    fun `clear calls destroy on mock adverts and sets list to empty`() {
        initUnderTest()
        underTest.clear()

        verify {
            mockAdvert.destroy()
            mockAdsCacheRepository.listOfAds = emptyList()
        }
    }
}