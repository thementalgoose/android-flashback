package tmg.flashback.common.ui.releasenotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import org.koin.android.ext.android.inject
import tmg.flashback.common.controllers.ReleaseNotesController
import tmg.flashback.common.databinding.FragmentBottomSheetReleaseNotesBinding
import tmg.flashback.ui.base.BaseBottomSheetFragment

class ReleaseBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetReleaseNotesBinding>() {

    // TODO: Move over to view model
    private val releaseNotesController: ReleaseNotesController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Release notes popup")
    }

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetReleaseNotesBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (releaseNotesController.majorReleaseNotes.isEmpty()) {
            dismiss()
        }

        binding.tvReleaseNotesDescription.text = releaseNotesController
            .majorReleaseNotes
            .map { getString(it.release) }
            .joinToString("\n\n")

        releaseNotesController.markReleaseNotesSeen()
    }
}