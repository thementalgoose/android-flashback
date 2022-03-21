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

    //region Dashboard Default to Schedule

    @Test
    fun `dashboard default to schedule reads value from repository`() {
        every { mockHomeRepository.defaultToSchedule } returns true
        initSUT()
        assertTrue(sut.dashboardDefaultToSchedule)
        verify {
            mockHomeRepository.defaultToSchedule
        }
    }

    @Test
    fun `dashboard default to schedule writes value to repository`() {
        initSUT()
        sut.dashboardDefaultToSchedule = true
        verify {
            mockHomeRepository.defaultToSchedule = true
        }
    }

    //endregion

    //region Dashboard calendar

    @Test
    fun `dashboard autoscroll reads value from repository`() {
        every { mockHomeRepository.dashboardAutoscroll } returns true
        initSUT()
        assertTrue(sut.dashboardAutoscroll)
        verify {
            mockHomeRepository.dashboardAutoscroll
        }
    }

    @Test
    fun `dashboard autoscroll writes value to repository`() {
        initSUT()
        sut.dashboardAutoscroll = true
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