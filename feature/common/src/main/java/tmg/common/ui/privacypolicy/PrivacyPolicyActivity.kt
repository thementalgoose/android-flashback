package tmg.common.ui.privacypolicy

import android.os.Bundle
import tmg.common.databinding.ActivityPrivacyPolicyBinding
import tmg.flashback.ui.base.BaseActivity

class PrivacyPolicyActivity: BaseActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}