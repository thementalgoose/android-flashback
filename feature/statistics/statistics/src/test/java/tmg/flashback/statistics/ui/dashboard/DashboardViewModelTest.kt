package tmg.flashback.statistics.ui.dashboard

import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.device.buildconfig.BuildConfigManager
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.models.AppLockout
import tmg.flashback.statistics.testutils.BaseTest
import tmg.flashback.statistics.testutils.assertEventFired
import tmg.flashback.statistics.testutils.assertEventNotFired
import tmg.flashback.statistics.testutils.test

internal class DashboardViewModelTest: BaseTest() {

    lateinit var sut: DashboardViewModel

    private val mockContext: Context = mockk(relaxed = true)
    private val mockDataRepository: DataRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: tmg.flashback.device.buildconfig.BuildConfigManager = mockk(relaxed = true)
    private val mockConfigurationController: ConfigurationController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        coEvery { mockConfigurationController.applyPending() } returns false
    }

    private fun initSUT() {
        sut = DashboardViewModel(
            mockContext,
            mockDataRepository,
            mockBuildConfigManager,
            mockConfigurationController
        )
    }

    //region App Lockout

    @Test
    fun `app lockout event is fired if show is true and build config provider says version is should lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventFired()
        }
    }

    @Test
    fun `app lockout event is not fired if show is false and build config provider says version is should lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout.copy(show = false)) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `app lockout event is not fired if show is true and build config provider says version is should not lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout.copy()) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `app lockout event is not fired if app lockout value is null`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(null) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region Remote config fetch and sync

    @Test
    fun `init if update returns changes and activate fails nothing happens`() = coroutineTest {

        coEvery { mockConfigurationController.applyPending() } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.appConfigSynced.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `init if update returns changes and activate successfully then notify app config synced event`() = coroutineTest {

        coEvery { mockConfigurationController.applyPending() } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.appConfigSynced.test {
            assertEventFired()
        }
    }

    //endregion

    //region Mock Data - App lockout

    private val expectedAppLockout = AppLockout(
        show = true,
        title = "msg",
        message = "Another msg",
        linkText = null,
        link = null,
        version = 10
    )

    //endregion
}