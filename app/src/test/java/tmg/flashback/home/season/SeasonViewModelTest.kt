package tmg.flashback.home.season

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.home.season.HeaderType.*
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.testutils.*

class SeasonViewModelTest: BaseTest() {

    private var currentYear: Int = 2020
    private var minYear: Int = 1950

    lateinit var sut: SeasonViewModel

    private val mockPrefsDB: PrefsDB = mock()

    private val headerCurrent: SeasonListItem.Header = SeasonListItem.Header(CURRENT, null)
    private val seasonCurrent: SeasonListItem.Season = SeasonListItem.Season(currentYear, false, CURRENT)
    private val headerFavouriteOpen: SeasonListItem.Header = SeasonListItem.Header(FAVOURITED, true)
    private val headerFavouriteClose: SeasonListItem.Header = SeasonListItem.Header(FAVOURITED, false)
    private val headerAllOpen: SeasonListItem.Header = SeasonListItem.Header(ALL, true)
    private val headerAllClose: SeasonListItem.Header = SeasonListItem.Header(ALL, false)


    @BeforeEach
    internal fun setUp() {

        whenever(mockPrefsDB.favouriteSeasons).thenReturn(setOf())
        whenever(mockPrefsDB.showBottomSheetFavourited).thenReturn(true)
        whenever(mockPrefsDB.showBottomSheetAll).thenReturn(true)
    }

    private fun initSUT() {
        sut = SeasonViewModel(mockPrefsDB, testScopeProvider)
    }

    @Test
    fun `SeasonViewModel header section favourited and all are false when prefs are false on initial load`() {

        whenever(mockPrefsDB.showBottomSheetFavourited).thenReturn(false)
        whenever(mockPrefsDB.showBottomSheetAll).thenReturn(false)

        initSUT()

        assertFalse(sut.headerSectionFavourited)
        assertFalse(sut.headerSectionAll)
    }

    @Test
    fun `SeasonViewModel header section favourited and all are true when prefs are true on initial load`() {

        whenever(mockPrefsDB.showBottomSheetFavourited).thenReturn(true)
        whenever(mockPrefsDB.showBottomSheetAll).thenReturn(true)

        initSUT()

        assertTrue(sut.headerSectionFavourited)
        assertTrue(sut.headerSectionAll)
    }

    @Test
    fun `SeasonViewModel list of all seasons with empty favourites is output`() {

        val favourites = emptySet<Int>()
        val expected = expectedList(favourites)

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `SeasonViewModel list of all seasons with some favourites is output`() {

        val favourites = setOf(2017, 2012, 20150)
        val expected = expectedList(favourites)

        whenever(mockPrefsDB.favouriteSeasons).thenReturn(favourites)

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `SeasonViewModel collapsing favourite and all section removes items from the list`() {

        val favourites: Set<Int> = setOf(2012, 2018, 2014)
        val expected = expectedList(favourites, showFavourites = false, showAll = false)

        whenever(mockPrefsDB.favouriteSeasons).thenReturn(favourites)
        whenever(mockPrefsDB.showBottomSheetAll).thenReturn(false)
        whenever(mockPrefsDB.showBottomSheetFavourited).thenReturn(false)

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `SeasonViewModel list is emitted all items toggling each section removes list`() {

        val favourites = setOf(2017, 2012, 2010)
        whenever(mockPrefsDB.favouriteSeasons).thenReturn(favourites)

        initSUT()

        assertLatestValue(expectedList(favourites), sut.outputs.list)

        sut.inputs.toggleHeader(FAVOURITED, false)
        assertLatestValue(expectedList(favourites, showFavourites = false), sut.outputs.list)

        sut.inputs.toggleHeader(ALL, false)
        assertLatestValue(expectedList(favourites, showFavourites = false, showAll = false), sut.outputs.list)

        sut.inputs.toggleHeader(FAVOURITED, true)
        assertLatestValue(expectedList(favourites, showAll = false), sut.outputs.list)

        sut.inputs.toggleHeader(ALL, true)
        assertLatestValue(expectedList(favourites), sut.outputs.list)
    }

    @Test
    fun `SeasonViewModel toggling a favourite season that exists removed it from favourites shared prefs`() {

        val favourites = mutableSetOf(2020, 2018)
        whenever(mockPrefsDB.favouriteSeasons).thenReturn(favourites)

        initSUT()

        sut.toggleFavourite(2020)

        verify(mockPrefsDB).favouriteSeasons = setOf(2018)
    }

    @Test
    fun `SeasonViewModel toggling a favourite season that does not exists adds it from favourites shared prefs`() {

        val favourites = mutableSetOf(2020, 2018)
        whenever(mockPrefsDB.favouriteSeasons).thenReturn(favourites)

        initSUT()

        sut.toggleFavourite(2019)

        verify(mockPrefsDB).favouriteSeasons = setOf(2020, 2018, 2019)
    }

    @Test
    fun `SeasonViewModel toggling favourite updates list to contain new favourite`() {

        val favourites = setOf(2017, 2012, 2010)
        whenever(mockPrefsDB.favouriteSeasons).thenReturn(favourites)

        initSUT()

        assertLatestValue(expectedList(favourites), sut.outputs.list)

        sut.inputs.toggleFavourite(2017)
        assertLatestValue(expectedList(setOf(2012, 2010)), sut.outputs.list)

        sut.inputs.toggleFavourite(2018)
        assertLatestValue(expectedList(setOf(2012, 2010, 2018)), sut.outputs.list)
    }

    @Test
    fun `SeasonViewModel clicking season fires show season event`() {

        initSUT()

        sut.inputs.clickSeason(2020)

        assertDataEventValue(2020, sut.outputs.showSeasonEvent)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockPrefsDB)
    }

    private fun expectedList(favourites: Set<Int> = emptySet(), showFavourites: Boolean = true, showAll: Boolean = true): List<SeasonListItem> {
        val expected = mutableListOf<SeasonListItem>()
        expected.add(SeasonListItem.Top)
        expected.add(headerCurrent)
        expected.add(seasonCurrent)
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

    private fun favouriteSeasons(favouriteItems: List<Int>): List<SeasonListItem> = List(favouriteItems.size) {
        val year = favouriteItems[it]
        SeasonListItem.Season(year, true, FAVOURITED)
    }.reversed()

    private fun allSeasons(favouriteItems: List<Int> = emptyList()): List<SeasonListItem> = List((currentYear - minYear) + 1) {
        val year = minYear + it
        SeasonListItem.Season(year, favouriteItems.contains(year), ALL)
    }.reversed()

}