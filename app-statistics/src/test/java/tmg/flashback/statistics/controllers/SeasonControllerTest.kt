package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.data.repositories.AppRepository
import tmg.flashback.statistics.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.testutils.BaseTest

internal class SeasonControllerTest: BaseTest() {

    private var mockAppRepository: AppRepository = mockk(relaxed = true)
    private var mockRemoteConfigRepository: ConfigurationController = mockk(relaxed = true)

    private lateinit var sut: SeasonController

    private fun initSUT() {
        sut = SeasonController(mockAppRepository, mockRemoteConfigRepository)
    }

    //region Default year

    @Test
    fun `returns current year if supported season list is empty`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns emptySet()
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockAppRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(currentSeasonYear, sut.defaultSeason)
    }

    @Test
    fun `returns user defined value if its supported`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2017, 2018)
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockAppRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2017, sut.defaultSeason)
    }

    @Test
    fun `runs clear default method if user defined value found to be invalid`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2019)
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockAppRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2019, sut.defaultSeason)
        verify {
            mockAppRepository.defaultSeason = null
        }
    }

    @Test
    fun `if user defined value invalid, return server value if in supported seasons`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockAppRepository.defaultSeason } returns 1921
        initSUT()

        assertEquals(2018, sut.defaultSeason)
    }

    @Test
    fun `if user defined value is null, return server value if in supported seasons`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockAppRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2018, sut.defaultSeason)
    }

    @Test
    fun `if user defined value invalid or null, return max in supported seasons value if server value is not valid`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockRemoteConfigRepository.defaultSeason } returns 2017
        every { mockAppRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2019, sut.defaultSeason)
    }

    @Test
    fun `set user default session updates user repository`() {
        initSUT()
        sut.clearDefault()
        verify {
            mockAppRepository.defaultSeason = null
        }
    }

    @Test
    fun `clear default sets default season to null in user repo`() {
        initSUT()
        sut.setUserDefaultSeason(2018)
        verify {
            mockAppRepository.defaultSeason = 2018
        }
    }

    @Test
    fun `default season server returns the server configured default season`() {
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        initSUT()
        assertEquals(2018, sut.serverDefaultSeason)
        verify {
            mockRemoteConfigRepository.defaultSeason
        }
    }

    @Test
    fun `is user defined season value set value`() {
        every { mockAppRepository.defaultSeason } returns 2018
        initSUT()
        assertTrue(sut.isUserDefinedValueSet)
        verify {
            mockAppRepository.defaultSeason
        }
    }

    @Test
    fun `is user defined season value set null`() {
        every { mockAppRepository.defaultSeason } returns null
        initSUT()
        assertFalse(sut.isUserDefinedValueSet)
        verify {
            mockAppRepository.defaultSeason
        }
    }

    //endregion

    //region All seasons

    @Test
    fun `all seasons are pulled from remote config`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2020)
        initSUT()

        assertEquals(setOf(2020), sut.supportedSeasons)
        verify {
            mockRemoteConfigRepository.supportedSeasons
        }
    }

    //endregion

    //region Show sections

    @Test
    fun `default show favourites section true`() {
        every { mockAppRepository.showListFavourited } returns true
        initSUT()

        assertTrue(sut.favouritesExpanded)
        verify { mockAppRepository.showListFavourited }
    }

    @Test
    fun `default show favourites section false`() {
        every { mockAppRepository.showListFavourited } returns false
        initSUT()

        assertFalse(sut.favouritesExpanded)
        verify { mockAppRepository.showListFavourited }
    }

    @Test
    fun `default show favourites update interacts with prefs`() {
        initSUT()
        sut.favouritesExpanded = true
        verify { mockAppRepository.showListFavourited = true }
    }

    @Test
    fun `default show all section true`() {
        every { mockAppRepository.showListAll } returns true
        initSUT()

        assertTrue(sut.allExpanded)
        verify { mockAppRepository.showListAll }
    }

    @Test
    fun `default show all section false`() {
        every { mockAppRepository.showListAll } returns false
        initSUT()

        assertFalse(sut.allExpanded)
        verify { mockAppRepository.showListAll }
    }

    @Test
    fun `default show all update interacts with prefs`() {
        initSUT()
        sut.allExpanded = true
        verify { mockAppRepository.showListAll = true }
    }

    //endregion

    //region Favourites

    @Test
    fun `favourites returns from prefs`() {
        every { mockAppRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertEquals(setOf(2018), sut.favouriteSeasons)
        verify { mockAppRepository.favouriteSeasons }
    }

    @Test
    fun `is favourited checks prefs`() {
        every { mockAppRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertFalse(sut.isFavourite(2017))
        assertTrue(sut.isFavourite(2018))
    }

    @Test
    fun `toggle season removes or adds properly`() {
        every { mockAppRepository.favouriteSeasons } returns setOf(2019)
        initSUT()

        sut.toggleFavourite(2018)
        verify { mockAppRepository.favouriteSeasons = setOf(2019, 2018) }
        sut.toggleFavourite(2019)
        verify { mockAppRepository.favouriteSeasons = emptySet() }
    }

    @Test
    fun `add favourite adds to prefs`() {
        every { mockAppRepository.favouriteSeasons } returns setOf(2017)
        initSUT()

        sut.addFavourite(2019)
        verify { mockAppRepository.favouriteSeasons = setOf(2017, 2019) }
    }

    @Test
    fun `remove favourite removes from prefs`() {
        every { mockAppRepository.favouriteSeasons } returns setOf(2017, 2019)
        initSUT()

        sut.removeFavourite(2019)
        verify { mockAppRepository.favouriteSeasons = setOf(2017) }
    }

    //endregion

}