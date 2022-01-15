package tmg.flashback.statistics.ui.circuit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityCircuitInfoBinding
import tmg.flashback.ui.base.BaseActivity

class CircuitActivity: BaseActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityCircuitInfoBinding

    private var navController: NavController? = null

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

        val navFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navFragment.navController
        val graph = navController!!.navInflater.inflate(R.navigation.nav_circuits)
        val bundle = CircuitFragment.bundle(circuitId, circuitName)

        navFragment.navController.setGraph(graph, bundle)

        navFragment.navController.addOnDestinationChangedListener(this)

        binding.titleExpanded.text = circuitName
        binding.titleCollapsed.text = circuitName

        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (navController?.popBackStack() != true) {
            super.onBackPressed()
        }
    }

    companion object {

        private const val keyCircuit: String = "circuit"
        private const val keyCircuitName: String = "circuitName"

        fun intent(context: Context, circuitId: String, circuitName: String): Intent {
            val intent = Intent(context, CircuitActivity::class.java)
            intent.putExtra(keyCircuit, circuitId)
            intent.putExtra(keyCircuitName, circuitName)
            return intent
        }
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.circuitInfoFragment -> {
                val circuitName = arguments?.get("circuitName")?.toString() ?: ""
                binding.titleExpanded.text = circuitName
                binding.titleCollapsed.text = circuitName
            }
        }
    }
}