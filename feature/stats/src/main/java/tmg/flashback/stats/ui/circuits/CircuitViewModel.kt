package tmg.flashback.stats.ui.circuits

import androidx.lifecycle.ViewModel

interface CircuitViewModelInputs {

}

interface CircuitViewModelOutputs {

}

class CircuitViewModel: ViewModel(), CircuitViewModelInputs, CircuitViewModelOutputs {

    val inputs: CircuitViewModelInputs = this
    val outputs: CircuitViewModelOutputs = this

}