package tmg.flashback.ui.settings.appearance.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import javax.inject.Inject

//region Inputs

interface ThemeViewModelInputs {
    fun selectTheme(theme: Theme)
}

//endregion

//region Outputs

interface ThemeViewModelOutputs {
    val currentlySelected: LiveData<Theme>
}

//endregion

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
): ViewModel(), ThemeViewModelInputs, ThemeViewModelOutputs {

    var inputs: ThemeViewModelInputs = this
    var outputs: ThemeViewModelOutputs = this

    override val currentlySelected: MutableLiveData<Theme> = MutableLiveData()

    init {
        currentlySelected.value = themeRepository.theme
    }

    //region Inputs

    override fun selectTheme(theme: Theme) {
        themeRepository.theme = theme
    }

    //endregion
}
