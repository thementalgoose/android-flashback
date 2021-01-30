package tmg.flashback.ui.settings.release

import kotlinx.android.synthetic.main.fragment_release_notes.*
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.constants.Releases
import tmg.flashback.controllers.ReleaseNotesController
import tmg.flashback.data.pref.DeviceRepository
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.utilities.extensions.fromHtml

class ReleaseBottomSheetFragment: BaseBottomSheetFragment() {

    val releaseNotesController: ReleaseNotesController by inject()

    override fun layoutId(): Int = R.layout.fragment_release_notes

    override fun initViews() {

        if (releaseNotesController.majorReleaseNotes.isEmpty()) {
            dismiss()
        }

        tvReleaseNotesDescription.text = releaseNotesController
            .majorReleaseNotes
            .map { getString(it.release) }
            .joinToString("<br/><br/>")
            .fromHtml()

        releaseNotesController.markReleaseNotesSeen()
    }
}