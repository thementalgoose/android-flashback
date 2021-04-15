package tmg.flashback.statistics.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.discord.panels.OverlappingPanelsLayout
import com.discord.panels.OverlappingPanelsLayout.Panel.CENTER
import com.discord.panels.OverlappingPanelsLayout.Panel.END
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.controllers.FeatureController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.core.enums.DisplayType
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityDashboardBinding
import tmg.flashback.statistics.ui.dashboard.list.ListFragment
import tmg.flashback.statistics.ui.dashboard.season.SeasonFragment
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.observeEvent

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