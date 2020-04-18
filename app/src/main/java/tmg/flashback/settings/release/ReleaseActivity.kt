package tmg.flashback.settings.release

import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_release_notes.*
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.releaseNotes
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.initToolbar

class ReleaseActivity: BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_release_notes

    override fun initViews() {

        initToolbar(R.id.toolbar, true)

        val list = releaseNotes
            .toList()
            .sortedBy { it.first }
            .map { it.second }

        tvReleaseNotes.text = list.map { getString(it) }.joinToString("<br/><br/>").fromHtml()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}