package tmg.flashback.ui.settings.release

import android.os.Bundle
import android.view.MenuItem
import tmg.flashback.R
import tmg.flashback.constants.Releases
import tmg.flashback.constants.ViewType
import tmg.flashback.constants.logEvent
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityReleaseNotesBinding
import tmg.utilities.extensions.fromHtml

class ReleaseActivity : BaseActivity() {

    private lateinit var binding: ActivityReleaseNotesBinding

    override val screenAnalytics: ScreenAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReleaseNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}