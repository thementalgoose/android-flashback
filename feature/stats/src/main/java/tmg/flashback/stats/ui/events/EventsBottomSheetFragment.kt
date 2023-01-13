package tmg.flashback.stats.ui.events

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment

@AndroidEntryPoint
class EventsBottomSheetFragment : BaseBottomSheetComposeFragment() {

    val season: Int get() = arguments?.getInt(keySeason, Formula1.currentSeasonYear) ?: Formula1.currentSeasonYear

    override val content = @Composable {
        EventsScreenVM(season = season)
    }

    companion object {

        private const val keySeason: String = "season"

        fun instance(season: Int): EventsBottomSheetFragment {
            return EventsBottomSheetFragment().apply {
                arguments = bundleOf(
                    keySeason to season
                )
            }
        }
    }

}