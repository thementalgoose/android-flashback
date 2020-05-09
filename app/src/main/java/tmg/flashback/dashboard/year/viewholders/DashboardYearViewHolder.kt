package tmg.flashback.dashboard.year.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_year.view.*
import tmg.flashback.dashboard.year.DashboardYearItem

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
        itemView.tvYear.text = season.year.toString()
//        season.numberOfRaces?.let {
//            itemView.tvTrackCount.text = itemView.context.resources.getQuantityString(R.plurals.dashboard_track_count, it, it)
//        }
//        itemView.tvConstructor.text = itemView.context.getString(R.string.dashboard_constructor_champion, "Mercedes").fromHtml()
//        itemView.tvDriver.text = itemView.context.getString(R.string.dashboard_drivers_champion, "Lewis Hamilton", "Mercedes").fromHtml()
    }

    override fun onClick(p0: View?) {
        itemClicked(item, itemPosId)
    }
}