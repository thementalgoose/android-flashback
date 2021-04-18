package tmg.flashback.ui.settings.privacy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.constants.ViewType
import tmg.flashback.constants.logEvent
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityPrivacyPolicyBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyActivity: BaseActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override val screenAnalytics: ScreenAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}