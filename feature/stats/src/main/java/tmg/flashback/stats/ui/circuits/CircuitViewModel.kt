package tmg.flashback.stats.ui.circuits

import androidx.lifecycle.ViewModel

interface CircuitViewModelInputs {
    fun load(circuitId: String)
}

interface CircuitViewModelOutputs {

}

class CircuitViewModel: ViewModel(), CircuitViewModelInputs, CircuitViewModelOutputs {

    val inputs: CircuitViewModelInputs = this
    val outputs: CircuitViewModelOutputs = this

    override fun load(circuitId: String) {

    }
}