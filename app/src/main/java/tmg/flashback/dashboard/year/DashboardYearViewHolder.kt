package tmg.flashback.dashboard.year

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_year.view.*

class DashboardYearViewHolder(
    itemView: View,
    val itemClicked: (model: DashboardYearItem, itemId: Long) -> Unit
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: DashboardYearItem
    private var itemPosId: Long = -1L

    init {
        itemView.clMain.setOnClickListener(this)
    }

    fun bind(year: DashboardYearItem, itemId: Long) {
        this.item = year
        this.itemPosId = itemId
        itemView.tvYear.text = year.year.toString()
    }

    override fun onClick(p0: View?) {
        itemClicked(item, itemPosId)
    }
}