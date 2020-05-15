package tmg.flashback.dashboard.year.viewholders

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_year.view.*
import tmg.flashback.R
import tmg.flashback.colours
import tmg.flashback.dashboard.year.DashboardYearItem
import tmg.flashback.minimumSupportedYear
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor
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
        season.numberOfRaces?.let {
            itemView.races.text = itemView.context.getString(R.string.dashboard_race_completed, it.toString()).fromHtml()
        }

        val countdown = ((season.year - minimumSupportedYear) + 1)
        itemView.season.text = itemView.context.getString(R.string.dashboard_season_th, countdown.toString()).fromHtml()

        val (light, dark) = colours.random()
        itemView.pill.setBackgroundColor(dark.toColorInt())

        itemView.arcView.backgroundColour = itemView.context.theme.getColor(R.attr.f1BackgroundPrimary)
        itemView.arcView.barColour = lighten(itemView.context.theme.getColor(R.attr.colorPrimary))
        itemView.arcView.animateProgress(1.0f)
    }

    override fun onClick(p0: View?) {
        itemClicked(item, itemPosId)
    }
}