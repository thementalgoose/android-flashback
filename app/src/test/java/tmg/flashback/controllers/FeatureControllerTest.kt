package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.controllers.FeatureController
import tmg.flashback.data.config.RemoteConfigRepository
import tmg.flashback.testutils.BaseTest

internal class FeatureControllerTest: BaseTest() {

    private var mockRemoteConfigRepository: RemoteConfigRepository = mockk(relaxed = true)

    private lateinit var sut: FeatureController

    private fun initSUT() {
        sut = FeatureController(mockRemoteConfigRepository)
    }

    //region RSS

    @Test
    fun `FeatureManager rss enabled`() {
        every { mockRemoteConfigRepository.rss } returns true
        initSUT()
        assertTrue(sut.rssEnabled)
        verify { mockRemoteConfigRepository.rss }
    }

    @Test
    fun `FeatureManager rss disabled`() {
        every { mockRemoteConfigRepository.rss } returns false
        initSUT()
        assertFalse(sut.rssEnabled)
        verify { mockRemoteConfigRepository.rss }
    }

    //endregion

    //region Search

    // TODO: Update this test when search functionality is enabled
    @Test
    fun `FeatureManager search enabled`() {
        every { mockRemoteConfigRepository.search } returns true
        initSUT()
        assertFalse(sut.searchEnabled)
//        verify { mockRemoteConfigRepository.search }
    }

    // TODO: Update this test when search functionality is enabled
    @Test
    fun `FeatureManager search disabled`() {
        every { mockRemoteConfigRepository.search } returns false
        initSUT()
        assertFalse(sut.searchEnabled)
//        verify { mockRemoteConfigRepository.search }
    }

    //endregion
}