package tmg.flashback.settings.privacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.ScopeProvider
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


class PrivacyPolicyViewModel(
        scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), PrivacyPolicyViewModelInputs, PrivacyPolicyViewModelOutputs {

    var inputs: PrivacyPolicyViewModelInputs = this
    var outputs: PrivacyPolicyViewModelOutputs = this

    override val goBack: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun clickBack() {
        goBack.value = Event()
    }

    //endregion
}
