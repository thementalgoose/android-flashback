package tmg.flashback.presentation.aboutthisapp

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.aboutthisapp.configuration.Configuration
import tmg.flashback.device.usecases.CopyToClipboardUseCase
import tmg.flashback.device.usecases.OpenPlayStoreUseCase
import tmg.flashback.device.usecases.OpenSendEmailUseCase
import tmg.flashback.repositories.ContactRepository
import tmg.flashback.web.usecases.OpenWebpageUseCase

internal class AboutThisAppViewModelTest {

    private val mockAboutThisAppConfigProvider: AboutThisAppConfigProvider = mockk(relaxed = true)
    private val mockContactRepository: ContactRepository = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
    private val mockOpenPlayStoreUseCase: OpenPlayStoreUseCase = mockk(relaxed = true)
    private val mockOpenSendEmailUseCase: OpenSendEmailUseCase = mockk(relaxed = true)
    private val mockCopyToClipboardUseCase: CopyToClipboardUseCase = mockk(relaxed = true)

    private lateinit var underTest: AboutThisAppViewModel

    private val configuration = mockk<Configuration>()

    @BeforeEach
    fun setUp() {
        every { mockAboutThisAppConfigProvider.getConfig() } returns configuration
    }

    private fun initUnderTest() {
        underTest = AboutThisAppViewModel(
            aboutThisAppConfigProvider = mockAboutThisAppConfigProvider,
            contactRepository = mockContactRepository,
            openWebpageUseCase = mockOpenWebpageUseCase,
            openPlayStoreUseCase = mockOpenPlayStoreUseCase,
            openSendEmailUseCase = mockOpenSendEmailUseCase,
            copyToClipboardUseCase = mockCopyToClipboardUseCase,
        )
    }

    @Test
    fun `config is set to initial value`() = runTest {
        initUnderTest()
        underTest.config.test {
            assertEquals(configuration, awaitItem())
        }
    }

    @Test
    fun `open url calls open webpage use case`() {
        initUnderTest()
        underTest.openUrl("url")

        verify {
            mockOpenWebpageUseCase.open("url", "")
        }
    }

    @Test
    fun `open playstore calls open playstore use case`() {
        initUnderTest()
        underTest.openPlaystore()

        verify {
            mockOpenPlayStoreUseCase.openPlaystore()
        }
    }

    @Test
    fun `open email calls open email use case`() {
        every { mockContactRepository.contactEmail } returns "email"
        initUnderTest()
        underTest.openEmail()
        verify {
            mockOpenSendEmailUseCase.sendEmail("email")
        }
    }

    @Test
    fun `copy to clipboard calls copy to clipboard use case`() {
        initUnderTest()
        underTest.copyToClipboard("content")
        verify {
            mockCopyToClipboardUseCase.copyToClipboard("content")
        }
    }
}