package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.controllers.ConfigController
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.repository.StatisticsRepository
import tmg.testutils.BaseTest

internal class SeasonControllerTest: BaseTest() {

    private var mockStatisticsRepository: StatisticsRepository = mockk(relaxed = true)

    private lateinit var sut: SeasonController

    private fun initSUT() {
        sut = SeasonController(mockStatisticsRepository)
    }

    //region Dashboard calendar

    @Test
    fun `dashboard calendar reads value from repository`() {
        every { mockStatisticsRepository.dashboardCalendar } returns true
        initSUT()
        assertTrue(sut.dashboardCalendar)
        verify {
            mockStatisticsRepository.dashboardCalendar
        }
    }

    //endregion

    //region Banner

    @Test
    fun `banner reads value from repository`() {
        every { mockStatisticsRepository.banner } returns "banner"
        initSUT()
        assertEquals("banner", sut.banner)
        verify {
            mockStatisticsRepository.banner
        }
    }

    //endregion

    //region Data provided by

    @Test
    fun `data provided by reads value from repository`() {
        every { mockStatisticsRepository.dataProvidedBy } returns "data provided by"
        initSUT()
        assertEquals("data provided by", sut.dataProvidedBy)
        verify {
            mockStatisticsRepository.dataProvidedBy
        }
    }

    //endregion

    //region Data Provided by at top

    @Test
    fun `data provided by at top reads from repository`() {
        every { mockStatisticsRepository.dataProvidedByAtTop } returns true
        initSUT()
        assertTrue(sut.dataProvidedByAtTop)
        verify {
            mockStatisticsRepository.dataProvidedByAtTop
        }
    }

    @Test
    fun `data provided by at top saves value to repository`() {
        initSUT()
        sut.dataProvidedByAtTop = true
        verify {
            mockStatisticsRepository.dataProvidedByAtTop = true
        }
    }

    //endregion

    //region Default year

    @Test
    fun `returns current year if supported season list is empty`() {
        every { mockStatisticsRepository.supportedSeasons } returns emptySet()
        every { mockStatisticsRepository.serverDefaultYear } returns 2018
        every { mockStatisticsRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(currentSeasonYear, sut.defaultSeason)
    }

    @Test
    fun `returns user defined value if its supported`() {
        every { mockStatisticsRepository.supportedSeasons } returns setOf(2017, 2018)
        every { mockStatisticsRepository.serverDefaultYear } returns 2018
        every { mockStatisticsRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2017, sut.defaultSeason)
    }

    @Test
    fun `runs clear default method if user defined value found to be invalid`() {
        every { mockStatisticsRepository.supportedSeasons } returns setOf(2019)
        every { mockStatisticsRepository.serverDefaultYear } returns 2018
        every { mockStatisticsRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2019, sut.defaultSeason)
        verify {
            mockStatisticsRepository.defaultSeason = null
        }
    }

    @Test
    fun `if user defined value invalid, return server value if in supported seasons`() {
        every { mockStatisticsRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockStatisticsRepository.serverDefaultYear } returns 2018
        every { mockStatisticsRepository.defaultSeason } returns 1921
        initSUT()

        assertEquals(2018, sut.defaultSeason)
    }

    @Test
    fun `if user defined value is null, return server value if in supported seasons`() {
        every { mockStatisticsRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockStatisticsRepository.serverDefaultYear } returns 2018
        every { mockStatisticsRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2018, sut.defaultSeason)
    }

    @Test
    fun `if user defined value invalid or null, return max in supported seasons value if server value is not valid`() {
        every { mockStatisticsRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockStatisticsRepository.serverDefaultYear } returns 2017
        every { mockStatisticsRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2019, sut.defaultSeason)
    }

    @Test
    fun `set user default session updates user repository`() {
        initSUT()
        sut.clearDefault()
        verify {
            mockStatisticsRepository.defaultSeason = null
        }
    }

    @Test
    fun `clear default sets default season to null in user repo`() {
        initSUT()
        sut.setUserDefaultSeason(2018)
        verify {
            mockStatisticsRepository.defaultSeason = 2018
        }
    }

    @Test
    fun `default season server returns the server configured default season`() {
        every { mockStatisticsRepository.serverDefaultYear } returns 2018
        initSUT()
        assertEquals(2018, sut.serverDefaultSeason)
        verify {
            mockStatisticsRepository.serverDefaultYear
        }
    }

    @Test
    fun `is user defined season value set value`() {
        every { mockStatisticsRepository.defaultSeason } returns 2018
        initSUT()
        assertTrue(sut.isUserDefinedValueSet)
        verify {
            mockStatisticsRepository.defaultSeason
        }
    }

    @Test
    fun `is user defined season value set null`() {
        every { mockStatisticsRepository.defaultSeason } returns null
        initSUT()
        assertFalse(sut.isUserDefinedValueSet)
        verify {
            mockStatisticsRepository.defaultSeason
        }
    }

    //endregion

    //region All seasons

    @Test
    fun `all seasons are pulled from remote config`() {
        every { mockStatisticsRepository.supportedSeasons } returns setOf(2020)
        initSUT()

        assertEquals(setOf(2020), sut.supportedSeasons)
        verify {
            mockStatisticsRepository.supportedSeasons
        }
    }

    //endregion

    //region Show sections

    @Test
    fun `default show favourites section true`() {
        every { mockStatisticsRepository.showListFavourited } returns true
        initSUT()

        assertTrue(sut.favouritesExpanded)
        verify { mockStatisticsRepository.showListFavourited }
    }

    @Test
    fun `default show favourites section false`() {
        every { mockStatisticsRepository.showListFavourited } returns false
        initSUT()

        assertFalse(sut.favouritesExpanded)
        verify { mockStatisticsRepository.showListFavourited }
    }

    @Test
    fun `default show favourites update interacts with prefs`() {
        initSUT()
        sut.favouritesExpanded = true
        verify { mockStatisticsRepository.showListFavourited = true }
    }

    @Test
    fun `default show all section true`() {
        every { mockStatisticsRepository.showListAll } returns true
        initSUT()

        assertTrue(sut.allExpanded)
        verify { mockStatisticsRepository.showListAll }
    }

    @Test
    fun `default show all section false`() {
        every { mockStatisticsRepository.showListAll } returns false
        initSUT()

        assertFalse(sut.allExpanded)
        verify { mockStatisticsRepository.showListAll }
    }

    @Test
    fun `default show all update interacts with prefs`() {
        initSUT()
        sut.allExpanded = true
        verify { mockStatisticsRepository.showListAll = true }
    }

    //endregion

    //region Favourites

    @Test
    fun `favourites returns from prefs`() {
        every { mockStatisticsRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertEquals(setOf(2018), sut.favouriteSeasons)
        verify { mockStatisticsRepository.favouriteSeasons }
    }

    @Test
    fun `is favourited checks prefs`() {
        every { mockStatisticsRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertFalse(sut.isFavourite(2017))
        assertTrue(sut.isFavourite(2018))
    }

    @Test
    fun `toggle season removes or adds properly`() {
        every { mockStatisticsRepository.favouriteSeasons } returns setOf(2019)
        initSUT()

        sut.toggleFavourite(2018)
        verify { mockStatisticsRepository.favouriteSeasons = setOf(2019, 2018) }
        sut.toggleFavourite(2019)
        verify { mockStatisticsRepository.favouriteSeasons = emptySet() }
    }

    @Test
    fun `add favourite adds to prefs`() {
        every { mockStatisticsRepository.favouriteSeasons } returns setOf(2017)
        initSUT()

        sut.addFavourite(2019)
        verify { mockStatisticsRepository.favouriteSeasons = setOf(2017, 2019) }
    }

    @Test
    fun `remove favourite removes from prefs`() {
        every { mockStatisticsRepository.favouriteSeasons } returns setOf(2017, 2019)
        initSUT()

        sut.removeFavourite(2019)
        verify { mockStatisticsRepository.favouriteSeasons = setOf(2017) }
    }

    //endregion

}