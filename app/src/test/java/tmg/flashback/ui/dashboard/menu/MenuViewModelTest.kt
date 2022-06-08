package tmg.flashback.ui.dashboard.menu

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.RssNavigationComponent
import tmg.flashback.formula1.constants.Formula1.decadeColours
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.di.StatsNavigator
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class MenuViewModelTest: BaseTest() {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)
    private val mockStyleManager: StyleManager = mockk(relaxed = true)
    private val mockRssNavigationComponent: RssNavigationComponent = mockk(relaxed = true)
    private val mockNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockStatsNavigator: StatsNavigator = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: MenuViewModel

    private fun initUnderTest() {
        underTest = MenuViewModel(
            mockHomeRepository,
            mockNotificationRepository,
            mockDefaultSeasonUseCase,
            mockChangeNightModeUseCase,
            mockStyleManager,
            mockRssNavigationComponent,
            mockNavigationComponent,
            mockStatsNavigator,
            mockStatsNavigationComponent
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockStyleManager.isDayMode } returns true
        every { mockHomeRepository.supportedSeasons } returns fakeSupportedSeasons
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2022
        every { mockNotificationRepository.seenNotificationOnboarding } returns false
    }

    @Test
    fun `initial load shows expected links`() {
        initUnderTest()

        underTest.outputs.links.test {
            assertValue(listOf(
                MenuItems.Button.Search,
                MenuItems.Button.Rss,
                MenuItems.Button.Settings,
                MenuItems.Button.Contact,
                MenuItems.Divider("a"),
                MenuItems.Toggle.DarkMode(_isEnabled = false),
                MenuItems.Divider("b"),
                MenuItems.Feature.Notifications
            ))
        }
    }

    @Test
    fun `initial load shows expected links with dark mode enabled`() {
        every { mockStyleManager.isDayMode } returns false
        initUnderTest()

        underTest.outputs.links.test {
            assertValue(listOf(
                MenuItems.Button.Search,
                MenuItems.Button.Rss,
                MenuItems.Button.Settings,
                MenuItems.Button.Contact,
                MenuItems.Divider("a"),
                MenuItems.Toggle.DarkMode(_isEnabled = true),
                MenuItems.Divider("b"),
                MenuItems.Feature.Notifications
            ))
        }
    }

    @Test
    fun `initial load shows expected seasons from supported seasons`() {
        initUnderTest()

        underTest.outputs.season.test {
            assertValue(expectedMenuItems)
        }
    }



    @Test
    fun `click button contact calls about this app`() {
        initUnderTest()

        underTest.inputs.clickButton(MenuItems.Button.Contact)

        verify {
            mockNavigationComponent.aboutApp()
        }
    }

    @Test
    fun `click button search calls search`() {
        initUnderTest()

        underTest.inputs.clickButton(MenuItems.Button.Search)

        verify {
            mockStatsNavigationComponent.search()
        }
    }

    @Test
    fun `click button rss calls rss`() {
        initUnderTest()

        underTest.inputs.clickButton(MenuItems.Button.Rss)

        verify {
            mockRssNavigationComponent.rss()
        }
    }

    @Test
    fun `click button settings calls settings`() {
        initUnderTest()

        underTest.inputs.clickButton(MenuItems.Button.Settings)

        verify {
            mockNavigationComponent.settings()
        }
    }



    @Test
    fun `click toggle dark mode sets night mode NIGHT if toggle disabled previously`() {
        initUnderTest()

        underTest.inputs.clickToggle(MenuItems.Toggle.DarkMode(_isEnabled = false))

        verify {
            mockChangeNightModeUseCase.setNightMode(NightMode.NIGHT)
        }
    }

    @Test
    fun `click toggle dark mode sets night mode DAY if toggle enabled previously`() {
        initUnderTest()

        underTest.inputs.clickToggle(MenuItems.Toggle.DarkMode(_isEnabled = true))

        verify {
            mockChangeNightModeUseCase.setNightMode(NightMode.DAY)
        }
    }



    @Test
    fun `click feature calls stats navigator`() {
        initUnderTest()

        underTest.inputs.clickFeature(MenuItems.Feature.Notifications)

        verify {
            mockStatsNavigator.goToNotificationOnboarding()
            mockNotificationRepository.seenNotificationOnboarding = true
        }
    }

    private val fakeSupportedSeasons: Set<Int> = setOf(
        2017, 2019, 2020, 2021, 2022
    )
    private val expectedMenuItems = listOf(
        MenuSeasonItem(
            colour = decadeColours["2020"]!!,
            season = 2022,
            isSelected = true,
            isFirst = true,
            isLast = false
        ),
        MenuSeasonItem(
            colour = decadeColours["2020"]!!,
            season = 2021,
            isSelected = false,
            isFirst = false,
            isLast = false
        ),
        MenuSeasonItem(
            colour = decadeColours["2020"]!!,
            season = 2020,
            isSelected = false,
            isFirst = false,
            isLast = false
        ),
        MenuSeasonItem(
            colour = decadeColours["2010"]!!,
            season = 2019,
            isSelected = false,
            isFirst = false,
            isLast = true
        ),
        MenuSeasonItem(
            colour = decadeColours["2010"]!!,
            season = 2017,
            isSelected = false,
            isFirst = true,
            isLast = true
        )
    )
}