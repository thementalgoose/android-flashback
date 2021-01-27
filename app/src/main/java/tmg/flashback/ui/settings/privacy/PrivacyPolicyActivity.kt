package tmg.flashback.ui.settings.privacy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_privacy_policy.header
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyActivity: BaseActivity() {

    private val viewModel: PrivacyPolicyViewModel by viewModel()

    override val analyticsScreenName: String
        get() = "Privacy policy"

    override fun layoutId(): Int = R.layout.activity_privacy_policy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = getString(R.string.privacy_policy_title)

        tvPolicy.text = getString(R.string.privacy_policy_data).fromHtml()
        tvPolicy.movementMethod = LinkMovementMethod.getInstance()

        back.setOnClickListener {
            viewModel.inputs.clickBack()
        }

        observeEvent(viewModel.outputs.goBack) {
            onBackPressed()
        }
    }
}