package tmg.flashback.ui.settings.privacy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.constants.ViewType
import tmg.flashback.shared.ui.base.BaseFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.FragmentPrivacyPolicyBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyFragment: BaseFragment<FragmentPrivacyPolicyBinding>() {

    private val viewModel: PrivacyPolicyViewModel by viewModel()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "Privacy Policy"
    )

    override fun inflateView(inflater: LayoutInflater) = FragmentPrivacyPolicyBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsController.logEvent(ViewType.SETTINGS_PRIVACY_POLICY)

        binding.tvPolicy.text = getString(R.string.privacy_policy_data).fromHtml()
        binding.tvPolicy.movementMethod = LinkMovementMethod.getInstance()

        binding.back.setOnClickListener {
            viewModel.inputs.clickBack()
        }

        observeEvent(viewModel.outputs.goBack) {
            activity?.finish()
        }
    }
}