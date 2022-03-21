package tmg.flashback.ui2.settings

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsViewModelTest: BaseTest() {

    private val exampleModelPref: SettingsModel.Pref = SettingsModel.Pref(
        title = 1,
        description = 2,
        onClick = {
            onClick++
        }
    )
    private val exampleModelSwitchPref: SettingsModel.SwitchPref = SettingsModel.SwitchPref(
        title = 1,
        description = 2,
        getState = {
            getState++
            true
        },
        saveState = {
            saveState++
        },
        saveStateNotification = {
            saveStateNotifications++
        }
    )
    private var onClick: Int = 0
    private var getState: Int = 0
    private var saveState: Int = 0
    private var saveStateNotifications: Int = 0

    private lateinit var sut: SettingsViewModel

    private fun initSUT() {
        sut = object : SettingsViewModel() {
            override val models: List<SettingsModel>
                get() = listOf(
                    exampleModelPref,
                    exampleModelSwitchPref
                )
        }
    }

    @Test
    fun `load settings emits settings with list initialised with`() {
        initSUT()
        sut.loadSettings()
        sut.settings.test {
            assertValue(listOf(exampleModelPref, exampleModelSwitchPref))
        }
    }

    @Test
    fun `click preference emits data event for model`() {
        initSUT()
        sut.clickPreference(exampleModelPref)
        assertEquals(1, onClick)
    }

    @Test
    fun `click switch preference emits data event for model`() {
        initSUT()
        sut.clickSwitchPreference(exampleModelSwitchPref, true)
        assertEquals(1, saveState)
        assertEquals(1, saveStateNotifications)
    }

    @Test
    fun `click switch preference updates list value`() {
        initSUT()
        val observer = sut.settings.testObserve()
        sut.clickSwitchPreference(exampleModelSwitchPref, true)
        observer.assertEmittedCount(1)
        sut.clickSwitchPreference(exampleModelSwitchPref, true)
        observer.assertEmittedCount(2)
    }

    @Test
    fun `click switch calls save state`() {
        initSUT()
        sut.clickSwitchPreference(exampleModelSwitchPref, true)
        assertEquals(1, saveState)
    }
}