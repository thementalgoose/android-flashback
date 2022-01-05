package tmg.flashback.common.ui.forceupgrade

import android.os.Bundle
import org.koin.android.ext.android.inject
import tmg.flashback.common.databinding.ActivityLockoutBinding
import tmg.flashback.configuration.controllers.ConfigController
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

    override fun onStop() {
        super.onStop()
        finish()
    }
}