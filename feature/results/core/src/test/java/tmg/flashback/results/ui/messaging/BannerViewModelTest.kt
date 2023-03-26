package tmg.flashback.results.ui.messaging

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.results.repository.models.Banner
import tmg.flashback.web.usecases.OpenWebpageUseCase

internal class BannerViewModelTest {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)

    private lateinit var underTest: BannerViewModel

    private fun initUnderTest() {
        underTest = BannerViewModel(
            homeRepository = mockHomeRepository,
            openWebpageUseCase = mockOpenWebpageUseCase
        )
    }

    @Test
    fun `message gets from home repository`() {
        every { mockHomeRepository.banners } returns listOf(fakeBanner)

        initUnderTest()
        assertEquals(fakeBanner, underTest.message[0])

        verify {
            mockHomeRepository.banners
        }
    }

    @Test
    fun `navigate to about app goes to about app`() {
        initUnderTest()
        underTest.navigateToWeb("url")

        verify {
            mockOpenWebpageUseCase.open("url", title = "")
        }
    }

    private val fakeBanner: Banner
        get() = Banner(
            message = "message",
            url = "https://www.google.com",
            highlight = false,
            season = null
        )
}