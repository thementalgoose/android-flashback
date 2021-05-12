package tmg.flashback.rss.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.ActivityRssSettingsBinding
import tmg.flashback.rss.ui.settings.InitialScreen.CONFIGURE
import tmg.flashback.rss.ui.settings.InitialScreen.SETTINGS
import tmg.core.ui.base.BaseActivity
import tmg.utilities.extensions.toEnum

class RSSSettingsActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityRssSettingsBinding

//    override val screenAnalytics: ScreenAnalytics? = null

    private var initialScreen: InitialScreen = SETTINGS
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRssSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialScreen = intent.extras?.let {
            it.getInt(keyInitial).toEnum<InitialScreen>() ?: SETTINGS
        } ?: SETTINGS

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        val controller = navHostFragment.findNavController().apply {
            val graph = navInflater.inflate(R.navigation.graph_rss_settings)
            graph.startDestination = initialScreen.startFragmentId
            this.graph = graph
        }
        navController = controller

        binding.back.setOnClickListener { onBackPressed() }
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

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.rssSettingsFragment -> updateTitle(R.string.settings_rss_title)
            R.id.rssSettingsConfigureFragment -> updateTitle(R.string.settings_rss_configure)
        }
        swipeDismissLock = when (initialScreen) {
            SETTINGS -> destination.id == R.id.rssSettingsConfigureFragment
            CONFIGURE -> false
        }
    }

    //endregion

    private fun updateTitle(@StringRes title: Int) {
        binding.titleCollapsed.setText(title)
        binding.titleExpanded.setText(title)
    }

    companion object {

        private const val keyInitial = "InitialScreen"

        fun intent(context: Context, initialScreen: InitialScreen = SETTINGS): Intent {
            val intent = Intent(context, RSSSettingsActivity::class.java)
            intent.putExtra(keyInitial, initialScreen.ordinal)
            return intent
        }
    }
}

enum class InitialScreen(
    @IdRes val startFragmentId: Int
) {
    SETTINGS(R.id.rssSettingsFragment),
    CONFIGURE(R.id.rssSettingsConfigureFragment);
}