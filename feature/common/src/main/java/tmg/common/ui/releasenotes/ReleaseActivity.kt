package tmg.common.ui.releasenotes

import android.os.Bundle
import tmg.common.databinding.ActivityReleaseNotesBinding
import tmg.core.ui.base.BaseActivity

class ReleaseActivity : BaseActivity() {

    private lateinit var binding: ActivityReleaseNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReleaseNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}