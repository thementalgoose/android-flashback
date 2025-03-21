package tmg.flashback.presentation.settings.widgets

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.usecases.RefreshWidgetsUseCase
import tmg.flashback.widgets.upnext.repository.UpNextWidgetRepository
import tmg.testutils.BaseTest

class SettingsWidgetViewModelTest: BaseTest() {

    private val mockUpNextWidgetRepository: UpNextWidgetRepository = mockk(relaxed = true)
    private val mockRefreshWidgetsUseCase: RefreshWidgetsUseCase = mockk(relaxed = true)
    private val mockToastManager: ToastManager = mockk(relaxed = true)

    private lateinit var underTest: SettingsWidgetViewModel

    private fun initUnderTest() {
        underTest = SettingsWidgetViewModel(
            upNextWidgetRepository = mockUpNextWidgetRepository,
            refreshWidgetsUseCase = mockRefreshWidgetsUseCase,
            toastManager = mockToastManager
        )
    }

    @Test
    fun `show background is true when pref is true`() = runTest(testDispatcher) {
        every { mockUpNextWidgetRepository.showBackground } returns true

        initUnderTest()
        underTest.outputs.showBackground.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `show background is false when pref is false`() = runTest(testDispatcher) {
        every { mockUpNextWidgetRepository.showBackground } returns false

        initUnderTest()
        underTest.outputs.showBackground.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `refreshing widgets calls use case`() {
        initUnderTest()

        underTest.refreshWidgets()

        verify {
            mockRefreshWidgetsUseCase.update()
        }
    }

    @Test
    fun `click show background updates pref and updates value`() = runTest(testDispatcher) {
        every { mockUpNextWidgetRepository.showBackground } returns false

        initUnderTest()
        underTest.outputs.showBackground.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Widgets.showBackground(true))

        verify {
            mockUpNextWidgetRepository.showBackground = true
        }
        underTest.outputs.showBackground.test { awaitItem() }
    }

    @Test
    fun `deeplink event is true when pref is true`() = runTest(testDispatcher) {
        every { mockUpNextWidgetRepository.deeplinkToEvent } returns true

        initUnderTest()
        underTest.outputs.deeplinkToEvent.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `deeplink event is false when pref is false`() = runTest(testDispatcher) {
        every { mockUpNextWidgetRepository.deeplinkToEvent } returns false

        initUnderTest()
        underTest.outputs.deeplinkToEvent.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show deeplink event updates pref and updates value`() = runTest(testDispatcher) {
        every { mockUpNextWidgetRepository.deeplinkToEvent } returns false

        initUnderTest()
        underTest.outputs.deeplinkToEvent.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Widgets.deeplinkToEvent(true))

        verify {
            mockUpNextWidgetRepository.deeplinkToEvent = true
        }
        underTest.outputs.deeplinkToEvent.test { awaitItem() }
    }

    @Test
    fun `click refresh widgets`() = runTest(testDispatcher) {
        initUnderTest()

        underTest.inputs.prefClicked(Settings.Widgets.refreshWidgets)

        verify {
            mockRefreshWidgetsUseCase.update()
            mockToastManager.displayToast(any())
        }
    }
}