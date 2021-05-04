package tmg.flashback.ui.settings.release

import android.os.Bundle
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityReleaseNotesBinding

class ReleaseActivity : BaseActivity() {

    private lateinit var binding: ActivityReleaseNotesBinding

    override val screenAnalytics: ScreenAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReleaseNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}