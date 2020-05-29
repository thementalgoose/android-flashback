package tmg.flashback.dashboard.year.viewholders

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_dashboard_year.view.*
import tmg.flashback.R
import tmg.flashback.colours
import tmg.flashback.coloursDecade
import tmg.flashback.dashboard.year.DashboardYearItem
import tmg.flashback.extensions.ordinalAbbreviation
import tmg.flashback.minimumSupportedYear
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.show
import tmg.utilities.utils.ColorUtils
import tmg.utilities.utils.ColorUtils.Companion.lighten

class DashboardYearViewHolder(
    itemView: View,
    val itemClicked: (model: DashboardYearItem.Season, itemId: Long) -> Unit
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: DashboardYearItem.Season
    private var itemPosId: Long = -1L

    init {
        itemView.clMain.setOnClickListener(this)
    }

    fun bind(season: DashboardYearItem.Season, itemId: Long) {
        this.item = season
        this.itemPosId = itemId
        val year: String = season.year.toString()
        itemView.year.text = year
        when {
            season.scheduled == 0 -> {
                itemView.races.text = itemView.context.getString(R.string.dashboard_race_completed, season.completed.toString()).fromHtml()
            }
            season.scheduled != 0 && season.completed != 0 -> {
                itemView.races.text = itemView.context.getString(R.string.dashboard_race_mid, season.completed.toString(), season.scheduled.toString()).fromHtml()
            }
            else -> {
                itemView.races.text = itemView.context.getString(R.string.dashboard_race_not_started, season.scheduled.toString()).fromHtml()
            }
        }

        val countdown = ((season.year - minimumSupportedYear) + 1)
        itemView.relative.text = countdown.ordinalAbbreviation
        coloursDecade["${season.year.toString().substring(0, 3)}0"]?.let {
            itemView.relative.setBackgroundColor(it.toColorInt())
        }

        itemView.pill.setBackgroundColor(season.colour)

        if (season.winnerDriver != null) {
            Glide.with(itemView.context)
                .load(season.winnerDriver.photoUrl)
                .into(itemView.driverImg)
            itemView.driver.text = itemView.context.getString(R.string.dashboard_drivers_champion, season.winnerDriver.name, season.winnerDriver.points.toString()).fromHtml()
        }
        else {
            Glide.with(itemView.context).clear(itemView.driverImg)
            itemView.driver.text = "..."
        }

        if (season.winnerConstructor != null) {
            itemView.constructor.text = itemView.context.getString(R.string.dashboard_drivers_champion, season.winnerConstructor.name, season.winnerConstructor.points.toString()).fromHtml()
            itemView.constructorText.setBackgroundColor(season.winnerConstructor.color.toColorInt())
        }
        else {
            itemView.constructor.text = "..."
            itemView.constructorText.setBackgroundColor(itemView.context.theme.getColor(R.attr.f1BackgroundSecondary))
        }

        val (light, dark) = colours.random()
        itemView.pill.progressColour = dark.toColorInt()
        itemView.pill.backgroundColour = itemView.context.theme.getColor(R.attr.f1BackgroundSecondary)
        itemView.pill.timeLimit = 400
        itemView.pill.animateProgress(season.completed.toFloat() / (season.scheduled.toFloat() + season.completed.toFloat())) { "" }
    }

    override fun onClick(p0: View?) {
        itemClicked(item, itemPosId)
    }
}