package tmg.common.ui.settings.appearance.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.core.ui.controllers.ThemeController
import tmg.core.ui.extensions.icon
import tmg.core.ui.extensions.label
import tmg.core.ui.model.NightMode
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

//region Inputs

interface ThemeViewModelInputs {
    fun selectTheme(nightMode: NightMode)
}

//endregion

//region Outputs

interface ThemeViewModelOutputs {

    val themePreferences: LiveData<List<Selected<BottomSheetItem>>>
    val nightModeUpdated: LiveData<DataEvent<Pair<NightMode, Boolean>>>
}

//endregion

class ThemeViewModel(
        private val themeController: ThemeController
): ViewModel(), ThemeViewModelInputs, ThemeViewModelOutputs {

    var inputs: ThemeViewModelInputs = this
    var outputs: ThemeViewModelOutputs = this

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val nightModeUpdated: MutableLiveData<DataEvent<Pair<NightMode, Boolean>>> = MutableLiveData()

    init {

        updateThemeList()
    }

    //region Inputs

    override fun selectTheme(nightMode: NightMode) {
        val same = themeController.nightMode == nightMode
        themeController.nightMode = nightMode
        updateThemeList()
        nightModeUpdated.value = DataEvent(Pair(nightMode, same))
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = NightMode.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == themeController.nightMode)
                }
    }
}
