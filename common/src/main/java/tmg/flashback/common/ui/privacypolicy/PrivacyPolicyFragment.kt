package tmg.flashback.common.ui.privacypolicy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.common.R
import tmg.flashback.common.databinding.FragmentPrivacyPolicyBinding
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyFragment: BaseFragment<FragmentPrivacyPolicyBinding>() {

    private val viewModel: PrivacyPolicyViewModel by viewModel()

    override fun inflateView(inflater: LayoutInflater) = FragmentPrivacyPolicyBinding
        .inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Privacy Policy")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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