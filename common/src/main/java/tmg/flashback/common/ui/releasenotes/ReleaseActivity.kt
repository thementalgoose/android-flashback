package tmg.flashback.common.ui.releasenotes

import android.os.Bundle
import tmg.flashback.common.databinding.ActivityReleaseNotesBinding
import tmg.flashback.ui.base.BaseActivity

class ReleaseActivity : BaseActivity() {

    private lateinit var binding: ActivityReleaseNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReleaseNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}