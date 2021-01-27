package tmg.flashback.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_lockout.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.ui.SplashActivity
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible

class LockoutActivity: BaseActivity() {

    private val viewModel: LockoutViewModel by viewModel()

    override val analyticsScreenName: String
        get() = "App Lockout"

    private var maintenanceLink: String = ""

    override fun layoutId(): Int = R.layout.activity_lockout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnLink.setOnClickListener {
            viewModel.inputs.clickLink(maintenanceLink)
        }

        observe(viewModel.outputs.showLink) {
            val (linkText, link) = it
            maintenanceLink = link
            if (linkText.isEmpty()) {
                btnLink.gone()
            }
            else {
                btnLink.text = linkText
                btnLink.visible()
            }
        }

        observe(viewModel.outputs.data) { (title, content) ->
            header.text = title
            tvMessage.text = content.fromHtml()
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