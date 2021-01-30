package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.data.config.RemoteConfigRepository
import tmg.flashback.data.models.remoteconfig.SupportedArticleSource
import tmg.flashback.testutils.BaseTest

internal class RSSSupplierControllerTest: BaseTest() {

    private var mockRemoteConfigRepository: RemoteConfigRepository = mockk(relaxed = true)

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