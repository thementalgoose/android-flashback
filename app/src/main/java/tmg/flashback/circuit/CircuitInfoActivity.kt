package tmg.flashback.circuit

import android.widget.Toast
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.utils.observe

class CircuitInfoActivity: BaseActivity() {

    private val viewModel: CircuitInfoViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_circuit_info

    override fun initViews() {
        observe(viewModel.outputs.circuitInfo) {
            Toast.makeText(this, "CIRCUIT INFO ${it.id}", Toast.LENGTH_LONG).show()
        }
    }
}