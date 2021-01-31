package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.core.model.SupportedArticleSource
import tmg.flashback.testutils.BaseTest

// TODO: Look at removing this as it shouldn't be needed anymore
internal class RSSSupplierControllerTest: BaseTest() {

    private var mockRemoteConfigRepository: ConfigurationController = mockk(relaxed = true)

    private lateinit var sut: RSSConfigurationFeedController

    private fun initSUT() {
        sut = RSSConfigurationFeedController(mockRemoteConfigRepository)
    }

    @Test
    fun `RSSController provider for RSS functionality gets rss values from remote config repository`() {

        val expected = tmg.flashback.rss.repo.enums.SupportedArticleSource("", "", "", "", "", "", "")
        every { mockRemoteConfigRepository.rssSupportedSources } returns listOf(
            SupportedArticleSource("", "", "", "", "", "", "")
        )

        initSUT()

        val sources = sut.sources
        assertEquals(listOf(expected).size, sources.size)
        assertEquals(expected, sources.first())

        verify {
            mockRemoteConfigRepository.rssSupportedSources
        }
    }

}