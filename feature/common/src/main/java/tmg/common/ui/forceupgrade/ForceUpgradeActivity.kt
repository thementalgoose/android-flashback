package tmg.common.ui.forceupgrade

import android.os.Bundle
import tmg.common.databinding.ActivityLockoutBinding
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.model.DisplayType

class ForceUpgradeActivity: BaseActivity() {

    private lateinit var binding: ActivityLockoutBinding

    override val themeType: DisplayType = DisplayType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}