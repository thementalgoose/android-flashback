package tmg.common.ui.privacypolicy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface PrivacyPolicyViewModelInputs {
    fun clickBack()
}

//endregion

//region Outputs

interface PrivacyPolicyViewModelOutputs {
    val goBack: LiveData<Event>
}

//endregion


class PrivacyPolicyViewModel: ViewModel(), PrivacyPolicyViewModelInputs,
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
