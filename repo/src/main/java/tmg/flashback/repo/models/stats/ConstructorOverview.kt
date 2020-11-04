package tmg.flashback.repo.models.stats

import com.sun.org.apache.xpath.internal.operations.Bool
import sun.security.util.math.IntegerModuloP
import tmg.flashback.repo.models.ConstructorDriver

data class ConstructorOverview(
        val id: String,
        val name: String,
        val wikiUrl: String,
        val nationality: String,
        val nationalityISO: String,
        val color: Int,
        val standings: List<ConstructorOverviewStanding>
)

data class ConstructorOverviewStanding(
        val drivers: Map<String, ConstructorOverviewDriverStanding>,
        val inProgress: Boolean,
        val championshipStanding: Int,
        val points: Int,
        val season: Int,
        val races: Int,
)

data class ConstructorOverviewDriverStanding(
        val driver: ConstructorDriver,
        val bestFinish: Int,
        val bestQualifying: Int,
        val points: Int,
        val finishesInP1: Int,
        val finishesInP2: Int,
        val finishesInP3: Int,
        val finishesInPoints: Int,
        val qualifyingP1: Int,
        val qualifyingP2: Int,
        val qualifyingP3: Int,
        val qualifyingTop10: Int?,
        val races: Int,
        val championshipStanding: Int
)