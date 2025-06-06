package tmg.flashback.season.presentation.messaging

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.season.repository.HomeRepository

internal class ProvidedByViewModelTest {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)
    private val mockNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: ProvidedByViewModel

    private fun initUnderTest() {
        underTest = ProvidedByViewModel(
            homeRepository = mockHomeRepository,
            navigationComponent = mockNavigationComponent
        )
    }

    @Test
    fun `message gets from home repository`() {
        every { mockHomeRepository.dataProvidedBy } returns "data provided by"

        initUnderTest()
        assertEquals("data provided by", underTest.message)

        verify {
            mockHomeRepository.dataProvidedBy
        }
    }

    @Test
    fun `navigate to about app goes to about app`() {
        initUnderTest()
        underTest.navigateToAboutApp()

        verify {
            mockNavigationComponent.aboutApp()
        }
    }
}