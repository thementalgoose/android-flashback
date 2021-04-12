package tmg.flashback.ui.admin.forceupgrade

import android.os.Bundle
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.enums.DisplayType
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityLockoutBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.show

class ForceUpgradeActivity: BaseActivity() {

    private lateinit var binding: ActivityLockoutBinding
    private val viewModel: ForceUpgradeViewModel by viewModel()

    override val themeType: DisplayType = DisplayType.DEFAULT
    override val screenAnalytics = ScreenAnalytics(
        screenName = "Force upgrade"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeDismissLock = true

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
            viewUrl(link)
        }
    }
}