package tmg.flashback.dashboard.year.viewholders

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
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
import kotlin.random.Random.Default.nextInt

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
            itemView.races.text = itemView.context.resources.getQuantityString(R.plurals.dashboard_track_count, it, it).fromHtml()
        }
        val (light, dark) = colours.random()
        itemView.shortname.setTextColor(ColorUtils.darken(dark.toColorInt()))
        itemView.shortname.text = ((season.year - minimumSupportedYear) + 1).toString()
        itemView.icon.setBackgroundColor(light.toColorInt())
        itemView.progressView.timeLimit = 200
        itemView.progressView.backgroundColour = itemView.context.theme.getColor(R.attr.f1BackgroundPrimary)
        itemView.progressView.setProgress(1.0f)
        itemView.progressView.progressColour = Color.GRAY
    }

    override fun onClick(p0: View?) {
        itemClicked(item, itemPosId)
    }
}