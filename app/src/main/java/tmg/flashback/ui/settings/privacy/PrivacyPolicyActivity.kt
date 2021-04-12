package tmg.flashback.ui.settings.privacy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityPrivacyPolicyBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyActivity: BaseActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding
    private val viewModel: PrivacyPolicyViewModel by viewModel()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "Privacy policy"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.text = getString(R.string.privacy_policy_title)

        binding.tvPolicy.text = getString(R.string.privacy_policy_data).fromHtml()
        binding.tvPolicy.movementMethod = LinkMovementMethod.getInstance()

        binding.back.setOnClickListener {
            viewModel.inputs.clickBack()
        }

        observeEvent(viewModel.outputs.goBack) {
            onBackPressed()
        }
    }
}