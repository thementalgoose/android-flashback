package tmg.flashback.web.ui.browser

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.device.usecases.OpenUrlUseCase
import tmg.flashback.web.presentation.browser.WebViewModel
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.flashback.web.usecases.ShareUseCase

internal class WebViewModelTest {

    private val mockOpenWebpageUseCase: OpenUrlUseCase = mockk(relaxed = true)
    private val mockShareUseCase: ShareUseCase = mockk(relaxed = true)

    private lateinit var underTest: WebViewModel

    private fun initUnderTest() {
        underTest = WebViewModel(
            openWebpageUseCase = mockOpenWebpageUseCase,
            shareUseCase = mockShareUseCase,
        )
    }

    @Test
    fun `opening webpage calls use case`() {
        initUnderTest()
        underTest.openWebpage("url")
        verify {
            mockOpenWebpageUseCase.openUrl("url")
        }
    }

    @Test
    fun `sharing webpage calls use case`() {
        initUnderTest()
        underTest.openShare("url")
        verify {
            mockShareUseCase.shareUrl("url")
        }
    }
}