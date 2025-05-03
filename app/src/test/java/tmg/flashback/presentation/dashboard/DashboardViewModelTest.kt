package tmg.flashback.presentation.dashboard

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.usecases.IsMenuIconEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsSnowEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsUkraineEnabledUseCase
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.device.repository.PermissionRepository
import tmg.flashback.device.usecases.OpenPlayStoreUseCase
import tmg.flashback.eastereggs.usecases.IsSummerEnabledUseCase
import tmg.flashback.maintenance.contract.usecases.ShouldSoftUpgradeUseCase
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.testutils.BaseTest

internal class DashboardViewModelTest: BaseTest() {

    private val mockStyleManager: StyleManager = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationsRepository = mockk(relaxed = true)
    private val mockGetSoftUpgradeUseCase: ShouldSoftUpgradeUseCase = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockOpenPlayStoreUseCase: OpenPlayStoreUseCase = mockk(relaxed = true)
    private val mockIsSnowEnabledUseCase: IsSnowEnabledUseCase = mockk(relaxed = true)
    private val mockIsSummerEnabledUseCase: IsSummerEnabledUseCase = mockk(relaxed = true)
    private val mockIsMenuIconEnabledUseCase: IsMenuIconEnabledUseCase = mockk(relaxed = true)
    private val mockIsUkraineEnabledUseCase: IsUkraineEnabledUseCase = mockk(relaxed = true)

    private lateinit var underTest: DashboardViewModel

    private fun initUnderTest() {
        underTest = DashboardViewModel(
            styleManager = mockStyleManager,
            changeNightModeUseCase = mockChangeNightModeUseCase,
            buildConfigManager = mockBuildConfigManager,
            notificationRepository = mockNotificationRepository,
            getSoftUpgradeUseCase = mockGetSoftUpgradeUseCase,
            permissionRepository = mockPermissionRepository,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            permissionManager = mockPermissionManager,
            openPlayStoreUseCase = mockOpenPlayStoreUseCase,
            isSnowEnabledUseCase = mockIsSnowEnabledUseCase,
            isSummerEnabledUseCase = mockIsSummerEnabledUseCase,
            isMenuIconEnabledUseCase = mockIsMenuIconEnabledUseCase,
            isUkraineEnabledUseCase = mockIsUkraineEnabledUseCase,
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns false
        every { mockNotificationRepository.seenRuntimeNotifications } returns false
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false
        every { mockStyleManager.isDayMode } returns false
    }

    @Test
    fun `dark mode result is read`() = runTest(testDispatcher) {
        every { mockStyleManager.isDayMode } returns true

        initUnderTest()
        underTest.outputs.isDarkMode.test {
            assertEquals(false, awaitItem())
        }
    }

    @ParameterizedTest(name = "Clicking dark mode to state {0} sets dark mode {1}")
    @CsvSource(
        "true,NIGHT",
        "false,DAY"
    )
    fun `clicking dark mode calls use case`(state: Boolean, nightMode: NightMode) = runTest(testDispatcher) {
        every { mockStyleManager.isDayMode } returns !state
        initUnderTest()

        underTest.inputs.clickDarkMode(state)
        underTest.outputs.isDarkMode.test {
            assertEquals(state, awaitItem())
        }
        verify {
            mockChangeNightModeUseCase.setNightMode(nightMode)
        }
    }

    @Test
    fun `app version is read from build config manager`() = runTest(testDispatcher) {
        every { mockBuildConfigManager.versionName } returns "version-name"

        initUnderTest()
        underTest.outputs.appVersion.test {
            assertEquals("version-name", awaitItem())
        }
    }

    @Test
    fun `snow easter egg is emitted from use case`() = runTest(testDispatcher) {
        every { mockIsSnowEnabledUseCase.invoke() } returns true

        initUnderTest()
        underTest.outputs.snow.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `summer easter egg is emitted from use case`() = runTest(testDispatcher) {
        every { mockIsSummerEnabledUseCase.invoke() } returns true

        initUnderTest()
        underTest.outputs.summer.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `ukraine easter egg is emitted from use case`() = runTest(testDispatcher) {
        every { mockIsUkraineEnabledUseCase.invoke() } returns true

        initUnderTest()
        underTest.outputs.ukraine.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `menu title is emitted from use case`() = runTest(testDispatcher) {
        every { mockIsMenuIconEnabledUseCase.invoke() } returns MenuIcons.CHRISTMAS

        initUnderTest()
        underTest.outputs.titleIcon.test {
            assertEquals(MenuIcons.CHRISTMAS, awaitItem())
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
        ) = runTest(testDispatcher) {
            every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns isRuntimeNotificationsSupported
            every { mockNotificationRepository.seenRuntimeNotifications } returns seenRuntimeNotifications
            every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns isRuntimeNotificationsEnabled

            initUnderTest()
            underTest.featurePromptsList.test {
                val item = awaitItem()
                if (isFeatureIncluded) {
                    assertTrue(item.any { it is FeaturePrompt.RuntimeNotifications  })
                } else {
                    assertTrue(item.none { it is FeaturePrompt.RuntimeNotifications  })
                }
            }
        }

        @Test
        fun `soft upgrade`() = runTest(testDispatcher) {
            every { mockGetSoftUpgradeUseCase.shouldSoftUpgrade() } returns true

            initUnderTest()
            underTest.featurePromptsList.test {
                val item = awaitItem()
                assertTrue(item.any { it is FeaturePrompt.SoftUpgrade })
            }
        }

        @Test
        fun `clicking soft upgrade`() = runTest(testDispatcher) {
            every { mockGetSoftUpgradeUseCase.shouldSoftUpgrade() } returns true

            initUnderTest()
            underTest.inputs.clickFeaturePrompt(FeaturePrompt.SoftUpgrade)

            verify {
                mockOpenPlayStoreUseCase.openPlaystore()
            }
        }

        @Test
        fun `clicking runtime notifications`() = runTest(testDispatcher) {
            val permissions: CompletableDeferred<Map<String, Boolean>> = CompletableDeferred()
            every { mockPermissionManager.requestPermission(AppPermissions.RuntimeNotifications) } returns permissions
            every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns true
            every { mockNotificationRepository.seenRuntimeNotifications } returns false
            every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

            initUnderTest()
            underTest.outputs.featurePromptsList.test {
                assertEquals(listOf(FeaturePrompt.RuntimeNotifications), awaitItem())
            }

            underTest.inputs.clickFeaturePrompt(FeaturePrompt.RuntimeNotifications)
            // Simulate ticking it
            every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true
            every { mockNotificationRepository.seenRuntimeNotifications } returns true
            permissions.complete(mapOf("" to true))

            verify {
                mockNotificationRepository.seenRuntimeNotifications = true
                mockApplicationNavigationComponent.appSettingsNotifications()
            }
            underTest.outputs.featurePromptsList.test {
                assertEquals(listOf<FeaturePrompt>(), awaitItem())
            }
        }
    }
}