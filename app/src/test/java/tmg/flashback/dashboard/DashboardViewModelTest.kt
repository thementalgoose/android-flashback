package tmg.flashback.dashboard

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import tmg.flashback.di.device.BuildConfigManager
import tmg.flashback.repo.db.DataRepository
import tmg.flashback.repo.models.remoteconfig.AppLockout
import tmg.flashback.repo.pref.PrefDeviceRepository
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.assertEventNotFired
import tmg.flashback.testutils.test

internal class DashboardViewModelTest: BaseTest() {

    lateinit var sut: DashboardViewModel

    private val mockDataRepository: DataRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockPrefDeviceRepository: PrefDeviceRepository = mockk(relaxed = true)

    private fun initSUT() {
        sut = DashboardViewModel(
            mockDataRepository,
            mockBuildConfigManager,
            mockPrefDeviceRepository
        )
    }

    //region Release Notes

    @Test
    fun `DashboardViewModel open release notes fires when release notes are different`() {

        every { mockPrefDeviceRepository.shouldShowReleaseNotes } returns true

        initSUT()

        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `DashboardViewModel open release notes doesnt fire when no release notes difference`() {

        every { mockPrefDeviceRepository.shouldShowReleaseNotes } returns false

        initSUT()

        sut.outputs.openReleaseNotes.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region App Lockout

    @Test
    fun `DashboardViewModel app lockout event is fired if show is true and build config provider says version is should lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventFired()
        }
    }

    @Test
    fun `DashboardViewModel app lockout event is not fired if show is false and build config provider says version is should lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout.copy(show = false)) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `DashboardViewModel app lockout event is not fired if show is true and build config provider says version is should not lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout.copy()) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `DashboardViewModel app lockout event is not fired if app lockout value is null`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(null) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
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