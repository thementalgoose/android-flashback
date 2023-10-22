package tmg.flashback.usecases

import tmg.flashback.season.repository.HomeRepository
import javax.inject.Inject
import kotlin.math.abs

@JvmInline
value class IsFirst(val value: Boolean)
@JvmInline
value class IsLast(val value: Boolean)

class GetSeasonsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    fun get(): Map<Int, Pair<IsFirst, IsLast>> {
        val seasons = homeRepository
            .supportedSeasons
            .sortedDescending()

        return seasons
            .mapIndexed { index, it ->
                it to Pair(
                    IsFirst(index == 0 || isGap(it, seasons.getOrNull(index - 1))),
                    IsLast(index == (homeRepository.supportedSeasons.size - 1)  || isGap(it, seasons.getOrNull(index + 1)))
                )
            }
            .toMap()
    }

    private fun isGap(ref: Int, targetSeason: Int?): Boolean {
        if (targetSeason == null) {
            return true
        }
        return abs(targetSeason - ref) > 1
    }
}