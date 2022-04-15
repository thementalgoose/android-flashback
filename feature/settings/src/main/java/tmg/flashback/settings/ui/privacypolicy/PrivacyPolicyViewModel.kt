package tmg.flashback.settings.ui.privacypolicy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

internal interface PrivacyPolicyViewModelInputs {
    fun clickBack()
}

//endregion

//region Outputs

internal interface PrivacyPolicyViewModelOutputs {
    val goBack: LiveData<Event>
}

//endregion


internal class PrivacyPolicyViewModel: ViewModel(), PrivacyPolicyViewModelInputs,
    PrivacyPolicyViewModelOutputs {

    var inputs: PrivacyPolicyViewModelInputs = this
    var outputs: PrivacyPolicyViewModelOutputs = this

    override val goBack: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun clickBack() {
        goBack.value = Event()
    }

    //endregion
}
