package tmg.flashback.ui.dashboard.list

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.Year
import tmg.flashback.DebugController
import tmg.flashback.R
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.ads.repository.model.AdvertConfig
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.*

internal class ListViewModelTest: BaseTest() {

    lateinit var sut: ListViewModel

    private var currentYear: Int = Year.now().value
    private var minYear: Int = 1950

    private val mockHomeController: HomeController = mockk(relaxed = true)
    private val mockRssController: RSSController = mockk(relaxed = true)
    private val mockDebugController: DebugController = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)
    private val mockStyleManager: StyleManager = mockk(relaxed = true)
    private val mockScheduleController: ScheduleController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {

        every { mockHomeController.favouriteSeasons } returns setOf()
        every { mockHomeController.favouritesExpanded } returns true
        every { mockHomeController.allExpanded } returns true
        every { mockHomeController.defaultSeason } returns 2018
        every { mockHomeController.supportedSeasons } returns List(currentYear - 1949) { it + 1950 }.toSet()

        every { mockAdsRepository.advertConfig } returns AdvertConfig(
            onHomeScreen = false,
            onRaceScreen = false,
            onSearch = false,
            onRss = false
        )

        every { mockRssController.enabled } returns false

        every { mockDebugController.listItem } returns null

        every { mockThemeRepository.nightMode } returns NightMode.DEFAULT
        every { mockStyleManager.isDayMode } returns true
    }

    private fun initSUT() {
        sut = ListViewModel(
            mockHomeController,
            mockRssController,
            mockDebugController,
            mockAdsRepository,
            mockThemeRepository,
            mockChangeNightModeUseCase,
            mockStyleManager,
            mockScheduleController
        )
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
            mockHomeController.setUserDefaultSeason(2020)
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
            mockHomeController.clearDefault()
        }
        sut.outputs.defaultSeasonUpdated.test {
            assertDataEventValue(null)
        }
    }

    @Test
    fun `with no user defined default set the option to show clear is false`() {

        every { mockHomeController.isUserDefinedValueSet } returns true

        initSUT()
        sut.outputs.list.test {
            assertListMatchesItem {
                it is ListItem.Season && it.showClearDefault
            }
        }
    }

    @Test
    fun `with no user defined default set the option to show clear is true`() {

        every { mockHomeController.isUserDefinedValueSet } returns false

        initSUT()
        sut.outputs.list.test {
            assertListMatchesItem {
                it is ListItem.Season && !it.showClearDefault
            }
        }
    }

    //endregion

    //region Night mode

    @Test
    fun `toggle night mode from default when device is true sets night mode to NIGHT`() {
        every { mockThemeRepository.nightMode } returns NightMode.DEFAULT
        every { mockStyleManager.isDayMode } returns true

        initSUT()
        sut.toggleDarkMode()

        verify {
            mockChangeNightModeUseCase.setNightMode(NightMode.NIGHT)
        }
        sut.outputs.list.test {
            assertListMatchesItem {
                it is ListItem.Switch && it.itemId == "dark_mode" && !it.isChecked
            }
        }
    }

    @Test
    fun `toggle night mode from default when device is false sets night mode to DAY`() {
        every { mockThemeRepository.nightMode } returns NightMode.DEFAULT
        every { mockStyleManager.isDayMode } returns false

        initSUT()
        sut.toggleDarkMode()

        verify {
            mockChangeNightModeUseCase.setNightMode(NightMode.DAY)
        }
        sut.outputs.list.test {
            assertListMatchesItem {
                it is ListItem.Switch && it.itemId == "dark_mode" && it.isChecked
            }
        }
    }

    //endregion

    //region Links section

    @Test
    fun `links section is displayed when rss feature is enabled`() {

        every { mockRssController.enabled } returns true
        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Button && it.itemId == "rss" }
        }
    }

    @Test
    fun `links section is hidden when rss feature is not enabled`() {

        every { mockRssController.enabled } returns false
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

    @Test
    fun `contact link fires open contact event`() {

        initSUT()

        sut.inputs.clickContact()

        sut.outputs.openContact.test {
            assertEventFired()
        }
    }

    //endregion

    @Test
    fun `header section favourited and all are false when prefs are false on initial load`() {

        every { mockHomeController.favouritesExpanded } returns false
        every { mockHomeController.allExpanded } returns false

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Header && it.type == HeaderType.ALL && it.expanded == false }
            assertListMatchesItem { it is ListItem.Header && it.type == HeaderType.FAVOURITED && it.expanded == false }
        }
    }

    @Test
    fun `header section favourited and all are true when prefs are true on initial load`() {

        every { mockHomeController.favouritesExpanded } returns true
        every { mockHomeController.allExpanded } returns true

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

        every { mockHomeController.favouriteSeasons } returns favourites

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `collapsing favourite and all section removes items from the list`() {

        val favourites: Set<Int> = setOf(2012, 2018, 2014)
        val expected = expectedList(favourites, showFavourites = false, showAll = false)

        every { mockHomeController.favouriteSeasons } returns favourites
        every { mockHomeController.favouritesExpanded } returns false
        every { mockHomeController.allExpanded } returns false

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `list is emitted all items toggling each section removes list`() = coroutineTest {

        val favourites = setOf(2017, 2012, 2010)

        every { mockHomeController.favouriteSeasons } returns favourites

        initSUT()

        val observer = sut.outputs.list.testObserve()

        observer.assertValue(expectedList(favourites))

        sut.inputs.toggleHeader(HeaderType.FAVOURITED, false)
        advanceUntilIdle()
        observer.assertValue(expectedList(favourites, showFavourites = false))

        sut.inputs.toggleHeader(HeaderType.ALL, false)
        advanceUntilIdle()
        observer.assertValue(expectedList(favourites, showFavourites = false, showAll = false))

        sut.inputs.toggleHeader(HeaderType.FAVOURITED, true)
        advanceUntilIdle()
        observer.assertValue(expectedList(favourites, showAll = false))

        sut.inputs.toggleHeader(HeaderType.ALL, true)
        advanceUntilIdle()
        observer.assertValue(expectedList(favourites))
    }

    //region Toggle favourites

    @Test
    fun `toggling a favourite season that is already favourited removes it`() {
        val favourites = mutableSetOf(2020, 2018)
        every { mockHomeController.favouriteSeasons } returns favourites
        every { mockHomeController.isFavourite(2018) } returns true

        initSUT()

        sut.inputs.toggleFavourite(2018)

        verify {
            mockHomeController.removeFavourite(2018)
        }
    }

    @Test
    fun `toggling a favourite season that is not favourited yet adds it to the controller`() {
        val favourites = mutableSetOf(2020)
        every { mockHomeController.favouriteSeasons } returns favourites
        every { mockHomeController.isFavourite(2018) } returns false

        initSUT()

        sut.inputs.toggleFavourite(2018)

        verify {
            mockHomeController.addFavourite(2018)
        }
    }

    @Test
    fun `toggling a favourite calls refresh`() = coroutineTest {
        val favourites = mutableSetOf(2020)
        every { mockHomeController.favouriteSeasons } returns favourites
        every { mockHomeController.isFavourite(2018) } returns false

        initSUT()

        val observer = sut.outputs.list.testObserve()
        observer.assertListMatchesItem { it is ListItem.Season && it.season == 2018 && !it.isFavourited }
        // Mock the favourite seasons update
        every { mockHomeController.favouriteSeasons } returns mutableSetOf(2020, 2018)
        sut.inputs.toggleFavourite(2018)
        advanceUntilIdle()

        verify {
            mockHomeController.addFavourite(2018)
        }

        observer.assertListMatchesItem(atIndex = 1) { it is ListItem.Season && it.season == 2018 && it.isFavourited }
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

    //region Show notification banner

    @Test
    fun `show notification banner if should show is true`() {
        every { mockScheduleController.shouldShowNotificationOnboarding } returns true

        initSUT()
        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.FeatureBanner.EnrolNotifications }
        }
    }

    @Test
    fun `click feature banner enrol notifications marks onboarding seen and refreshes list`() = coroutineTest {
        every { mockScheduleController.shouldShowNotificationOnboarding } returns true

        initSUT()
        val observer = sut.outputs.list.testObserve()
        observer.assertEmittedCount(1)

        sut.inputs.clickFeatureBanner(ListItem.FeatureBanner.EnrolNotifications)
        advanceUntilIdle()

        verify {
            mockScheduleController.seenOnboarding()
        }
        sut.outputs.openNotificationsOnboarding.test {
            assertEventFired()
        }
        observer.assertEmittedCount(2)
    }

    //endregion

    //region Show Adverts

    @Test
    fun `show ads banner if should show is true`() {
        every { mockAdsRepository.advertConfig } returns AdvertConfig(onHomeScreen = true)

        initSUT()
        sut.outputs.list.test {
            assertListMatchesItem { it is ListItem.Advert }
        }
    }

    //endregion

    //region Mock Data - Expected list

    private fun expectedList(
        favourites: Set<Int> = emptySet(),
        showFavourites: Boolean = true,
        showAll: Boolean = true,
        darkModeChecked: Boolean = false
    ): List<ListItem> {
        val expected = mutableListOf<ListItem>()
        expected.add(ListItem.Hero)
        expected.add(ListItem.Divider)
        expected.add(headerLinks)
        expected.add(buttonSettings)
        expected.add(buttonContact)
        expected.add(ListItem.Divider)
        expected.add(switchDarkMode(darkModeChecked))
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

    private val buttonSettings = ListItem.Button("settings",
            R.string.dashboard_season_list_extra_settings_title,
            R.drawable.nav_settings
    )
    private val buttonContact = ListItem.Button("contact",
            R.string.dashboard_season_list_extra_contact_title,
            R.drawable.ic_contact
    )
    private fun switchDarkMode(isChecked: Boolean = true) = ListItem.Switch("dark_mode",
            R.string.dashboard_season_list_extra_dark_mode_title,
            R.drawable.ic_nightmode_dark,
            isChecked = isChecked
    )

    //endregion

    //region Mock Data - Headers

    private val headerLinks: ListItem.Header =
        ListItem.Header(tmg.flashback.ui.dashboard.list.HeaderType.LINKS, null)
    private val headerFavouriteOpen: ListItem.Header = ListItem.Header(
        HeaderType.FAVOURITED, true)
    private val headerFavouriteClose: ListItem.Header =
        ListItem.Header(HeaderType.FAVOURITED, false)
    private val headerAllOpen: ListItem.Header = ListItem.Header(
        HeaderType.ALL, true)
    private val headerAllClose: ListItem.Header = ListItem.Header(
        HeaderType.ALL, false)

    //endregion
}