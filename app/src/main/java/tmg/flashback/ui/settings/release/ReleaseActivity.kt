package tmg.flashback.ui.settings.release

import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_release_notes.*
import tmg.flashback.R
import tmg.flashback.constants.Releases
import tmg.flashback.core.ui.BaseActivity
import tmg.utilities.extensions.fromHtml

class ReleaseActivity : BaseActivity() {

    override val analyticsScreenName: String
        get() = "Release notes"

    override fun layoutId(): Int = R.layout.activity_release_notes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = getString(R.string.settings_help_release_notes_title)

        val list = Releases
            .values()
            .sortedByDescending { it.version }
            .map { it.release }

        back.setOnClickListener { finish() }

        tvReleaseNotes.text = list.map { getString(it) }.joinToString("<br/><br/>").fromHtml()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}