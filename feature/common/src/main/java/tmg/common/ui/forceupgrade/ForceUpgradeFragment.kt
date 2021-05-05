package tmg.common.ui.forceupgrade

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.databinding.FragmentLockoutBinding
import tmg.flashback.shared.ui.base.BaseFragment
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.show

class ForceUpgradeFragment: BaseFragment<FragmentLockoutBinding>() {

    private val viewModel: ForceUpgradeViewModel by viewModel()

    override fun inflateView(inflater: LayoutInflater) = FragmentLockoutBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLink.setOnClickListener {
            viewModel.inputs.clickLink()
        }

        observe(viewModel.outputs.data) { (title, message) ->
            binding.header.text = title
            binding.tvMessage.text = message.fromHtml()
        }

        observe(viewModel.outputs.showLink) { link ->
            binding.btnLink.show(link != null)
            link?.let { (linkText, _) ->
                binding.btnLink.text = linkText
            }
        }

        observeEvent(viewModel.outputs.openLinkEvent) { link ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            startActivity(intent)
        }
    }
}