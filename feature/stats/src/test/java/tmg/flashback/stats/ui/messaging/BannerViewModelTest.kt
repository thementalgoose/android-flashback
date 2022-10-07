package tmg.flashback.stats.ui.messaging

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.repository.models.Banner
import tmg.flashback.web.WebNavigationComponent

internal class BannerViewModelTest {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)
    private val mockWebNavigationComponent: WebNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: BannerViewModel

    private fun initUnderTest() {
        underTest = BannerViewModel(
            homeRepository = mockHomeRepository,
            webNavigationComponent = mockWebNavigationComponent
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
            mockWebNavigationComponent.web("url")
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