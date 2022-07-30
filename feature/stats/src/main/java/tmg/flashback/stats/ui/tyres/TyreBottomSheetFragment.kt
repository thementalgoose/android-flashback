package tmg.flashback.stats.ui.tyres

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment

@AndroidEntryPoint
class TyreBottomSheetFragment: BaseBottomSheetComposeFragment() {

    val season: Int get() = arguments?.getInt(keySeason, currentSeasonYear) ?: currentSeasonYear

    override val content = @Composable {
        TyreCompounds(season = season)
    }

    companion object {

        private const val keySeason: String = "season"

        fun instance(season: Int): TyreBottomSheetFragment {
            return TyreBottomSheetFragment().apply {
                arguments = bundleOf(
                    keySeason to season
                )
            }
        }
    }

}