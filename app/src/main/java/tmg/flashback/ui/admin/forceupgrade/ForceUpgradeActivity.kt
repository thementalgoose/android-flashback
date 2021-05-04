package tmg.flashback.ui.admin.forceupgrade

import android.os.Bundle
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityForceUpgradeBinding

class ForceUpgradeActivity: BaseActivity() {

    private lateinit var binding: ActivityForceUpgradeBinding

    override val themeType: DisplayType = DisplayType.DEFAULT
    override val screenAnalytics: ScreenAnalytics? = null // Opt out of analytics in activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForceUpgradeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}