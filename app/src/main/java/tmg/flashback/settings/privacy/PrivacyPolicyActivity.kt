package tmg.flashback.settings.privacy

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_privacy_policy.header
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyActivity: BaseActivity() {

    private val viewModel: PrivacyPolicyViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_privacy_policy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = getString(R.string.privacy_policy_title)

        tvPolicy.text = getString(R.string.privacy_policy_data).fromHtml()

        back.setOnClickListener {
            viewModel.inputs.clickBack()
        }

        observeEvent(viewModel.outputs.goBack) {
            onBackPressed()
        }
    }
}