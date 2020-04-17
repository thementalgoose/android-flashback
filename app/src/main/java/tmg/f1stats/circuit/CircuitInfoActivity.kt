package tmg.f1stats.circuit

import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity

class CircuitInfoActivity: BaseActivity() {

    private val viewModel: CircuitInfoViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_circuit_info

    override fun observeViewModel() {

        viewModel.outputs
            .circuitInfo()

    }

}