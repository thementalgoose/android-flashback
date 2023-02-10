package tmg.flashback.ui.dashboard

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.eastereggs.usecases.IsMenuIconEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsSnowEnabledUseCase
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.flashback.usecases.GetSeasonsUseCase
import tmg.testutils.BaseTest

internal class DashboardViewModelTest: BaseTest() {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockStyleManager: StyleManager = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockGetSeasonUseCase: GetSeasonsUseCase = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockIsSnowEnabledUseCase: IsSnowEnabledUseCase = mockk(relaxed = true)
    private val mockIsMenuIconEnabledUseCase: IsMenuIconEnabledUseCase = mockk(relaxed = true)

    private lateinit var underTest: DashboardViewModel

    private fun initUnderTest() {
        underTest = DashboardViewModel(
            rssRepository = mockRssRepository,
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            styleManager = mockStyleManager,
            changeNightModeUseCase = mockChangeNightModeUseCase,
            buildConfigManager = mockBuildConfigManager,
            notificationRepository = mockNotificationRepository,
            permissionRepository = mockPermissionRepository,
            statsNavigationComponent = mockStatsNavigationComponent,
            permissionManager = mockPermissionManager,
            getSeasonUseCase = mockGetSeasonUseCase,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            isSnowEnabledUseCase = mockIsSnowEnabledUseCase,
            isMenuIconEnabledUseCase = mockIsMenuIconEnabledUseCase,
        )
    }
}