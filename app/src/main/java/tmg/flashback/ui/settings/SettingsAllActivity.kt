package tmg.flashback.ui.settings

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import tmg.flashback.R
import tmg.flashback.databinding.ActivitySettingsBinding
import tmg.core.ui.base.BaseActivity

class SettingsAllActivity: BaseActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivitySettingsBinding

    private var navController: NavController? = null

//    override val screenAnalytics: ScreenAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        navController = navHostFragment.navController

        binding.back.setOnClickListener {
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
        when (destination.id) {
            R.id.settingsFragment -> updateTitle(R.string.settings_title)
            R.id.settingsAppearanceFragment -> updateTitle(R.string.settings_all_appearance)
            R.id.settingsStatisticsFragment -> updateTitle(R.string.settings_all_statistics)
            R.id.rssSettingsFragment -> updateTitle(R.string.settings_rss_title)
            R.id.rssSettingsConfigureFragment -> updateTitle(R.string.settings_rss_configure)
            R.id.settingsNotificationFragment -> updateTitle(R.string.settings_all_notifications)
            R.id.settingsSupportFragment -> updateTitle(R.string.settings_all_support)
            R.id.settingsAboutFragment -> updateTitle(R.string.settings_all_about)
        }
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

    private fun updateTitle(@StringRes title: Int) {
        binding.titleCollapsed.setText(title)
        binding.titleExpanded.setText(title)
    }
}