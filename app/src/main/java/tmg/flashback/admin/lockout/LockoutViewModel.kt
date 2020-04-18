package tmg.flashback.admin.lockout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.DataDB
import tmg.flashback.utils.DataEvent

//region Inputs

interface LockoutViewModelInputs {
    fun clickLink()
}

//endregion

//region Outputs

interface LockoutViewModelOutputs {
    val data: MutableLiveData<String>
    val showLink: MutableLiveData<String>

    val openLinkEvent: MutableLiveData<DataEvent<String>>
}

//endregion

class LockoutViewModel(
    private val dataDB: DataDB
): BaseViewModel(), LockoutViewModelInputs, LockoutViewModelOutputs {

    override val data: MutableLiveData<String> = MutableLiveData()
    override val showLink: MutableLiveData<String> = MutableLiveData()

    private var link: String = ""

    override val openLinkEvent: MutableLiveData<DataEvent<String>> = MutableLiveData()

    var inputs: LockoutViewModelInputs = this
    var outputs: LockoutViewModelOutputs = this

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataDB.appLockout().collect {
                data.postValue(it.message)
                showLink.postValue(it.linkText ?: "")
                link = it.link ?: ""
            }
        }
    }

    //region Inputs

    override fun clickLink() {
        openLinkEvent.postValue(DataEvent(link))
    }

    //endregion
}