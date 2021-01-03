package tmg.flashback.managers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.currentYear
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.testutils.BaseTest

internal class SeasonPrefManagerTest: BaseTest() {

    private var mockUserRepository: UserRepository = mockk(relaxed = true)
    private var mockRemoteConfigRepository: RemoteConfigRepository = mockk(relaxed = true)

    private lateinit var sut: SeasonPrefManager

    private fun initSUT() {
        sut = SeasonPrefManager(mockUserRepository, mockRemoteConfigRepository)
    }

    //region Default year

    @Test
    fun `SeasonPrefManager default year reads from remote config by default`() {
        every { mockRemoteConfigRepository.defaultYear } returns 2019
        every { mockUserRepository.defaultSeason } returns null
        initSUT()

        assertEquals(2019, sut.defaultYear)

        verify {
            mockRemoteConfigRepository.defaultYear
            mockUserRepository.defaultSeason
        }
    }

    @Test
    fun `SeasonPrefManager default season is returned from prefs if contains a valid value`() {
        every { mockRemoteConfigRepository.defaultYear } returns 2019
        every { mockUserRepository.defaultSeason } returns 2017
        initSUT()

        assertEquals(2017, sut.defaultYear)

        verify {
            mockUserRepository.defaultSeason
        }
    }

    @Test
    fun `SeasonPrefManager default season is returned from remote config if contains an invalid value`() {
        every { mockRemoteConfigRepository.defaultYear } returns 2019
        every { mockUserRepository.defaultSeason } returns 1921
        initSUT()

        assertEquals(2019, sut.defaultYear)

        verify {
            mockRemoteConfigRepository.defaultYear
            mockUserRepository.defaultSeason
        }
    }

    @Test
    fun `SeasonPrefManager current year if both remote config and prefs are invalid`() {
        every { mockRemoteConfigRepository.defaultYear } returns 1921
        every { mockUserRepository.defaultSeason } returns 1921
        initSUT()

        assertEquals(currentYear, sut.defaultYear)

        verify {
            mockRemoteConfigRepository.defaultYear
            mockUserRepository.defaultSeason
        }
    }

    @Test
    fun `SeasonPrefManager clear default sets default season in prefs to null`() {
        every { mockUserRepository.defaultSeason } returns 2018
        initSUT()
        sut.clearDefault()
        verify {
            mockUserRepository.defaultSeason = null
        }
    }

    @Test
    fun `SeasonPrefManager set default season updates prefs`() {
        every { mockUserRepository.defaultSeason } returns 2018
        initSUT()
        sut.setDefaultSeason(2019)
        verify {
            mockUserRepository.defaultSeason = 2019
        }
    }

    //endregion

    //region Show sections

    @Test
    fun `SeasonPrefManager default show favourites section true`() {
        every { mockUserRepository.showListFavourited } returns true
        initSUT()

        assertTrue(sut.defaultFavouritesExpanded)
        verify { mockUserRepository.showListFavourited }
    }

    @Test
    fun `SeasonPrefManager default show favourites section false`() {
        every { mockUserRepository.showListFavourited } returns false
        initSUT()

        assertFalse(sut.defaultFavouritesExpanded)
        verify { mockUserRepository.showListFavourited }
    }

    @Test
    fun `SeasonPrefManager default show all section true`() {
        every { mockUserRepository.showListAll } returns true
        initSUT()

        assertTrue(sut.defaultAllExpanded)
        verify { mockUserRepository.showListAll }
    }

    @Test
    fun `SeasonPrefManager default show all section false`() {
        every { mockUserRepository.showListAll } returns false
        initSUT()

        assertFalse(sut.defaultAllExpanded)
        verify { mockUserRepository.showListAll }
    }

    //endregion

    //region Favourites

    @Test
    fun `SeasonPrefManager favourites returns from prefs`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertEquals(setOf(2018), sut.favouriteSeasons)
        verify { mockUserRepository.favouriteSeasons }
    }

    @Test
    fun `SeasonPrefManager is favourited checks prefs`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2018)
        initSUT()

        assertFalse(sut.isFavourite(2017))
        assertTrue(sut.isFavourite(2018))
    }

    @Test
    fun `SeasonPrefManager add favourite adds to prefs`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2017)
        initSUT()

        sut.addFavourite(2019)
        verify { mockUserRepository.favouriteSeasons = setOf(2017, 2019) }
    }

    @Test
    fun `SeasonPrefManager remove favourite removes from prefs`() {
        every { mockUserRepository.favouriteSeasons } returns setOf(2017, 2019)
        initSUT()

        sut.removeFavourite(2019)
        verify { mockUserRepository.favouriteSeasons = setOf(2017) }
    }

    //endregion

}