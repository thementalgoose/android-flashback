package tmg.flashback.stats.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.stats.repository.json.AllSeasonsJson
import tmg.flashback.stats.repository.json.BannerJson
import tmg.flashback.stats.repository.models.Banner
import java.time.Year

internal class HomeRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockConfigManager: ConfigManager = mockk(relaxed = true)

    private lateinit var sut: HomeRepository

    private fun initSUT() {
        sut = HomeRepository(mockPreferenceManager, mockConfigManager)
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
        every { mockConfigManager.getJson(keyDefaultBanner, BannerJson.serializer()) } returns BannerJson("hey", "sup")
        initSUT()
        assertEquals(Banner("hey", "sup"), sut.banner)
        verify {
            mockConfigManager.getJson(keyDefaultBanner, BannerJson.serializer())
        }
    }

    @Test
    fun `banner returned as null results null value`() {
        every { mockConfigManager.getJson(keyDefaultBanner, BannerJson.serializer()) } returns null
        initSUT()
        assertNull(sut.banner)
        verify {
            mockConfigManager.getJson(keyDefaultBanner, BannerJson.serializer())
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






    //region Default to schedule

    @Test
    fun `default to schedule reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyDefaultToSchedule, true) } returns true
        initSUT()

        assertTrue(sut.defaultToSchedule)
        verify {
            mockPreferenceManager.getBoolean(keyDefaultToSchedule, true)
        }
    }

    @Test
    fun `default to schedule saves value to shared prefs repository`() {
        initSUT()

        sut.defaultToSchedule = true
        verify {
            mockPreferenceManager.save(keyDefaultToSchedule, true)
        }
    }

    //endregion

    //region Show list favourited

    @Test
    fun `show list favourited reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyShowListFavourited, true) } returns true
        initSUT()

        assertTrue(sut.showListFavourited)
        verify {
            mockPreferenceManager.getBoolean(keyShowListFavourited, true)
        }
    }

    @Test
    fun `show list favourited saves value to shared prefs repository`() {
        initSUT()

        sut.showListFavourited = true
        verify {
            mockPreferenceManager.save(keyShowListFavourited, true)
        }
    }

    //endregion

    //region Show list all

    @Test
    fun `show list all reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyShowListAll, true) } returns true
        initSUT()

        assertTrue(sut.showListAll)
        verify {
            mockPreferenceManager.getBoolean(keyShowListAll, true)
        }
    }

    @Test
    fun `show list all saves value to shared prefs repository`() {
        initSUT()

        sut.showListAll = true
        verify {
            mockPreferenceManager.save(keyShowListAll, true)
        }
    }

    //endregion

    //region Dashboard Autoscroll

    @Test
    fun `dashboard autoscroll reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyDashboardAutoscroll, true) } returns true
        initSUT()

        assertTrue(sut.dashboardAutoscroll)
        verify {
            mockPreferenceManager.getBoolean(keyDashboardAutoscroll, true)
        }
    }

    @Test
    fun `dashboard autoscroll saves value to shared prefs repository`() {
        initSUT()

        sut.dashboardAutoscroll = true
        verify {
            mockPreferenceManager.save(keyDashboardAutoscroll, true)
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
        private const val keyDefaultBanner: String = "banner"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keySearch: String = "search"

        // Prefs
        private const val keyDefaultToSchedule: String = "DASHBOARD_DEFAULT_TAB_SCHEDULE"
        private const val keyShowListFavourited: String = "BOTTOM_SHEET_FAVOURITED"
        private const val keyShowListAll: String = "BOTTOM_SHEET_ALL"
        private const val keyDashboardAutoscroll: String = "DASHBOARD_AUTOSCROLL"
        private const val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"
        private const val keyDefaultSeason: String = "DEFAULT_SEASON"
        private const val keyProvidedByAtTop: String = "PROVIDED_BY_AT_TOP"
    }
}