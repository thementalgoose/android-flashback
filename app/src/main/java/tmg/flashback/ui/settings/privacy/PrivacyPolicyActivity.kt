package tmg.flashback.ui.settings.privacy

import android.os.Bundle
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity: BaseActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override val screenAnalytics: ScreenAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}