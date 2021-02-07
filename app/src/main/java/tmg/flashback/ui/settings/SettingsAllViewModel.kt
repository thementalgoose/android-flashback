package tmg.flashback.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.core.ui.BaseViewModel

//region Inputs

interface SettingsAllViewModelInputs {
    fun clickCategory(category: Category)
}

//endregion

//region Outputs

interface SettingsAllViewModelOutputs {
    val categories: LiveData<List<Category>>
}

//endregion

class SettingsAllViewModel: BaseViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    var inputs: SettingsAllViewModelInputs = this
    var outputs: SettingsAllViewModelOutputs = this

    override val categories: MutableLiveData<List<Category>> = MutableLiveData()

    init {
        categories.value = Category.values().toList()
    }

    //region Inputs

    override fun clickCategory(category: Category) {
        println("CATEGORY $category")
    }

    //endregion

    //region Outputs

    //endregion
}
