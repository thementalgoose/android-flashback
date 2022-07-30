package tmg.flashback.releasenotes.ui.releasenotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.releasenotes.databinding.FragmentBottomSheetReleaseNotesBinding
import tmg.flashback.releasenotes.repository.ReleaseNotesRepository
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase
import tmg.flashback.ui.base.BaseBottomSheetFragment
import javax.inject.Inject

@AndroidEntryPoint
class ReleaseBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetReleaseNotesBinding>() {

    @Inject
    protected lateinit var releaseNotesRepository: ReleaseNotesRepository
    @Inject
    protected lateinit var newReleaseNotesUseCase: NewReleaseNotesUseCase

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