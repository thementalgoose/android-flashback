package tmg.flashback.statistics.ui.overview.constructor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityConstructorBinding
import tmg.flashback.statistics.ui.overview.constructor.stats.ConstructorFragment
import tmg.flashback.statistics.ui.overview.driver.stats.DriverFragment
import tmg.utilities.extensions.loadFragment

class ConstructorActivity: BaseActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityConstructorBinding

    private var navController: NavController? = null

    private lateinit var constructorName: String
    private lateinit var constructorId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstructorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            constructorId = it.getString(keyConstructorId)!!
            constructorName = it.getString(keyConstructorName)!!
        }

        val navFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navFragment.navController
        val graph = navController!!.navInflater.inflate(R.navigation.nav_constructors)
        val bundle = ConstructorFragment.bundle(constructorId, constructorName)

        navFragment.navController.setGraph(graph, bundle)

        navFragment.navController.addOnDestinationChangedListener(this)

        binding.titleExpanded.text = constructorName
        binding.titleCollapsed.text = constructorName

        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (navController?.popBackStack() != true) {
            super.onBackPressed()
        }
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.constructorFragment -> {
                val constructorName = arguments?.get("constructorId")?.toString() ?: ""
                binding.titleExpanded.text = constructorName
                binding.titleCollapsed.text = constructorName
            }
        }
    }

    companion object {
        private const val keyConstructorId: String = "constructorId"
        private const val keyConstructorName: String = "constructorName"

        fun intent(context: Context, constructorId: String, constructorName: String): Intent {
            val intent = Intent(context, ConstructorActivity::class.java)
            intent.putExtra(keyConstructorId, constructorId)
            intent.putExtra(keyConstructorName, constructorName)
            return intent
        }
    }
}