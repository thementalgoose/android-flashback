package tmg.flashback.ui.settings.customisation.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.enums.Theme
import tmg.flashback.core.ui.BaseViewModel
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.core.utils.Selected
import tmg.flashback.core.utils.StringHolder
import tmg.flashback.core.ui.bottomsheet.BottomSheetItem
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface ThemeViewModelInputs {
    fun selectTheme(theme: Theme)
}

//endregion

//region Outputs

interface ThemeViewModelOutputs {

    val themePreferences: LiveData<List<Selected<BottomSheetItem>>>
    val themeUpdated: LiveData<DataEvent<Pair<Theme, Boolean>>>
}

//endregion

class ThemeViewModel(
        private val appearanceController: AppearanceController
): BaseViewModel(), ThemeViewModelInputs, ThemeViewModelOutputs {

    var inputs: ThemeViewModelInputs = this
    var outputs: ThemeViewModelOutputs = this

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val themeUpdated: MutableLiveData<DataEvent<Pair<Theme, Boolean>>> = MutableLiveData()

    init {

        updateThemeList()
    }

    //region Inputs

    override fun selectTheme(theme: Theme) {
        val same = appearanceController.currentTheme == theme
        appearanceController.currentTheme = theme
        updateThemeList()
        themeUpdated.value = DataEvent(Pair(theme, same))
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = Theme.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == appearanceController.currentTheme)
                }
    }
}
