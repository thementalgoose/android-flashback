package tmg.flashback.statistics.ui.dashboard.list

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.Year
import tmg.flashback.core.controllers.FeatureController
import tmg.flashback.core.model.TimeListDisplayType
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.controllers.UpNextController
import tmg.flashback.statistics.testutils.*

internal class ListViewModelTest: BaseTest() {

    lateinit var sut: ListViewModel

    private var currentYear: Int = Year.now().value
    private var minYear: Int = 1950

    private val mockSeasonController: SeasonController = mockk(relaxed = true)
    private val mockUpNextController: UpNextController = mockk(relaxed = true)
    private val mockFeatureController: FeatureController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {

        every { mockSeasonController.favouriteSeasons } returns setOf()
        every { mockSeasonController.favouritesExpanded } returns true
        every { mockSeasonController.allExpanded } returns true

        every { mockSeasonController.defaultSeason } returns 2018

        every { mockUpNextController.getNextEvent() } returns null
        every { mockUpNextController.upNextDisplayType } returns TimeListDisplayType.LOCAL

        every { mockSeasonController.supportedSeasons } returns List(currentYear - 1949) { it + 1950 }.toSet()

        every { mockFeatureController.rssEnabled } returns false
    }

    private fun initSUT() {
        sut = ListViewModel(mockSeasonController, mockUpNextController, mockFeatureController)
    }

    //region Default

    @Test
    fun `defaults to remote config default`() {

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Season && it.selected && it.season == 2018 }
        }
    }

    //endregion

    //region Setting defaults

    @Test
    fun `set user default season updates it in controller`() {

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
    fun `clear default updates it in controller`() {

        initSUT()

        sut.inputs.clickClearDefaultSeason()
        verify {
            mockSeasonController.clearDefault()
        }
        sut.outputs.defaultSeasonUpdated.test {
            assertDataEventValue(null)
        }
    }

    @Test
    fun `with no user defined default set the option to show clear is false`() {

        every { mockSeasonController.isUserDefinedValueSet } returns true

        initSUT()
        sut.outputs.list.test {
            assertListMatchesItem {
                it is ListItem.Season && it.showClearDefault
            }
        }
    }

    @Test
    fun `with no user defined default set the option to show clear is true`() {

        every { mockSeasonController.isUserDefinedValueSet } returns false

        initSUT()
        sut.outputs.list.test {
            assertListMatchesItem {
                it is ListItem.Season && !it.showClearDefault
            }
        }
    }

    //endregion

    //region Up Next section

    @Test
    fun `up next section not shown item is null`() {

        every { mockUpNextController.getNextEvent() } returns null

        initSUT()

        sut.outputs.list.test {
            assertListDoesNotMatchItem { it is ListItem.UpNext }
        }
    }

    @Test
    fun `up next section shown when valid next race item found`() {

        val expected = UpNextSchedule(1,2,"test", null, emptyList(),null,null)
        every { mockUpNextController.getNextEvent() } returns expected

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.UpNext }
        }
    }

    @Test
    fun `clicking time display list format with type puts it in up next item`() = coroutineTest {

        val expected = UpNextSchedule(1,2,"test", null, emptyList(),null,null)
        every { mockUpNextController.getNextEvent() } returns expected
        initSUT()

        sut.inputs.clickTimeDisplayType(TimeListDisplayType.RELATIVE)

        sut.outputs.list.test {
            println(latestValue)
            assertListMatchesItem { it is ListItem.UpNext && it.timeFormatType == TimeListDisplayType.RELATIVE }
        }
    }

    //endregion

    //region Links section

    @Test
    fun `links section is displayed when rss feature is enabled`() {

        every { mockFeatureController.rssEnabled } returns true
        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Button && it.itemId == "rss" }
        }
    }

    @Test
    fun `links section is hidden when rss feature is not enabled`() {

        every { mockFeatureController.rssEnabled } returns false
        initSUT()

        sut.outputs.list.test {
            assertListDoesNotMatchItem { it is ListItem.Button && it.itemId == "rss" }
        }
    }

    @Test
    fun `rss inside links fires open rss event`() {

        initSUT()

        sut.inputs.clickRss()

        sut.outputs.openRss.test {
            assertEventFired()
        }
    }

    @Test
    fun `settings inside links fires open settings event`() {

        initSUT()

        sut.inputs.clickSettings()

        sut.outputs.openSettings.test {
            assertEventFired()
        }
    }

    //endregion

    @Test
    fun `header section favourited and all are false when prefs are false on initial load`() {

        every { mockSeasonController.favouritesExpanded } returns false
        every { mockSeasonController.allExpanded } returns false

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Header && it.type == HeaderType.ALL && it.expanded == false }
            assertListMatchesItem { it is ListItem.Header && it.type == HeaderType.FAVOURITED && it.expanded == false }
        }
    }

    @Test
    fun `header section favourited and all are true when prefs are true on initial load`() {

        every { mockSeasonController.favouritesExpanded } returns true
        every { mockSeasonController.allExpanded } returns true

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Header && it.type == HeaderType.ALL && it.expanded == true }
            assertListMatchesItem { it is ListItem.Header && it.type == HeaderType.FAVOURITED && it.expanded == true }
        }
    }

    @Test
    fun `list of all seasons with empty favourites is output`() {

        val favourites = emptySet<Int>()
        val expected = expectedList(favourites)

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `list of all seasons with some favourites is output`() {

        val favourites = setOf(2017, 2012, 2015)
        val expected = expectedList(favourites)


        every { mockSeasonController.favouriteSeasons } returns favourites

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `collapsing favourite and all section removes items from the list`() {

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
    fun `list is emitted all items toggling each section removes list`() {

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
    fun `toggling a favourite season that is already favourited removes it`() {
        val favourites = mutableSetOf(2020, 2018)
        every { mockSeasonController.favouriteSeasons } returns favourites
        every { mockSeasonController.isFavourite(2018) } returns true

        initSUT()

        sut.inputs.toggleFavourite(2018)

        verify {
            mockSeasonController.removeFavourite(2018)
        }
    }

    @Test
    fun `toggling a favourite season that is not favourited yet adds it to the controller`() {
        val favourites = mutableSetOf(2020)
        every { mockSeasonController.favouriteSeasons } returns favourites
        every { mockSeasonController.isFavourite(2018) } returns false

        initSUT()

        sut.inputs.toggleFavourite(2018)

        verify {
            mockSeasonController.addFavourite(2018)
        }
    }

    @Test
    fun `toggling a favourite calls refresh`() {
        val favourites = mutableSetOf(2020)
        every { mockSeasonController.favouriteSeasons } returns favourites
        every { mockSeasonController.isFavourite(2018) } returns false

        initSUT()

        val observer = sut.outputs.list.testObserve()
        observer.assertListMatchesItem { it is ListItem.Season && it.season == 2018 && !it.isFavourited }
        // Mock the favourite seasons update
        every { mockSeasonController.favouriteSeasons } returns mutableSetOf(2020, 2018)
        sut.inputs.toggleFavourite(2018)
        verify {
            mockSeasonController.addFavourite(2018)
        }

        observer.assertListMatchesItem { it is ListItem.Season && it.season == 2018 && it.isFavourited }
    }

    //endregion

    //region Clicking season

    @Test
    fun `clicking season fires show season event`() {

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
        expected.add(ListItem.Divider)
        expected.add(headerLinks)
        expected.add(buttonSettings)
        expected.add(ListItem.Divider)
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
        ListItem.Season(year, true, HeaderType.FAVOURITED, year == 2018, year == 2018)
    }.reversed()

    private fun allSeasons(favouriteItems: List<Int> = emptyList()): List<ListItem> = List((currentYear - minYear) + 1) {
        val year = minYear + it
        ListItem.Season(year, favouriteItems.contains(year), HeaderType.ALL, year == 2018, year == 2018)
    }.reversed()

    private val buttonSettings = ListItem.Button("settings", R.string.dashboard_season_list_extra_settings_title, R.drawable.nav_settings)

    //endregion

    //region Mock Data - Headers

    private val headerLinks: ListItem.Header =
        ListItem.Header(HeaderType.LINKS, null)
    private val headerFavouriteOpen: ListItem.Header = ListItem.Header(HeaderType.FAVOURITED, true)
    private val headerFavouriteClose: ListItem.Header =
        ListItem.Header(HeaderType.FAVOURITED, false)
    private val headerAllOpen: ListItem.Header = ListItem.Header(HeaderType.ALL, true)
    private val headerAllClose: ListItem.Header = ListItem.Header(HeaderType.ALL, false)

    //endregion
}