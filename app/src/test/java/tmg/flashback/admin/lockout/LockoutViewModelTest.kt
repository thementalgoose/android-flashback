package tmg.flashback.admin.lockout

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.di.device.BuildConfigManager
import tmg.flashback.repo.db.DataRepository
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.testutils.*

class LockoutViewModelTest : BaseTest() {

    lateinit var sut: LockoutViewModel

    private val mockDataRepository: DataRepository = mock()
    private val mockBuildConfigProvider: BuildConfigManager = mock()

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

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(mockAppLockoutVersionEqualToCurrent) })
        whenever(mockBuildConfigProvider.versionCode).thenReturn(mockAppVersion)
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)
    }

    private fun initSUT() {

        sut = LockoutViewModel(mockDataRepository, mockBuildConfigProvider)
    }


    @Test
    fun `LockoutViewModel app lockout shown when show is true and lockout version is higher than current app version`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(mockAppLockoutVersionHigherThanCurrent) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `LockoutViewModel app lockout shown when show is true and lockout version is equal to current app version`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(mockAppLockoutVersionEqualToCurrent) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `LockoutViewModel app lockout not shown when show is true and lockout version is lower than current app version`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(mockAppLockoutVersionLessThanCurrent) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(false)

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventFired()
        }
    }

    @Test
    fun `LockoutViewModel app lockout not shown when show is true and version is null`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(mockAppLockoutVersionNull) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(false)

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventFired()
        }
    }

    @Test
    fun `LockoutViewModel app lockout not shown when show is false`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(mockAppLockoutShowFalse) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        sut.outputs.returnToHome.test {
            assertEventFired()
        }
    }


    @Test
    fun `LockoutViewModel app lockout title and message displayed on lockout startup`() = coroutineTest {

        val expected: Pair<String, String> = Pair(mockTitle, mockMessage)

        initSUT()
        advanceUntilIdle()

        sut.outputs.data.test {
            assertValue(expected)
        }
    }



    @Test
    fun `LockoutViewModel app lockout link shown when link text and link url is visible`() = coroutineTest {

        val expected: Pair<String, String> = Pair(mockLinkText, mockLink)

        initSUT()
        advanceUntilIdle()

        sut.outputs.showLink.test {
            assertValue(expected)
        }
    }

    @Test
    fun `LockoutViewModel app lockout link empty (not shown) when link text or link url is not included`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(mockAppLockoutWithoutLink) })

        val expected: Pair<String, String> = Pair("", "")

        initSUT()
        advanceUntilIdle()

        sut.outputs.showLink.test {
            assertValue(expected)
        }
    }



    @Test
    fun `LockoutViewModel clicking link opens link event`() = coroutineTest {

        val expected = mockLink

        initSUT()
        sut.inputs.clickLink(mockLink)
        advanceUntilIdle()

        sut.outputs.openLinkEvent.test {
            assertDataEventValue(expected)
        }
    }



    @AfterEach
    internal fun tearDown() {

        reset(mockDataRepository, mockBuildConfigProvider)
    }
}