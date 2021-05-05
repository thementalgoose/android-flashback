package tmg.flashback.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.core.controllers.FeatureController
import androidx.lifecycle.ViewModel
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface SettingsAllViewModelInputs {
    fun clickCategory(category: Category)
}

//endregion

//region Outputs

interface SettingsAllViewModelOutputs {
    val categories: LiveData<List<Category>>

    val navigateToo: LiveData<DataEvent<Category>>
}

//endregion

class SettingsAllViewModel(
        private val featureController: FeatureController
): ViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    var inputs: SettingsAllViewModelInputs = this
    var outputs: SettingsAllViewModelOutputs = this

    override val categories: MutableLiveData<List<Category>> = MutableLiveData()
    override val navigateToo: MutableLiveData<DataEvent<Category>> = MutableLiveData()

    init {
        categories.value = when {
            !featureController.rssEnabled -> Category.values()
                    .filter { it != Category.RSS }
                    .toList()
            else -> Category.values().toList()
        }
    }

    //region Inputs

    override fun clickCategory(category: Category) {
        navigateToo.value = DataEvent(category)
    }

    //endregion

    //region Outputs

    //endregion
}
