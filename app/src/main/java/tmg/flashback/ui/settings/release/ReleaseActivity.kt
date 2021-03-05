package tmg.flashback.ui.settings.release

import android.os.Bundle
import android.view.MenuItem
import tmg.flashback.R
import tmg.flashback.constants.Releases
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.databinding.ActivityReleaseNotesBinding
import tmg.utilities.extensions.fromHtml

class ReleaseActivity : BaseActivity() {

    private lateinit var binding: ActivityReleaseNotesBinding

    override val analyticsScreenName: String
        get() = "Release notes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReleaseNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.text = getString(R.string.settings_help_release_notes_title)

        val list = Releases
            .values()
            .sortedByDescending { it.version }
            .map { it.release }

        binding.back.setOnClickListener { finish() }

        binding.tvReleaseNotes.text = list.map { getString(it) }.joinToString("<br/><br/>").fromHtml()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}