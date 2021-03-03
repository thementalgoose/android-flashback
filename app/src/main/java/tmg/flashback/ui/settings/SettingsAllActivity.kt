package tmg.flashback.statistics.ui.settings

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_settings.back
import tmg.flashback.R
import tmg.flashback.core.ui.BaseActivity

class SettingsAllActivity: BaseActivity(), NavController.OnDestinationChangedListener {

    override fun layoutId() = R.layout.activity_settings

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        navController = navHostFragment.navController

        back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        navController?.addOnDestinationChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        navController?.removeOnDestinationChangedListener(this)
    }

    //region OnDestinationChanged

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        swipeDismissLock = when (destination.id) {
            R.id.settingsFragment -> {
                false
            }
            else -> {
                true
            }
        }
    }

    //endregion
}