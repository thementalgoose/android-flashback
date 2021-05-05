package tmg.flashback.statistics.ui.dashboard

import android.os.Bundle
import org.koin.android.ext.android.inject
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.shared.ui.base.BaseActivity
import tmg.flashback.shared.ui.model.DisplayType
import tmg.flashback.statistics.databinding.ActivityDashboardBinding

class DashboardActivity: BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val navigationManager: NavigationManager by inject()

    override val swipeDismissInitialise: Boolean = false
    override val themeType: DisplayType = DisplayType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationManager.openContextualReleaseNotes(this)
    }
}