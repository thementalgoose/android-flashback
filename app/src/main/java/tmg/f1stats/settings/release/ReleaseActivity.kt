package tmg.f1stats.settings.release

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_release_notes.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.releaseNotes
import tmg.f1stats.repo.db.PrefsDB
import tmg.f1stats.utils.observe
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