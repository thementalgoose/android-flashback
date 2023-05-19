package tmg.flashback.results.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.results.repository.json.AllSeasonsJson
import tmg.flashback.results.repository.json.BannerItemJson
import tmg.flashback.results.repository.json.BannerJson
import tmg.flashback.results.repository.models.Banner
import java.time.Year

internal class HomeRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockConfigManager: ConfigManager = mockk(relaxed = true)
    private val mockCrashManager: CrashManager = mockk(relaxed = true)

    private lateinit var sut: HomeRepository

    private fun initSUT() {
        sut = HomeRepository(
            preferenceManager = mockPreferenceManager,
            configManager = mockConfigManager,
            crashManager = mockCrashManager
        )
    }

    //region Server default year

    @Test
    fun `server default year is current year if from config returns valid string`() {
        every { mockConfigManager.getString(keyDefaultYear) } returns "2020"
        initSUT()
        assertEquals(2020, sut.serverDefaultYear)
        verify {
            mockConfigManager.getString(keyDefaultYear)
        }
    }

    @Test
    fun `server default year is current year if from config returns non int string`() {
        every { mockConfigManager.getString(keyDefaultYear) } returns "testing"
        initSUT()
        assertEquals(Year.now().value, sut.serverDefaultYear)
        verify {
            mockConfigManager.getString(keyDefaultYear)
        }
    }

    @Test
    fun `server default year is current year if from config returns null`() {
        every { mockConfigManager.getString(keyDefaultYear) } returns "testing"
        initSUT()
        assertEquals(Year.now().value, sut.serverDefaultYear)
        verify {
            mockConfigManager.getString(keyDefaultYear)
        }
    }

    @Test
    fun `server default year is lazy loaded`() {
        every { mockConfigManager.getString(keyDefaultYear) } returns "2020"
        initSUT()
        assertEquals(2020, sut.serverDefaultYear)
        verify(exactly = 1) {
            mockConfigManager.getString(keyDefaultYear)
        }
        every { mockConfigManager.getString(keyDefaultYear) } returns "2021"
        assertEquals(2020, sut.serverDefaultYear)
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
        every { mockPreferenceManager.getBoolean(keyEmptyWeeksInSchedule, true) } returns true
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
            mockPreferenceManager.save(keyEmptyWeeksInSchedule, false)
        }
    }

    //endregion

    //region Favourite Seasons

    @Test
    fun `favourite seasons reads value from preferences repository`() {
        every { mockPreferenceManager.getSet(keyFavouriteSeasons, emptySet()) } returns mutableSetOf("2013")
        initSUT()
        assertEquals(mutableSetOf(2013), sut.favouriteSeasons)
        verify {
            mockPreferenceManager.getSet(keyFavouriteSeasons, emptySet())
        }
    }

    @Test
    fun `favourite seasons saves value in preferences repository`() {
        initSUT()
        sut.favouriteSeasons = setOf(2012)
        verify {
            mockPreferenceManager.save(keyFavouriteSeasons, setOf("2012"))
        }
    }

    //endregion

    //region Default Season

    @Test
    fun `default stream will return null if value is not specified`() {
        every { mockPreferenceManager.getInt(keyDefaultSeason, -1) } returns -1
        initSUT()
        assertNull(sut.defaultSeason)
        verify {
            mockPreferenceManager.getInt(keyDefaultSeason, -1)
        }
    }

    @Test
    fun `default stream will return value if value is specified`() {
        every { mockPreferenceManager.getInt(keyDefaultSeason, -1) } returns 123
        initSUT()
        assertEquals(123, sut.defaultSeason)
        verify {
            mockPreferenceManager.getInt(keyDefaultSeason, -1)
        }
    }

    @Test
    fun `default stream saving null saves -1 to shared preferences`() {
        initSUT()
        sut.defaultSeason = null
        verify {
            mockPreferenceManager.save(keyDefaultSeason, -1)
        }
    }

    @Test
    fun `default stream saving values saves value to shared preferences`() {
        initSUT()
        sut.defaultSeason = 123
        verify {
            mockPreferenceManager.save(keyDefaultSeason, 123)
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
        private const val keyEmptyWeeksInSchedule: String = "empty_weeks_in_schedule"

        // Prefs
        private const val keyDashboardCollapseList: String = "DASHBOARD_COLLAPSE_LIST"
        private const val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"
        private const val keyDefaultSeason: String = "DEFAULT_SEASON"
        private const val keyProvidedByAtTop: String = "PROVIDED_BY_AT_TOP"
    }
}