package tmg.flashback.home.list

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.repo.models.news.NewsItem
import tmg.flashback.repo.models.stats.Circuit

sealed class HomeItem(
    @LayoutRes val layoutId: Int
) {
    data class Track(
        val season: Int,
        val raceName: String,
        val circuitName: String,
        val raceCountry: String,
        val raceCountryISO: String,
        val date: LocalDate,
        val round: Int,
        val hasData: Boolean
    ): HomeItem(R.layout.view_home_track)

    data class Driver(
        val points: Int,
        val driver: tmg.flashback.repo.models.stats.Driver,
        val driverId: String = driver.id,
        val position: Int,
        val bestQualifying: Pair<Int, List<Circuit>>?,
        val bestFinish: Pair<Int, List<Circuit>>?,
        val maxPointsInSeason: Int
    ): HomeItem(R.layout.view_home_driver)

    data class Constructor(
        val position: Int,
        val constructor: tmg.flashback.repo.models.stats.Constructor,
        val constructorId: String = constructor.id,
        val driver: List<Pair<tmg.flashback.repo.models.stats.Driver, Int>>,
        val points: Int,
        val maxPointsInSeason: Int
    ): HomeItem(R.layout.view_home_constructor)

    data class NewsArticle(
        val item: NewsItem
    ): HomeItem(R.layout.view_home_news)
}