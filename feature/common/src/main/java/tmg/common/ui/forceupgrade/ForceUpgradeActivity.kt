package tmg.common.ui.forceupgrade

import android.os.Bundle
import tmg.common.databinding.ActivityForceUpgradeBinding
import tmg.flashback.shared.ui.base.BaseActivity
import tmg.flashback.shared.ui.model.DisplayType

class ForceUpgradeActivity: BaseActivity() {

    private lateinit var binding: ActivityForceUpgradeBinding

    override val themeType: DisplayType = DisplayType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForceUpgradeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}