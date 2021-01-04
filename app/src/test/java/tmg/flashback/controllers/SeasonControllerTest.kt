package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.constants.App.currentYear
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.testutils.BaseTest

internal class SeasonControllerTest: BaseTest() {

    private var mockUserRepository: UserRepository = mockk(relaxed = true)
    private var mockRemoteConfigRepository: RemoteConfigRepository = mockk(relaxed = true)

    private lateinit var sut: SeasonController

    private fun initSUT() {
        sut = SeasonController(mockUserRepository, mockRemoteConfigRepository)
    }

    //region Default year

    @Test
    fun `SeasonController returns current year if supported season list is empty`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns emptySet()
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockUserRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(currentYear, sut.defaultSeason)
    }

    @Test
    fun `SeasonController returns user defined value if its supported`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2017, 2018)
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockUserRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2017, sut.defaultSeason)
    }

    @Test
    fun `SeasonController runs clear default method if user defined value found to be invalid`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2019)
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockUserRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2019, sut.defaultSeason)
        verify {
            mockUserRepository.defaultSeason = null
        }
    }

    @Test
    fun `SeasonController if user defined value invalid, return server value if in supported seasons`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockUserRepository.defaultSeason } returns 1921
        initSUT()

        assertEquals(2018, sut.defaultSeason)
    }

    @Test
    fun `SeasonController if user defined value is null, return server value if in supported seasons`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        every { mockUserRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2018, sut.defaultSeason)
    }

    @Test
    fun `SeasonController if user defined value invalid or null, return max in supported seasons value if server value is not valid`() {
        every { mockRemoteConfigRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockRemoteConfigRepository.defaultSeason } returns 2017
        every { mockUserRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2019, sut.defaultSeason)
    }

    @Test
    fun `SeasonController set user default session updates user repository`() {
        initSUT()
        sut.clearDefault()
        verify {
            mockUserRepository.defaultSeason = null
        }
    }

    @Test
    fun `SeasonController clear default sets default season to null in user repo`() {
        initSUT()
        sut.setUserDefaultSeason(2018)
        verify {
            mockUserRepository.defaultSeason = 2018
        }
    }

    @Test
    fun `SeasonController default season server returns the server configured default season`() {
        every { mockRemoteConfigRepository.defaultSeason } returns 2018
        initSUT()
        assertEquals(2018, sut.serverDefaultSeason)
        verify {
            mockRemoteConfigRepository.defaultSeason
        }
    }

    @Test
    fun `SeasonController is user defined season value set value`() {
        every { mockUserRepository.defaultSeason } returns 2018
        initSUT()
        assertTrue(sut.isUserDefinedValueSet)
        verify {
            mockUserRepository.defaultSeason
        }
    }

    @Test
    fun `SeasonController is user defined season value set null`() {
        every { mockUserRepository.defaultSeason } returns null
        initSUT()
        assertFalse(sut.isUserDefinedValueSet)
        verify {
            mockUserRepository.defaultSeason
        }
    }

    //endregion

    //region All seasons

    @Test
    fun `SeasonController all seasons are pulled from remote config`() {
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
    fun `SeasonController default show favourites section true`() {
        every { mockUserRepository.showListFavourited } returns true
        initSUT()

        assertTrue(sut.favouritesExpanded)
        verify { mockUserRepository.showListFavourited }
    }

    @Test
    fun `SeasonController default show favourites section false`() {
        every { mockUserRepository.showListFavourited } returns false
        initSUT()

        assertFalse(sut.favouritesExpanded)
        verify { mockUserRepository.showListFavourited }
    }

    @Test
    fun `SeasonController default show favourites update interacts with prefs`() {
        initSUT()
        sut.favouritesExpanded = true
        verify { mockUserRepository.showListFavourited = true }
    }

    @Test
    fun `SeasonController default show all section true`() {
        every { mockUserRepository.showListAll } returns true
        initSUT()

        assertTrue(sut.allExpanded)
        verify { mockUserRepository.showListAll }
    }

    @Test
    fun `SeasonController default show all section false`() {
        every { mockUserRepository.showListAll } returns false
        initSUT()

        assertFalse(sut.allExpanded)
        verify { mockUserRepository.showListAll }
    }

    @Test
    fun `SeasonController default show all update interacts with prefs`() {
        initSUT()
        sut.allExpanded = true
        verify { mockUserRepository.showListAll = true }
    }

    //endregion

    //region Favourites

    @Test
    fun `SeasonController favourites returns from prefs`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertEquals(setOf(2018), sut.favouriteSeasons)
        verify { mockUserRepository.favouriteSeasons }
    }

    @Test
    fun `SeasonController is favourited checks prefs`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertFalse(sut.isFavourite(2017))
        assertTrue(sut.isFavourite(2018))
    }

    @Test
    fun `SeasonController toggle season removes or adds properly`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2019)
        initSUT()

        sut.toggleFavourite(2018)
        verify { mockUserRepository.favouriteSeasons = setOf(2019, 2018) }
        sut.toggleFavourite(2019)
        verify { mockUserRepository.favouriteSeasons = emptySet() }
    }

    @Test
    fun `SeasonController add favourite adds to prefs`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2017)
        initSUT()

        sut.addFavourite(2019)
        verify { mockUserRepository.favouriteSeasons = setOf(2017, 2019) }
    }

    @Test
    fun `SeasonController remove favourite removes from prefs`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2017, 2019)
        initSUT()

        sut.removeFavourite(2019)
        verify { mockUserRepository.favouriteSeasons = setOf(2017) }
    }

    //endregion

}