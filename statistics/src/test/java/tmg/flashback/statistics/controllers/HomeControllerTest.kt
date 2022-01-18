package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.repository.models.Banner
import tmg.testutils.BaseTest

internal class HomeControllerTest: BaseTest() {

    private var mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var sut: HomeController

    private fun initSUT() {
        sut = HomeController(mockHomeRepository)
    }

    //region Dashboard calendar

    @Test
    fun `dashboard autoscroll reads value from repository`() {
        every { mockHomeRepository.dashboardAutoscroll } returns true
        initSUT()
        assertTrue(mockHomeRepository.dashboardAutoscroll)
        verify {
            mockHomeRepository.dashboardAutoscroll
        }
    }

    @Test
    fun `dashboard autoscroll writes value to repository`() {
        initSUT()
        mockHomeRepository.dashboardAutoscroll = true
        verify {
            mockHomeRepository.dashboardAutoscroll = true
        }
    }

    //endregion

    //region Banner

    @Test
    fun `banner reads value from repository`() {
        every { mockHomeRepository.banner } returns Banner("hello", "world")
        initSUT()
        assertEquals(Banner("hello", "world"), sut.banner)
        verify {
            mockHomeRepository.banner
        }
    }

    //endregion

    //region Data provided by

    @Test
    fun `data provided by reads value from repository`() {
        every { mockHomeRepository.dataProvidedBy } returns "data provided by"
        initSUT()
        assertEquals("data provided by", sut.dataProvidedBy)
        verify {
            mockHomeRepository.dataProvidedBy
        }
    }

    //endregion

    //region Data Provided by at top

    @Test
    fun `data provided by at top reads from repository`() {
        every { mockHomeRepository.dataProvidedByAtTop } returns true
        initSUT()
        assertTrue(sut.dataProvidedByAtTop)
        verify {
            mockHomeRepository.dataProvidedByAtTop
        }
    }

    @Test
    fun `data provided by at top saves value to repository`() {
        initSUT()
        sut.dataProvidedByAtTop = true
        verify {
            mockHomeRepository.dataProvidedByAtTop = true
        }
    }

    //endregion

    //region Default year

    @Test
    fun `returns current year if supported season list is empty`() {
        every { mockHomeRepository.supportedSeasons } returns emptySet()
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(currentSeasonYear, sut.defaultSeason)
    }

    @Test
    fun `returns user defined value if its supported`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2017, 2018)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2017, sut.defaultSeason)
    }

    @Test
    fun `runs clear default method if user defined value found to be invalid`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2019, sut.defaultSeason)
        verify {
            mockHomeRepository.defaultSeason = null
        }
    }

    @Test
    fun `if user defined value invalid, return server value if in supported seasons`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns 1921
        initSUT()

        assertEquals(2018, sut.defaultSeason)
    }

    @Test
    fun `if user defined value is null, return server value if in supported seasons`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2018, sut.defaultSeason)
    }

    @Test
    fun `if user defined value invalid or null, return max in supported seasons value if server value is not valid`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2017
        every { mockHomeRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2019, sut.defaultSeason)
    }

    @Test
    fun `set user default session updates user repository`() {
        initSUT()
        sut.clearDefault()
        verify {
            mockHomeRepository.defaultSeason = null
        }
    }

    @Test
    fun `clear default sets default season to null in user repo`() {
        initSUT()
        sut.setUserDefaultSeason(2018)
        verify {
            mockHomeRepository.defaultSeason = 2018
        }
    }

    @Test
    fun `default season server returns the server configured default season`() {
        every { mockHomeRepository.serverDefaultYear } returns 2018
        initSUT()
        assertEquals(2018, sut.serverDefaultSeason)
        verify {
            mockHomeRepository.serverDefaultYear
        }
    }

    @Test
    fun `is user defined season value set value`() {
        every { mockHomeRepository.defaultSeason } returns 2018
        initSUT()
        assertTrue(sut.isUserDefinedValueSet)
        verify {
            mockHomeRepository.defaultSeason
        }
    }

    @Test
    fun `is user defined season value set null`() {
        every { mockHomeRepository.defaultSeason } returns null
        initSUT()
        assertFalse(sut.isUserDefinedValueSet)
        verify {
            mockHomeRepository.defaultSeason
        }
    }

    //endregion

    //region All seasons

    @Test
    fun `all seasons are pulled from remote config`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2020)
        initSUT()

        assertEquals(setOf(2020), sut.supportedSeasons)
        verify {
            mockHomeRepository.supportedSeasons
        }
    }

    //endregion

    //region Show sections

    @Test
    fun `default show favourites section true`() {
        every { mockHomeRepository.showListFavourited } returns true
        initSUT()

        assertTrue(sut.favouritesExpanded)
        verify { mockHomeRepository.showListFavourited }
    }

    @Test
    fun `default show favourites section false`() {
        every { mockHomeRepository.showListFavourited } returns false
        initSUT()

        assertFalse(sut.favouritesExpanded)
        verify { mockHomeRepository.showListFavourited }
    }

    @Test
    fun `default show favourites update interacts with prefs`() {
        initSUT()
        sut.favouritesExpanded = true
        verify { mockHomeRepository.showListFavourited = true }
    }

    @Test
    fun `default show all section true`() {
        every { mockHomeRepository.showListAll } returns true
        initSUT()

        assertTrue(sut.allExpanded)
        verify { mockHomeRepository.showListAll }
    }

    @Test
    fun `default show all section false`() {
        every { mockHomeRepository.showListAll } returns false
        initSUT()

        assertFalse(sut.allExpanded)
        verify { mockHomeRepository.showListAll }
    }

    @Test
    fun `default show all update interacts with prefs`() {
        initSUT()
        sut.allExpanded = true
        verify { mockHomeRepository.showListAll = true }
    }

    //endregion

    //region Favourites

    @Test
    fun `favourites returns from prefs`() {
        every { mockHomeRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertEquals(setOf(2018), sut.favouriteSeasons)
        verify { mockHomeRepository.favouriteSeasons }
    }

    @Test
    fun `is favourited checks prefs`() {
        every { mockHomeRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertFalse(sut.isFavourite(2017))
        assertTrue(sut.isFavourite(2018))
    }

    @Test
    fun `toggle season removes or adds properly`() {
        every { mockHomeRepository.favouriteSeasons } returns setOf(2019)
        initSUT()

        sut.toggleFavourite(2018)
        verify { mockHomeRepository.favouriteSeasons = setOf(2019, 2018) }
        sut.toggleFavourite(2019)
        verify { mockHomeRepository.favouriteSeasons = emptySet() }
    }

    @Test
    fun `add favourite adds to prefs`() {
        every { mockHomeRepository.favouriteSeasons } returns setOf(2017)
        initSUT()

        sut.addFavourite(2019)
        verify { mockHomeRepository.favouriteSeasons = setOf(2017, 2019) }
    }

    @Test
    fun `remove favourite removes from prefs`() {
        every { mockHomeRepository.favouriteSeasons } returns setOf(2017, 2019)
        initSUT()

        sut.removeFavourite(2019)
        verify { mockHomeRepository.favouriteSeasons = setOf(2017) }
    }

    //endregion

}