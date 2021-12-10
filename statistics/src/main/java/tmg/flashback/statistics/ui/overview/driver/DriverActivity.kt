package tmg.flashback.statistics.ui.overview.driver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityDriverBinding
import tmg.flashback.statistics.ui.overview.driver.stats.DriverFragment
import tmg.flashback.ui.base.BaseActivity

class DriverActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityDriverBinding

    private lateinit var driverId: String
    private lateinit var driverName: String

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            driverId = it.getString(keyDriverId)!!
            driverName = it.getString(keyDriverName)!!
        }

        binding.titleExpanded.text = driverName
        binding.titleCollapsed.text = driverName

        val navFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navFragment.navController
        val graph = navController!!.navInflater.inflate(R.navigation.nav_drivers)
        val bundle = DriverFragment.bundle(driverId, driverName)

        navFragment.navController.setGraph(graph, bundle)

        navFragment.navController.addOnDestinationChangedListener(this)

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

        private const val keyDriverId: String = "keyDriverId"
        private const val keyDriverName: String = "keyDriverName"

        fun intent(context: Context, driverId: String, driverName: String): Intent {
            val intent = Intent(context, DriverActivity::class.java)
            intent.putExtra(keyDriverId, driverId)
            intent.putExtra(keyDriverName, driverName)
            return intent
        }
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.driverFragment -> {
                binding.titleExpanded.text = driverName
                binding.titleCollapsed.text = driverName
            }
            R.id.driverSeasonFragment -> {
                val season = arguments?.getInt("season")?.toString() ?: ""
                binding.titleExpanded.text = "$driverName\n$season"
                binding.titleCollapsed.text = "$driverName $season"
            }
        }
        binding.container.transitionToEnd()
    }
}