package tmg.flashback.statistics.ui.circuit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tmg.core.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityCircuitInfoBinding
import tmg.utilities.extensions.loadFragment

class CircuitInfoActivity: BaseActivity() {

    private lateinit var binding: ActivityCircuitInfoBinding

    private lateinit var circuitId: String
    private lateinit var circuitName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircuitInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            circuitId = it.getString(keyCircuit)!!
            circuitName = it.getString(keyCircuitName)!!
        }

        loadFragment(CircuitInfoFragment.instance(circuitId, circuitName), R.id.container)
    }

    companion object {

        private const val keyCircuit: String = "circuit"
        private const val keyCircuitName: String = "circuitName"

        fun intent(context: Context, circuitId: String, circuitName: String): Intent {
            val intent = Intent(context, CircuitInfoActivity::class.java)
            intent.putExtra(keyCircuit, circuitId)
            intent.putExtra(keyCircuitName, circuitName)
            return intent
        }
    }
}