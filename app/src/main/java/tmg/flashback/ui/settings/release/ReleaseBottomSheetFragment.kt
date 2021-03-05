package tmg.flashback.ui.settings.release

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import org.koin.android.ext.android.inject
import tmg.flashback.controllers.ReleaseNotesController
import tmg.flashback.core.ui.BaseBottomSheetFragment
import tmg.flashback.databinding.FragmentReleaseNotesBinding
import tmg.utilities.extensions.fromHtml

class ReleaseBottomSheetFragment: BaseBottomSheetFragment<FragmentReleaseNotesBinding>() {

    private val releaseNotesController: ReleaseNotesController by inject()

    override fun inflateView(inflater: LayoutInflater) =
        FragmentReleaseNotesBinding.inflate(inflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (releaseNotesController.majorReleaseNotes.isEmpty()) {
            dismiss()
        }

        binding.tvReleaseNotesDescription.text = releaseNotesController
            .majorReleaseNotes
            .map { getString(it.release) }
            .joinToString("<br/><br/>")
            .fromHtml()

        releaseNotesController.markReleaseNotesSeen()
    }
}