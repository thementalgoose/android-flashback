package tmg.flashback.common.ui.forceupgrade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.common.databinding.FragmentLockoutBinding
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.show

class ForceUpgradeFragment: BaseFragment<FragmentLockoutBinding>() {

    private val viewModel: ForceUpgradeViewModel by viewModel()

    override fun inflateView(inflater: LayoutInflater) = FragmentLockoutBinding
        .inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Force upgrade")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLink.setOnClickListener {
            (binding.btnLink.tag as? String)?.let {
                viewUrl(it)
            } ?: let {
                activity?.finish()
            }
        }

        observe(viewModel.outputs.data) { (title, message) ->
            binding.header.text = title
            binding.tvMessage.text = message.fromHtml()
        }

        observe(viewModel.outputs.showLink) { link ->
            binding.btnLink.show(link != null)
            link?.let { (linkText, url) ->
                binding.btnLink.text = linkText
                binding.btnLink.tag = url
            }
        }
    }
}