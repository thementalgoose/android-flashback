package tmg.flashback.ui.dashboard

import android.os.Bundle
import tmg.core.ui.base.BaseActivity
import tmg.core.ui.model.DisplayType
import tmg.flashback.databinding.ActivityDashboardBinding

class DashboardActivity: BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override val swipeDismissInitialise: Boolean = false
    override val themeType: DisplayType = DisplayType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}