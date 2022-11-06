package tmg.flashback.usecases

import androidx.compose.ui.graphics.Color
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.ui.dashboard.MenuSeasonItem
import javax.inject.Inject
import kotlin.math.abs

class GetSeasonsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    fun get(selectedSeason: Int? = null): List<MenuSeasonItem> {
        val seasons = homeRepository
            .supportedSeasons
            .sortedDescending()

        return seasons
            .mapIndexed { index, it ->
                MenuSeasonItem(
                    colour = Formula1.decadeColours["${it.toString().substring(0, 3)}0"] ?: Color.Gray,
                    season = it,
                    isSelected = selectedSeason == it,
                    isFirst = index == 0 || isGap(it, seasons.getOrNull(index - 1)),
                    isLast = index == (homeRepository.supportedSeasons.size - 1)  || isGap(it, seasons.getOrNull(index + 1))
                )
            }
    }

    private fun isGap(ref: Int, targetSeason: Int?): Boolean {
        if (targetSeason == null) {
            return true
        }
        return abs(targetSeason - ref) > 1
    }
}