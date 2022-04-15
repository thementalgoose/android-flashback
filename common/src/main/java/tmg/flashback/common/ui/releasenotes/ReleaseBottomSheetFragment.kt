package tmg.flashback.common.ui.releasenotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import org.koin.android.ext.android.inject
import tmg.flashback.common.databinding.FragmentBottomSheetReleaseNotesBinding
import tmg.flashback.common.repository.ReleaseNotesRepository
import tmg.flashback.common.usecases.NewReleaseNotesUseCase
import tmg.flashback.ui.base.BaseBottomSheetFragment

class ReleaseBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetReleaseNotesBinding>() {

    // TODO: Move over to view model
    private val releaseNotesRepository: ReleaseNotesRepository by inject()
    private val newReleaseNotesUseCase: NewReleaseNotesUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Release notes popup")
    }

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetReleaseNotesBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (newReleaseNotesUseCase.getNotes().isEmpty()) {
            dismiss()
        }

        binding.tvReleaseNotesDescription.text = newReleaseNotesUseCase
            .getNotes()
            .map { getString(it.release) }
            .joinToString("\n\n")

        releaseNotesRepository.releaseNotesSeen()
    }
}