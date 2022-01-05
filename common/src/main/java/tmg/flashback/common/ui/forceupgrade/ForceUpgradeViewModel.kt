package tmg.flashback.common.ui.forceupgrade

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.flashback.common.BuildConfig
import tmg.flashback.common.controllers.ForceUpgradeController
import tmg.flashback.configuration.controllers.ConfigController
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
    private val forceUpgradeController: ForceUpgradeController,
    private val configController: ConfigController
): ViewModel(), ForceUpgradeViewModelInputs, ForceUpgradeViewModelOutputs {

    var inputs: ForceUpgradeViewModelInputs = this
    var outputs: ForceUpgradeViewModelOutputs = this

    override val data: MutableLiveData<Pair<String, String>> = MutableLiveData()
    override val showLink: MutableLiveData<Pair<String, String>?> = MutableLiveData()

    override val openLinkEvent: MutableLiveData<DataEvent<String>> = MutableLiveData()

    init {
        forceUpgradeController.forceUpgrade?.let {
            data.value = Pair(it.title, it.message)
            showLink.value = it.link
        }
        if (forceUpgradeController.forceUpgrade == null) {
            data.value = Pair("Error :(", "Please restart the app")
            showLink.value = null
        }

        viewModelScope.launch {
            val result = configController.fetchAndApply()
            if (BuildConfig.DEBUG) {
                Log.i("Force Upgrade", "Force Upgrade fetch and apply $result")
            }
        }
    }

    //region Inputs

    override fun clickLink() {
        forceUpgradeController.forceUpgrade?.link?.let { (_, link) ->
            openLinkEvent.value = DataEvent(link)
        }
    }

    //endregion

    //region Outputs

    //endregion
}
