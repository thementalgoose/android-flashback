package tmg.flashback.ui.dashboard

import app.cash.turbine.test
import app.cash.turbine.testIn
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
import tmg.flashback.results.contract.ResultsNavigationComponent
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
    private val mockResultsNavigationComponent: ResultsNavigationComponent = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockIsSnowEnabledUseCase: IsSnowEnabledUseCase = mockk(relaxed = true)
    private val mockIsMenuIconEnabledUseCase: IsMenuIconEnabledUseCase = mockk(relaxed = true)
    private val mockIsUkraineEnabledUseCase: IsUkraineEnabledUseCase = mockk(relaxed = true)

    private lateinit var underTest: DashboardViewModel

    private fun initUnderTest() {
        underTest = DashboardViewModel(
            styleManager = mockStyleManager,
            changeNightModeUseCase = mockChangeNightModeUseCase,
            buildConfigManager = mockBuildConfigManager,
            notificationRepository = mockNotificationRepository,
            permissionRepository = mockPermissionRepository,
            resultsNavigationComponent = mockResultsNavigationComponent,
            permissionManager = mockPermissionManager,
            isSnowEnabledUseCase = mockIsSnowEnabledUseCase,
            isMenuIconEnabledUseCase = mockIsMenuIconEnabledUseCase,
            isUkraineEnabledUseCase = mockIsUkraineEnabledUseCase
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
    fun `dark mode result is read`() = runTest {
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
    fun `clicking dark mode calls use case`(state: Boolean, nightMode: NightMode) = runTest {
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
    fun `app version is read from build config manager`() = runTest {
        every { mockBuildConfigManager.versionName } returns "version-name"

        initUnderTest()
        underTest.outputs.appVersion.test {
            assertEquals("version-name", awaitItem())
        }
    }

    @Test
    fun `snow easter egg is emitted from use case`() = runTest {
        every { mockIsSnowEnabledUseCase.invoke() } returns true

        initUnderTest()
        underTest.outputs.snow.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `ukraine easter egg is emitted from use case`() = runTest {
        every { mockIsUkraineEnabledUseCase.invoke() } returns true

        initUnderTest()
        underTest.outputs.ukraine.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `menu title is emitted from use case`() = runTest {
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
        ) = runTest {
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
        ) = runTest {
            every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns isRuntimeNotificationsSupported
            every { mockNotificationRepository.seenNotificationOnboarding } returns seenNotificationOnboarding

            initUnderTest()
            underTest.featurePromptsList.test {
                val item = awaitItem()
                if (isFeatureIncluded) {
                    assertTrue(item.any { it is FeaturePrompt.Notifications })
                } else {
                    assertTrue(item.none { it is FeaturePrompt.Notifications })
                }
            }
        }

        @Test
        fun `clicking notifications`() = runTest {
            initUnderTest()
            underTest.outputs.featurePromptsList.test {
                assertEquals(listOf(FeaturePrompt.Notifications), awaitItem())
            }

            every { mockNotificationRepository.seenNotificationOnboarding } returns true
            underTest.inputs.clickFeaturePrompt(FeaturePrompt.Notifications)

            verify {
                mockResultsNavigationComponent.featureNotificationOnboarding()
                mockNotificationRepository.seenNotificationOnboarding = true
            }
            underTest.outputs.featurePromptsList.test {
                assertEquals(listOf<FeaturePrompt>(), awaitItem())
            }
        }

        @Test
        fun `clicking runtime notifications`() = runTest {
            val permissions: CompletableDeferred<Boolean> = CompletableDeferred()
            every { mockPermissionManager.requestPermission(RationaleType.RuntimeNotifications) } returns permissions
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
            permissions.complete(true)

            verify {
                mockNotificationRepository.seenRuntimeNotifications = true
                mockResultsNavigationComponent.featureNotificationOnboarding()
            }
            underTest.outputs.featurePromptsList.test {
                assertEquals(listOf<FeaturePrompt>(), awaitItem())
            }
        }
    }
}