package tmg.flashback.admin.lockout

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_lockout.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.SplashActivity
import tmg.flashback.base.BaseActivity
import tmg.utilities.extensions.*
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible

class LockoutActivity: BaseActivity() {

    private val viewModel: LockoutViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_lockout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnLink.setOnClickListener {
            viewModel.inputs.clickLink()
        }

        observe(viewModel.outputs.showLink) {
            if (it.isEmpty()) {
                btnLink.gone()
            }
            else {
                btnLink.text = it
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