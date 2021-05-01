package tmg.flashback.ui.admin.forceupgrade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.configuration.controllers.ConfigController
import tmg.flashback.core.ui.BaseViewModel
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface ForceUpgradeViewModelInputs {
    fun clickLink()
}

//endregion

//region Outputs

interface ForceUpgradeViewModelOutputs {
    val data: LiveData<Pair<String, String>> // title, message
    val showLink: LiveData<Pair<String, String>?> // linkText, link

    val openLinkEvent: LiveData<DataEvent<String>>
}

//endregion

class ForceUpgradeViewModel(
    private val configurationController: ConfigController
): BaseViewModel(), ForceUpgradeViewModelInputs, ForceUpgradeViewModelOutputs {

    var inputs: ForceUpgradeViewModelInputs = this
    var outputs: ForceUpgradeViewModelOutputs = this

    override val data: MutableLiveData<Pair<String, String>> = MutableLiveData()
    override val showLink: MutableLiveData<Pair<String, String>?> = MutableLiveData()

    override val openLinkEvent: MutableLiveData<DataEvent<String>> = MutableLiveData()

    init {
        configurationController.forceUpgrade?.let {
            data.value = Pair(it.title, it.message)
            showLink.value = it.link
        }
        if (configurationController.forceUpgrade == null) {
            data.value = Pair("Error :(", "Please restart the app")
            showLink.value = null
        }

        viewModelScope.launch {
            configurationController.fetch()
        }
    }

    //region Inputs

    override fun clickLink() {
        configurationController.forceUpgrade?.link?.let { (_, link) ->
            openLinkEvent.value = DataEvent(link)
        }
    }

    //endregion

    //region Outputs

    //endregion
}
