package tmg.flashback.statistics.ui.dashboard

import android.os.Bundle
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.statistics.databinding.ActivityDashboardBinding

class DashboardActivity: BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val navigationManager: NavigationManager by inject()

    override val swipeDismissInitialise: Boolean = false
    override val themeType: DisplayType = DisplayType.DEFAULT

    override val screenAnalytics: ScreenAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationManager.openContextualReleaseNotes(this)
    }
}