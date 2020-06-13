package tmg.flashback.circuit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.models.stats.Circuit

//region Inputs

interface CircuitInfoViewModelInputs {
    fun circuitId(circuitId: String)
}

//endregion

//region Outputs

interface CircuitInfoViewModelOutputs {
    val circuitInfo: LiveData<Circuit>
}

//endregion

class CircuitInfoViewModel: BaseViewModel(), CircuitInfoViewModelInputs, CircuitInfoViewModelOutputs {

    private lateinit var circuitId: String

    override val circuitInfo: MutableLiveData<Circuit> = MutableLiveData()

    var inputs: CircuitInfoViewModelInputs = this
    var outputs: CircuitInfoViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun circuitId(circuitId: String) {
        this.circuitId = circuitId
    }

    //endregion

    //region Outputs

    //endregion
}