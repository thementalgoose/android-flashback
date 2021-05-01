package tmg.configuration.repository

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.firebase.models.RemoteConfigAllSeasons
import tmg.configuration.managers.RemoteConfigManager

internal class ConfigRepositoryTest {

    private val mockRemoteConfigManager: RemoteConfigManager = mockk(relaxed = true)

    private lateinit var sut: ConfigRepository

    private fun initSUT() {
        sut = ConfigRepository(mockRemoteConfigManager)
    }

    //region Supported Seasons

    @Test
    fun `supported seasons gets json from remote config`() {
        val expected = setOf(2020, 2019)
        every { mockRemoteConfigManager.getJson<RemoteConfigAllSeasons, Set<Int>>(keySupportedSeasons, any()) } returns expected

        initSUT()
        assertEquals(sut.supportedSeasons, expected)
    }

    //endregion

    companion object {
        private const val keyDefaultYear: String = "default_year"
        private const val keyUpNext: String = "up_next"
        private const val keyDefaultBanner: String = "banner"
        private const val keyForceUpgrade: String = "force_upgrade"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keyDashboardCalendar: String = "dashboard_calendar"
        private const val keyRss: String = "rss"
        private const val keyRssAddCustom: String = "rss_add_custom"
        private const val keyRssSupportedSources: String = "rss_supported_sources"
    }
}