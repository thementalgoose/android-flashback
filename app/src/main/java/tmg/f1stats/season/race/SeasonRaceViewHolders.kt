package tmg.f1stats.season.race

import androidx.annotation.LayoutRes
import tmg.f1stats.R

enum class SeasonRaceAdapterViewHolderType(
        @LayoutRes val viewHolderRes: Int
) {
    RACE_PODIUM(R.layout.view_race_podium),
    RACE_RESULT(R.layout.view_race_result),
    QUALIFYING_RESULT_HEADER(R.layout.view_qualifying_header),
    QUALIFYING_RESULT(R.layout.view_qualifying_result),
}
