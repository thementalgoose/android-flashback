package tmg.flashback.ui.admin.maintenance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.constants.ViewType
import tmg.flashback.constants.logEvent
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.FragmentLockoutBinding
import tmg.flashback.ui.SplashActivity
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible

class MaintenanceFragment: BaseFragment<FragmentLockoutBinding>() {

    private val viewModel: MaintenanceViewModel by viewModel()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "Maintenance"
    )

    override fun inflateView(inflater: LayoutInflater) = FragmentLockoutBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsController.logEvent(ViewType.MAINTENANCE)

        binding.btnLink.setOnClickListener {
            viewModel.inputs.clickLink()
        }

        observe(viewModel.outputs.showLink) {
            val (linkText, _) = it
            if (linkText.isEmpty()) {
                binding.btnLink.gone()
            }
            else {
                binding.btnLink.text = linkText
                binding.btnLink.visible()
            }
        }

        observe(viewModel.outputs.data) { (title, content) ->
            binding.header.text = title
            binding.tvMessage.text = content.fromHtml()
        }

        observeEvent(viewModel.outputs.openLinkEvent) { link ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            startActivity(intent)
        }

        observeEvent(viewModel.outputs.returnToHome) {
            activity?.let {
                it.finish()
                startActivity(Intent(it, SplashActivity::class.java))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.let {
                finishAffinity(it)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}