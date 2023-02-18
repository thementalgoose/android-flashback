package tmg.flashback.releasenotes.ui.releasenotes

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.releasenotes.repository.ReleaseNotesRepository
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment
import javax.inject.Inject

@AndroidEntryPoint
class NewReleaseBottomSheetFragment: BaseBottomSheetComposeFragment() {

    @Inject
    lateinit var releaseNotesRepository: ReleaseNotesRepository
    @Inject
    lateinit var newReleaseNotesUseCase: NewReleaseNotesUseCase

    override val content: @Composable () -> Unit = {
        NewReleaseScreen(releaseNotes = newReleaseNotesUseCase.getNotes())
    }
}