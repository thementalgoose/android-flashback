package tmg.flashback.home.season

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.home.season.HeaderType.*
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.flashback.testutils.*

class SeasonViewModelTest: BaseTest() {

    private var currentYear: Int = 2020
    private var minYear: Int = 1950

    lateinit var sut: SeasonViewModel

    private val mockPrefsRepository: PrefCustomisationRepository = mockk(relaxed = true)

    private val headerCurrent: SeasonListItem.Header = SeasonListItem.Header(CURRENT, null)
    private val seasonCurrent: SeasonListItem.Season = SeasonListItem.Season(currentYear, false, CURRENT)
    private val headerFavouriteOpen: SeasonListItem.Header = SeasonListItem.Header(FAVOURITED, true)
    private val headerFavouriteClose: SeasonListItem.Header = SeasonListItem.Header(FAVOURITED, false)
    private val headerAllOpen: SeasonListItem.Header = SeasonListItem.Header(ALL, true)
    private val headerAllClose: SeasonListItem.Header = SeasonListItem.Header(ALL, false)


    @BeforeEach
    internal fun setUp() {

        every { mockPrefsRepository.favouriteSeasons } returns setOf()
        every { mockPrefsRepository.showBottomSheetFavourited } returns true
        every { mockPrefsRepository.showBottomSheetAll } returns true
    }

    private fun initSUT() {
        sut = SeasonViewModel(mockPrefsRepository)
    }

    @Test
    fun `SeasonViewModel header section favourited and all are false when prefs are false on initial load`() {

        every { mockPrefsRepository.showBottomSheetFavourited } returns false
        every { mockPrefsRepository.showBottomSheetAll } returns false

        initSUT()

        assertFalse(sut.headerSectionFavourited)
        assertFalse(sut.headerSectionAll)
    }

    @Test
    fun `SeasonViewModel header section favourited and all are true when prefs are true on initial load`() {

        every { mockPrefsRepository.showBottomSheetFavourited } returns true
        every { mockPrefsRepository.showBottomSheetAll } returns true

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

        every { mockPrefsRepository.favouriteSeasons } returns favourites

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `SeasonViewModel collapsing favourite and all section removes items from the list`() {

        val favourites: Set<Int> = setOf(2012, 2018, 2014)
        val expected = expectedList(favourites, showFavourites = false, showAll = false)

        every { mockPrefsRepository.favouriteSeasons } returns favourites
        every { mockPrefsRepository.showBottomSheetFavourited } returns false
        every { mockPrefsRepository.showBottomSheetAll } returns false

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `SeasonViewModel list is emitted all items toggling each section removes list`() {

        val favourites = setOf(2017, 2012, 2010)

        every { mockPrefsRepository.favouriteSeasons } returns favourites

        initSUT()

        val observer = sut.outputs.list.testObserve()

        observer.assertValue(expectedList(favourites))

        sut.inputs.toggleHeader(FAVOURITED, false)
        observer.assertValue(expectedList(favourites, showFavourites = false))

        sut.inputs.toggleHeader(ALL, false)
        observer.assertValue(expectedList(favourites, showFavourites = false, showAll = false))

        sut.inputs.toggleHeader(FAVOURITED, true)
        observer.assertValue(expectedList(favourites, showAll = false))

        sut.inputs.toggleHeader(ALL, true)
        observer.assertValue(expectedList(favourites))
    }

    @Test
    fun `SeasonViewModel toggling a favourite season that exists removed it from favourites shared prefs`() {

        val favourites = mutableSetOf(2020, 2018)

        every { mockPrefsRepository.favouriteSeasons } returns favourites

        initSUT()

        sut.toggleFavourite(2020)

        verify { mockPrefsRepository.favouriteSeasons = setOf(2018) }
    }

    @Test
    fun `SeasonViewModel toggling a favourite season that does not exists adds it from favourites shared prefs`() {

        val favourites = mutableSetOf(2020, 2018)
        every { mockPrefsRepository.favouriteSeasons } returns favourites

        initSUT()

        sut.toggleFavourite(2019)

        verify { mockPrefsRepository.favouriteSeasons = setOf(2020, 2018, 2019) }
    }

    @Test
    fun `SeasonViewModel toggling favourite updates list to contain new favourite`() {

        val favourites = setOf(2017, 2012, 2010)

        every { mockPrefsRepository.favouriteSeasons } returns favourites

        initSUT()

        val observer = sut.outputs.list.testObserve()

        observer.assertValue(expectedList(favourites))

        sut.inputs.toggleFavourite(2017)
        observer.assertValue(expectedList(setOf(2012, 2010)))

        sut.inputs.toggleFavourite(2018)
        observer.assertValue(expectedList(setOf(2012, 2010, 2018)))
    }

    @Test
    fun `SeasonViewModel clicking season fires show season event`() {

        initSUT()

        sut.inputs.clickSeason(2020)

        sut.outputs.showSeasonEvent.test {
            assertDataEventValue(2020)
        }
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