package tmg.flashback.ui.admin.maintenance

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.buildconfig.BuildConfigManager
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.models.AppLockout
import tmg.flashback.testutils.*
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventNotFired
import tmg.flashback.testutils.test

internal class MaintenanceViewModelTest : BaseTest() {

    lateinit var sut: MaintenanceViewModel

    private val mockDataRepository: DataRepository = mockk(relaxed = true)
    private val mockBuildConfigProvider: tmg.flashback.device.buildconfig.BuildConfigManager = mockk(relaxed = true)

    private val mockTitle: String = "mock title"
    private val mockMessage: String = "mock description"
    private val mockLinkText: String = "mock link"
    private val mockLink: String = "http://www.mocklinks.com"
    private val mockAppVersion: Int = 10
    private val mockAppLockoutWithLink = AppLockout(
        show = true,
        title = mockTitle,
        message = mockMessage,
        linkText = mockLinkText,
        link = mockLink,
        version = Int.MAX_VALUE
    )
    private val mockAppLockoutWithoutLink = mockAppLockoutWithLink.copy(
        linkText = null,
        link = null
    )
    private val mockAppLockoutShowFalse = mockAppLockoutWithLink.copy(
        show = false
    )
    private val mockAppLockoutVersionHigherThanCurrent = mockAppLockoutWithLink.copy(
        show = true,
        version = mockAppVersion + 1
    )
    private val mockAppLockoutVersionEqualToCurrent = mockAppLockoutWithLink.copy(
        show = true,
        version = mockAppVersion
    )
    private val mockAppLockoutVersionLessThanCurrent = mockAppLockoutWithLink.copy(
        show = true,
        version = mockAppVersion - 1
    )
    private val mockAppLockoutVersionNull = mockAppLockoutWithLink.copy(
        show = true,
        version = null
    )

    private val flowAppLockoutEmpty: Flow<AppLockout?> = flow { emit(null) }

    @BeforeEach
    internal fun setUp() {

        every { mockDataRepository.appLockout() } returns flow { emit(mockAppLockoutVersionEqualToCurrent) }
        every { mockBuildConfigProvider.versionCode } returns mockAppVersion
        every { mockBuildConfigProvider.shouldLockoutBasedOnVersion(version = any()) } returns true
    }

    private fun initSUT() {

        sut = MaintenanceViewModel(mockDataRepository, mockBuildConfigProvider)
    }


    @Test
    fun `app lockout shown when show is true and lockout version is higher than current app version`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(mockAppLockoutVersionHigherThanCurrent) }
        every { mockBuildConfigProvider.shouldLockoutBasedOnVersion(version = any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `app lockout shown when show is true and lockout version is equal to current app version`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(mockAppLockoutVersionEqualToCurrent) }
        every { mockBuildConfigProvider.shouldLockoutBasedOnVersion(version = any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `app lockout not shown when show is true and lockout version is lower than current app version`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(mockAppLockoutVersionLessThanCurrent) }
        every { mockBuildConfigProvider.shouldLockoutBasedOnVersion(version = any()) } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventFired()
        }
    }

    @Test
    fun `app lockout not shown when show is true and version is null`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(mockAppLockoutVersionNull) }
        every { mockBuildConfigProvider.shouldLockoutBasedOnVersion(version = any()) } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventFired()
        }
    }

    @Test
    fun `app lockout not shown when show is false`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(mockAppLockoutShowFalse) }
        every { mockBuildConfigProvider.shouldLockoutBasedOnVersion(version = any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventFired()
        }
    }


    @Test
    fun `app lockout title and message displayed on lockout startup`() = coroutineTest {

        val expected: Pair<String, String> = Pair(mockTitle, mockMessage)

        initSUT()
        advanceUntilIdle()

        sut.outputs.data.test {
            assertValue(expected)
        }
    }



    @Test
    fun `app lockout link shown when link text and link url is visible`() = coroutineTest {

        val expected: Pair<String, String> = Pair(mockLinkText, mockLink)

        initSUT()
        advanceUntilIdle()

        sut.outputs.showLink.test {
            assertValue(expected)
        }
    }

    @Test
    fun `app lockout link empty (not shown) when link text or link url is not included`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(mockAppLockoutWithoutLink) }

        val expected: Pair<String, String> = Pair("", "")

        initSUT()
        advanceUntilIdle()

        sut.outputs.showLink.test {
            assertValue(expected)
        }
    }

    @Test
    fun `clicking link opens link event`() = coroutineTest {

        val expectedShowLink: Pair<String, String> = Pair(mockLinkText, mockLink)
        val expected = mockLink

        initSUT()
        advanceUntilIdle()

        sut.outputs.showLink.test {
            assertValue(expectedShowLink)
        }

        sut.inputs.clickLink()
        advanceUntilIdle()

        sut.outputs.openLinkEvent.test {
            assertDataEventValue(expected)
        }
    }
}