package tmg.flashback.search.presentation

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.ads.repository.model.AdvertConfig
import tmg.testutils.BaseTest

internal class SearchViewModelTest: BaseTest() {

    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)

    private lateinit var underTest: SearchViewModel

    private fun initUnderTest() {
        underTest = SearchViewModel(
            adsRepository = mockAdsRepository
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockAdsRepository.areAdvertsEnabled } returns false
    }

    @Test
    fun `initialise will show ads settings from repo`() = runTest {
        every { mockAdsRepository.areAdvertsEnabled } returns true
        every { mockAdsRepository.advertConfig } returns AdvertConfig(onSearch = true)

        initUnderTest()

        underTest.outputs.uiState.test {
            assertTrue(awaitItem().showAdvert)
        }
    }

    @Test
    fun `initialise will not show ads from repo if config isnt enabled`() = runTest {
        every { mockAdsRepository.areAdvertsEnabled } returns false
        every { mockAdsRepository.advertConfig } returns AdvertConfig(onSearch = true)

        initUnderTest()

        underTest.outputs.uiState.test {
            assertFalse(awaitItem().showAdvert)
        }
    }

    @Test
    fun `selecting type will update state`() = runTest {
        initUnderTest()

        underTest.inputs.selectType(SearchScreenStateCategory.CIRCUITS)

        underTest.outputs.uiState.test {
            assertEquals(SearchScreenStateCategory.CIRCUITS, awaitItem().category)
        }
    }

    @Test
    fun `search term updated will update state`() = runTest {
        initUnderTest()

        underTest.inputs.searchTermUpdated("test")

        underTest.outputs.uiState.test {
            assertEquals("test", awaitItem().searchTerm)
        }
    }

    @Test
    fun `search term clear will update state`() = runTest {
        initUnderTest()

        underTest.inputs.searchTermUpdated("test")

        underTest.outputs.uiState.test {
            assertEquals("test", awaitItem().searchTerm)

            underTest.inputs.searchTermClear()

            assertEquals("", awaitItem().searchTerm)
        }
    }
}