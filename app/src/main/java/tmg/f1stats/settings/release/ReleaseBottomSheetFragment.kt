package tmg.f1stats.settings.release

import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_release_notes.*
import kotlinx.android.synthetic.main.fragment_release_notes.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseBottomSheetFragment
import tmg.f1stats.releaseNotes
import tmg.f1stats.repo.db.PrefsDB
import tmg.f1stats.utils.observe
import tmg.utilities.extensions.fromHtml
import tmg.utilities.lifecycle.common.CommonBottomSheetFragment

class ReleaseBottomSheetFragment: BaseBottomSheetFragment() {

    private val prefsDB: PrefsDB by inject()

    override fun layoutId(): Int = R.layout.fragment_release_notes

    override fun initViews() {

        imgbtnClose.setOnClickListener {
            dismiss()
        }

        val list = releaseNotes
            .filterKeys { it > prefsDB.lastAppVersion }
            .toList()
            .sortedBy { it.first }
            .map { it.second }

        tvReleaseNotes.text = list.map { getString(it) }.joinToString { "<br/><br/>" }.fromHtml()
    }
}