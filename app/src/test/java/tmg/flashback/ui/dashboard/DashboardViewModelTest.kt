package tmg.flashback.ui.dashboard

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.usecases.IsMenuIconEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsSnowEnabledUseCase
import tmg.flashback.results.ResultsNavigationComponentImpl
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListDoesNotMatchItem
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class DashboardViewModelTest: BaseTest() {

    private val mockStyleManager: StyleManager = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockStatsNavigationComponent: ResultsNavigationComponentImpl = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockIsSnowEnabledUseCase: IsSnowEnabledUseCase = mockk(relaxed = true)
    private val mockIsMenuIconEnabledUseCase: IsMenuIconEnabledUseCase = mockk(relaxed = true)

    private lateinit var underTest: DashboardViewModel

    private fun initUnderTest() {
        underTest = DashboardViewModel(
            styleManager = mockStyleManager,
            changeNightModeUseCase = mockChangeNightModeUseCase,
            buildConfigManager = mockBuildConfigManager,
            notificationRepository = mockNotificationRepository,
            permissionRepository = mockPermissionRepository,
            statsNavigationComponent = mockStatsNavigationComponent,
            permissionManager = mockPermissionManager,
            isSnowEnabledUseCase = mockIsSnowEnabledUseCase,
            isMenuIconEnabledUseCase = mockIsMenuIconEnabledUseCase,
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns false
        every { mockNotificationRepository.seenNotificationOnboarding } returns false
        every { mockNotificationRepository.seenRuntimeNotifications } returns false
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false
        every { mockStyleManager.isDayMode } returns false
    }

    @Test
    fun `dark mode result is read`() {
        every { mockStyleManager.isDayMode } returns true

        initUnderTest()
        underTest.outputs.isDarkMode.test { assertValue(false) }
    }

    @ParameterizedTest(name = "Clicking dark mode to state {0} sets dark mode {1}")
    @CsvSource(
        "true,NIGHT",
        "false,DAY"
    )
    fun `clicking dark mode calls use case`(state: Boolean, nightMode: NightMode) {
        every { mockStyleManager.isDayMode } returns !state
        initUnderTest()

        underTest.inputs.clickDarkMode(state)
        underTest.outputs.isDarkMode.test {
            assertValue(state)
        }
        verify {
            mockChangeNightModeUseCase.setNightMode(nightMode)
        }
    }

    @Test
    fun `app version is read from build config manager`() {
        every { mockBuildConfigManager.versionName } returns "version-name"

        initUnderTest()
        underTest.outputs.appVersion.test {
            assertValue("version-name")
        }
    }

    @Test
    fun `snow easter egg is emitted from use case`() {
        every { mockIsSnowEnabledUseCase.invoke() } returns true

        initUnderTest()
        underTest.outputs.snow.test {
            assertValue(true)
        }
    }

    @Test
    fun `menu title is emitted from use case`() {
        every { mockIsMenuIconEnabledUseCase.invoke() } returns MenuIcons.CHRISTMAS

        initUnderTest()
        underTest.outputs.titleIcon.test {
            assertValue(MenuIcons.CHRISTMAS)
        }
    }

    @Nested
    inner class FeatureList {

        @ParameterizedTest(name = "isRuntimeNotificationSupported = {0}, seenRuntimeNotifications = {1}, isRuntimeNotificationsEnabled = {2}, result = {3}")
        @CsvSource(
            "true ,true ,true ,false",
            "false,false,false,false",
            "false,true ,true ,false",
            "true ,false,true ,false",
            "true ,true ,false,false",
            "false,false,true ,false",
            "true ,false,false,true ",
            "false,true ,false,false"
        )
        fun `runtime notifications`(
            isRuntimeNotificationsSupported: Boolean,
            seenRuntimeNotifications: Boolean,
            isRuntimeNotificationsEnabled: Boolean,
            isFeatureIncluded: Boolean
        ) {
            every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns isRuntimeNotificationsSupported
            every { mockNotificationRepository.seenRuntimeNotifications } returns seenRuntimeNotifications
            every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns isRuntimeNotificationsEnabled

            initUnderTest()
            underTest.featurePromptsList.test {
                if (isFeatureIncluded) {
                    assertListMatchesItem { it is FeaturePrompt.RuntimeNotifications }
                } else {
                    assertListDoesNotMatchItem { it is FeaturePrompt.RuntimeNotifications }
                }
            }
        }

        @ParameterizedTest(name = "isRuntimeNotificationSupported = {0}, seenNotificationOnboarding = {1}, result = {2}")
        @CsvSource(
            "true ,true ,false",
            "false,false,true ",
            "false,true ,false",
            "true ,false,false"
        )
        fun notifications(
            isRuntimeNotificationsSupported: Boolean,
            seenNotificationOnboarding: Boolean,
            isFeatureIncluded: Boolean
        ) {
            every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns isRuntimeNotificationsSupported
            every { mockNotificationRepository.seenNotificationOnboarding } returns seenNotificationOnboarding

            initUnderTest()
            underTest.featurePromptsList.test {
                if (isFeatureIncluded) {
                    assertListMatchesItem { it is FeaturePrompt.Notifications }
                } else {
                    assertListDoesNotMatchItem { it is FeaturePrompt.Notifications }
                }
            }
        }

        @Test
        fun `clicking notifications`() {
            initUnderTest()
            val featureList = underTest.outputs.featurePromptsList.testObserve()

            underTest.inputs.clickFeaturePrompt(FeaturePrompt.Notifications)

            verify {
                mockStatsNavigationComponent.featureNotificationOnboarding()
                mockNotificationRepository.seenNotificationOnboarding = true
            }
            featureList.assertEmittedCount(2)
        }

        @Test
        fun `clicking runtime notifications`() {
            val permissions: CompletableDeferred<Boolean> = CompletableDeferred()
            every { mockPermissionManager.requestPermission(RationaleType.RuntimeNotifications) } returns permissions
            every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true

            initUnderTest()
            val featureList = underTest.outputs.featurePromptsList.testObserve()

            underTest.inputs.clickFeaturePrompt(FeaturePrompt.RuntimeNotifications)
            permissions.complete(true)

            verify {
                mockNotificationRepository.seenRuntimeNotifications = true
                mockStatsNavigationComponent.featureNotificationOnboarding()
            }
            featureList.assertEmittedCount(2)
        }
    }
}