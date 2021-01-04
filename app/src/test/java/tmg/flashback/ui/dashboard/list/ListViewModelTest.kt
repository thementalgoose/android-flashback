package tmg.flashback.ui.dashboard.list

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Year
import tmg.flashback.controllers.SeasonController
import tmg.flashback.controllers.UpNextController
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule
import tmg.flashback.testutils.*
import tmg.flashback.testutils.assertDataEventValue
import tmg.flashback.testutils.test
import tmg.flashback.testutils.testObserve

internal class ListViewModelTest: BaseTest() {

    lateinit var sut: ListViewModel

    private var currentYear: Int = Year.now().value
    private var minYear: Int = 1950

    private val mockSeasonController: SeasonController = mockk(relaxed = true)
    private val mockUpNextController: UpNextController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {

        every { mockSeasonController.favouriteSeasons } returns setOf()
        every { mockSeasonController.favouritesExpanded } returns true
        every { mockSeasonController.allExpanded } returns true

        every { mockSeasonController.defaultSeason } returns 2018
        every { mockUpNextController.getNextRace() } returns null

        every { mockSeasonController.supportedSeasons } returns List(currentYear - 1949) { it + 1950 }.toSet()
    }

    private fun initSUT() {
        sut = ListViewModel(mockSeasonController, mockUpNextController)
    }

    //region Default

    @Test
    fun `ListViewModel defaults to remote config default`() {

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Season && it.selected && it.season == 2018 }
        }
    }

    //endregion

    //region Setting defaults

    @Test
    fun `ListViewModel set user default season updates it in controller`() {

        initSUT()

        sut.inputs.clickSetDefaultSeason(2020)
        verify {
            mockSeasonController.setUserDefaultSeason(2020)
        }
        sut.outputs.defaultSeasonUpdated.test {
            assertDataEventValue(2020)
        }
    }

    @Test
    fun `ListViewModel clear default updates it in controller`() {

        initSUT()

        sut.inputs.clickClearDefaultSeason()
        verify {
            mockSeasonController.clearDefault()
        }
        sut.outputs.defaultSeasonUpdated.test {
            assertDataEventValue(null)
        }
    }

    //endregion

    //region Navigation

    @Test
    fun `ListViewModel settings inside hero fires open settings event`() {

        initSUT()

        sut.inputs.clickSettings()

        sut.outputs.openSettings.test {
            assertEventFired()
        }
    }

    //endregion

    //region Up Next section

    @Test
    fun `ListViewModel up next section not shown item is null`() {

        every { mockUpNextController.getNextRace() } returns null

        initSUT()

        sut.outputs.list.test {
            assertListDoesNotMatchItem { it is ListItem.UpNext }
        }
    }

    @Test
    fun `ListViewModel up next section shown when valid next race item found`() {

        val expected = UpNextSchedule(1,2,"test", LocalDate.now(),null,null,null,null)
        every { mockUpNextController.getNextRace() } returns expected

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.UpNext }
        }
    }

    //endregion

    @Test
    fun `ListViewModel header section favourited and all are false when prefs are false on initial load`() {

        every { mockSeasonController.favouritesExpanded } returns false
        every { mockSeasonController.allExpanded } returns false

        initSUT()

        assertFalse(sut.headerSectionFavourited)
        assertFalse(sut.headerSectionAll)
    }

    @Test
    fun `ListViewModel header section favourited and all are true when prefs are true on initial load`() {

        every { mockSeasonController.favouritesExpanded } returns true
        every { mockSeasonController.allExpanded } returns true

        initSUT()

        assertTrue(sut.headerSectionFavourited)
        assertTrue(sut.headerSectionAll)
    }

    @Test
    fun `ListViewModel list of all seasons with empty favourites is output`() {

        val favourites = emptySet<Int>()
        val expected = expectedList(favourites)

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `ListViewModel list of all seasons with some favourites is output`() {

        val favourites = setOf(2017, 2012, 2015)
        val expected = expectedList(favourites)


        every { mockSeasonController.favouriteSeasons } returns favourites

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `ListViewModel collapsing favourite and all section removes items from the list`() {

        val favourites: Set<Int> = setOf(2012, 2018, 2014)
        val expected = expectedList(favourites, showFavourites = false, showAll = false)

        every { mockSeasonController.favouriteSeasons } returns favourites
        every { mockSeasonController.favouritesExpanded } returns false
        every { mockSeasonController.allExpanded } returns false

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `ListViewModel list is emitted all items toggling each section removes list`() {

        val favourites = setOf(2017, 2012, 2010)

        every { mockSeasonController.favouriteSeasons } returns favourites

        initSUT()

        val observer = sut.outputs.list.testObserve()

        observer.assertValue(expectedList(favourites))

        sut.inputs.toggleHeader(HeaderType.FAVOURITED, false)
        observer.assertValue(expectedList(favourites, showFavourites = false))

        sut.inputs.toggleHeader(HeaderType.ALL, false)
        observer.assertValue(expectedList(favourites, showFavourites = false, showAll = false))

        sut.inputs.toggleHeader(HeaderType.FAVOURITED, true)
        observer.assertValue(expectedList(favourites, showAll = false))

        sut.inputs.toggleHeader(HeaderType.ALL, true)
        observer.assertValue(expectedList(favourites))
    }

    //region Toggle favourites

    @Test
    fun `ListViewModel toggling a favourite season that exists removed it from favourites shared prefs`() {

        val favourites = mutableSetOf(2020, 2018)

        every { mockSeasonController.favouriteSeasons } returns favourites

        initSUT()

        sut.toggleFavourite(2020)

        verify {
            mockSeasonController.removeFavourite(2020)
        }
    }

    @Test
    fun `ListViewModel toggling a favourite season that does not exists adds it from favourites shared prefs`() {

        val favourites = mutableSetOf(2020, 2018)
        every { mockSeasonController.favouriteSeasons } returns favourites

        initSUT()

        sut.toggleFavourite(2019)

        verify {
            mockSeasonController.addFavourite(2019)
        }
    }

    @Test
    fun `ListViewModel toggling favourite updates list to contain new favourite`() {

        val favourites = setOf(2017, 2012, 2010)

        every { mockSeasonController.favouriteSeasons } returns favourites

        initSUT()

        val observer = sut.outputs.list.testObserve()

        observer.assertValue(expectedList(favourites))

        sut.inputs.toggleFavourite(2017)
        observer.assertValue(expectedList(setOf(2012, 2010)))

        sut.inputs.toggleFavourite(2018)
        observer.assertValue(expectedList(setOf(2012, 2010, 2018)))
    }

    //endregion

    //region Clicking season

    @Test
    fun `ListViewModel clicking season fires show season event`() {

        initSUT()

        sut.inputs.clickSeason(2020)

        sut.outputs.showSeasonEvent.test {
            assertDataEventValue(2020)
        }
    }

    //endregion

    //region Mock Data - Expected list

    private fun expectedList(favourites: Set<Int> = emptySet(), showFavourites: Boolean = true, showAll: Boolean = true): List<ListItem> {
        val expected = mutableListOf<ListItem>()
        expected.add(ListItem.Hero)
        if (showFavourites) {
            expected.add(headerFavouriteOpen)
            expected.addAll(favouriteSeasons(favourites.toList().sorted()))
        }
        else {
            expected.add(headerFavouriteClose)
        }
        if (showAll) {
            expected.add(headerAllOpen)
            expected.addAll(allSeasons(favourites.toList()))
        }
        else {
            expected.add(headerAllClose)
        }
        return expected
    }

    private fun favouriteSeasons(favouriteItems: List<Int>): List<ListItem> = List(favouriteItems.size) {
        val year = favouriteItems[it]
        ListItem.Season(year, true, HeaderType.FAVOURITED, year == 2018)
    }.reversed()

    private fun allSeasons(favouriteItems: List<Int> = emptyList()): List<ListItem> = List((currentYear - minYear) + 1) {
        val year = minYear + it
        ListItem.Season(year, favouriteItems.contains(year), HeaderType.ALL, year == 2018)
    }.reversed()

    //endregion

    //region Mock Data - Headers

    private val headerFavouriteOpen: ListItem.Header = ListItem.Header(HeaderType.FAVOURITED, true)
    private val headerFavouriteClose: ListItem.Header =
            ListItem.Header(HeaderType.FAVOURITED, false)
    private val headerAllOpen: ListItem.Header = ListItem.Header(HeaderType.ALL, true)
    private val headerAllClose: ListItem.Header = ListItem.Header(HeaderType.ALL, false)

    //endregion
}