package tmg.flashback.statistics.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.core.prefs.manager.PreferenceManager

internal class StatisticsRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: StatisticsRepository

    private fun initSUT() {
        sut = StatisticsRepository(mockPreferenceManager)
    }

    //region Show Qualifying Delta

    @Test
    fun `show qualifying delta reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyShowQualifyingDelta, false) } returns true
        initSUT()

        assertTrue(sut.showQualifyingDelta)
        verify {
            mockPreferenceManager.getBoolean(keyShowQualifyingDelta, false)
        }
    }

    @Test
    fun `show qualifying delta saves value to shared prefs repository`() {
        initSUT()

        sut.showQualifyingDelta = true
        verify {
            mockPreferenceManager.save(keyShowQualifyingDelta, true)
        }
    }

    //endregion

    //region Fade DNF

    @Test
    fun `fade dnf reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyFadeDNF, true) } returns true
        initSUT()

        assertTrue(sut.fadeDNF)
        verify {
            mockPreferenceManager.getBoolean(keyFadeDNF, true)
        }
    }

    @Test
    fun `fade dnf saves value to shared prefs repository`() {
        initSUT()

        sut.fadeDNF = true
        verify {
            mockPreferenceManager.save(keyFadeDNF, true)
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

    //region Show Grid Penalties in Qualifying

    @Test
    fun `show grid penalties in qualifying reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyShowGridPenaltiesInQualifying, true) } returns true
        initSUT()

        assertTrue(sut.showGridPenaltiesInQualifying)
        verify {
            mockPreferenceManager.getBoolean(keyShowGridPenaltiesInQualifying, true)
        }
    }

    @Test
    fun `show grid penalties in qualifying saves value to shared prefs repository`() {
        initSUT()

        sut.showGridPenaltiesInQualifying = true
        verify {
            mockPreferenceManager.save(keyShowGridPenaltiesInQualifying, true)
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
            mockPreferenceManager.save(keyFavouriteSeasons, emptySet())
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

    //region Widget Open App

    @Test
    fun `widget open app reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyWidgetOpenApp, true) } returns true
        initSUT()

        assertTrue(sut.widgetOpenApp)
        verify {
            mockPreferenceManager.getBoolean(keyWidgetOpenApp, true)
        }
    }

    @Test
    fun `widget open app saves value to shared prefs repository`() {
        initSUT()

        sut.widgetOpenApp = true
        verify {
            mockPreferenceManager.save(keyWidgetOpenApp, true)
        }
    }
    //endregion

    companion object {
        private const val keyShowQualifyingDelta: String = "SHOW_QUALIFYING_DELTA"
        private const val keyFadeDNF: String = "FADE_DNF"
        private const val keyShowListFavourited: String = "BOTTOM_SHEET_FAVOURITED"
        private const val keyShowListAll: String = "BOTTOM_SHEET_ALL"
        private const val keyShowGridPenaltiesInQualifying: String = "SHOW_GRID_PENALTIES_IN_QUALIFYING"
        private const val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"
        private const val keyDefaultSeason: String = "DEFAULT_SEASON"
        private const val keyWidgetOpenApp: String = "WIDGET_OPEN_BEHAVIOR"
    }
}