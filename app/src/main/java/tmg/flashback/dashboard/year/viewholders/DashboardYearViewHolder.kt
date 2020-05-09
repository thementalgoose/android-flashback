package tmg.flashback.dashboard.year.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_year.view.*
import tmg.flashback.dashboard.year.DashboardYearItem

class DashboardYearViewHolder(
    itemView: View,
    val itemClicked: (model: DashboardYearItem.Season, itemId: Long) -> Unit
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: DashboardYearItem.Season
    private var itemPosId: Long = -1L

    init {
        itemView.clMain.setOnClickListener(this)
    }

    fun bind(year: DashboardYearItem.Season, itemId: Long) {
        this.item = year
        this.itemPosId = itemId
        itemView.tvYear.text = year.year.toString()
    }

    override fun onClick(p0: View?) {
        itemClicked(item, itemPosId)
    }
}