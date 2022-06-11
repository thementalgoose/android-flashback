package tmg.flashback.stats.ui.circuits

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

interface CircuitViewModelInputs {
    fun load(circuitId: String)
    fun linkClicked(link: String)
}

interface CircuitViewModelOutputs {
    val list: LiveData<List<CircuitModel>>
}

class CircuitViewModel: ViewModel(), CircuitViewModelInputs, CircuitViewModelOutputs {

    val inputs: CircuitViewModelInputs = this
    val outputs: CircuitViewModelOutputs = this

    private val circuitId: MutableStateFlow

    override val list: LiveData<List<CircuitModel>> =

    override fun load(circuitId: String) {

    }

    override fun linkClicked(link: String) {

    }
}