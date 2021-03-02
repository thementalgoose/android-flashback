package tmg.flashback.core.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FeatureControllerTest {

    private var mockConfigurationController: ConfigurationController = mockk(relaxed = true)

    private lateinit var sut: FeatureController

    private fun initSUT() {
        sut = FeatureController(mockConfigurationController)
    }

    //region RSS

    @Test
    fun `rss enabled`() {
        every { mockConfigurationController.rss } returns true
        initSUT()
        assertTrue(sut.rssEnabled)
        verify { mockConfigurationController.rss }
    }

    @Test
    fun `rss disabled`() {
        every { mockConfigurationController.rss } returns false
        initSUT()
        assertFalse(sut.rssEnabled)
        verify { mockConfigurationController.rss }
    }

    //endregion

    //region Search

    // TODO: Update this test when search functionality is enabled
    @Test
    fun `search enabled`() {
        every { mockConfigurationController.search } returns true
        initSUT()
        assertFalse(sut.searchEnabled)
//        verify { mockRemoteConfigRepository.search }
    }

    // TODO: Update this test when search functionality is enabled
    @Test
    fun `search disabled`() {
        every { mockConfigurationController.search } returns false
        initSUT()
        assertFalse(sut.searchEnabled)
//        verify { mockRemoteConfigRepository.search }
    }

    //endregion
}