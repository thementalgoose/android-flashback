package tmg.flashback.dashboard.list

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Year
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.flashback.testutils.*
import tmg.flashback.testutils.assertDataEventValue
import tmg.flashback.testutils.test
import tmg.flashback.testutils.testObserve

internal class ListViewModelTest: BaseTest() {

    lateinit var sut: ListViewModel

    private var currentYear: Int = Year.now().value
    private var minYear: Int = 1950

    private val mockPrefCustomisationRepository: PrefCustomisationRepository = mockk(relaxed = true)
    private val mockRemoteConfigRepository: RemoteConfigRepository = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {

        every { mockPrefCustomisationRepository.favouriteSeasons } returns setOf()
        every { mockPrefCustomisationRepository.showListFavourited } returns true
        every { mockPrefCustomisationRepository.showListAll } returns true

        every { mockRemoteConfigRepository.upNext } returns emptyList()
        every { mockRemoteConfigRepository.defaultYear } returns 2018
    }

    private fun initSUT() {
        sut = ListViewModel(mockPrefCustomisationRepository, mockRemoteConfigRepository)
    }

    //region Default

    @Test
    fun `SeasonViewModel defaults to remote config default`() {

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Season && it.selected && it.season == 2018 }
        }
    }

    //endregion

    //region Navigation

    @Test
    fun `SeasonViewModel settings inside hero fires open settings event`() {

        initSUT()

        sut.inputs.clickSettings()

        sut.outputs.openSettings.test {
            assertEventFired()
        }
    }

    //endregion

    //region Up Next section

    @Test
    fun `SeasonViewModel up next section not shown when empty list returned from remote config`() {

        every { mockRemoteConfigRepository.upNext } returns emptyList()

        initSUT()

        sut.outputs.list.test {
            assertListDoesNotMatchItem { it is ListItem.UpNext }
        }
    }

    @Test
    fun `SeasonViewModel up next section not shown when only up next item is in the past`() {

        val upNextList = listOf(mockUpNextYesterday)
        every { mockRemoteConfigRepository.upNext } returns upNextList

        initSUT()

        sut.outputs.list.test {
            assertListDoesNotMatchItem { it is ListItem.UpNext }
        }
    }

    @Test
    fun `SeasonViewModel up next section shown when theres an item in the future`() {

        val upNextList = listOf(mockUpNextTomorrow)
        every { mockRemoteConfigRepository.upNext } returns upNextList

        initSUT()

        sut.outputs.list.test {
            assertListContainsItem(ListItem.UpNext(mockUpNextTomorrow))
        }
    }

    @Test
    fun `SeasonViewModel up next section shows the correct up next item`() {

        val upNextList = listOf(mockUpNextYesterday, mockUpNextToday, mockUpNextTomorrow)
        every { mockRemoteConfigRepository.upNext } returns upNextList

        initSUT()

        sut.outputs.list.test {
            assertListContainsItem(ListItem.UpNext(mockUpNextToday))
        }
    }

    //endregion

    @Test
    fun `SeasonViewModel header section favourited and all are false when prefs are false on initial load`() {

        every { mockPrefCustomisationRepository.showListFavourited } returns false
        every { mockPrefCustomisationRepository.showListAll } returns false

        initSUT()

        assertFalse(sut.headerSectionFavourited)
        assertFalse(sut.headerSectionAll)
    }

    @Test
    fun `SeasonViewModel header section favourited and all are true when prefs are true on initial load`() {

        every { mockPrefCustomisationRepository.showListFavourited } returns true
        every { mockPrefCustomisationRepository.showListAll } returns true

        initSUT()

        assertTrue(sut.headerSectionFavourited)
        assertTrue(sut.headerSectionAll)
    }

    @Test
    fun `SeasonViewModel list of all seasons with empty favourites is output`() {

        val favourites = emptySet<Int>()
        val expected = expectedList(favourites)

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `SeasonViewModel list of all seasons with some favourites is output`() {

        val favourites = setOf(2017, 2012, 20150)
        val expected = expectedList(favourites)

        every { mockPrefCustomisationRepository.favouriteSeasons } returns favourites

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `SeasonViewModel collapsing favourite and all section removes items from the list`() {

        val favourites: Set<Int> = setOf(2012, 2018, 2014)
        val expected = expectedList(favourites, showFavourites = false, showAll = false)

        every { mockPrefCustomisationRepository.favouriteSeasons } returns favourites
        every { mockPrefCustomisationRepository.showListFavourited } returns false
        every { mockPrefCustomisationRepository.showListAll } returns false

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `SeasonViewModel list is emitted all items toggling each section removes list`() {

        val favourites = setOf(2017, 2012, 2010)

        every { mockPrefCustomisationRepository.favouriteSeasons } returns favourites

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
    fun `SeasonViewModel toggling a favourite season that exists removed it from favourites shared prefs`() {

        val favourites = mutableSetOf(2020, 2018)

        every { mockPrefCustomisationRepository.favouriteSeasons } returns favourites

        initSUT()

        sut.toggleFavourite(2020)

        verify { mockPrefCustomisationRepository.favouriteSeasons = setOf(2018) }
    }

    @Test
    fun `SeasonViewModel toggling a favourite season that does not exists adds it from favourites shared prefs`() {

        val favourites = mutableSetOf(2020, 2018)
        every { mockPrefCustomisationRepository.favouriteSeasons } returns favourites

        initSUT()

        sut.toggleFavourite(2019)

        verify { mockPrefCustomisationRepository.favouriteSeasons = setOf(2020, 2018, 2019) }
    }

    @Test
    fun `SeasonViewModel toggling favourite updates list to contain new favourite`() {

        val favourites = setOf(2017, 2012, 2010)

        every { mockPrefCustomisationRepository.favouriteSeasons } returns favourites

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
    fun `SeasonViewModel clicking season fires show season event`() {

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

    //region Mock Data - Up Next

    private val mockUpNextYesterday = UpNextSchedule(1, 1, "Test", LocalDate.now().minusDays(1), null, null, null, null)
    private val mockUpNextToday = UpNextSchedule(1, 2, "Second", LocalDate.now(), null, null, null, null)
    private val mockUpNextTomorrow = UpNextSchedule(1, 3, "Second", LocalDate.now().plusDays(1), null, null, null, null)

    //endregion

    //region Mock Data - Headers

    private val headerFavouriteOpen: ListItem.Header = ListItem.Header(HeaderType.FAVOURITED, true)
    private val headerFavouriteClose: ListItem.Header =
        ListItem.Header(HeaderType.FAVOURITED, false)
    private val headerAllOpen: ListItem.Header = ListItem.Header(HeaderType.ALL, true)
    private val headerAllClose: ListItem.Header = ListItem.Header(HeaderType.ALL, false)

    //endregion
}