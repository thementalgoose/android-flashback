package tmg.flashback.stats.ui.messaging

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.HomeRepository
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
        val banner = tmg.flashback.stats.repository.models.Banner("Hey")
        every { mockHomeRepository.banner } returns banner

        initUnderTest()
        assertEquals(banner, underTest.message)

        verify {
            mockHomeRepository.banner
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
}