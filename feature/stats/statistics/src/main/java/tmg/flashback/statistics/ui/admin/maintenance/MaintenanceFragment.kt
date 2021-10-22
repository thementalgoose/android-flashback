package tmg.flashback.statistics.ui.admin.maintenance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.core.ui.navigation.NavigationProvider
import tmg.flashback.statistics.databinding.FragmentMaintenanceBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible

class MaintenanceFragment: BaseFragment<FragmentMaintenanceBinding>() {

    private val viewModel: MaintenanceViewModel by viewModel()

    private val navigationProvider: NavigationProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Maintenance")
    }

    override fun inflateView(inflater: LayoutInflater) = FragmentMaintenanceBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            viewUrl(link)
        }

        observeEvent(viewModel.outputs.returnToHome) {
            activity?.let {
                it.finish()
                startActivity(navigationProvider.relaunchAppIntent(it))
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