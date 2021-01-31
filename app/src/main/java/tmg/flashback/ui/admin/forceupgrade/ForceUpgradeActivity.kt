package tmg.flashback.ui.admin.forceupgrade

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_lockout.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.core.enums.DisplayType
import tmg.flashback.core.ui.BaseActivity
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.show

class ForceUpgradeActivity: BaseActivity() {

    private val viewModel: ForceUpgradeViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_lockout

    override val themeType: DisplayType = DisplayType.DEFAULT
    override val analyticsScreenName: String
        get() = "Force upgrade"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        swipeDismissLock = true

        btnLink.setOnClickListener {
            viewModel.inputs.clickLink()
        }

        observe(viewModel.outputs.data) { (title, message) ->
            header.text = title
            tvMessage.text = message.fromHtml()
        }

        observe(viewModel.outputs.showLink) { link ->
            btnLink.show(link != null)
            link?.let { (linkText, _) ->
                btnLink.text = linkText
            }
        }

        observeEvent(viewModel.outputs.openLinkEvent) { link ->
            viewUrl(link)
        }
    }
}