package tmg.f1stats.circuit

import android.widget.Toast
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.utils.observe

class CircuitInfoActivity: BaseActivity() {

    private val viewModel: CircuitInfoViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_circuit_info

    override fun initViews() {
        observe(viewModel.outputs.circuitInfo) {
            Toast.makeText(this, "CIRCUIT INFO ${it.id}", Toast.LENGTH_LONG).show()
        }
    }
}