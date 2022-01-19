package tmg.flashback.common.ui.privacypolicy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.common.R
import tmg.flashback.common.databinding.FragmentPrivacyPolicyBinding
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observeEvent
import tmg.utilities.lifecycle.viewInflateBinding

class PrivacyPolicyFragment: BaseFragment() {

    private val viewModel: PrivacyPolicyViewModel by viewModel()
    private val binding by viewInflateBinding(FragmentPrivacyPolicyBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

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