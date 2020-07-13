package tmg.flashback.settings.release

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.WindowInsetsCompat
import kotlinx.android.synthetic.main.activity_release_notes.*
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.releaseNotes
import tmg.utilities.extensions.fromHtml

class ReleaseActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_release_notes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = getString(R.string.settings_help_release_notes_title)

        val list = releaseNotes
            .toList()
            .sortedByDescending { it.first }
            .map { it.second }

        back.setOnClickListener { finish() }

        tvReleaseNotes.text = list.map { getString(it) }.joinToString("<br/><br/>").fromHtml()
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        scrollview.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}