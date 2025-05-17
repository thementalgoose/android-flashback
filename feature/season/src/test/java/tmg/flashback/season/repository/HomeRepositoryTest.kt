package tmg.flashback.season.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.season.repository.json.AllSeasonsJson
import tmg.flashback.season.repository.json.BannerItemJson
import tmg.flashback.season.repository.json.BannerJson
import tmg.flashback.season.repository.models.Banner
import java.time.Year

internal class HomeRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockConfigManager: ConfigManager = mockk(relaxed = true)
    private val mockCrashlyticsManager: CrashlyticsManager = mockk(relaxed = true)

    private lateinit var sut: HomeRepository

    private fun initSUT() {
        sut = HomeRepository(
            preferenceManager = mockPreferenceManager,
            configManager = mockConfigManager,
            crashlyticsManager = mockCrashlyticsManager
        )
    }

    //region Server default year

    @Test
    fun `server default year is current year if from config returns valid string`() {
        every { mockConfigManager.getString(keyDefaultYear) } returns "2020"
        initSUT()
        assertEquals(2020, sut.defaultSeason)
        verify {
            mockConfigManager.getString(keyDefaultYear)
        }
    }

    @Test
    fun `server default year is current year if from config returns non int string`() {
        every { mockConfigManager.getString(keyDefaultYear) } returns "testing"
        initSUT()
        assertEquals(Year.now().value, sut.defaultSeason)
        verify {
            mockConfigManager.getString(keyDefaultYear)
        }
    }

    @Test
    fun `server default year is current year if from config returns null`() {
        every { mockConfigManager.getString(keyDefaultYear) } returns "testing"
        initSUT()
        assertEquals(Year.now().value, sut.defaultSeason)
        verify {
            mockConfigManager.getString(keyDefaultYear)
        }
    }

    @Test
    fun `server default year is lazy loaded`() {
        every { mockConfigManager.getString(keyDefaultYear) } returns "2020"
        initSUT()
        assertEquals(2020, sut.defaultSeason)
        verify(exactly = 1) {
            mockConfigManager.getString(keyDefaultYear)
        }
        every { mockConfigManager.getString(keyDefaultYear) } returns "2021"
        assertEquals(2020, sut.defaultSeason)
        verify(exactly = 1) {
            mockConfigManager.getString(keyDefaultYear)
        }
    }

    //endregion

    //region Banner

    @Test
    fun `banner is returned from config repository`() {
        val inputBanner = BannerItemJson("hey", "sup", false, null)
        val expected = Banner("hey", null, false, null)
        every { mockConfigManager.getJson(keyDefaultBanners, BannerJson.serializer()) } returns BannerJson(listOf(inputBanner))
        initSUT()
        assertEquals(listOf(expected), sut.banners)
        verify {
            mockConfigManager.getJson(keyDefaultBanners, BannerJson.serializer())
        }
    }

    @Test
    fun `banner returned as null results null value`() {
        every { mockConfigManager.getJson(keyDefaultBanners, BannerJson.serializer()) } returns null
        initSUT()
        assertEquals(emptyList<Banner>(), sut.banners)
        verify {
            mockConfigManager.getJson(keyDefaultBanners, BannerJson.serializer())
        }
    }

    //endregion

    //region Data Provided by

    @Test
    fun `data provided by value is returned from config repository`() {
        every { mockConfigManager.getString(keyDataProvidedBy) } returns "value"
        initSUT()
        assertEquals("value", sut.dataProvidedBy)
        verify {
            mockConfigManager.getString(keyDataProvidedBy)
        }
    }

    //endregion

    //region Server search enabled

    @Test
    fun `server search is returned from config repository`() {
        every { mockConfigManager.getBoolean(keySearch) } returns true
        initSUT()
        assertTrue(sut.searchEnabled)
        verify {
            mockConfigManager.getBoolean(keySearch)
        }
    }

    //endregion

    //region Supported Seasons

    @Test
    fun `supported seasons is returned from config repository`() {
        every { mockConfigManager.getJson(keySupportedSeasons, AllSeasonsJson.serializer()) } returns AllSeasonsJson(seasons = emptyList())
        initSUT()
        assertEquals(emptySet<Int>(), sut.supportedSeasons)
        verify {
            mockConfigManager.getJson(keySupportedSeasons, AllSeasonsJson.serializer())
        }
    }

    @Test
    fun `supported seasons returned as null results in empty set`() {
        every { mockConfigManager.getJson(keySupportedSeasons, AllSeasonsJson.serializer()) } returns null
        initSUT()
        assertEquals(emptySet<Int>(), sut.supportedSeasons)
        verify {
            mockConfigManager.getJson(keySupportedSeasons, AllSeasonsJson.serializer())
        }
    }

    //endregion





    //region Dashboard Collapse List

    @Test
    fun `dashboard collapse list reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyDashboardCollapseList, true) } returns true
        initSUT()

        assertTrue(sut.collapseList)
        verify {
            mockPreferenceManager.getBoolean(keyDashboardCollapseList, true)
        }
    }

    @Test
    fun `dashboard collapse list saves value to shared prefs repository`() {
        initSUT()

        sut.collapseList = true
        verify {
            mockPreferenceManager.save(keyDashboardCollapseList, true)
        }
    }


    @Test
    fun `empty weeks in schedule reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyEmptyWeeksInSchedule, false) } returns true
        initSUT()

        assertTrue(sut.emptyWeeksInSchedule)
        verify {
            mockPreferenceManager.getBoolean(keyEmptyWeeksInSchedule, false)
        }
    }

    @Test
    fun `empty weeks in schedule saves value to shared prefs repository`() {
        initSUT()

        sut.emptyWeeksInSchedule = true
        verify {
            mockPreferenceManager.save(keyEmptyWeeksInSchedule, true)
        }
    }

    @Test
    fun `recent highlights in schedule reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyRecentHighlights, true) } returns true
        initSUT()

        assertTrue(sut.recentHighlights)
        verify {
            mockPreferenceManager.getBoolean(keyRecentHighlights, true)
        }
    }

    @Test
    fun `recent highlights in schedule saves value to shared prefs repository`() {
        initSUT()

        sut.recentHighlights = true
        verify {
            mockPreferenceManager.save(keyRecentHighlights, true)
        }
    }

    @Test
    fun `remember season change reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyRememberSeasonChange, false) } returns true
        initSUT()

        assertTrue(sut.keepUserSelectedSeason)
        verify {
            mockPreferenceManager.getBoolean(keyRememberSeasonChange, false)
        }
    }

    @Test
    fun `remember season change saves value to shared prefs repository`() {
        initSUT()

        sut.keepUserSelectedSeason = true
        verify {
            mockPreferenceManager.save(keyRememberSeasonChange, true)
        }
    }


    @Test
    fun `user season change reads value from preferences repository null when not set`() {
        every { mockPreferenceManager.getInt(keyUserSeasonChange, -1) } returns -1
        initSUT()

        assertEquals(null, sut.userSelectedSeason)
        verify {
            mockPreferenceManager.getInt(keyUserSeasonChange, -1)
        }
    }
    @Test
    fun `user season change reads value from preferences repository value when valid`() {
        every { mockPreferenceManager.getInt(keyUserSeasonChange, -1) } returns 2023
        initSUT()

        assertEquals(2023, sut.userSelectedSeason)
        verify {
            mockPreferenceManager.getInt(keyUserSeasonChange, -1)
        }
    }

    @Test
    fun `user season change setting value to null saves -1`() {
        initSUT()

        sut.userSelectedSeason = null
        verify {
            mockPreferenceManager.save(keyUserSeasonChange, -1)
        }
    }

    @Test
    fun `user season change saves value to shared prefs repository`() {
        initSUT()

        sut.userSelectedSeason = 2024
        verify {
            mockPreferenceManager.save(keyUserSeasonChange, 2024)
        }
    }

    @Test
    fun `seasons seen reads value from preferences repository`() {
        every { mockPreferenceManager.getSet(keySeenSeasons, any()) } returns mutableSetOf("1234")
        initSUT()

        assertEquals(setOf(1234), sut.viewedSeasons)
        verify {
            mockPreferenceManager.getSet(keySeenSeasons, emptySet())
        }
    }

    @Test
    fun `seasons seen saves value to shared prefs repository`() {
        initSUT()

        sut.viewedSeasons = setOf(1234)
        verify {
            mockPreferenceManager.save(keySeenSeasons, setOf("1234"))
        }
    }

    //endregion

    //region Season Onboarding prompt

    @Test
    fun `season onboarding prompt reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keySeasonOnboarding, false) } returns true
        initSUT()
        assertEquals(true, sut.hasSeenSeasonOnboarding)
        verify {
            mockPreferenceManager.getBoolean(keySeasonOnboarding, false)
        }
    }

    @Test
    fun `season onboarding prompt saves value in preferences repository`() {
        initSUT()
        sut.setHasSeenSeasonOnboarding()
        verify {
            mockPreferenceManager.save(keySeasonOnboarding, true)
        }
    }

    //endregion

    //region Data Provided by label at top

    @Test
    fun `provided by at top reads value from preferences repository`() {

        every { mockPreferenceManager.getBoolean(keyProvidedByAtTop, true) } returns true
        initSUT()

        assertTrue(sut.dataProvidedByAtTop)
        verify {
            mockPreferenceManager.getBoolean(keyProvidedByAtTop, true)
        }
    }

    @Test
    fun `provided by at top saves value to shared prefs repository`() {
        initSUT()

        sut.dataProvidedByAtTop = true
        verify {
            mockPreferenceManager.save(keyProvidedByAtTop, true)
        }
    }

    //endregion

    companion object {

        // Config
        private const val keyDefaultYear: String = "default_year"
        private const val keyDefaultBanners: String = "banners"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keySearch: String = "search"

        // Prefs
        private const val keyEmptyWeeksInSchedule: String = "empty_weeks_in_schedule"
        private const val keyRecentHighlights: String = "RECENT_HIGHLIGHTS"
        private const val keySeenSeasons: String = "SEASONS_VIEWED"
        private const val keyDashboardCollapseList: String = "DASHBOARD_COLLAPSE_LIST"
        private const val keyProvidedByAtTop: String = "PROVIDED_BY_AT_TOP"
        private const val keySeasonOnboarding: String = "ONBOARDING_SEASON"
        private const val keyRememberSeasonChange: String = "REMEMBER_SEASON_CHANGE"
        private const val keyUserSeasonChange: String = "USER_SEASON_CHANGE"
    }
}