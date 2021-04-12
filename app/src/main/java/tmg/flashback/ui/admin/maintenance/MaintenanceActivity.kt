package tmg.flashback.ui.admin.maintenance

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityLockoutBinding
import tmg.flashback.ui.SplashActivity
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible

class MaintenanceActivity: BaseActivity() {

    private lateinit var binding: ActivityLockoutBinding
    private val viewModel: MaintenanceViewModel by viewModel()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "App Lockout"
    )

    // TODO: Remove this from the maintenance activity
    private var maintenanceLink: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLink.setOnClickListener {
            viewModel.inputs.clickLink(maintenanceLink)
        }

        observe(viewModel.outputs.showLink) {
            val (linkText, link) = it
            maintenanceLink = link
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

        observeEvent(viewModel.outputs.openLinkEvent) {
            viewUrl(it)
        }

        observeEvent(viewModel.outputs.returnToHome) {
            finish()
            startActivity(Intent(this, SplashActivity::class.java))
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finishAffinity()
        }
        return super.onOptionsItemSelected(item)
    }
}